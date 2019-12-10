package org.kettle.scheduler.system.biz.controller;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.system.api.api.SysTransMonitorApi;
import org.kettle.scheduler.system.api.basic.IdVO;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.TaskCountRes;
import org.kettle.scheduler.system.api.response.TransMonitorRes;
import org.kettle.scheduler.system.api.response.TransRecordRes;
import org.kettle.scheduler.system.biz.entity.TransRecord;
import org.kettle.scheduler.system.biz.service.SysTransMonitorService;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author lyf
 */
@Slf4j
@RestController
public class SysTransMonitorApiController implements SysTransMonitorApi {
    private final SysTransMonitorService transMonitorService;

    public SysTransMonitorApiController(SysTransMonitorService transMonitorService) {
        this.transMonitorService = transMonitorService;
    }

    /**
     * 根据条件查询转换监控列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<TransMonitorRes>> findTransMonitorListByPage(QueryHelper<MonitorQueryReq> req) {
        return Result.ok(transMonitorService.findTransMonitorListByPage(req.getQuery(), req.getPage().getPageable()));
    }

    /**
     * 查询转换id查询转换执行记录
     *
     * @param req 根据转换id查询
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<TransRecordRes>> findTransRecordList(QueryHelper<IdVO> req) {
        return Result.ok(transMonitorService.findTransRecordList(req.getQuery().getId(), req.getPage().getPageable()));
    }

    /**
     * 查看转换执行记录明细
     *
     * @param transRecordId 根据转换记录id查询
     * @return {@link Result}
     */
    @Override
    public Result<String> viewTransRecordDetail(Integer transRecordId) {
        TransRecord record = transMonitorService.getTransRecord(transRecordId);
        String logContent = "";
        try {
            logContent = FileUtil.readFileToString(new File(record.getLogFilePath()), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(logContent.replace("\r\n", "<br/>"));
    }

    /**
     * 下载转换执行记录明细
     *
     * @param transRecordId 根据转换记录id查询
     */
    @Override
    public void downloadTransRecord(HttpServletResponse response,Integer transRecordId) {
        // 查询文件路径
        TransRecord record = transMonitorService.getTransRecord(transRecordId);
        FileUtil.downloadFile(response, record.getLogFilePath());
    }

	/**
	 * 对转换任务执行结果统计
	 *
	 * @return {@link Result}
	 */
	@Override
	public Result<TaskCountRes> countTrans() {
		return Result.ok(transMonitorService.countTrans());
	}
}
