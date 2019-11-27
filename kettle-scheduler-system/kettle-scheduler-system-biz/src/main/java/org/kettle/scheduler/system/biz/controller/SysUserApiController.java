package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.groups.Insert;
import org.kettle.scheduler.common.groups.Update;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.api.SysUserApi;
import org.kettle.scheduler.system.api.request.UserReq;
import org.kettle.scheduler.system.api.response.UserRes;
import org.kettle.scheduler.system.biz.entity.User;
import org.kettle.scheduler.system.biz.service.SysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理API
 *
 * @author lyf
 */
@RestController
public class SysUserApiController implements SysUserApi {

    private final SysUserService userService;

    public SysUserApiController(SysUserService userService) {
        this.userService = userService;
    }

    /**
     * 添加用户
     *
     * @param req {@link UserReq}
     * @return {@link Result}
     */
    @Override
    public Result add(@Validated(Insert.class) UserReq req) {
        userService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除用户
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        userService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除用户
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        userService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新用户
     *
     * @param req {@link UserReq}
     * @return {@link Result}
     */
    @Override
    public Result update(@Validated(Update.class) UserReq req) {
        userService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询用户列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<UserRes>> findUserListByPage(QueryHelper<UserReq> req) {
        return Result.ok(userService.findUserListByPage(req.getQuery(), req.getPage()));
    }

    /**
     * 查询用户明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<UserRes> getUserDetail(Integer id) {
        return Result.ok(userService.getUserDetail(id));
    }

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return {@link Result}
	 */
	@Override
	public Result<UserRes> getUserByUsername(String username) {
		User user = userService.getUserByAccount(username);
		if (user!=null) {
			return Result.ok(BeanUtil.copyProperties(user, UserRes.class));
		} else {
			throw new MyMessageException("用户信息不存在");
		}
	}
}
