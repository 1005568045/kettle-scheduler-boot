package org.kettle.scheduler.core.execute;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.utils.CollectionUtil;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.AbstractRepository;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;

import java.util.Map;

/**
 * kettle的kjb脚本执行器
 *
 * @author lyf
 */
@Slf4j
public class JobExecute {

    /**
     * kjb执行单元，所有的kjb执行都调用该方法
     *
     * @param rep kjb所在资源库, 单个文件执行时传入null
     * @param jm kjb元对象
     * @param params kjb需要的命名参数
     */
    private static void executeJob(Repository rep, JobMeta jm, Map<String, String> params) {
        // 通过元数据获取kjb的实例
        Job job = new Job(rep, jm);
        // 开启进程守护
        job.setDaemon(true);
        try {
            // 传入kjb需要的变量
            if (CollectionUtil.isNotEmpty(params)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    job.setParameterValue(entry.getKey(), entry.getValue());
                }
            }
        } catch (UnknownParamException e) {
            String msg = "未知参数异常";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
        // 开始执行kjb
        job.start();
        // 线程等待，直到kjb执行完成
        job.waitUntilFinished();
        // 判断执行过程中是否有错误
        if (job.getErrors() > 0) {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, job.getResult().getLogText());
        }
    }

    /**
     * 运行单个kjb
     *
     * @param fullPathName kjb全路径名
     * @param params kjb需要的命名参数
     * @param logLevel 日志级别
     */
    public static void run(String fullPathName, Map<String, String> params, LogLevel logLevel) {
        try {
            // 通过ktr全路径名获取ktr元数据
            JobMeta jm = new JobMeta(fullPathName, null);
            // 设置日志级别
            if (logLevel != null) {
                jm.setLogLevel(logLevel);
            }
            // 开始执行kjb
            executeJob(null, jm, params);
        } catch (KettleXMLException e) {
            String msg = "解析XML文件获取元数据异常";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
    }

    /**
     * 运行资源库中的kjb
     * @param rep 资源库对象
     * @param relDir kjb所在目录的相对路径
     * @param jobName kjb名称
     * @param versionLabel 版本号，传入null表示执行最新的kjb
     * @param params kjb需要的命名参数
     * @param logLevel 日志级别
     */
    public static void run(AbstractRepository rep, String relDir, String jobName, String versionLabel, Map<String, String> params, LogLevel logLevel) {
        try {
            // 根据相对目录地址获取ktr所在目录信息
            RepositoryDirectoryInterface rdi = rep.loadRepositoryDirectoryTree().findDirectory(relDir);
            // 在指定资源库的目录下找到要执行的转换
            JobMeta jm = rep.loadJob(jobName, rdi, new ProgressNullMonitorListener(), versionLabel);
            // 设置日志级别
            if (logLevel != null) {
                jm.setLogLevel(logLevel);
            }
            // 开始执行kjb
            executeJob(rep, jm, params);
        } catch (KettleException e) {
            String msg = "获取kjb的元数据失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
    }
}
