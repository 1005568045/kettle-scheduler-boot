package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.HomeApi;
import org.kettle.scheduler.system.api.response.HomeMonitorTaskCountRes;
import org.kettle.scheduler.system.api.response.HomeMonitorTaskRunRes;
import org.kettle.scheduler.system.biz.service.SysJobService;
import org.kettle.scheduler.system.biz.service.SysTransService;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页信息
 *
 * @author lyf
 */
@RestController
public class HomeApiController implements HomeApi {
	private final SysJobService jobService;
	private final SysTransService transService;

	public HomeApiController(SysJobService jobService, SysTransService transService) {
		this.jobService = jobService;
		this.transService = transService;
	}

	/**
     * 首页监控任务统计
     *
     * @return {@link Result}
     */
    @Override
    public Result<HomeMonitorTaskCountRes> taskCount() {
    	// 统计运行的转换数量
		Integer transTaskNum = transService.countRunTrans();
		// 统计运行的作业数量
		Integer jobTaskNum = jobService.countRunJob();

		HomeMonitorTaskCountRes res = new HomeMonitorTaskCountRes();
		res.setTotalTaskNum(transTaskNum + jobTaskNum);
		res.setJobTaskNum(jobTaskNum);
		res.setTransTaskNum(transTaskNum);
        return Result.ok(res);
    }

    /**
     * 首页监控任务统计
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<HomeMonitorTaskRunRes>> runStatus() {
		List<HomeMonitorTaskRunRes> list = new ArrayList<>();
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-20").setJobRunNum(5).setTransRunNum(2));
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-21").setJobRunNum(1).setTransRunNum(12));
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-22").setJobRunNum(3).setTransRunNum(5));
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-23").setJobRunNum(8).setTransRunNum(6));
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-24").setJobRunNum(6).setTransRunNum(8));
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-25").setJobRunNum(2).setTransRunNum(12));
		list.add(new HomeMonitorTaskRunRes().setRunTime("2019-11-26").setJobRunNum(4).setTransRunNum(20));
		return Result.ok(list);
    }
}
