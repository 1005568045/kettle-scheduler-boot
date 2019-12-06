package org.kettle.scheduler.system.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.kettle.scheduler.system.biz.entity.Category;

/**
 * @author lyf
 */
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

	/**
	 * 根据名称查询
	 * @param categoryName 名称
	 * @return {@link Category}
	 */
	Category getByCategoryName(String categoryName);
}