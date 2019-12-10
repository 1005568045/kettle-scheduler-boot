package org.kettle.scheduler.system.biz.entity.bo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 脚本运行结果统计
 *
 * @author lyf
 */
@Data
@Entity
public class TaskCountBO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 总任务数
	 */
	@Id
	@Column(name = "total")
	private Integer total;

	/**
	 * 成功任务数
	 */
	@Column(name = "success")
	private Integer success;

	/**
	 * 失败任务数
	 */
	@Column(name = "fail")
	private Integer fail;
}
