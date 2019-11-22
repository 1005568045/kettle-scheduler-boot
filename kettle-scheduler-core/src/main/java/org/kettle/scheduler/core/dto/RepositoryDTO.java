package org.kettle.scheduler.core.dto;

import lombok.Data;

/**
 * 连接数据资源库需要的参数
 *
 * @author lyf
 */
@Data
public class RepositoryDTO {

    /**
     * 资源库ID
     */
    private Integer id;

    /**
     * 资源库名称
     */
    private String repName;

    /**
     * 资源库类型: fileRep-文件, dbRep-数据库
     * {@link org.kettle.scheduler.core.enums.RepTypeEnum}
     */
    private String repType;

    /**
     * 资源库用户名
     */
    private String repUsername;

    /**
     * 资源库密码
     */
    private String repPassword;

    /**
     * 文件资源库路径
     * 格式：file:///E:/Workspaces/Kettle
     */
    private String repBasePath;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 数据库访问权限
     */
    private String dbAccess;

    /**
     * 数据库Host
     */
    private String dbHost;

    /**
     * 数据库端口
     */
    private String dbPort;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据库用户名
     */
    private String dbUsername;

    /**
     * 数据库密码
     */
    private String dbPassword;

}
