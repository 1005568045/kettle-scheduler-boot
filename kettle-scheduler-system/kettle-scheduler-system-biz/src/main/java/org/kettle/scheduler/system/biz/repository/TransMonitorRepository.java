package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.TransMonitor;

import java.util.List;

/**
 * @author lyf
 */
public interface TransMonitorRepository extends JpaRepository<TransMonitor, Integer>, JpaSpecificationExecutor<TransMonitor> {

    /**
     * 根据转换ID和状态分页查询
     * @param transIds 转换ID
     * @param monitorStatus 监控状态
     * @param pageable 分页条件
     * @return {@link Page}
     */
    Page<TransMonitor> findByMonitorTransIdInAndMonitorStatus(List<Integer> transIds, Integer monitorStatus, Pageable pageable);

    /**
     * 根据转换ID查询监控信息
     * @param transId 转换ID
     * @return {@link TransMonitor}
     */
    TransMonitor findByMonitorTransId(Integer transId);
}