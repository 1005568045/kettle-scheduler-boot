package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.Trans;

import java.util.List;

/**
 * @author lyf
 */
public interface TransRepository extends JpaRepository<Trans, Integer>, JpaSpecificationExecutor<Trans> {

    /**
     * 根据分类ID和转换名称模糊查询
     * @param categoryId 分类ID
     * @param transName 转换名称
     * @return {@link List}
     */
    List<Trans> findByCategoryIdAndTransNameLike(Integer categoryId, String transName);

    /**
     * 根据状态查询有效的转换
     * @param transStatus 运行状态{@link org.kettle.scheduler.system.api.enums.RunStatusEnum}
     * @return {@link List}
     */
    List<Trans> findByTransStatus(Integer transStatus);
}