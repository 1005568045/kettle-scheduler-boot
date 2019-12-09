package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.basic.IdVO;
import org.kettle.scheduler.system.api.request.MonitorQueryReq;
import org.kettle.scheduler.system.api.response.JobMonitorRes;
import org.kettle.scheduler.system.api.response.JobRecordRes;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 作业监控管理API
 *
 * @author lyf
 */
@Api(tags = "作业监控管理API")
@RequestMapping("/sys/job/monitor")
public interface SysJobMonitorApi {

    /**
     * 根据条件查询作业监控列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询作业监控列表")
    @PostMapping("/findJobMonitorListByPage.do")
    Result<PageOut<JobMonitorRes>> findJobMonitorListByPage(@RequestBody QueryHelper<MonitorQueryReq> req);

    /**
     * 查询作业id查询作业执行记录
     *
     * @param req 根据作业id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询作业监控明细")
    @GetMapping("/findJobRecordList.do")
    Result<PageOut<JobRecordRes>> findJobRecordList(@RequestBody QueryHelper<IdVO> req);

    /**
     * 查看作业执行记录明细
     *
     * @param jobRecordId 根据作业记录id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查看作业执行记录明细")
    @GetMapping("/viewJobRecordDetail.do")
    Result<String> viewJobRecordDetail(@RequestParam("jobRecordId") Integer jobRecordId);

    /**
     * 下载作业执行记录明细
     *
     * @param response 响应结果
     * @param jobRecordId 根据作业记录id查询
     */
    @ApiOperation(value = "下载作业执行记录明细")
    @GetMapping("/downloadJobRecord.do")
    void downloadJobRecord(HttpServletResponse response, @RequestParam("jobRecordId") Integer jobRecordId);
}
