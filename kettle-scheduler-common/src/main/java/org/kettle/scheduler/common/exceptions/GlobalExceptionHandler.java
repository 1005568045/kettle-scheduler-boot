package org.kettle.scheduler.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.povo.Result;

import java.util.concurrent.TimeoutException;

/**
 * 全局异常处理类
 * 继承该类需要添加@Order注解，并指定排序的值
 * 用来控制多个全局异常处理类的加载顺序
 *
 * @author lyf
 */
@Order
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result defaultException(Exception ex) {
        if (ex instanceof MyMessageException) {
            MyMessageException be = (MyMessageException) ex;
            return Result.error(be.getErrorCode(), be.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            StringBuilder sb = new StringBuilder();
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()
                    .forEach(fe -> sb.append(",").append(fe.getField()).append(": ").append(fe.getDefaultMessage()));
            sb.delete(0, 1);
            return Result.error(GlobalStatusEnum.ERROR_PARAM.getCode(), sb.toString());
        } else if (ex instanceof BindException) {
            StringBuilder sb = new StringBuilder();
            ((BindException) ex).getBindingResult().getFieldErrors()
                    .forEach(fe -> sb.append(",").append(fe.getField()).append(": ").append(fe.getDefaultMessage()));
            sb.delete(0, 1);
            return Result.error(GlobalStatusEnum.ERROR_PARAM.getCode(), sb.toString());
        } else if (ex instanceof IllegalAccessException) {
            return Result.error(GlobalStatusEnum.ILLEGAL_REQUEST.getCode(), GlobalStatusEnum.ILLEGAL_REQUEST.getDesc());
        } else if (ex instanceof TimeoutException) {
            return Result.error(GlobalStatusEnum.TIMEOUT.getCode(), GlobalStatusEnum.TIMEOUT.getDesc());
        } else if (ex instanceof IllegalStateException) {
            return Result.error(GlobalStatusEnum.ILLEGAL_STATE.getCode(), ex.getMessage());
        } else if (ex instanceof DataIntegrityViolationException) {
			return Result.error(GlobalStatusEnum.DATA_INTEGRITY_ERROR.getCode(), GlobalStatusEnum.DATA_INTEGRITY_ERROR.getDesc());
		} else {
            log.error("系统内部异常: ", ex);
            return Result.error(GlobalStatusEnum.SYS_ERROR.getCode(), GlobalStatusEnum.SYS_ERROR.getDesc());
        }
    }
}
