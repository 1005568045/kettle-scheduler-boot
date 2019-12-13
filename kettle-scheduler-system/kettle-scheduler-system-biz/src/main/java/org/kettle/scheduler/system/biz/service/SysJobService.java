package org.kettle.scheduler.system.biz.service;

import com.google.common.collect.ImmutableMap;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.quartz.dto.QuartzDTO;
import org.kettle.scheduler.quartz.manage.QuartzManage;
import org.kettle.scheduler.system.api.enums.RunStatusEnum;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;
import org.kettle.scheduler.system.api.request.JobReq;
import org.kettle.scheduler.system.api.response.JobRes;
import org.kettle.scheduler.system.biz.component.EntityManagerUtil;
import org.kettle.scheduler.system.biz.entity.Job;
import org.kettle.scheduler.system.biz.entity.JobMonitor;
import org.kettle.scheduler.system.biz.entity.Quartz;
import org.kettle.scheduler.system.biz.entity.bo.JobBO;
import org.kettle.scheduler.system.biz.entity.bo.NativeQueryResultBO;
import org.kettle.scheduler.system.biz.quartz.JobQuartz;
import org.kettle.scheduler.system.biz.repository.JobMonitorRepository;
import org.kettle.scheduler.system.biz.repository.JobRepository;
import org.kettle.scheduler.system.biz.repository.QuartzRepository;
import org.quartz.JobDataMap;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 作业管理业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysJobService {
    private final JobRepository jobRepository;
    private final QuartzRepository quartzRepository;
    private final JobMonitorRepository monitorRepository;
	private final EntityManagerUtil entityManagerUtil;

    public SysJobService(JobRepository jobRepository, QuartzRepository quartzRepository,
			JobMonitorRepository monitorRepository, EntityManagerUtil entityManagerUtil) {
        this.jobRepository = jobRepository;
        this.quartzRepository = quartzRepository;
        this.monitorRepository = monitorRepository;
		this.entityManagerUtil = entityManagerUtil;
	}

	/**
	 * 根据定时策略和作业组装执行参数
	 * @param job 作业信息
	 * @param cron 定时策略
	 * @return {@link QuartzDTO}
	 */
	private QuartzDTO getQuartzDTO(Job job, String cron) {
		String categoryId = job.getCategoryId()==null ? "null" : String.valueOf(job.getCategoryId());

		QuartzDTO dto = new QuartzDTO();
		dto.setJobName("JOB@" + job.getId());
		dto.setJobGroupName("JOB_GROUP@" + categoryId + "@" + job.getId());
		dto.setTriggerName("JOB_TRIGGER@" + job.getId());
		dto.setTriggerGroupName("JOB_TRIGGER_GROUP@" + categoryId + "@" + job.getId());
		if (StringUtil.hasText(cron)) {
			dto.setCron(cron);
		}
		dto.setJobClass(JobQuartz.class);
		dto.setJobDataMap(new JobDataMap(ImmutableMap.of("id", job.getId())));
		return dto;
	}

	/**
	 * 修改监控信息状态
	 * @param jobId 作业ID
	 * @param statusEnum 状态枚举
	 */
	private void updateJobsMonitorStatus(Integer jobId, RunStatusEnum statusEnum) {
		JobMonitor jobMonitor = monitorRepository.findByMonitorJobId(jobId);
		if (jobMonitor == null) {
			jobMonitor = new JobMonitor();
			jobMonitor.setMonitorFail(0);
			jobMonitor.setMonitorSuccess(0);
			jobMonitor.setMonitorJobId(jobId);
			jobMonitor.setRunStatus(System.currentTimeMillis() + "-");
		} else {
			switch (statusEnum) {
				case RUN:
					String runStatus = jobMonitor.getRunStatus();
					if (runStatus.endsWith("-")) {
						runStatus = runStatus.concat(String.valueOf(System.currentTimeMillis()));
					}
					jobMonitor.setRunStatus(runStatus.concat(",").concat(System.currentTimeMillis() + "-"));
					break;
				case STOP:
					jobMonitor.setRunStatus(jobMonitor.getRunStatus().concat(String.valueOf(System.currentTimeMillis())));
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + statusEnum);
			}
		}
		jobMonitor.setMonitorStatus(statusEnum.getCode());
		monitorRepository.save(jobMonitor);
	}

    /**
     * 因程序中断后所有的定时会中断，因此在程序启动的时候需要初始化类调用该方法重新恢复定时任务
     */
    public void initJobsQuartz() {
        List<Job> jobsList = jobRepository.findByJobStatus(RunStatusEnum.RUN.getCode());
        List<Quartz> quartzList = quartzRepository.findAllById(jobsList.stream().map(Job::getJobQuartz).distinct().collect(Collectors.toList()));
        List<Quartz> quarts = quartzList.stream().filter(quartz -> StringUtil.hasText(quartz.getQuartzCron())).collect(Collectors.toList());
        List<Job> collect = jobsList.stream().filter(trans -> quarts.stream().anyMatch(quartz -> quartz.getId().equals(trans.getJobQuartz()))).collect(Collectors.toList());
        collect.forEach(job -> stopJob(job.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(JobReq req) {
        Job job = BeanUtil.copyProperties(req, Job.class);
		job.setJobStatus(RunStatusEnum.STOP.getCode());
        jobRepository.save(job);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
		Optional<Job> optional = jobRepository.findById(id);
		if (optional.isPresent()) {
			Job job = optional.get();

			// 删除前停止定时任务
			if (RunStatusEnum.RUN.getCode().equals(job.getJobStatus())) {
				stopJob(id);
			}

			if (RunTypeEnum.FILE.getCode().equals(job.getJobType())) {
				FileUtil.deleteFile(job.getJobPath());
			}

			jobRepository.delete(job);
		} else {
			throw new MyMessageException("删除资源不存在");
		}
    }

    public void deleteBatch(List<Integer> ids) {
        List<Job> jobs = jobRepository.findAllById(ids);
		jobs.forEach(job -> delete(job.getId()));
	}

    @Transactional(rollbackFor = Exception.class)
    public void update(JobReq req) {
        Optional<Job> optional = jobRepository.findById(req.getId());
        if (optional.isPresent()) {
            Job job = optional.get();
            BeanUtil.copyProperties(req, job);
            jobRepository.save(job);
        }
    }

    public PageOut<JobRes> findJobListByPage(JobReq query, Pageable pageable) {
		// select 部分sql
		String selectSql = "SELECT a.*, c.category_name, q.quartz_cron, q.quartz_description ";
		// from部分sql
		StringBuilder fromSql = new StringBuilder();
		fromSql.append("FROM `k_job` a ");
		fromSql.append("LEFT JOIN `k_category` c ON a.category_id = c.id ");
		fromSql.append("LEFT JOIN `k_quartz` q ON a.job_quartz=q.id ");
		if (query != null) {
			fromSql.append("WHERE 1=1 ");
			if (query.getCategoryId() != null) {
				fromSql.append("AND a.category_id = ").append(query.getCategoryId()).append(" ");
			}
			if (!StringUtil.isEmpty(query.getJobName())) {
				fromSql.append("AND a.job_name like '%").append(query.getJobName()).append("%'").append(" ");
			}
		}
		// order by 部分sql
		String orderSql = "order by a.add_time desc ";

		// 执行sql
		NativeQueryResultBO result = entityManagerUtil.executeNativeQueryForList(selectSql, fromSql.toString(), orderSql, pageable, JobBO.class);

		List<JobRes> list = new ArrayList<>();
		for (Object o : result.getResultList()) {
			list.add(BeanUtil.copyProperties(o, JobRes.class));
		}

		// 封装数据
		return new PageOut<>(list, pageable.getPageNumber(), pageable.getPageSize(), result.getTotal());
    }

    public JobRes getJobDetail(Integer id) {
        Optional<Job> optional = jobRepository.findById(id);
        return optional.map(job -> BeanUtil.copyProperties(job, JobRes.class)).orElse(null);
    }

    public Job getJobById(Integer id) {
        Optional<Job> optional = jobRepository.findById(id);
        return optional.orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void startAllJob() {
        List<Job> jobsList = jobRepository.findByJobStatus(RunStatusEnum.STOP.getCode());
        jobsList.forEach(job -> startJob(job.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void startJob(Integer id) {
        Optional<Job> optional = jobRepository.findById(id);
        if (!optional.isPresent()) {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "当前作业不存在");
        }
        Job job = optional.get();

        // 查询定时策略
        Optional<Quartz> optionalQuartz = quartzRepository.findById(job.getJobQuartz());
        if (!optionalQuartz.isPresent()) {
            throw new MyMessageException("当前定时策略不存在");
        }
        Quartz quartz = optionalQuartz.get();

        // 修改监控状态
        updateJobsMonitorStatus(job.getId(), RunStatusEnum.RUN);

        // 修改作业状态
        job.setJobStatus(RunStatusEnum.RUN.getCode());
        jobRepository.save(job);

        // 获取定时任务需要的参数
        QuartzDTO quartzDTO = getQuartzDTO(job, quartz.getQuartzCron());
        // 根据定时策略添加任务
        if (StringUtil.hasText(quartz.getQuartzCron())) {
            // 添加定时任务
            QuartzManage.addCronJob(quartzDTO);
        } else {
            // 添加一次性任务
            QuartzManage.addOnceJob(quartzDTO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void stopAllJob() {
        List<Job> jobsList = jobRepository.findByJobStatus(RunStatusEnum.RUN.getCode());
        jobsList.forEach(job -> stopJob(job.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void stopJob(Integer id) {
        // 查询作业信息
        Optional<Job> optional = jobRepository.findById(id);
        if (!optional.isPresent()) {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "当前作业不存在");
        }
        Job job = optional.get();

        // 已经关闭的任务不在处理
        if (RunStatusEnum.STOP.getCode().equals(job.getJobStatus())) {
            return;
        }

        // 修改监控状态
        updateJobsMonitorStatus(job.getId(), RunStatusEnum.STOP);

        // 修改作业状态
        job.setJobStatus(RunStatusEnum.STOP.getCode());
        jobRepository.save(job);

        // 关闭定时任务
        QuartzManage.removeJob(getQuartzDTO(job, null));
    }

	public Job getByJobName(String jobName) {
		return jobRepository.getByJobName(jobName);
	}
}
