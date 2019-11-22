package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.response.HomeMonitorTaskCountRes;
import org.kettle.scheduler.system.api.response.HomeMonitorTaskRunRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 系统首页API
 *
 * @author lyf
 */
@Api(tags = "系统首页API")
@RequestMapping("/home/monitor")
public interface HomeApi {

    /**
     * 首页监控任务统计
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "首页监控任务统计")
    @PostMapping("/taskCount")
    Result<HomeMonitorTaskCountRes> taskCount();

    /**
     * 首页监控任务统计
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "首页监控任务统计")
    @PostMapping("/runStatus")
    Result<List<HomeMonitorTaskRunRes>> runStatus();
}
