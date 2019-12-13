package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业执行记录表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "作业执行记录表-入参")
public class JobRecordReq extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 作业ID
     */
    @ApiModelProperty(value = "作业ID")
    private Integer recordJobId;

    /**
     * 启动时间
     */
    @ApiModelProperty(value = "启动时间")
    private Date startTime;

    /**
     * 停止时间
     */
    @ApiModelProperty(value = "停止时间")
    private Date stopTime;

    /**
     * 任务执行结果（1：成功；0：失败）
     */
    @ApiModelProperty(value = "任务执行结果（1：成功；0：失败）")
    private Integer recordStatus;

    /**
     * 作业日志记录文件保存位置
     */
    @ApiModelProperty(value = "作业日志记录文件保存位置")
    private String logFilePath;


}