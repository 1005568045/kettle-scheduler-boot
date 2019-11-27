package org.kettle.scheduler.system.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 首页监控任务运行状况统计-出参
 * @author lyf
 */
@Accessors(chain = true)
@Data
@ApiModel(value = "首页监控任务运行状况统计-出参")
public class HomeMonitorTaskRunRes {

    @ApiModelProperty(value = "运行时间")
    private String runTime;

    @ApiModelProperty(value = "作业执行量")
    private Integer jobRunNum;

    @ApiModelProperty(value = "转换执行量")
    private Integer transRunNum;
}
