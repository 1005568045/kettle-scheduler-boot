package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.groups.Insert;
import org.kettle.scheduler.common.groups.Update;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.SysQuartzApi;
import org.kettle.scheduler.system.api.request.QuartzReq;
import org.kettle.scheduler.system.api.response.QuartzRes;
import org.kettle.scheduler.system.biz.service.SysQuartzService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定时任务管理API
 *
 * @author lyf
 */
@RestController
public class SysQuartzApiController implements SysQuartzApi {

    private final SysQuartzService quartzService;

    public SysQuartzApiController(SysQuartzService quartzService) {
        this.quartzService = quartzService;
    }

    /**
     * 添加定时任务
     *
     * @param req {@link QuartzReq}
     * @return {@link Result}
     */
    @Override
    public Result add(@Validated(Insert.class) QuartzReq req) {
        quartzService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除定时任务
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        quartzService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除定时任务
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        quartzService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新定时任务
     *
     * @param req {@link QuartzReq}
     * @return {@link Result}
     */
    @Override
    public Result update(@Validated(Update.class) QuartzReq req) {
        quartzService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询定时任务列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<QuartzRes>> findQuartzListByPage(QueryHelper<QuartzReq> req) {
        return Result.ok(quartzService.findQuartzListByPage(req.getQuery(), req.getPage().getPageable()));
    }

    /**
     * 查询定时任务明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<QuartzRes> getQuartzDetail(Integer id) {
        return Result.ok(quartzService.getQuartzDetail(id));
    }

    /**
     * 查询定时任务列表
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<QuartzRes>> findQuartzList() {
        return Result.ok(quartzService.findQuartzList());
    }
}
