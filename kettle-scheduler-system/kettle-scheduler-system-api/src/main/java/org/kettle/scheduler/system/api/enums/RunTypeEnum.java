package org.kettle.scheduler.system.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 脚本运行类型枚举类
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum RunTypeEnum implements BaseEnum<String> {
    /**
     * 资源库方式运行
     */
    REP("rep", "资源库方式运行"),
    /**
     * 文件方式运行
     */
    FILE("file", "文件方式运行");

    private String code;
    private String desc;

    public static RunTypeEnum getEnum(String code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(String code) {
        RunTypeEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
