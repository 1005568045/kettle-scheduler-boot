package org.kettle.scheduler.system.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.biz.entity.basic.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 资源库表
 *
 * @author lyf
 */
@Entity
@Table(name = "k_repository")
@Data
@EqualsAndHashCode(callSuper = true)
public class Repository extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源库名称
     */
    @Column(name = "rep_name")
    private String repName;

    /**
     * 资源库类型: fileRep-文件, dbRep-数据库
     */
    @Column(name = "rep_type")
    private String repType;

    /**
     * 登录用户名
     */
    @Column(name = "rep_username")
    private String repUsername;

    /**
     * 登录密码
     */
    @Column(name = "rep_password")
    private String repPassword;

    /**
     * 文件资源库路径: rep_type=fileRep生效
     */
    @Column(name = "rep_base_path")
    private String repBasePath;

    /**
     * 资源库数据库类型（MYSQL、ORACLE）
     */
    @Column(name = "db_type")
    private String dbType;

    /**
     * 资源库数据库访问模式（"Native", "ODBC", "OCI", "Plugin", "JNDI")
     */
    @Column(name = "db_access")
    private String dbAccess;

    /**
     * 资源库数据库主机名或者IP地址
     */
    @Column(name = "db_host")
    private String dbHost;

    /**
     * 资源库数据库端口号
     */
    @Column(name = "db_port")
    private String dbPort;

    /**
     * 资源库数据库名称
     */
    @Column(name = "db_name")
    private String dbName;

    /**
     * 数据库登录账号
     */
    @Column(name = "db_username")
    private String dbUsername;

    /**
     * 数据库登录密码
     */
    @Column(name = "db_password")
    private String dbPassword;


}