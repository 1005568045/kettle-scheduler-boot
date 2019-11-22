package org.kettle.scheduler.system.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页监控任务统计-出参
 * @author lyf
 */
@Data
@ApiModel(value = "首页监控任务统计-出参")
public class HomeMonitorTaskCountRes {

    @ApiModelProperty(value = "总任务数")
    private Integer totalTaskNum;

    @ApiModelProperty(value = "作业任务数")
    private Integer jobTaskNum;

    @ApiModelProperty(value = "转换任务数")
    private Integer transTaskNum;
}
