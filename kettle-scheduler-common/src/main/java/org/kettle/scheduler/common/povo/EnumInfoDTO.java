package org.kettle.scheduler.common.povo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 枚举类型对外显示
 *
 * @author lyf
 */
@Data
@ApiModel(value = "枚举类型对外显示-出参")
public class EnumInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "枚举编码")
    private String code;

    @ApiModelProperty(value = "枚举显示值")
    private String value;
}
