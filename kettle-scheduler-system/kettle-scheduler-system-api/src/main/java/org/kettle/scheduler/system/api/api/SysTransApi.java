package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.request.TransReq;
import org.kettle.scheduler.system.api.response.TransRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 转换管理API
 *
 * @author lyf
 */
@Api(tags = "转换管理API")
@RequestMapping("/sys/trans")
public interface SysTransApi {

    /**
     * 添加转换
     *
     * @param req {@link TransReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "添加转换")
    @PostMapping("/add.do")
    Result add(@RequestBody TransReq req);

    /**
     * 通过id删除转换
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @ApiOperation(value = "通过id删除转换")
    @DeleteMapping("/delete.do")
    Result delete(@RequestParam("id") Integer id);

    /**
     * 批量删除转换
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @ApiOperation(value = "批量删除转换")
    @DeleteMapping("/deleteBatch.do")
    Result deleteBatch(@RequestBody List<Integer> ids);

    /**
     * 更新转换
     *
     * @param req {@link TransReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "更新转换")
    @PutMapping("/update.do")
    Result update(@RequestBody TransReq req);

    /**
     * 根据条件查询转换列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询转换列表")
    @PostMapping("/findTransListByPage.do")
    Result<PageOut<TransRes>> findTransListByPage(@RequestBody QueryHelper<TransReq> req);

    /**
     * 查询转换明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询转换明细")
    @GetMapping("/getTransDetail.do")
    Result<TransRes> getTransDetail(@RequestParam("id") Integer id);

    /**
     * 全部启动
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "全部启动")
    @GetMapping("/startAllTrans.do")
    Result startAllTrans();

    /**
     * 单个启动
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "单个启动")
    @GetMapping("/startTrans.do")
    Result startTrans(@RequestParam("id") Integer id);

    /**
     * 全部停止
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "全部停止")
    @GetMapping("/stopAllTrans.do")
    Result stopAllTrans();

    /**
     * 单个停止
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "单个停止")
    @GetMapping("/stopTrans.do")
    Result stopTrans(@RequestParam("id") Integer id);
}
