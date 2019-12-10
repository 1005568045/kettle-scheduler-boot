package org.kettle.scheduler.system.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 运行结果枚举类
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum RunResultEnum implements BaseEnum<Integer> {
    /**
     * 资源库方式运行
     */
    SUCCESS(1, "运行成功"),
    /**
     * 文件方式运行
     */
    FAIL(2, "运行失败");

    private Integer code;
    private String desc;

    public static RunResultEnum getEnum(Integer code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(Integer code) {
        RunResultEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
