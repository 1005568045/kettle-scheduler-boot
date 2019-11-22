package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.request.QuartzReq;
import org.kettle.scheduler.system.api.response.QuartzRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务管理API
 *
 * @author lyf
 */
@Api(tags = "定时任务管理API")
@RequestMapping("/sys/quartz")
public interface SysQuartzApi {

    /**
     * 添加定时任务
     *
     * @param req {@link QuartzReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "添加定时任务")
    @PostMapping("/add")
    Result add(@RequestBody QuartzReq req);

    /**
     * 通过id删除定时任务
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @ApiOperation(value = "通过id删除定时任务")
    @DeleteMapping("/delete")
    Result delete(@RequestParam("id") Integer id);

    /**
     * 批量删除定时任务
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @ApiOperation(value = "批量删除定时任务")
    @DeleteMapping("/deleteBatch")
    Result deleteBatch(@RequestBody List<Integer> ids);

    /**
     * 更新定时任务
     *
     * @param req {@link QuartzReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "更新定时任务")
    @PutMapping("/update")
    Result update(@RequestBody QuartzReq req);

    /**
     * 根据条件查询定时任务列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询定时任务列表")
    @PostMapping("/findQuartzListByPage")
    Result<PageOut<QuartzRes>> findQuartzListByPage(@RequestBody QueryHelper<QuartzReq> req);

    /**
     * 查询定时任务明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询定时任务明细")
    @GetMapping("/getQuartzDetail")
    Result<QuartzRes> getQuartzDetail(@RequestParam("id") Integer id);

    /**
     * 查询定时任务列表
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "查询定时任务列表")
    @GetMapping("/findQuartzList")
    Result<List<QuartzRes>> findQuartzList();
}
