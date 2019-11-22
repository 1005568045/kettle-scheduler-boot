package org.kettle.scheduler.system.biz.repository;

import org.kettle.scheduler.system.biz.entity.JobMonitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

/**
 * @author lyf
 */
public interface JobMonitorRepository extends JpaRepository<JobMonitor, Integer>, JpaSpecificationExecutor<JobMonitor> {

    /**
     * 根据作业ID分页查询
     * @param monitorJobId 作业ID
     * @param monitorStatus 监控状态
     * @param pageable 分页参数
     * @return {@link Page}
     */
    Page<JobMonitor> findByMonitorJobIdInAndMonitorStatus(Collection<Integer> monitorJobId, Integer monitorStatus, Pageable pageable);
}