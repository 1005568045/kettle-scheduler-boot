package org.kettle.scheduler.system.biz.entity.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 原生sql查询结果
 *
 * @author lyf
 */
@Data
@Accessors(chain = true)
public class NativeQueryResultBO {

	/**
	 * 查询结果列表
	 */
	private List resultList;

	/**
	 * 数据总条数
	 */
	private Long total;
}
