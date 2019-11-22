package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 监控查询条件
 *
 * @author lyf
 */
@Data
@ApiModel(value = "监控查询条件-入参")
public class MonitorQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;

    /**
     * 转换或作业名称
     */
    @ApiModelProperty(value = "转换或作业名称")
    private String scriptName;

    /**
     * 监控状态（是否启动，1:启动；2:停止）
     */
    @ApiModelProperty(value = "监控状态（是否启动，1:启动；2:停止）")
    private Integer monitorStatus;

}