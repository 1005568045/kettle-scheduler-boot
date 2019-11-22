package org.kettle.scheduler.system.api.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.kettle.scheduler.common.groups.Update;

import javax.validation.constraints.NotNull;

/**
 * 主键id
 *
 * @author lyf
 */
@Data
public class IdVO {

    @ApiModelProperty(value = "主键id", example = "主键id:更新时必填,添加时为空")
    @NotNull(message = "主键id不能是空", groups = Update.class)
    private Integer id;
}
