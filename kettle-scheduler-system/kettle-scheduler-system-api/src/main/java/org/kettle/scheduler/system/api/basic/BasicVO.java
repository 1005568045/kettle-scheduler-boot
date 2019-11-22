package org.kettle.scheduler.system.api.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 公共属性字段类
 *
 * @author lyf
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BasicVO extends IdVO {

    /**
     * 添加者
     */
    @ApiModelProperty(value = "添加者")
    private Integer addUser;

    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间")
    private Date addTime;

    /**
     * 编辑者
     */
    @ApiModelProperty(value = "编辑者")
    private Integer editUser;

    /**
     * 编辑时间
     */
    @ApiModelProperty(value = "编辑时间")
    private Date editTime;

    /**
     * 是否删除（1：删除；0：存在）
     */
    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;
}
