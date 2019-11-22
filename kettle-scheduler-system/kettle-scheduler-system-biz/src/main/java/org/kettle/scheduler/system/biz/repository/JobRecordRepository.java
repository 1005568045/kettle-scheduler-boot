package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.JobRecord;

/**
 * @author lyf
 */
public interface JobRecordRepository extends JpaRepository<JobRecord, Integer>, JpaSpecificationExecutor<JobRecord> {

    /**
     * 根据作业ID分页查询
     * @param recordJobId 作业ID
     * @param pageable 分页参数
     * @return {@link Page}
     */
    Page<JobRecord> findByRecordJobId(Integer recordJobId, Pageable pageable);
}