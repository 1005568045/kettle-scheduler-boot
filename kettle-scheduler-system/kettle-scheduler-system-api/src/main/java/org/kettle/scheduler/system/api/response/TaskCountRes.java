package org.kettle.scheduler.system.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 脚本运行结果统计
 *
 * @author lyf
 */
@Data
@ApiModel(value = "脚本运行结果统计-出参")
public class TaskCountRes implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 总任务数
	 */
	@ApiModelProperty(value = "总任务数")
	private Integer total;

	/**
	 * 成功任务数
	 */
	@ApiModelProperty(value = "成功任务数")
	private Integer success;

	/**
	 * 失败任务数
	 */
	@ApiModelProperty(value = "失败任务数")
	private Integer fail;
}
