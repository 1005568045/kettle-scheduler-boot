package org.kettle.scheduler.system.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 分类表
 *
 * @author lyf
 */
@Entity
@Data
@Table(name = "k_category")
@EqualsAndHashCode(callSuper = true)
public class Category extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @Column(name = "category_name")
    private String categoryName;


}