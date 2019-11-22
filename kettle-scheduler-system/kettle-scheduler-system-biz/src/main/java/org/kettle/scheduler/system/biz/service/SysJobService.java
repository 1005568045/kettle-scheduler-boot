package org.kettle.scheduler.system.biz.service;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageHelper;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.JobReq;
import org.kettle.scheduler.system.api.response.JobRes;
import org.kettle.scheduler.system.biz.entity.Job;
import org.kettle.scheduler.system.biz.repository.JobRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 作业管理业务逻辑层
 *
 * @author lyf
 */
@Slf4j
@Service
public class SysJobService {
    private final JobRepository jobRepository;

    public SysJobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(JobReq req) {
        Job job = BeanUtil.copyProperties(req, Job.class);
        jobRepository.save(job);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        jobRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        List<Job> jobs = jobRepository.findAllById(ids);
        jobRepository.deleteInBatch(jobs);
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

    public PageOut<JobRes> findJobListByPage(JobReq query, PageHelper page) {
        // 排序
        Sort sort = page.getSorts().isEmpty() ? Sort.by(Sort.Direction.DESC, "addTime") : page.getSorts();
        // 查询
        Job job = BeanUtil.copyProperties(query, Job.class);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<Job> example = Example.of(job, matcher);
        Page<Job> pageList = jobRepository.findAll(example, PageRequest.of(page.getNumber(), page.getSize(), sort));
        // 封装数据
        List<JobRes> collect = pageList.get().map(t -> BeanUtil.copyProperties(t, JobRes.class)).collect(Collectors.toList());
        return new PageOut<>(collect, pageList.getNumber(), pageList.getSize(), pageList.getTotalElements(), pageList.getTotalPages());
    }

    public JobRes getJobDetail(Integer id) {
        Optional<Job> optional = jobRepository.findById(id);
        return optional.map(job -> BeanUtil.copyProperties(job, JobRes.class)).orElse(null);
    }

    public void startAllJob() {

    }

    public void startJob(Integer id) {
        Optional<Job> optional = jobRepository.findById(id);
        if (optional.isPresent()) {
            Job job = optional.get();

        } else {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "当前作业不存在");
        }
    }

    public void stopAllJob() {

    }

    public void stopJob(Integer id) {
        Optional<Job> optional = jobRepository.findById(id);
        if (optional.isPresent()) {
            Job job = optional.get();

        } else {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "当前作业不存在");
        }
    }
}
