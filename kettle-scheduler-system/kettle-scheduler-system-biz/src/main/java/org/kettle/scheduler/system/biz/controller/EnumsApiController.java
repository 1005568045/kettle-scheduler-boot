package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.EnumInfoDTO;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.EnumUtil;
import org.kettle.scheduler.core.enums.RepTypeEnum;
import org.kettle.scheduler.system.api.api.EnumsApi;
import org.kettle.scheduler.system.api.enums.LogLevelEnum;
import org.kettle.scheduler.system.api.enums.RunStatusEnum;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;
import org.kettle.scheduler.system.api.response.DatabaseTypeRes;
import org.kettle.scheduler.system.biz.service.SysDatabaseTypeService;
import org.pentaho.di.core.database.DatabaseMeta;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一枚举API
 *
 * @author lyf
 */
@RestController
public class EnumsApiController implements EnumsApi {
    private final SysDatabaseTypeService databaseTypeService;

    public EnumsApiController(SysDatabaseTypeService databaseTypeService) {
        this.databaseTypeService = databaseTypeService;
    }

    /**
     * 运行状态枚举类
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<EnumInfoDTO>> runStatus() {
        return Result.ok(EnumUtil.getEnumInfo(RunStatusEnum.values()));
    }

    /**
     * 脚本运行类型枚举类
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<EnumInfoDTO>> runType() {
        return Result.ok(EnumUtil.getEnumInfo(RunTypeEnum.values()));
    }

    /**
     * 数据库访问类型枚举
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<EnumInfoDTO>> databaseAccessType() {
        // 数据库访问类型编码
        String[] dbAccessTypeCode = DatabaseMeta.dbAccessTypeCode;
        // 数据库访问类型编码说明
        String[] dbAccessTypeDesc = DatabaseMeta.dbAccessTypeDesc;
        List<EnumInfoDTO> list = new ArrayList<>();
        for (int i = 0; i < dbAccessTypeCode.length; i++) {
            EnumInfoDTO dto = new EnumInfoDTO();
            dto.setCode(dbAccessTypeCode[i]);
            dto.setValue(dbAccessTypeDesc[i]);
            list.add(dto);
        }
        return Result.ok(list);
    }

    /**
     * 日志级别枚举
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<EnumInfoDTO>> logLevel() {
        return Result.ok(EnumUtil.getEnumInfo(LogLevelEnum.values()));
    }

    /**
     * 资源库类型枚举
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<EnumInfoDTO>> repositoryType() {
        return Result.ok(EnumUtil.getEnumInfo(RepTypeEnum.values()));
    }

    /**
     * 数据库类型列表
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<EnumInfoDTO>> databaseType() {
        List<DatabaseTypeRes> dbTypeList = databaseTypeService.findDbTypeList();
        List<EnumInfoDTO> collect = dbTypeList.stream().map(databaseTypeRes -> {
            EnumInfoDTO dto = new EnumInfoDTO();
            dto.setCode(databaseTypeRes.getCode());
            dto.setValue(databaseTypeRes.getDescription());
            return dto;
        }).collect(Collectors.toList());
        return Result.ok(collect);
    }
}
