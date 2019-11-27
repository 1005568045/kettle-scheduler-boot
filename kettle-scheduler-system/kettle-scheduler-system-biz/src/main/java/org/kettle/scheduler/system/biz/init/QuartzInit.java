package org.kettle.scheduler.system.biz.init;

import org.kettle.scheduler.system.biz.service.SysJobService;
import org.kettle.scheduler.system.biz.service.SysTransService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 项目启动完成后重新设置定时任务
 *
 * @author lyf
 */
@Component
@Order(value = 99)
public class QuartzInit implements ApplicationRunner {
    private final SysTransService transService;
    private final SysJobService jobService;

    public QuartzInit(SysTransService transService, SysJobService jobService) {
        this.transService = transService;
        this.jobService = jobService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jobService.initJobsQuartz();
        transService.initTransQuartz();
    }
}
