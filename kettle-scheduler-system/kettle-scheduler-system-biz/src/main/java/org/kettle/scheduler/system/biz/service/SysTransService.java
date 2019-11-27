package org.kettle.scheduler.system.biz.service;

import com.google.common.collect.ImmutableMap;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.quartz.dto.QuartzDTO;
import org.kettle.scheduler.quartz.manage.QuartzManage;
import org.kettle.scheduler.system.api.enums.RunStatusEnum;
import org.kettle.scheduler.system.api.request.TransReq;
import org.kettle.scheduler.system.api.response.TransRes;
import org.kettle.scheduler.system.biz.entity.Quartz;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.entity.TransMonitor;
import org.kettle.scheduler.system.biz.quartz.TransQuartz;
import org.kettle.scheduler.system.biz.repository.QuartzRepository;
import org.kettle.scheduler.system.biz.repository.TransMonitorRepository;
import org.kettle.scheduler.system.biz.repository.TransRepository;
import org.quartz.JobDataMap;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 转换管理业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysTransService {
    private final TransRepository transRepository;
    private final QuartzRepository quartzRepository;
    private final TransMonitorRepository monitorRepository;

    public SysTransService(TransRepository transRepository, QuartzRepository quartzRepository, TransMonitorRepository monitorRepository) {
        this.transRepository = transRepository;
        this.quartzRepository = quartzRepository;
        this.monitorRepository = monitorRepository;
    }

    /**
     * 因程序中断后所有的定时会中断，因此在程序启动的时候需要初始化类调用该方法重新恢复定时任务
     */
    public void initTransQuartz() {
        List<Trans> transList = transRepository.findByTransStatus(RunStatusEnum.RUN.getCode());
        List<Quartz> quartzList = quartzRepository.findAllById(transList.stream().map(Trans::getTransQuartz).distinct().collect(Collectors.toList()));
        List<Quartz> quarts = quartzList.stream().filter(quartz -> StringUtil.hasText(quartz.getQuartzCron())).collect(Collectors.toList());
        List<Trans> collect = transList.stream().filter(trans -> quarts.stream().anyMatch(quartz -> quartz.getId().equals(trans.getTransQuartz()))).collect(Collectors.toList());
        collect.forEach(trans -> startTrans(trans.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(TransReq req) {
        Trans trans = BeanUtil.copyProperties(req, Trans.class);
        transRepository.save(trans);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Optional<Trans> optionalTrans = transRepository.findById(id);
        if (!optionalTrans.isPresent()) {
            throw new MyMessageException("转换不存在");
        }
        Trans trans = optionalTrans.get();

        // 删除前停止定时任务
        if (RunStatusEnum.RUN.getCode().equals(trans.getTransStatus())) {
            stopTrans(id);
        }
        transRepository.delete(trans);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        List<Trans> transList = transRepository.findAllById(ids);
        transList.forEach(trans -> delete(trans.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(TransReq req) {
        Optional<Trans> optional = transRepository.findById(req.getId());
        if (optional.isPresent()) {
            Trans trans = optional.get();
            BeanUtil.copyProperties(req, trans);
            transRepository.save(trans);
        }
    }

    public PageOut<TransRes> findTransListByPage(TransReq query, Pageable pageable) {
        // 默认排序
        pageable.getSort().and(Sort.by(Sort.Direction.DESC, "addTime"));
        // 查询
        Trans trans = BeanUtil.copyProperties(query, Trans.class);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<Trans> example = Example.of(trans, matcher);
        Page<Trans> pageList = transRepository.findAll(example, pageable);
        // 封装数据
        List<TransRes> collect = pageList.get().map(t -> BeanUtil.copyProperties(t, TransRes.class)).collect(Collectors.toList());
        return new PageOut<>(collect, pageList.getNumber(), pageList.getSize(), pageList.getTotalElements(), pageList.getTotalPages());
    }

    public TransRes getTransDetail(Integer id) {
        Optional<Trans> optional = transRepository.findById(id);
        return optional.map(trans -> BeanUtil.copyProperties(trans, TransRes.class)).orElse(null);
    }

    public Trans getTransById(Integer id) {
        Optional<Trans> optional = transRepository.findById(id);
        return optional.orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void startAllTrans() {
        List<Trans> transList = transRepository.findByTransStatus(RunStatusEnum.STOP.getCode());
        transList.forEach(trans -> startTrans(trans.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void startTrans(Integer id) {
        // 查询转换信息
        Optional<Trans> optionalTrans = transRepository.findById(id);
        if (!optionalTrans.isPresent()) {
            throw new MyMessageException("当前转换不存在");
        }
        Trans trans = optionalTrans.get();

        // 查询定时策略
        Optional<Quartz> optionalQuartz = quartzRepository.findById(trans.getTransQuartz());
        if (!optionalQuartz.isPresent()) {
            throw new MyMessageException("当前定时策略不存在");
        }
        Quartz quartz = optionalQuartz.get();

        // 修改监控状态
        updateTransMonitorStatus(trans.getId(), RunStatusEnum.RUN);

        // 修改转换状态
        trans.setTransStatus(RunStatusEnum.RUN.getCode());
        transRepository.save(trans);

        // 获取定时任务需要的参数
        QuartzDTO quartzDTO = getQuartzDTO(trans, quartz.getQuartzCron());
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
    public void stopAllTrans() {
        List<Trans> transList = transRepository.findByTransStatus(RunStatusEnum.RUN.getCode());
        transList.forEach(trans -> stopTrans(trans.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void stopTrans(Integer id) {
        // 查询转换信息
        Optional<Trans> optionalTrans = transRepository.findById(id);
        if (!optionalTrans.isPresent()) {
            throw new MyMessageException("当前转换不存在");
        }
        Trans trans = optionalTrans.get();

        // 已经关闭的任务不在处理
        if (RunStatusEnum.STOP.getCode().equals(trans.getTransStatus())) {
            return;
        }

        // 修改监控状态
        updateTransMonitorStatus(trans.getId(), RunStatusEnum.STOP);

        // 修改转换状态
        trans.setTransStatus(RunStatusEnum.STOP.getCode());
        transRepository.save(trans);

        // 关闭定时任务
        QuartzManage.removeJob(getQuartzDTO(trans, null));
    }

    /**
     * 根据定时策略和转换组装执行参数
     * @param trans 转换信息
     * @param cron 定时策略
     * @return {@link QuartzDTO}
     */
    private QuartzDTO getQuartzDTO(Trans trans, String cron) {
        String categoryId = trans.getCategoryId()==null ? "null" : String.valueOf(trans.getCategoryId());

        QuartzDTO dto = new QuartzDTO();
        dto.setJobName("JOB@" + trans.getId());
        dto.setJobGroupName("JOB_GROUP@" + categoryId + "@" + trans.getId());
        dto.setTriggerName("TRIGGER@" + trans.getId());
        dto.setTriggerGroupName("TRIGGER_GROUP@" + categoryId + "@" + trans.getId());
        if (StringUtil.hasText(cron)) {
            dto.setCron(cron);
        }
        dto.setJobClass(TransQuartz.class);
        dto.setJobDataMap(new JobDataMap(ImmutableMap.of("id", trans.getId())));
        return dto;
    }

    /**
     * 修改监控信息状态
     * @param transId 转换ID
     * @param statusEnum 状态枚举
     */
    private void updateTransMonitorStatus(Integer transId, RunStatusEnum statusEnum) {
        TransMonitor transMonitor = monitorRepository.findByMonitorTransId(transId);
        if (transMonitor == null) {
            transMonitor = new TransMonitor();
            transMonitor.setMonitorFail(0);
            transMonitor.setMonitorSuccess(0);
            transMonitor.setMonitorTransId(transId);
            transMonitor.setRunStatus(System.currentTimeMillis() + "-");
        } else {
            switch (statusEnum) {
                case RUN:
                    String runStatus = transMonitor.getRunStatus();
                    if (runStatus.endsWith("-")) {
                        runStatus = runStatus.concat(String.valueOf(System.currentTimeMillis()));
                    }
                    transMonitor.setRunStatus(runStatus.concat(",").concat(System.currentTimeMillis() + "-"));
                    break;
                case STOP:
                    transMonitor.setRunStatus(transMonitor.getRunStatus().concat(String.valueOf(System.currentTimeMillis())));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + statusEnum);
            }
        }
        transMonitor.setMonitorStatus(statusEnum.getCode());
        monitorRepository.save(transMonitor);
    }
}
