package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.QuartzReq;
import org.kettle.scheduler.system.api.response.QuartzRes;
import org.kettle.scheduler.system.biz.entity.Quartz;
import org.kettle.scheduler.system.biz.repository.QuartzRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 定时任务管理业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysQuartzService {
    private final QuartzRepository quartzRepository;

    public SysQuartzService(QuartzRepository quartzRepository) {
        this.quartzRepository = quartzRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(QuartzReq req) {
        Quartz quartz = BeanUtil.copyProperties(req, Quartz.class);
        quartzRepository.save(quartz);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        quartzRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        List<Quartz> quarts = quartzRepository.findAllById(ids);
        quartzRepository.deleteInBatch(quarts);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(QuartzReq req) {
        Optional<Quartz> optional = quartzRepository.findById(req.getId());
        if (optional.isPresent()) {
            Quartz quartz = optional.get();
            BeanUtil.copyProperties(req, quartz);
            quartzRepository.save(quartz);
        }
    }

    public PageOut<QuartzRes> findQuartzListByPage(QuartzReq query, Pageable pageable) {
        // 默认排序
        pageable.getSort().and(Sort.by(Sort.Direction.DESC, "addTime"));
        // 查询
        Quartz quartz = BeanUtil.copyProperties(query, Quartz.class);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<Quartz> example = Example.of(quartz, matcher);
        Page<Quartz> pageList = quartzRepository.findAll(example, pageable);
        // 封装数据
        List<QuartzRes> collect = pageList.get().map(t -> BeanUtil.copyProperties(t, QuartzRes.class)).collect(Collectors.toList());
        return new PageOut<>(collect, pageList.getNumber(), pageList.getSize(), pageList.getTotalElements());
    }

    public QuartzRes getQuartzDetail(Integer id) {
        Optional<Quartz> optional = quartzRepository.findById(id);
        return optional.map(quartz -> BeanUtil.copyProperties(quartz, QuartzRes.class)).orElse(null);
    }

    public List<QuartzRes> findQuartzList() {
        List<Quartz> list = quartzRepository.findAll();
        return list.stream().map(t -> BeanUtil.copyProperties(t, QuartzRes.class)).collect(Collectors.toList());
    }
}
