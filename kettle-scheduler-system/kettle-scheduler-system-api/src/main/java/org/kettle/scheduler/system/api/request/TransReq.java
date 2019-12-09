package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import java.io.Serializable;

/**
 * 转换表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "转换表-入参")
public class TransReq extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;

    /**
     * 转换名称
     */
    @ApiModelProperty(value = "转换名称")
    private String transName;

    /**
     * 转换描述
     */
    @ApiModelProperty(value = "转换描述")
    private String transDescription;

    /**
     * 执行类型（rep：资源库；file：文件）
     */
    @ApiModelProperty(value = "执行类型")
    private String transType;

    /**
     * 转换保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）
     */
    @ApiModelProperty(value = "转换保存路径")
    private String transPath;

    /**
     * 转换的资源库ID
     */
    @ApiModelProperty(value = "转换的资源库ID")
    private Integer transRepositoryId;

    /**
     * 定时策略（外键ID）
     */
    @ApiModelProperty(value = "定时策略")
    private Integer transQuartz;

    /**
     * 日志级别(Basic，Detailed，Error，Debug，Minimal，Rowlevel）
     */
    @ApiModelProperty(value = "日志级别")
    private String transLogLevel;

    /**
     * 状态（1：正在运行；2：已停止）
     */
    @ApiModelProperty(value = "状态（1：正在运行；2：已停止）")
    private Integer transStatus;


}