package org.kettle.scheduler.common.povo;

import lombok.Data;

import java.util.List;

/**
 * 树形结构基础类, 可以继承该类进行扩展
 *
 * @author lyf
 */
@Data
public class TreeDTO<T> {

    /**
     * id
     */
    private String id;

    /**
     * 父级id
     */
    private String parent;

    /**
     * 名称
     */
    private String text;

    /**
     * 是否叶子节点, 叶子节点是末尾节点, 没有子级
     */
    private Boolean leaf;

    /**
     * 是否展开
     */
    private Boolean expand;

    /**
     * 子级
     */
    private List<TreeDTO<T>> children;

    /**
     * 附加的扩展信息, 可以是自定义实体类
     */
    private T extra;
}
