package org.kettle.scheduler.system.biz.entity.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * 作业监控表
 *
 * @author lyf
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class JobMonitorBO extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**
	 * 分类名称
	 */
	@Column(name = "category_name")
	private String categoryName;

	/**
	 * 作业名称
	 */
	@Column(name = "job_name")
	private String jobName;

    /**
     * 监控的作业ID
     */
    @Column(name = "monitor_job_id")
    private Integer monitorJobId;

    /**
     * 成功次数
     */
    @Column(name = "monitor_success")
    private Integer monitorSuccess;

    /**
     * 失败次数
     */
    @Column(name = "monitor_fail")
    private Integer monitorFail;

    /**
     * 监控状态（是否启动，1:启动；2:停止）
     */
    @Column(name = "monitor_status")
    private Integer monitorStatus;

    /**
     * 运行状态（起始时间-结束时间,起始时间-结束时间……）
     */
    @Column(name = "run_status")
    private String runStatus;

    /**
     * 上一次执行时间
     */
    @Column(name = "last_execute_time")
    private Date lastExecuteTime;

    /**
     * 下一次执行时间
     */
    @Column(name = "next_execute_time")
    private Date nextExecuteTime;


}