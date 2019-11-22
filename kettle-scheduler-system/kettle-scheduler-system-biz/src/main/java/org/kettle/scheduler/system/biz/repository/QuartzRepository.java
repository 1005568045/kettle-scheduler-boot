package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.Quartz;

/**
 * @author lyf
 */
public interface QuartzRepository extends JpaRepository<Quartz, Integer>, JpaSpecificationExecutor<Quartz> {

}