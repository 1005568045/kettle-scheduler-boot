package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.PageHelper;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.TaskCountRes;
import org.kettle.scheduler.system.api.response.TransMonitorRes;
import org.kettle.scheduler.system.api.response.TransRecordRes;
import org.kettle.scheduler.system.biz.component.EntityManagerUtil;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.entity.TransMonitor;
import org.kettle.scheduler.system.biz.entity.TransRecord;
import org.kettle.scheduler.system.biz.entity.bo.NativeQueryResultBO;
import org.kettle.scheduler.system.biz.entity.bo.TaskCountBO;
import org.kettle.scheduler.system.biz.entity.bo.TransMonitorBO;
import org.kettle.scheduler.system.biz.repository.TransMonitorRepository;
import org.kettle.scheduler.system.biz.repository.TransRecordRepository;
import org.kettle.scheduler.system.biz.repository.TransRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
	private final EntityManagerUtil entityManagerUtil;

    public SysTransMonitorService(TransRepository transRepository, TransMonitorRepository monitorRepository,
			TransRecordRepository recordRepository, EntityManagerUtil entityManagerUtil) {
        this.transRepository = transRepository;
        this.monitorRepository = monitorRepository;
        this.recordRepository = recordRepository;
		this.entityManagerUtil = entityManagerUtil;
	}

    public PageOut<TransMonitorRes> findTransMonitorListByPage(MonitorQueryReq query, Pageable pageable) {
    	String selectSql = "SELECT a.*,b.trans_name,c.category_name ";
    	// 动态拼接from部分的sql
		StringBuilder fromSql = new StringBuilder(" FROM `k_trans_monitor` a ");
		fromSql.append("INNER JOIN k_trans b ON a.monitor_trans_id=b.id ");
		fromSql.append("LEFT JOIN k_category c ON b.category_id=c.id ");
		if (query!=null) {
			fromSql.append("WHERE 1=1 ");
			if (!StringUtil.isEmpty(query.getScriptName())) {
				fromSql.append("AND b.trans_name like '%").append(query.getScriptName()).append("%'").append(" ");
			}
			if (query.getMonitorStatus() != null) {
				fromSql.append("AND a.monitor_status = ").append(query.getMonitorStatus()).append(" ");
			}
			if (query.getCategoryId() != null) {
				fromSql.append("AND b.category_id = ").append(query.getCategoryId()).append(" ");
			}
		}
		// order by 部分的sql
		String orderSql = "order by a.add_time desc ";
		// 初始化sql语句
		NativeQueryResultBO resultBo = entityManagerUtil.executeNativeQueryForList(selectSql, fromSql.toString(), orderSql, pageable, TransMonitorBO.class);
		List<TransMonitorRes> list = new ArrayList<>();
		for (Object o : resultBo.getResultList()) {
			list.add(BeanUtil.copyProperties(o, TransMonitorRes.class));
		}
        // 封装数据
        return new PageOut<>(list, pageable.getPageNumber(), pageable.getPageSize(), resultBo.getTotal());
    }

    public PageOut<TransRecordRes> findTransRecordList(Integer transId, PageHelper pageHelper) {
        // 默认排序
		Pageable pageable = pageHelper.getPageable(Sort.by(Sort.Direction.DESC, "addTime"));
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

	public TaskCountRes countTrans() {
    	String sql = "SELECT count(1) total, IFNULL(sum(monitor_success),0) success, IFNULL(sum(monitor_fail),0) fail FROM `k_trans_monitor`";
		TaskCountBO result = entityManagerUtil.executeNativeQueryForOne(sql, TaskCountBO.class);
		return BeanUtil.copyProperties(result, TaskCountRes.class);
	}
}
