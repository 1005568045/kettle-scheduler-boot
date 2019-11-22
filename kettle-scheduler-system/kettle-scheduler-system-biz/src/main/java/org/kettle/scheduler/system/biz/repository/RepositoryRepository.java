package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.Repository;

/**
 * @author lyf
 */
public interface RepositoryRepository extends JpaRepository<Repository, Integer>, JpaSpecificationExecutor<Repository> {

}