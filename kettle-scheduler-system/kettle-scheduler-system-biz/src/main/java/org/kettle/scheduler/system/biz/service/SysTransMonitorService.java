package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.TransMonitorRes;
import org.kettle.scheduler.system.api.response.TransRecordRes;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.entity.TransMonitor;
import org.kettle.scheduler.system.biz.entity.TransRecord;
import org.kettle.scheduler.system.biz.entity.bo.TransMonitorBO;
import org.kettle.scheduler.system.biz.repository.TransMonitorRepository;
import org.kettle.scheduler.system.biz.repository.TransRecordRepository;
import org.kettle.scheduler.system.biz.repository.TransRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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

    @PersistenceContext
	private EntityManager entityManager;

    public SysTransMonitorService(TransRepository transRepository, TransMonitorRepository monitorRepository,
			TransRecordRepository recordRepository) {
        this.transRepository = transRepository;
        this.monitorRepository = monitorRepository;
        this.recordRepository = recordRepository;
	}

    public PageOut<TransMonitorRes> findTransMonitorListByPage(MonitorQueryReq query, Pageable pageable) {
    	// 动态拼接sql
		StringBuilder sql = new StringBuilder(" FROM `k_trans_monitor` a ");
		sql.append("INNER JOIN k_trans b ON a.monitor_trans_id=b.id ");
		sql.append("LEFT JOIN k_category c ON b.category_id=c.id ");
		if (query!=null) {
			sql.append("WHERE 1=1 ");
			if (!StringUtil.isEmpty(query.getScriptName())) {
				sql.append("AND b.trans_name like '%").append(query.getScriptName()).append("%'");
			}
			if (query.getMonitorStatus() != null) {
				sql.append("AND a.monitor_status = ").append(query.getMonitorStatus());
			}
			if (query.getCategoryId() != null) {
				sql.append("AND b.category_id = ").append(query.getCategoryId());
			}
		}
		sql.append(" ").append("order by a.add_time desc ");
		// 初始化sql语句
		Query nativeQuery = entityManager.createNativeQuery("SELECT a.*,b.trans_name,c.category_name  " + sql.toString(), TransMonitorBO.class);
		Query countQuery = entityManager.createNativeQuery("SELECT count(1) " + sql.toString());
		// 添加分页参数
		nativeQuery.setFirstResult(pageable.getPageNumber());
		nativeQuery.setMaxResults(pageable.getPageSize());
		// 执行sql
		long total = Long.parseLong(countQuery.getSingleResult().toString());
		List resultList = nativeQuery.getResultList();
		List<TransMonitorRes> list = new ArrayList<>();
		for (Object o : resultList) {
			list.add(BeanUtil.copyProperties(o, TransMonitorRes.class));
		}
        // 封装数据
        return new PageOut<>(list, pageable.getPageNumber(), pageable.getPageSize(), total);
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
        return new PageOut<>(collect, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public TransRecord getTransRecord(Integer transRecordId) {
        Optional<TransRecord> optional = recordRepository.findById(transRecordId);
        return optional.orElse(null);
    }

    public void updateMonitor(TransMonitor transMonitor, boolean success) {
        TransMonitor monitor = monitorRepository.findByMonitorTransId(transMonitor.getMonitorTransId());
        if (monitor == null) {
            throw new MyMessageException("当前转换对应的监控对象不存在");
        } else {
            BeanUtil.copyProperties(transMonitor, monitor);
            if (success) {
                monitor.setMonitorSuccess(monitor.getMonitorSuccess() + 1);
            } else {
                monitor.setMonitorFail(monitor.getMonitorFail() + 1);
            }
            monitorRepository.save(monitor);
        }
    }

    public void addTransRecord(TransRecord transRecord) {
        recordRepository.save(transRecord);
    }
}
