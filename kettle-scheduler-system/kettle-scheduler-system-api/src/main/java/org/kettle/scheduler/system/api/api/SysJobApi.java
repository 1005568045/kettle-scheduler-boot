package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.request.JobReq;
import org.kettle.scheduler.system.api.response.JobRes;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 作业管理API
 *
 * @author lyf
 */
@Api(tags = "作业管理API")
@RequestMapping("/sys/job")
public interface SysJobApi {

    /**
     * 添加作业
     *
     * @param req {@link JobReq}
	 * @param jobFile {@link MultipartFile}
     * @return {@link Result}
     */
    @ApiOperation(value = "添加作业")
    @PostMapping("/add.do")
    Result add(JobReq req, MultipartFile jobFile);

    /**
     * 通过id删除作业
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @ApiOperation(value = "通过id删除作业")
    @DeleteMapping("/delete.do")
    Result delete(@RequestParam("id") Integer id);

    /**
     * 批量删除作业
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @ApiOperation(value = "批量删除作业")
    @DeleteMapping("/deleteBatch.do")
    Result deleteBatch(@RequestBody List<Integer> ids);

    /**
     * 更新作业
     *
     * @param req {@link JobReq}
	 * @param jobFile {@link MultipartFile}
     * @return {@link Result}
     */
    @ApiOperation(value = "更新作业")
    @PutMapping("/update.do")
    Result update(JobReq req, MultipartFile jobFile);

    /**
     * 根据条件查询作业列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询作业列表")
    @PostMapping("/findJobListByPage.do")
    Result<PageOut<JobRes>> findJobListByPage(@RequestBody QueryHelper<JobReq> req);

    /**
     * 查询作业明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询作业明细")
    @GetMapping("/getJobDetail.do")
    Result<JobRes> getJobDetail(@RequestParam("id") Integer id);

    /**
     * 全部启动
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "全部启动")
    @GetMapping("/startAllJob.do")
    Result startAllJob();

    /**
     * 单个启动
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "单个启动")
    @GetMapping("/startJob.do")
    Result startJob(Integer id);

    /**
     * 全部停止
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "全部停止")
    @GetMapping("/stopAllJob.do")
    Result stopAllJob();

    /**
     * 单个停止
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "单个停止")
    @GetMapping("/stopJob.do")
    Result stopJob(Integer id);
}
