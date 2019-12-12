package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.groups.Insert;
import org.kettle.scheduler.common.groups.Update;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.common.utils.ValidatorUtil;
import org.kettle.scheduler.core.enums.RepTypeEnum;
import org.kettle.scheduler.system.api.api.SysJobApi;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;
import org.kettle.scheduler.system.api.request.JobReq;
import org.kettle.scheduler.system.api.response.JobRes;
import org.kettle.scheduler.system.biz.constant.KettleConfig;
import org.kettle.scheduler.system.biz.service.SysJobService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.groups.Default;
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
    public Result add(@Validated({Insert.class, Default.class}) JobReq req, MultipartFile jobFile) {
    	// 参数验证
		validatedParam(req);
		// 保存上传文件
		getJobPath(req, jobFile);
		// 保存结果
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
    public Result update(@Validated({Update.class, Default.class}) JobReq req, MultipartFile jobFile) {
		// 参数验证
		validatedParam(req);
		// 保存上传文件
		getJobPath(req, jobFile);
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
        return Result.ok(jobService.findJobListByPage(req.getQuery(), req.getPage().getPageable()));
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

	private void validatedParam(JobReq req) {
		switch (RunTypeEnum.getEnum(req.getJobType())) {
			case FILE:
				String result1 = ValidatorUtil.validateWithString(req, JobReq.File.class);
				if (!StringUtil.isEmpty(result1)) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, result1);
				}
				break;
			case REP:
				String result2 = ValidatorUtil.validateWithString(req, JobReq.Rep.class);
				if (!StringUtil.isEmpty(result2)) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, result2);
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + RepTypeEnum.getEnum(req.getJobType()));
		}
	}

	private void getJobPath(JobReq req, MultipartFile jobFile) {
		switch (RunTypeEnum.getEnum(req.getJobType())) {
			case FILE:
				if (jobFile == null || jobFile.isEmpty()) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, "上传文件不能为空");
				}
				req.setJobPath(FileUtil.uploadFile(jobFile, KettleConfig.uploadPath));
				break;
			case REP:
				String path = req.getJobPath().substring(0, req.getJobPath().lastIndexOf("/"));
				req.setJobPath(StringUtil.isEmpty(path) ? FileUtil.separator : path);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + RepTypeEnum.getEnum(req.getJobType()));
		}
	}
}
