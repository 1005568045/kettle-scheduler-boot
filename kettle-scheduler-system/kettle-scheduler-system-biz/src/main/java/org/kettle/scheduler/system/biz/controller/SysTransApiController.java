package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.SysTransApi;
import org.kettle.scheduler.system.api.request.TransReq;
import org.kettle.scheduler.system.api.response.TransRes;
import org.kettle.scheduler.system.biz.service.SysRepositoryService;
import org.kettle.scheduler.system.biz.service.SysTransService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 转换管理API
 *
 * @author lyf
 */
@RestController
public class SysTransApiController implements SysTransApi {

    private final SysTransService transService;
    private final SysRepositoryService repositoryService;

    public SysTransApiController(SysTransService transService, SysRepositoryService repositoryService) {
        this.transService = transService;
        this.repositoryService = repositoryService;
    }

    /**
     * 添加转换
     *
     * @param req {@link TransReq}
     * @return {@link Result}
     */
    @Override
    public Result add(TransReq req) {
        transService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除转换
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        transService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除转换
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        transService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新转换
     *
     * @param req {@link TransReq}
     * @return {@link Result}
     */
    @Override
    public Result update(TransReq req) {
        transService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询转换列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<TransRes>> findTransListByPage(QueryHelper<TransReq> req) {
        return Result.ok(transService.findTransListByPage(req.getQuery(), req.getPage().getPageable()));
    }

    /**
     * 查询转换明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<TransRes> getTransDetail(Integer id) {
        return Result.ok(transService.getTransDetail(id));
    }

    /**
     * 全部启动
     *
     * @return {@link Result}
     */
    @Override
    public Result startAllTrans() {
        transService.startAllTrans();
        return Result.ok();
    }

    /**
     * 单个启动
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result startTrans(Integer id) {
        transService.startTrans(id);
        return Result.ok();
    }

    /**
     * 全部停止
     *
     * @return {@link Result}
     */
    @Override
    public Result stopAllTrans() {
        transService.stopAllTrans();
        return Result.ok();
    }

    /**
     * 单个停止
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result stopTrans(Integer id) {
        transService.stopTrans(id);
        return Result.ok();
    }
}
