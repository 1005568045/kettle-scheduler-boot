package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.TransRecord;

/**
 * @author lyf
 */
public interface TransRecordRepository extends JpaRepository<TransRecord, Integer>, JpaSpecificationExecutor<TransRecord> {

    /**
     * 根据转换ID分页查询执行记录
     * @param transId 转换ID
     * @param pageable 分页参数
     * @return {@link Page}
     */
    Page<TransRecord> findByRecordTransId(Integer transId, Pageable pageable);
}