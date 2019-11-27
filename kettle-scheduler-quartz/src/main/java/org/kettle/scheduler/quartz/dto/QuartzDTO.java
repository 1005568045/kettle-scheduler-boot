package org.kettle.scheduler.quartz.dto;

import lombok.Data;
import org.quartz.Job;
import org.quartz.JobDataMap;

/**
 * 定时任务需要的参数
 *
 * @author lyf
 */
@Data
public class QuartzDTO {
    /**
     * 任务名-自定义
     */
    private String jobName;

    /**
     * 任务所属组名-自定义
     */
    private String jobGroupName;

    /**
     * 触发器名-自定义
     */
    private String triggerName;

    /**
     * 触发器所属组名-自定义
     */
    private String triggerGroupName;

    /**
     * cron定时策略
     */
    private String cron;

    /**
     * 任务执行参数-可以传入任意类型的数据给执行类
     */
    private JobDataMap jobDataMap;

    /**
     * 任务执行类-实现了{@code org.quartz.Job}接口的类
     */
    Class<? extends Job> jobClass;
}
