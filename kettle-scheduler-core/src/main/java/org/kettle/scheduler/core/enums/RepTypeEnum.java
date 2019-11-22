package org.kettle.scheduler.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 资源库类型枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum RepTypeEnum implements BaseEnum<String> {
    /**
     * 文件资源库
     */
    FILE("fileRep", "文件资源库"),
    /**
     * 数据库资源库
     */
    DB("dbRep", "数据库资源库");

    private final String code;
    private final String desc;

    public static RepTypeEnum getEnum(String code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(String code) {
        RepTypeEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
