package org.kettle.scheduler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 系统代码枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum SystemCodeEnum implements BaseEnum<String> {
    /**
     * 系统功能模块
     */
    SYSTEM("SYSTEM", "系统功能模块"),
    /**
     * 定时任务模块
     */
    QUARTZ("QUARTZ", "定时任务模块"),
    /**
     * 文件服务模块
     */
    FILE("FILE", "文件服务模块"),
    /**
     * 公共组件模块
     */
    COMMON("COMMON", "公共组件模块"),
    /**
     * 未知系统代码
     */
    OTHER("OTHER", "未知系统代码");

    private String code;
    private String desc;

    public static SystemCodeEnum getEnum(String code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(String code) {
        SystemCodeEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
