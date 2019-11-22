package org.kettle.scheduler.system.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户表
 *
 * @author lyf
 */
@Entity
@Data
@Table(name = "k_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 用于电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 用户账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;


}