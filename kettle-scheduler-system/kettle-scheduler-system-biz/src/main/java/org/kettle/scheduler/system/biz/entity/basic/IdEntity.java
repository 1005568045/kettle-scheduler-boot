package org.kettle.scheduler.system.biz.entity.basic;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA会根据生成策略自动生成主键id
 *
 * @author lyf
 */
@MappedSuperclass
@Data
public class IdEntity {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;
}
