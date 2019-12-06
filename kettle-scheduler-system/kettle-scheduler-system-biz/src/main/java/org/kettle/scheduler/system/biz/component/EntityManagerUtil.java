package org.kettle.scheduler.system.biz.component;

import org.kettle.scheduler.system.biz.entity.bo.NativeQueryResultBO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 *	原生sql执行工具
 * @author lyf
 */
@Component
public class EntityManagerUtil {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 分页执行原生sql并绑定结果类
	 * @param selectSql 原生sql的select部分
	 * @param fromSql 原生sql的from部分
	 * @param pageable 分页参数
	 * @param resultClass 需要绑定的结果类
	 * @return {@link List}
	 */
	public NativeQueryResultBO executeNativeQuery(String selectSql, String fromSql, String orderSql, Pageable pageable, Class resultClass) {
		String sql = selectSql.concat(" ").concat(fromSql).concat(" ").concat(orderSql);
		// 初始化sql语句
		Query nativeQuery = entityManager.createNativeQuery(sql, resultClass);
		// 添加分页参数
		nativeQuery.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
		nativeQuery.setMaxResults(pageable.getPageSize());
		List resultList = nativeQuery.getResultList();

		// 查询总数
		Query countQuery = entityManager.createNativeQuery("SELECT count(1) " + fromSql);
		long total = Long.parseLong(countQuery.getSingleResult().toString());

		// 封装数据
		return new NativeQueryResultBO().setTotal(total).setResultList(resultList);
	}
}
