package org.kettle.scheduler.system.biz.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.AssertUtil;
import org.kettle.scheduler.system.api.api.SysLoginApi;
import org.kettle.scheduler.system.api.request.LoginReq;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

/**
 * 登录鉴权API
 *
 * @author lyf
 */
@Controller
public class SysLoginApiController implements SysLoginApi {

    /**
     * 用户登录
     *
     * @param req {@link LoginReq}
     * @return {@link Result}
     */
    @Override
    public Result loginIn(@Validated LoginReq req) {
		// 获取当前用户主体
		Subject currentUser = SecurityUtils.getSubject();
		// 根据用户名和密码获取token认证信息
		UsernamePasswordToken token = new UsernamePasswordToken(req.getUsername(), req.getPassword());
		// 是否自动登录
		if (req.isRememberMe()) {
			token.setRememberMe(true);
		}
		// 执行登录认证, 这里会抛出权限异常, 需要做全局异常处理
		currentUser.login(token);
		// 判断是否认证成功
		AssertUtil.state(currentUser.isAuthenticated(), "登录状态无效");
		return Result.ok();
    }

    /**
     * 用户退出
     *
     * @return {@link Result}
     */
    @Override
    public Result loginOut() {
		// 获取当前用户主体
		Subject currentUser = SecurityUtils.getSubject();
		// 执行退出
		currentUser.logout();
		return Result.ok();
    }
}
