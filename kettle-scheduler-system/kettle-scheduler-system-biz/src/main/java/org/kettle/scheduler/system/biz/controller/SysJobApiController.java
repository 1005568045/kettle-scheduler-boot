package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.SysJobApi;
import org.kettle.scheduler.system.api.request.JobReq;
import org.kettle.scheduler.system.api.response.JobRes;
import org.kettle.scheduler.system.biz.service.SysJobService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作业管理API
 *
 * @author lyf
 */
@RestController
public class SysJobApiController implements SysJobApi {

    private final SysJobService jobService;

    public SysJobApiController(SysJobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 添加作业
     *
     * @param req {@link JobReq}
     * @return {@link Result}
     */
    @Override
    public Result add(JobReq req) {
        jobService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除作业
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        jobService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除作业
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        jobService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新作业
     *
     * @param req {@link JobReq}
     * @return {@link Result}
     */
    @Override
    public Result update(JobReq req) {
        jobService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询作业列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<JobRes>> findJobListByPage(QueryHelper<JobReq> req) {
        return Result.ok(jobService.findJobListByPage(req.getQuery(), req.getPage()));
    }

    /**
     * 查询作业明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<JobRes> getJobDetail(Integer id) {
        return Result.ok(jobService.getJobDetail(id));
    }

    /**
     * 全部启动
     *
     * @return {@link Result}
     */
    @Override
    public Result startAllJob() {
        jobService.startAllJob();
        return Result.ok();
    }

    /**
     * 单个启动
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result startJob(Integer id) {
        jobService.startJob(id);
        return Result.ok();
    }

    /**
     * 全部停止
     *
     * @return {@link Result}
     */
    @Override
    public Result stopAllJob() {
        jobService.stopAllJob();
        return Result.ok();
    }

    /**
     * 单个停止
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result stopJob(Integer id) {
        jobService.stopJob(id);
        return Result.ok();
    }
}
