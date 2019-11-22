package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.EnumInfoDTO;
import org.kettle.scheduler.common.povo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 统一枚举api
 *
 * @author admin
 */
@Api(tags = "统一枚举api")
@RequestMapping("/enum")
public interface EnumsApi {

    /**
     * 运行状态枚举类
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "运行状态枚举类")
    @GetMapping("/runStatus")
    Result<List<EnumInfoDTO>> runStatus();

    /**
     * 脚本运行类型枚举类
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "脚本运行类型枚举类")
    @GetMapping("/runType")
    Result<List<EnumInfoDTO>> runType();

    /**
     * 数据库访问类型枚举
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "数据库访问类型枚举")
    @GetMapping("/databaseAccessType")
    Result<List<EnumInfoDTO>> databaseAccessType();

    /**
     * 日志级别枚举
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "日志级别枚举")
    @GetMapping("/logLevel")
    Result<List<EnumInfoDTO>> logLevel();

    /**
     * 资源库类型枚举
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "资源库类型枚举")
    @GetMapping("/repositoryType")
    Result<List<EnumInfoDTO>> repositoryType();

    /**
     * 数据库类型列表
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "数据库类型列表")
    @GetMapping("/databaseType")
    Result<List<EnumInfoDTO>> databaseType();
}
