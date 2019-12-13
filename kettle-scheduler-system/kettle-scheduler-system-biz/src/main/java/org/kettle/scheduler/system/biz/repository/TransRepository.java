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
     * 根据状态查询有效的转换
     * @param transStatus 运行状态{@link org.kettle.scheduler.system.api.enums.RunStatusEnum}
     * @return {@link List}
     */
    List<Trans> findByTransStatus(Integer transStatus);

	/**
	 * 根据转换名称查询
	 * @param transName 转换名称
	 * @return {@link Trans}
	 */
	Trans getByTransName(String transName);

	/**
	 * 根据状态统计转换数量
	 * @param transStatus 运行状态
	 * @return {@link Integer}
	 */
	Integer countByTransStatus(Integer transStatus);
}