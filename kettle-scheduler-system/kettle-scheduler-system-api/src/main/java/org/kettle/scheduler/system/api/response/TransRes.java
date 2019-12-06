package org.kettle.scheduler.system.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;
import org.kettle.scheduler.system.api.enums.LogLevelEnum;
import org.kettle.scheduler.system.api.enums.RunStatusEnum;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;

import java.io.Serializable;

/**
 * 转换表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "转换表-出参")
public class TransRes extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;

	/**
	 * 分类名
	 */
	@ApiModelProperty(value = "分类名")
	private String categoryName;

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

	@ApiModelProperty(value = "执行类型显示值")
	public String getTransTypeStr() {
		return RunTypeEnum.getEnumDesc(transType);
	}

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
	 * 任务描述
	 */
	@ApiModelProperty(value = "任务描述")
	private String quartzDescription;

	/**
	 * 定时策略
	 */
	@ApiModelProperty(value = "定时策略")
	private String quartzCron;

    /**
     * 日志级别(Basic，Detailed，Error，Debug，Minimal，Rowlevel）
     */
    @ApiModelProperty(value = "日志级别")
    private String transLogLevel;

	@ApiModelProperty(value = "日志级别显示值")
	public String getTransLogLevelStr() {
		return LogLevelEnum.getEnumDesc(transLogLevel);
	}

	/**
     * 运行状态（1：正在运行；2：已停止）
     */
    @ApiModelProperty(value = "运行状态（1：正在运行；2：已停止）")
    private Integer transStatus;

	@ApiModelProperty(value = "运行状态显示值")
	public String getTransStatusStr() {
		return RunStatusEnum.getEnumDesc(transStatus);
	}
}