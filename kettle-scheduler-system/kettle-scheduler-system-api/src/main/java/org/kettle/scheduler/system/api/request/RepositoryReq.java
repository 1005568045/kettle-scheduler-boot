package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import java.io.Serializable;

/**
 * 资源库表
 *
 * @author lyf
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "资源库表-入参")
public class RepositoryReq extends BasicVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源库名称
     */
    @ApiModelProperty(value = "资源库名称")
    private String repName;

    /**
     * 资源库类型: fileRep-文件, dbRep-数据库
     */
    @ApiModelProperty(value = "资源库类型: fileRep-文件, dbRep-数据库")
    private String repType;

    /**
     * 登录用户名
     */
    @ApiModelProperty(value = "登录用户名")
    private String repUsername;

    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码")
    private String repPassword;

    /**
     * 文件资源库路径: rep_type=fileRep生效
     */
    @ApiModelProperty(value = "文件资源库路径")
    private String repBasePath;

    /**
     * 资源库数据库类型（MYSQL、ORACLE）
     */
    @ApiModelProperty(value = "资源库数据库类型")
    private String dbType;

    /**
     * 资源库数据库访问模式（"Native", "ODBC", "OCI", "Plugin", "JNDI")
     */
    @ApiModelProperty(value = "资源库数据库访问模式")
    private String dbAccess;

    /**
     * 资源库数据库主机名或者IP地址
     */
    @ApiModelProperty(value = "资源库数据库主机名或者IP地址")
    private String dbHost;

    /**
     * 资源库数据库端口号
     */
    @ApiModelProperty(value = "资源库数据库端口号")
    private String dbPort;

    /**
     * 资源库数据库名称
     */
    @ApiModelProperty(value = "资源库数据库名称")
    private String dbName;

    /**
     * 数据库登录账号
     */
    @ApiModelProperty(value = "数据库登录账号")
    private String dbUsername;

    /**
     * 数据库登录密码
     */
    @ApiModelProperty(value = "数据库登录密码")
    private String dbPassword;


}