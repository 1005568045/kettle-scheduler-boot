package org.kettle.scheduler.system.biz.quartz;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.utils.*;
import org.kettle.scheduler.core.constant.KettleConfig;
import org.kettle.scheduler.core.dto.RepositoryDTO;
import org.kettle.scheduler.core.execute.JobExecute;
import org.kettle.scheduler.core.repository.RepositoryUtil;
import org.kettle.scheduler.system.api.enums.RunResultEnum;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;
import org.kettle.scheduler.system.biz.entity.Job;
import org.kettle.scheduler.system.biz.entity.JobMonitor;
import org.kettle.scheduler.system.biz.entity.JobRecord;
import org.kettle.scheduler.system.biz.entity.Repository;
import org.kettle.scheduler.system.biz.repository.RepositoryRepository;
import org.kettle.scheduler.system.biz.service.SysJobMonitorService;
import org.kettle.scheduler.system.biz.service.SysJobService;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.AbstractRepository;
import org.quartz.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 作业定时任务执行器
 * 因为定时器的job类和kettle的job类名一样，因此这里采用继承{@code org.quartz。InterruptableJob}类
 * @author lyf
 */
@Slf4j
@DisallowConcurrentExecution
public class JobQuartz implements InterruptableJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 此处无法使用常规注入方式注入bean
        SysJobMonitorService monitorService = SpringContextUtil.getBean(SysJobMonitorService.class);
        SysJobService jobService = SpringContextUtil.getBean(SysJobService.class);

        // 本次执行时间
        Date lastExecuteTime = jobExecutionContext.getFireTime();
        // 下一次任务时间
        Date nexExecuteTime = jobExecutionContext.getNextFireTime();
        // 运行状态
        boolean runStatus = true;
        // 获取传入过来的作业ID
        Integer jobId = jobExecutionContext.getMergedJobDataMap().getInt("id");

        // 获取作业
        Job job = jobService.getJobById(jobId);
        // 设置执行参数
		Map<String, String> params = new HashMap<>(2);
		if (StringUtil.hasText(job.getSyncStrategy())) {
			Integer day = Integer.valueOf(job.getSyncStrategy().substring(2, job.getSyncStrategy().length()));

			params.put("start_time", DateUtil.getDateTimeStr(DateUtil.addDays(DateUtil.getTodayStartTime(), -day)));
			params.put("end_time", DateUtil.getDateTimeStr(DateUtil.addDays(DateUtil.getTodayEndTime(), -day)));
		}
        // 执行作业并返回日志
        String logText = "";
        try {
            // 判断是执行资源库还是执行文件脚本
            switch (RunTypeEnum.getEnum(job.getJobType())) {
                case REP:
                    logText = JobExecute.run(getAbstractRepository(job.getJobRepositoryId())
                            , job.getJobPath(), job.getJobName()
                            , null, params
                            , LogLevel.getLogLevelForCode(job.getJobLogLevel()));
                    break;
                case FILE:
                    logText = JobExecute.run(job.getJobPath(), null
                            , LogLevel.getLogLevelForCode(job.getJobLogLevel()));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + RunTypeEnum.getEnum(job.getJobType()));
            }
        } catch (KettleException e) {
            runStatus = false;
            String msg = "执行作业失败";
            log.error(msg, e);
            logText = e.getMessage();
        }
        // 执行结束时间
        Date stopDate = new Date();

        // 输出日志到文件中,返回输出路径
        String logPath = writeStringToFile(String.valueOf(jobId), logText);

        // 修改监控表数据
        JobMonitor jobMonitor = new JobMonitor();
        jobMonitor.setMonitorJobId(jobId);
        jobMonitor.setLastExecuteTime(lastExecuteTime);
        jobMonitor.setNextExecuteTime(nexExecuteTime);
        monitorService.updateMonitor(jobMonitor, runStatus);

        // 添加作业执行记录
        JobRecord jobRecord = new JobRecord();
        jobRecord.setLogFilePath(logPath);
        jobRecord.setRecordStatus(runStatus ? RunResultEnum.SUCCESS.getCode() : RunResultEnum.FAIL.getCode());
        jobRecord.setRecordJobId(jobId);
        jobRecord.setStartTime(lastExecuteTime);
        jobRecord.setStopTime(stopDate);
        monitorService.addJobRecord(jobRecord);
    }

    /**
     * 获取资源库
     * @param transRepositoryId 资源库id
     * @return {@link AbstractRepository}
     */
    private AbstractRepository getAbstractRepository(Integer transRepositoryId) {
		AbstractRepository repository = RepositoryUtil.getRepository(transRepositoryId);
		if (repository == null) {
			RepositoryRepository repRepository = SpringContextUtil.getBean(RepositoryRepository.class);
			Optional<Repository> optionalRepository = repRepository.findById(transRepositoryId);
            if (!optionalRepository.isPresent()) {
                throw new MyMessageException("资源库不存在");
            }
            Repository rep = optionalRepository.get();
            RepositoryDTO repDto = BeanUtil.copyProperties(rep, RepositoryDTO.class);
            // 连接资源库
            repository = RepositoryUtil.connection(repDto);
        }
        return repository;
    }

    /**
     * 输出日志到文件
     * @param jobId 作业ID
     * @param logText 日志内容
     * @return 日志输出路径
     */
    private String writeStringToFile(String jobId, String logText) {
        String logPath = KettleConfig.logFilePath.concat("/").concat("job/").concat(jobId).concat("/").concat(String.valueOf(System.currentTimeMillis())).concat(".txt");
        try {
            FileUtil.writeStringToFile(new File(logPath), logText, KettleConfig.encoding.name(), false);
        } catch (IOException e) {
            String msg = "输出日志到文件失败";
            log.error(msg, e);
        }
        return logPath;
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }
}
