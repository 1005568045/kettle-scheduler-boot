package org.kettle.scheduler.system.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.system.api.basic.BasicVO;

import javax.validation.constraints.NotBlank;
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
	@NotBlank(message = "资源库名称不能为空")
    private String repName;

    /**
     * 资源库类型: fileRep-文件, dbRep-数据库
     */
    @ApiModelProperty(value = "资源库类型: fileRep-文件, dbRep-数据库")
	@NotBlank(message = "资源库类型不能为空")
    private String repType;

    /**
     * 登录用户名
     */
    @ApiModelProperty(value = "登录用户名")
	@NotBlank(message = "登录用户名不能为空")
    private String repUsername;

    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码")
	@NotBlank(message = "登录密码不能为空")
    private String repPassword;

    /**
     * 文件资源库路径: rep_type=fileRep生效
     */
    @ApiModelProperty(value = "文件资源库路径")
	@NotBlank(message = "文件资源库路径不能为空", groups = {FileRep.class})
    private String repBasePath;

    /**
     * 资源库数据库类型（MYSQL、ORACLE）
     */
    @ApiModelProperty(value = "资源库数据库类型")
	@NotBlank(message = "资源库数据库类型不能为空", groups = {DatabaseRep.class})
    private String dbType;

    /**
     * 资源库数据库访问模式（"Native", "ODBC", "OCI", "Plugin", "JNDI")
     */
    @ApiModelProperty(value = "资源库数据库访问模式")
	@NotBlank(message = "资源库数据库访问模式不能为空", groups = {DatabaseRep.class})
    private String dbAccess;

    /**
     * 资源库数据库主机名或者IP地址
     */
    @ApiModelProperty(value = "资源库数据库主机名或者IP地址")
	@NotBlank(message = "数据库主机名或者IP地址不能为空", groups = {DatabaseRep.class})
    private String dbHost;

    /**
     * 资源库数据库端口号
     */
    @ApiModelProperty(value = "资源库数据库端口号")
	@NotBlank(message = "数据库端口号不能为空", groups = {DatabaseRep.class})
    private String dbPort;

    /**
     * 资源库数据库名称
     */
    @ApiModelProperty(value = "资源库数据库名称")
	@NotBlank(message = "数据库名称不能为空", groups = {DatabaseRep.class})
    private String dbName;

    /**
     * 数据库登录账号
     */
    @ApiModelProperty(value = "数据库登录账号")
	@NotBlank(message = "数据库登录账号不能为空", groups = {DatabaseRep.class})
    private String dbUsername;

    /**
     * 数据库登录密码
     */
    @ApiModelProperty(value = "数据库登录密码")
	@NotBlank(message = "数据库登录密码不能为空", groups = {DatabaseRep.class})
    private String dbPassword;

	/**
	 * 验证文件资源库需要
	 */
	public interface FileRep {}

	/**
	 * 验证数据库资源库需要
	 */
	public interface DatabaseRep {}
}