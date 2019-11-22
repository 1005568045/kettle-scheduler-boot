package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import java.io.Serializable;

/**
 * 定时任务表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "定时任务表-入参")
public class QuartzReq extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务描述
     */
    @ApiModelProperty(value = "任务描述")
    private String quartzDescription;

    /**
     * 定时策略
     */
    @ApiModelProperty(value = "定时策略")
    private String quartzCron;


}