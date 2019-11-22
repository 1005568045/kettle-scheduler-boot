package org.kettle.scheduler.system.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 数据库连接类型表
 *
 * @author lyf
 */
@Table(name = "k_database_type")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class DatabaseType extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;


}