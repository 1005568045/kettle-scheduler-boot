package org.kettle.scheduler.quartz.manage;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.quartz.dto.QuartzDTO;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * quartz定时任务工具类，对任务进行管理（创建、修改、删除、暂停）
 *
 * @author lyf
 */
@Slf4j
public class QuartzManage {

    private static final SchedulerFactory SCHEDULER_FACTORY = new StdSchedulerFactory();

    /**
     * 启动调度任务
     * @param jobDetail 执行任务
     * @param trigger 触发器
     */
    private static void startScheduler(JobDetail jobDetail, Trigger trigger) {
        try {
            // 获取调度任务实例
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            // 绑定触发器和调度任务
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            scheduler.start();
        } catch (SchedulerException e) {
            String msg = "启动调度任务失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.QUARTZ_ERROR, msg);
        }
    }

    /**
     * 任务执行实例
     * @param jobName 任务名称
     * @param jobGroupName 任务分组名
     * @param jobDataMap 执行参数
     * @param jobClass 执行类-实现了{@code org.quartz.Job}接口的类
     * @return {@link JobDetail}
     */
    private static JobDetail getJobDetail(String jobName, String jobGroupName, JobDataMap jobDataMap, Class<? extends Job> jobClass) {
        // 获取任务执行类的实例
        JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
        // 添加任务名，任务组
        jobBuilder.withIdentity(jobName, jobGroupName);
        // 添加任务执行的参数
        if (jobDataMap!=null && jobDataMap.size()>0) {
            jobBuilder.setJobData(jobDataMap);
        }
        // 获取job执行实例
        return jobBuilder.build();
    }

    /**
     * cron触发器
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器分组名
     * @param cron 定时策略
     * @return {@link CronTrigger}
     */
    private static CronTrigger getCronTrigger(String triggerName, String triggerGroupName, String cron) {
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(triggerName, triggerGroupName);
        // 触发器时间设定
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        // 从当前时间判断触发
        triggerBuilder.startNow();
        // 创建Trigger对象
        return (CronTrigger) triggerBuilder.build();
    }

    /**
     * 一次任务触发器
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器分组名
     * @return {@link SimpleTrigger}
     */
    private static SimpleTrigger getOnceTrigger(String triggerName, String triggerGroupName) {
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(triggerName, triggerGroupName);
        // 5秒后立即执行，重复次数设为0，表示只执行一次
        triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).withRepeatCount(0));
        // 从当前时间判断触发
        triggerBuilder.startNow();
        // 创建Trigger对象
        return (SimpleTrigger) triggerBuilder.build();
    }

    /**
     * 创建添加定时任务
     * @param dto 任务参数
     */
    public static void addCronJob(QuartzDTO dto) {
        // 判断定时任务是否存在
        String triggerState = getTriggerState(dto.getTriggerName(), dto.getTriggerGroupName());
        if (Trigger.TriggerState.NONE.name().equals(triggerState)) {
            JobDetail jobDetail = getJobDetail(dto.getJobName(), dto.getJobGroupName(), dto.getJobDataMap(), dto.getJobClass());
            CronTrigger cronTrigger = getCronTrigger(dto.getTriggerName(), dto.getTriggerGroupName(), dto.getCron());
            // 启动调度任务
            startScheduler(jobDetail, cronTrigger);
        }
    }

    /**
     * 创建添加只执行一次的任务
     * @param dto 任务参数
     */
    public static void addOnceJob(QuartzDTO dto) {
        // 判断定时任务是否存在
        String triggerState = getTriggerState(dto.getTriggerName(), dto.getTriggerGroupName());
        if (Trigger.TriggerState.NONE.name().equals(triggerState)) {
            JobDetail jobDetail = getJobDetail(dto.getJobName(), dto.getJobGroupName(), dto.getJobDataMap(), dto.getJobClass());
            SimpleTrigger simpleTrigger = getOnceTrigger(dto.getTriggerName(), dto.getTriggerGroupName());
            // 启动调度任务
            startScheduler(jobDetail, simpleTrigger);
        }
    }

    /**
     * 修改定时任务
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器分组名
     * @param cron 定时策略
     */
    public static void updateCronJob(String triggerName, String triggerGroupName, String cron) {
        try {
            // 获取调度任务实例
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            // 获取原来触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger != null) {
                String oldTime = trigger.getCronExpression();
                if (!oldTime.equalsIgnoreCase(cron)) {
                    CronTrigger cronTrigger = getCronTrigger(triggerName, triggerGroupName, cron);
                    // 修改一个任务的触发时间
                    scheduler.rescheduleJob(triggerKey, cronTrigger);
                }
            }
        } catch (SchedulerException e) {
            String msg = "修改调度任务触发时间失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.QUARTZ_ERROR, msg);
        }
    }

    /**
     * 移除定时任务
     * @param dto 任务参数
     */
    public static void removeJob(QuartzDTO dto) {
        // 定时任务不存在就不处理
        String triggerState = getTriggerState(dto.getTriggerName(), dto.getTriggerGroupName());
        if (Trigger.TriggerState.NONE.name().equals(triggerState)) {
            return;
        }
        try {
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(dto.getTriggerName(), dto.getTriggerGroupName());
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);

            JobKey jobKey = JobKey.jobKey(dto.getJobName(), dto.getJobGroupName());
            // 停止任务
            scheduler.interrupt(jobKey);
            // 删除任务
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            String msg = "停止定时任务失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.QUARTZ_ERROR, msg);
        }
    }

    /**
     * 获取触发器状态
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器分组名
     * @return 状态
     * 		NONE: 不存在
     * 		NORMAL: 正常
     * 		PAUSED: 暂停
     * 		COMPLETE:完成
     * 		ERROR : 错误
     * 		BLOCKED : 阻塞
     */
    public static String getTriggerState(String triggerName, String triggerGroupName){
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            Scheduler scheduler = SCHEDULER_FACTORY.getScheduler();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            return triggerState.name();
        } catch (SchedulerException e) {
            String msg = "获取触发器状态失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.QUARTZ_ERROR, msg);
        }
    }
}
