package org.kettle.scheduler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/***
 * 全局状态码枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum GlobalStatusEnum implements BaseEnum<String> {
    /**
     * 系统处理业务的执行状态：成功
     */
    SUCCESS("0000", "成功"),
    /**
     * 系统处理业务的执行状态：失败
     */
    FAIL("0001", "失败"),
    /**
     * 系统处理业务的执行状态：参数校验失败
     */
    ERROR_PARAM("0002", "参数校验失败"),
    /**
     * 系统处理业务的执行状态：非法请求
     */
    ILLEGAL_REQUEST("0003", "非法请求"),
    /**
     * 系统处理业务的执行状态：未登录或登录Session过期
     */
    NOT_LOGIN("0004", "未登录或登录Session过期"),
    /**
     * 系统处理业务的执行状态：未登录或登录Session过期
     */
    ILLEGAL_STATE("0005", "无效状态"),
    /**
     * 系统处理业务的执行状态：超时
     */
    TIMEOUT("1111", "超时"),
    /**
     * 系统处理quartz定时任务执行状态：失败
     */
    QUARTZ_ERROR("9997", "定时任务执行异常"),
    /**
     * 系统处理ETL执行状态：失败
     */
    KETTLE_ERROR("9998", "ETL执行异常"),
    /**
     * 系统处理业务的执行状态：系统错误
     */
    SYS_ERROR("9999", "系统错误");

    private final String code;
    private final String desc;

    public static GlobalStatusEnum getEnum(String code) {
        return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
    }

    public static String getEnumDesc(String code) {
        GlobalStatusEnum e = getEnum(code);
        return e != null ? e.desc : null;
    }
}
