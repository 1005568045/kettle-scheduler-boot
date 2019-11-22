package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import java.io.Serializable;

/**
 * 分类表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "分类表-入参")
public class CategoryReq extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String categoryName;


}