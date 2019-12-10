package org.kettle.scheduler.system.biz.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.system.api.api.SysJobMonitorApi;
import org.kettle.scheduler.system.api.basic.IdVO;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.JobMonitorRes;
import org.kettle.scheduler.system.api.response.JobRecordRes;
import org.kettle.scheduler.system.api.response.TaskCountRes;
import org.kettle.scheduler.system.biz.entity.JobRecord;
import org.kettle.scheduler.system.biz.service.SysJobMonitorService;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author lyf
 */
@Slf4j
@RestController
public class SysJobMonitorApiController implements SysJobMonitorApi {
    private final SysJobMonitorService jobMonitorService;

    public SysJobMonitorApiController(SysJobMonitorService jobMonitorService) {
        this.jobMonitorService = jobMonitorService;
    }

    /**
     * 根据条件查询作业监控列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<JobMonitorRes>> findJobMonitorListByPage(QueryHelper<MonitorQueryReq> req) {
        return Result.ok(jobMonitorService.findJobMonitorListByPage(req.getQuery(), req.getPage().getPageable()));
    }

    /**
     * 查询作业id查询作业执行记录
     *
     * @param req 根据作业id查询
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<JobRecordRes>> findJobRecordList(QueryHelper<IdVO> req) {
        return Result.ok(jobMonitorService.findJobRecordList(req.getQuery().getId(), req.getPage().getPageable()));
    }

    /**
     * 查看作业执行记录明细
     *
     * @param jobRecordId 根据作业记录id查询
     * @return {@link Result}
     */
    @Override
    public Result<String> viewJobRecordDetail(Integer jobRecordId) {
        JobRecord record = jobMonitorService.getJobRecord(jobRecordId);
        String logContent = "";
        try {
            logContent = FileUtils.readFileToString(new File(record.getLogFilePath()), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(logContent.replace("\r\n", "<br/>"));
    }

    /**
     * 下载作业执行记录明细
     *
     * @param jobRecordId 根据作业记录id查询
     */
    @Override
    public void downloadJobRecord(HttpServletResponse response,Integer jobRecordId) {
        // 查询文件路径
        JobRecord record = jobMonitorService.getJobRecord(jobRecordId);
        FileUtil.downloadFile(response, record.getLogFilePath());
    }

	/**
	 * 对作业任务执行结果统计
	 *
	 * @return {@link Result}
	 */
	@Override
	public Result<TaskCountRes> countJob() {
		return Result.ok(jobMonitorService.countJob());
	}
}
