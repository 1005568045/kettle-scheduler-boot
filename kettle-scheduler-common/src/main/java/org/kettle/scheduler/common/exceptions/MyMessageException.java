package org.kettle.scheduler.common.exceptions;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.enums.base.BaseEnum;

import java.text.MessageFormat;

/**
 * 自定义业务异常类
 *
 * @author lyf
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MyMessageException extends RuntimeException {
    private static final long serialVersionUID = 1950743775280327107L;

    private String errorCode;
    private String errorMsg;

    public MyMessageException() {
    }

    public MyMessageException(String errorMsg) {
        this(GlobalStatusEnum.FAIL, errorMsg);
    }

    public MyMessageException(BaseEnum errorEnum, String errorMsg) {
        super(MessageFormat.format("errorCode:{0}, errorMsg:{1}---->{2}", errorEnum.getCode(), errorEnum.getDesc(), errorMsg));
        this.errorCode = errorEnum.getCode().toString();
        this.errorMsg = errorMsg;
    }

    public MyMessageException(String errorCode, String errorMsg) {
        super(MessageFormat.format("errorCode:{0}, errorMsg:{1}", errorCode, errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public MyMessageException(String errorCode, String errorMsg, Throwable cause) {
        super(MessageFormat.format("errorCode:{0}, errorMsg:{1}", errorCode, errorMsg), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}