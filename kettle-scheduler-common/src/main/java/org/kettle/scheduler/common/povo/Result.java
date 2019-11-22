package org.kettle.scheduler.common.povo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;

import java.io.Serializable;

/**
 * 响应结果, 返回给前端的数据
 *
 * @author lyf
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回代码
     */
    private String code = GlobalStatusEnum.SUCCESS.getCode();

    /**
     * 返回处理消息
     */
    private String message = GlobalStatusEnum.SUCCESS.getDesc();

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 返回数据对象
     */
    private T result;

    public static <T> Result<T> error(String msg) {
        return error(GlobalStatusEnum.FAIL.getCode(), msg);
    }

    public static <T> Result<T> error(String code, String msg) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> ok() {
        return new Result<>();
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setResult(data);
        return r;
    }
}