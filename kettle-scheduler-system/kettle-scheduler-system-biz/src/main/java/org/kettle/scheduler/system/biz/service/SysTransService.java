package org.kettle.scheduler.system.biz.service;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.TransReq;
import org.kettle.scheduler.system.api.response.TransRes;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.repository.TransRepository;
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
@Slf4j
@Service
public class SysTransService {
    private final TransRepository transRepository;

    public SysTransService(TransRepository transRepository) {
        this.transRepository = transRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(TransReq req) {
        Trans trans = BeanUtil.copyProperties(req, Trans.class);
        transRepository.save(trans);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        transRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        List<Trans> trans = transRepository.findAllById(ids);
        transRepository.deleteInBatch(trans);
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

    public void startAllTrans() {
    }

    public void startTrans(Integer id) {
        Optional<Trans> optional = transRepository.findById(id);
        if (optional.isPresent()) {
            Trans trans = optional.get();

        } else {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "当前转换不存在");
        }
    }

    public void stopAllTrans() {

    }

    public void stopTrans(Integer id) {
        Optional<Trans> optional = transRepository.findById(id);
        if (optional.isPresent()) {
            Trans trans = optional.get();

        } else {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "当前转换不存在");
        }
    }
}
