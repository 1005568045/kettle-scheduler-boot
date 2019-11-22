package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.DatabaseType;

/**
 * @author lyf
 */
public interface DatabaseTypeRepository extends JpaRepository<DatabaseType, Integer>, JpaSpecificationExecutor<DatabaseType> {

}