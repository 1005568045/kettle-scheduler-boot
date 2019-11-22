package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.JobMonitorRes;
import org.kettle.scheduler.system.api.response.JobRecordRes;
import org.kettle.scheduler.system.biz.entity.Category;
import org.kettle.scheduler.system.biz.entity.Job;
import org.kettle.scheduler.system.biz.entity.JobMonitor;
import org.kettle.scheduler.system.biz.entity.JobRecord;
import org.kettle.scheduler.system.biz.entity.basic.IdEntity;
import org.kettle.scheduler.system.biz.repository.CategoryRepository;
import org.kettle.scheduler.system.biz.repository.JobMonitorRepository;
import org.kettle.scheduler.system.biz.repository.JobRecordRepository;
import org.kettle.scheduler.system.biz.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private final CategoryRepository categoryRepository;

    public SysJobMonitorService(JobRepository jobRepository, JobMonitorRepository monitorRepository, JobRecordRepository recordRepository, CategoryRepository categoryRepository) {
        this.jobRepository = jobRepository;
        this.monitorRepository = monitorRepository;
        this.recordRepository = recordRepository;
        this.categoryRepository = categoryRepository;
    }

    public PageOut<JobMonitorRes> findJobMonitorListByPage(MonitorQueryReq query, Pageable pageable) {
        // 默认排序
        pageable.getSort().and(Sort.by(Sort.Direction.DESC, "addTime"));
        // 查询分类信息
        Optional<Category> optionalCategory = categoryRepository.findById(query.getCategoryId());
        String categoryName = "";
        if (optionalCategory.isPresent()) {
            categoryName = optionalCategory.get().getCategoryName();
        }
        // 获取JOB信息
        List<Job> jobList = jobRepository.findByCategoryIdAndJobNameLike(query.getCategoryId(), query.getScriptName());
        List<Integer> jobIds = jobList.stream().map(IdEntity::getId).collect(Collectors.toList());
        // 根据作业ID分页查询
        Page<JobMonitor> page = monitorRepository.findByMonitorJobIdInAndMonitorStatus(jobIds, query.getMonitorStatus(), pageable);
        // 封装数据
        String finalCategoryName = categoryName;
        List<JobMonitorRes> collect = page.get().map(t -> {
            JobMonitorRes res = BeanUtil.copyProperties(t, JobMonitorRes.class);
            res.setCategoryName(finalCategoryName);
            jobList.stream().filter(a -> t.getMonitorJobId().equals(a.getId())).forEach(b -> res.setJobName(b.getJobName()));
            return res;
        }).collect(Collectors.toList());
        return new PageOut<>(collect, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public PageOut<JobRecordRes> findJobRecordList(Integer jobId, Pageable pageable) {
        // 默认排序
        pageable.getSort().and(Sort.by(Sort.Direction.DESC, "addTime"));
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
        return new PageOut<>(collect, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public JobRecord getJobRecord(Integer jobRecordId) {
        Optional<JobRecord> optional = recordRepository.findById(jobRecordId);
        return optional.orElse(null);
    }
}
