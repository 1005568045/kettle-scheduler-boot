package org.kettle.scheduler.system.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作业表
 *
 * @author lyf
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "k_job")
@Data
public class Job extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID（外键ID）
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 作业名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 任务描述
     */
    @Column(name = "job_description")
    private String jobDescription;

    /**
     * 执行类型（rep：资源库；file：文件）
     */
    @Column(name = "job_type")
    private String jobType;

    /**
     * 作业保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）
     */
    @Column(name = "job_path")
    private String jobPath;

    /**
     * 作业的资源库ID
     */
    @Column(name = "job_repository_id")
    private Integer jobRepositoryId;

    /**
     * 定时策略（外键ID）
     */
    @Column(name = "job_quartz")
    private Integer jobQuartz;

	/**
	 * 同步策略(T+n)方式
	 */
	@Column(name = "sync_strategy")
	private String syncStrategy;

    /**
     * 日志级别(Basic，Detailed，Error，Debug，Minimal，Rowlevel）
     */
    @Column(name = "job_log_level")
    private String jobLogLevel;

    /**
     * 状态（1：正在运行；2：已停止）
     */
    @Column(name = "job_status")
    private Integer jobStatus;


}