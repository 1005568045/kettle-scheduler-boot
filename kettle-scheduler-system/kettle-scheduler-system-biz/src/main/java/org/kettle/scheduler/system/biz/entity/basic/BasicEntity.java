package org.kettle.scheduler.system.biz.entity.basic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * JPA公共属性字段类
 *
 * @author lyf
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public class BasicEntity extends IdEntity {
    /**
     * 添加者
     */
    @Column(name = "add_user")
    private Integer addUser;

    /**
     * 添加时间, 添加{@code @CreatedDate}注解后, 系统会自动添加日期
     */
    @CreatedDate
    @Column(name = "add_time")
    private Date addTime;

    /**
     * 编辑者
     */
    @Column(name = "edit_user")
    private Integer editUser;

    /**
     * 编辑时间, 添加{@code @LastModifiedDate}注解后, 系统会自动更新日期
     */
    @LastModifiedDate
    @Column(name = "edit_time")
    private Date editTime;

    /**
     * 是否删除（1：删除；0：存在）
     */
    @Column(name = "del_flag")
    private Integer delFlag = 0;
}
