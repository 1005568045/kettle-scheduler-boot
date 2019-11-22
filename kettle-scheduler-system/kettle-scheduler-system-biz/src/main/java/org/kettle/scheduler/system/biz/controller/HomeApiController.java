package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.HomeApi;
import org.kettle.scheduler.system.api.response.HomeMonitorTaskCountRes;
import org.kettle.scheduler.system.api.response.HomeMonitorTaskRunRes;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页信息
 *
 * @author lyf
 */
@RestController
public class HomeApiController implements HomeApi {
    /**
     * 首页监控任务统计
     *
     * @return {@link Result}
     */
    @Override
    public Result<HomeMonitorTaskCountRes> taskCount() {
        return null;
    }

    /**
     * 首页监控任务统计
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<HomeMonitorTaskRunRes>> runStatus() {
        return null;
    }
}
