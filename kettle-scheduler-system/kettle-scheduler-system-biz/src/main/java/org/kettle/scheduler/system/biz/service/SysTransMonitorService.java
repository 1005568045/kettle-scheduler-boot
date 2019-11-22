package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.TransMonitorRes;
import org.kettle.scheduler.system.api.response.TransRecordRes;
import org.kettle.scheduler.system.biz.entity.Category;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.entity.TransMonitor;
import org.kettle.scheduler.system.biz.entity.TransRecord;
import org.kettle.scheduler.system.biz.entity.basic.IdEntity;
import org.kettle.scheduler.system.biz.repository.CategoryRepository;
import org.kettle.scheduler.system.biz.repository.TransMonitorRepository;
import org.kettle.scheduler.system.biz.repository.TransRecordRepository;
import org.kettle.scheduler.system.biz.repository.TransRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 转换监控业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysTransMonitorService {
    private final TransRepository transRepository;
    private final TransMonitorRepository monitorRepository;
    private final TransRecordRepository recordRepository;
    private final CategoryRepository categoryRepository;

    public SysTransMonitorService(TransRepository transRepository, TransMonitorRepository monitorRepository, TransRecordRepository recordRepository, CategoryRepository categoryRepository) {
        this.transRepository = transRepository;
        this.monitorRepository = monitorRepository;
        this.recordRepository = recordRepository;
        this.categoryRepository = categoryRepository;
    }

    public PageOut<TransMonitorRes> findTransMonitorListByPage(MonitorQueryReq query, Pageable pageable) {
        // 默认排序
        pageable.getSort().and(Sort.by(Sort.Direction.DESC, "addTime"));
        // 查询分类信息
        Optional<Category> optionalCategory = categoryRepository.findById(query.getCategoryId());
        String categoryName = "";
        if (optionalCategory.isPresent()) {
            categoryName = optionalCategory.get().getCategoryName();
        }
        // 获取Trans信息
        List<Trans> transList = transRepository.findByCategoryIdAndTransNameLike(query.getCategoryId(), query.getScriptName());
        List<Integer> transIds = transList.stream().map(IdEntity::getId).collect(Collectors.toList());
        // 根据转换ID分页查询
        Page<TransMonitor> page = monitorRepository.findByMonitorTransIdInAndMonitorStatus(transIds, query.getMonitorStatus(), pageable);
        // 封装数据
        String finalCategoryName = categoryName;
        List<TransMonitorRes> collect = page.get().map(t -> {
            TransMonitorRes res = BeanUtil.copyProperties(t, TransMonitorRes.class);
            res.setCategoryName(finalCategoryName);
            transList.stream().filter(a -> t.getMonitorTransId().equals(a.getId())).forEach(b -> res.setTransName(b.getTransName()));
            return res;
        }).collect(Collectors.toList());
        return new PageOut<>(collect, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public PageOut<TransRecordRes> findTransRecordList(Integer transId, Pageable pageable) {
        // 默认排序
        pageable.getSort().and(Sort.by(Sort.Direction.DESC, "addTime"));
        // 查询trans信息
        Optional<Trans> transOptional = transRepository.findById(transId);
        String transName = "";
        if (transOptional.isPresent()) {
            transName = transOptional.get().getTransName();
        }
        // 分页查询执行记录
        Page<TransRecord> page = recordRepository.findByRecordTransId(transId, pageable);
        // 封装数据
        String finalTransName = transName;
        List<TransRecordRes> collect = page.get().map(t -> {
            TransRecordRes res = BeanUtil.copyProperties(t, TransRecordRes.class);
            res.setTransName(finalTransName);
            return res;
        }).collect(Collectors.toList());
        return new PageOut<>(collect, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public TransRecord getTransRecord(Integer transRecordId) {
        Optional<TransRecord> optional = recordRepository.findById(transRecordId);
        return optional.orElse(null);
    }
}
