package org.kettle.scheduler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 性别枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum SexEnum implements BaseEnum<Integer> {
    /**
     * 未知, 没有填写的时候默认
     */
    UNKNOWN(0, "未知"),
    /**
     * 男
     */
    MALE(1, "男"),
    /**
     * 女
     */
    FEMALE(2, "女");

    private final Integer code;
    private final String desc;

    public static SexEnum getEnum(Integer code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(Integer code) {
        SexEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
