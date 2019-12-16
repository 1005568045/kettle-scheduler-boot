package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageHelper;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.JobMonitorRes;
import org.kettle.scheduler.system.api.response.JobRecordRes;
import org.kettle.scheduler.system.api.response.TaskCountRes;
import org.kettle.scheduler.system.biz.component.EntityManagerUtil;
import org.kettle.scheduler.system.biz.entity.Job;
import org.kettle.scheduler.system.biz.entity.JobMonitor;
import org.kettle.scheduler.system.biz.entity.JobRecord;
import org.kettle.scheduler.system.biz.entity.bo.JobMonitorBO;
import org.kettle.scheduler.system.biz.entity.bo.NativeQueryResultBO;
import org.kettle.scheduler.system.biz.entity.bo.TaskCountBO;
import org.kettle.scheduler.system.biz.repository.JobMonitorRepository;
import org.kettle.scheduler.system.biz.repository.JobRecordRepository;
import org.kettle.scheduler.system.biz.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 作业监控业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysJobMonitorService {
    private final JobRepository jobRepository;
    private final JobMonitorRepository monitorRepository;
    private final JobRecordRepository recordRepository;
	private final EntityManagerUtil entityManagerUtil;

    public SysJobMonitorService(JobRepository jobRepository, JobMonitorRepository monitorRepository,
			JobRecordRepository recordRepository, EntityManagerUtil entityManagerUtil) {
        this.jobRepository = jobRepository;
        this.monitorRepository = monitorRepository;
        this.recordRepository = recordRepository;
		this.entityManagerUtil = entityManagerUtil;
	}

    public PageOut<JobMonitorRes> findJobMonitorListByPage(MonitorQueryReq query, Pageable pageable) {
    	String selectSql = "SELECT a.*,b.job_name,c.category_name ";
		// 动态拼接sql
		StringBuilder fromSql = new StringBuilder(" FROM `k_job_monitor` a ");
		fromSql.append("INNER JOIN k_job b ON a.monitor_job_id=b.id ");
		fromSql.append("LEFT JOIN k_category c ON b.category_id=c.id ");
		if (query!=null) {
			fromSql.append("WHERE 1=1 ");
			if (!StringUtil.isEmpty(query.getScriptName())) {
				fromSql.append("AND b.job_name like '%").append(query.getScriptName()).append("%'").append(" ");
			}
			if (query.getMonitorStatus() != null) {
				fromSql.append("AND a.monitor_status = ").append(query.getMonitorStatus()).append(" ");
			}
			if (query.getCategoryId() != null) {
				fromSql.append("AND b.category_id = ").append(query.getCategoryId()).append(" ");
			}
		}
		// 排序sql
		String orderSql = "order by a.add_time desc ";
		// 执行sql
		NativeQueryResultBO resultBo = entityManagerUtil.executeNativeQueryForList(selectSql, fromSql.toString(), orderSql, pageable, JobMonitorBO.class);
		List<JobMonitorRes> list = new ArrayList<>();
		for (Object o : resultBo.getResultList()) {
			list.add(BeanUtil.copyProperties(o, JobMonitorRes.class));
		}
		// 封装数据
		return new PageOut<>(list, pageable.getPageNumber(), pageable.getPageSize(), resultBo.getTotal());
    }

    public PageOut<JobRecordRes> findJobRecordList(Integer jobId, PageHelper pageHelper) {
        // 默认排序
		Pageable pageable = pageHelper.getPageable(Sort.by(Sort.Direction.DESC, "addTime"));
        // 查询job信息
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        String jobName = "";
        if (jobOptional.isPresent()) {
            jobName = jobOptional.get().getJobName();
        }
        // 分页查询执行记录
        Page<JobRecord> page = recordRepository.findByRecordJobId(jobId, pageable);
        // 封装数据
        String finalJobName = jobName;
        List<JobRecordRes> collect = page.get().map(t -> {
            JobRecordRes res = BeanUtil.copyProperties(t, JobRecordRes.class);
            res.setJobName(finalJobName);
            return res;
        }).collect(Collectors.toList());
        return new PageOut<>(collect, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public JobRecord getJobRecord(Integer jobRecordId) {
        Optional<JobRecord> optional = recordRepository.findById(jobRecordId);
        return optional.orElse(null);
    }


    public void updateMonitor(JobMonitor jobMonitor, boolean success) {
        JobMonitor monitor = monitorRepository.findByMonitorJobId(jobMonitor.getMonitorJobId());
        if (monitor == null) {
            throw new MyMessageException("当前作业对应的监控对象不存在");
        } else {
            BeanUtil.copyProperties(jobMonitor, monitor);
            if (success) {
                monitor.setMonitorSuccess(monitor.getMonitorSuccess() + 1);
            } else {
                monitor.setMonitorFail(monitor.getMonitorFail() + 1);
            }
            monitorRepository.save(monitor);
        }
    }

    public void addJobRecord(JobRecord jobRecord) {
        recordRepository.save(jobRecord);
    }

	public TaskCountRes countJob() {
		String sql = "SELECT count(1) total, IFNULL(sum(monitor_success),0) success, IFNULL(sum(monitor_fail),0) fail FROM `k_job_monitor`";
		TaskCountBO result = entityManagerUtil.executeNativeQueryForOne(sql, TaskCountBO.class);
		return BeanUtil.copyProperties(result, TaskCountRes.class);
	}
}
