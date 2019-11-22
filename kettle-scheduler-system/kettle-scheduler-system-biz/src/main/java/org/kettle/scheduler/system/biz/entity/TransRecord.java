package org.kettle.scheduler.system.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 转换执行记录表
 *
 * @author lyf
 */
@Table(name = "k_trans_record")
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class TransRecord extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 转换ID
     */
    @Column(name = "record_trans_id")
    private Integer recordTransId;

    /**
     * 启动时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 停止时间
     */
    @Column(name = "stop_time")
    private Date stopTime;

    /**
     * 任务执行结果（1：成功；2：失败）
     */
    @Column(name = "record_status")
    private Integer recordStatus;

    /**
     * 转换日志记录文件保存位置
     */
    @Column(name = "log_file_path")
    private String logFilePath;


}