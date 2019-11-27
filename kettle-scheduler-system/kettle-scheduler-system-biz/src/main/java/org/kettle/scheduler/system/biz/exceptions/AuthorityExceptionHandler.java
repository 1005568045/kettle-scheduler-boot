package org.kettle.scheduler.system.biz.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.UnauthorizedException;
import org.kettle.scheduler.common.exceptions.GlobalExceptionHandler;
import org.kettle.scheduler.common.povo.Result;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 权限系统全局异常处理类
 *
 * @author lyf
 */
@Order(value = 1)
@Slf4j
@RestControllerAdvice
public class AuthorityExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(value = ShiroException.class)
    public Result shiroException(ShiroException ex) {
        if (ex instanceof UnknownAccountException) {
            return Result.error("用户名 / 密码错误");
        } else if (ex instanceof IncorrectCredentialsException) {
            return Result.error("用户名 / 密码错误");
        } else if (ex instanceof LockedAccountException) {
            return Result.error("您的账户已被锁定");
        } else if (ex instanceof DisabledAccountException) {
            return Result.error("账户已禁用");
        } else if (ex instanceof ExcessiveAttemptsException) {
            return Result.error("短时间内登录失败次数过多，请稍后再试？");
        } else if (ex instanceof ExpiredCredentialsException) {
            return Result.error("登录已失效，请重新登录");
        } else if (ex instanceof UnsupportedTokenException) {
            return Result.error("不支持的token认证");
        } else if (ex instanceof UnauthorizedException) {
            return Result.error("你的访问权限不足");
        } else {
            log.error("权限认证失败异常: ", ex);
            return Result.error("权限认证失败");
        }
    }
}
