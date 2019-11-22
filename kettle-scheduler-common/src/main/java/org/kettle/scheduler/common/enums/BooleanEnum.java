package org.kettle.scheduler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 布尔标识枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum BooleanEnum implements BaseEnum<Integer> {
    /**
     * 是
     */
    TRUE(1, "是"),
    /**
     * 否
     */
    FALSE(0, "否");

    private final Integer code;
    private final String desc;

    public static BooleanEnum getEnum(Integer code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(Integer code) {
        BooleanEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
