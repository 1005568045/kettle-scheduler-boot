package org.kettle.scheduler.system.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import java.io.Serializable;
import java.util.Date;

/**
 * 转换监控表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "转换监控表-出参")
public class TransMonitorRes extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 转换名称
     */
    @ApiModelProperty(value = "转换名称")
    private String transName;

    /**
     * 监控的转换的ID
     */
    @ApiModelProperty(value = "监控的转换的ID")
    private Integer monitorTransId;

    /**
     * 成功次数
     */
    @ApiModelProperty(value = "成功次数")
    private Integer monitorSuccess;

    /**
     * 失败次数
     */
    @ApiModelProperty(value = "失败次数")
    private Integer monitorFail;

    /**
     * 监控状态（是否启动，1:启动；2:停止）
     */
    @ApiModelProperty(value = "监控状态（是否启动，1:启动；2:停止）")
    private Integer monitorStatus;

    /**
     * 运行状态（起始时间-结束时间,起始时间-结束时间……）
     */
    @ApiModelProperty(value = "运行状态")
    private String runStatus;

    /**
     * 上一次执行时间
     */
    @ApiModelProperty(value = "上一次执行时间")
    private Date lastExecuteTime;

    /**
     * 下一次执行时间
     */
    @ApiModelProperty(value = "下一次执行时间")
    private Date nextExecuteTime;


}