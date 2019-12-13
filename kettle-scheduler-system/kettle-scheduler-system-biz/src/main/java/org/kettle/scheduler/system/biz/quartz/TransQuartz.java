package org.kettle.scheduler.system.biz.quartz;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.SpringContextUtil;
import org.kettle.scheduler.core.dto.RepositoryDTO;
import org.kettle.scheduler.core.execute.TransExecute;
import org.kettle.scheduler.core.repository.RepositoryUtil;
import org.kettle.scheduler.system.api.enums.RunResultEnum;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;
import org.kettle.scheduler.system.biz.constant.KettleConfig;
import org.kettle.scheduler.system.biz.entity.Repository;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.entity.TransMonitor;
import org.kettle.scheduler.system.biz.entity.TransRecord;
import org.kettle.scheduler.system.biz.repository.RepositoryRepository;
import org.kettle.scheduler.system.biz.service.SysTransMonitorService;
import org.kettle.scheduler.system.biz.service.SysTransService;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.repository.AbstractRepository;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * 转换定时任务执行器
 * @author lyf
 */
@Slf4j
@DisallowConcurrentExecution
public class TransQuartz implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 此处无法使用常规注入方式注入bean
        SysTransMonitorService monitorService = SpringContextUtil.getBean(SysTransMonitorService.class);
        SysTransService transService = SpringContextUtil.getBean(SysTransService.class);

        // 本次执行时间
        Date lastExecuteTime = jobExecutionContext.getFireTime();
        // 下一次任务时间
        Date nexExecuteTime = jobExecutionContext.getNextFireTime();
        // 运行状态
        boolean runStatus = true;
        // 获取传入过来的转换ID
        Integer transId = jobExecutionContext.getMergedJobDataMap().getInt("id");

        // 获取转换
        Trans trans = transService.getTransById(transId);
        // 执行转换并返回日志
        String logText = "";
        try {
            // 判断是执行资源库还是执行文件脚本
            switch (RunTypeEnum.getEnum(trans.getTransType())) {
                case REP:
                    logText = TransExecute.run(getAbstractRepository(trans.getTransRepositoryId())
                            , trans.getTransPath(), trans.getTransName()
                            , null, null
                            , LogLevel.getLogLevelForCode(trans.getTransLogLevel()));
                    break;
                case FILE:
                    logText = TransExecute.run(trans.getTransPath(), null
                            , LogLevel.getLogLevelForCode(trans.getTransLogLevel()));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + RunTypeEnum.getEnum(trans.getTransType()));
            }
        } catch (KettleException e) {
            runStatus = false;
            String msg = "执行转换失败";
            log.error(msg, e);
            logText = e.getMessage();
        }
        // 执行结束时间
        Date stopDate = new Date();

        // 输出日志到文件中,返回输出路径
        String logPath = writeStringToFile(String.valueOf(transId), logText);

        // 修改监控表数据
        TransMonitor transMonitor = new TransMonitor();
        transMonitor.setMonitorTransId(transId);
        transMonitor.setLastExecuteTime(lastExecuteTime);
        transMonitor.setNextExecuteTime(nexExecuteTime);
        monitorService.updateMonitor(transMonitor, runStatus);

        // 添加转换执行记录
        TransRecord transRecord = new TransRecord();
        transRecord.setLogFilePath(logPath);
        transRecord.setRecordStatus(runStatus ? RunResultEnum.SUCCESS.getCode() : RunResultEnum.FAIL.getCode());
        transRecord.setRecordTransId(transId);
        transRecord.setStartTime(lastExecuteTime);
        transRecord.setStopTime(stopDate);
        monitorService.addTransRecord(transRecord);
    }

    /**
     * 获取资源库
     * @param transRepositoryId 资源库id
     * @return {@link AbstractRepository}
     */
    private AbstractRepository getAbstractRepository(Integer transRepositoryId) {
        RepositoryRepository repRepository = SpringContextUtil.getBean(RepositoryRepository.class);

        AbstractRepository repository = RepositoryUtil.getRepository(transRepositoryId);
        if (repository == null) {
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
     * @param transId 转换ID
     * @param logText 日志内容
     * @return 日志输出路径
     */
    private String writeStringToFile(String transId, String logText) {
        String logPath = KettleConfig.logFilePath.concat("/").concat("trans/").concat(transId).concat("/").concat(String.valueOf(System.currentTimeMillis())).concat(".txt");
        try {
            FileUtil.writeStringToFile(new File(logPath), logText, KettleConfig.encoding.name(), false);
        } catch (IOException e) {
            String msg = "输出日志到文件失败";
            log.error(msg, e);
        }
        return logPath;
    }
}
