package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.request.UserReq;
import org.kettle.scheduler.system.api.response.UserRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理API
 *
 * @author lyf
 */
@Api(tags = "用户管理API")
@RequestMapping("/sys/user")
public interface SysUserApi {

    /**
     * 添加用户
     *
     * @param req {@link UserReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/add.do")
    Result add(@RequestBody UserReq req);

    /**
     * 通过id删除用户
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @ApiOperation(value = "通过id删除用户")
    @DeleteMapping("/delete.do")
    Result delete(@RequestParam("id") Integer id);

    /**
     * 批量删除用户
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @ApiOperation(value = "批量删除用户")
    @DeleteMapping("/deleteBatch.do")
    Result deleteBatch(@RequestBody List<Integer> ids);

    /**
     * 更新用户
     *
     * @param req {@link UserReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "更新用户")
    @PutMapping("/update.do")
    Result update(@RequestBody UserReq req);

    /**
     * 根据条件查询用户列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询用户列表")
    @PostMapping("/findUserListByPage.do")
    Result<PageOut<UserRes>> findUserListByPage(@RequestBody QueryHelper<UserReq> req);

    /**
     * 查询用户明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询用户明细")
    @GetMapping("/getUserDetail.do")
    Result<UserRes> getUserDetail(@RequestParam("id") Integer id);

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return {@link Result}
	 */
	@ApiOperation(value = "查询用户明细")
	@GetMapping("/getUserByUsername.do")
	Result<UserRes> getUserByUsername(@RequestParam("username") String username);

	/**
	 * 验证账户是否存在
	 *
	 * @param username 账户名
	 * @return 只能返回true或false
	 */
	@ApiOperation(value = "验证账户是否存在")
	@PostMapping("/accountExist.do")
	String accountExist(String username);
}
