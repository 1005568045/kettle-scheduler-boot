package org.kettle.scheduler.system.biz.repository;

import org.kettle.scheduler.system.biz.entity.TransMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lyf
 */
public interface TransMonitorRepository extends JpaRepository<TransMonitor, Integer>, JpaSpecificationExecutor<TransMonitor> {

    /**
     * 根据转换ID查询监控信息
     * @param transId 转换ID
     * @return {@link TransMonitor}
     */
    TransMonitor findByMonitorTransId(Integer transId);
}