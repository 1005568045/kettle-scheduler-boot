package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.Job;

import java.util.List;

/**
 * @author lyf
 */
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {

    /**
     * 根据运行状态查询作业信息
     * @param jobStatus 运行状态{@link org.kettle.scheduler.system.api.enums.RunStatusEnum}
     * @return {@link List}
     */
    List<Job> findByJobStatus(Integer jobStatus);

	/**
	 * 根据作业名称查询
	 * @param jobName 作业名称
	 * @return {@link Job}
	 */
	Job getByJobName(String jobName);

	/**
	 * 根据状态统计作业数量
	 * @param jobStatus 运行状态
	 * @return {@link Integer}
	 */
	Integer countByJobStatus(Integer jobStatus);
}