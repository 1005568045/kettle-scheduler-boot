package org.kettle.scheduler.common.povo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * 查询助手
 * <pre>
 *     包含分页对象和查询条件对象
 * </pre>
 *
 * @author lyf
 */
@Data
@NoArgsConstructor
public class QueryHelper<T> {

    @Valid
    private T query;
    private PageHelper page = new PageHelper();
}
