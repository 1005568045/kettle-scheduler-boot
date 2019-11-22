package org.kettle.scheduler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 启用禁用状态枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum EnableEnum implements BaseEnum<Integer> {
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 可用
     */
    ENABLE(1, "可用");

    private Integer code;
    private String desc;

    public static EnableEnum getEnum(Integer code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(Integer code) {
        EnableEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
