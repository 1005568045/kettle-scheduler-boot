package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.request.LoginReq;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录鉴权API
 *
 * @author lyf
 */
@Api(tags = "登录鉴权API")
@RequestMapping("/sys/login")
public interface SysLoginApi {

    /**
     * 用户登录
     *
     * @param req {@link LoginReq}
     * @param request http请求
     * @return {@link Result}
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/in")
    Result loginIn(@RequestBody LoginReq req, HttpServletRequest request);

    /**
     * 用户退出
     *
     * @param request http请求
     * @return {@link Result}
     */
    @ApiOperation(value = "用户退出")
    @GetMapping("/out")
    Result loginOut(HttpServletRequest request);
}
