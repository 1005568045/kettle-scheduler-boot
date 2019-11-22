package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.SysLoginApi;
import org.kettle.scheduler.system.api.request.LoginReq;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录鉴权API
 *
 * @author lyf
 */
@RestController
public class SysLoginApiController implements SysLoginApi {
    /**
     * 用户登录
     *
     * @param req {@link LoginReq}
     * @param request http请求
     * @return {@link Result}
     */
    @Override
    public Result loginIn(LoginReq req, HttpServletRequest request) {
        return null;
    }

    /**
     * 用户退出
     *
     * @param request http请求
     * @return {@link Result}
     */
    @Override
    public Result loginOut(HttpServletRequest request) {
        return null;
    }
}
