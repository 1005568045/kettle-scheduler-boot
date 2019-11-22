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
     * 根据分类ID和作业名称模糊查询
     * @param categoryId 分类ID
     * @param jobName 作业名称
     * @return {@link List}
     */
    List<Job> findByCategoryIdAndJobNameLike(Integer categoryId, String jobName);
}