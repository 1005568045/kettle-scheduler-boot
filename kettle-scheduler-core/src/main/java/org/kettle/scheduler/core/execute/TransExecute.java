package org.kettle.scheduler.core.execute;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.utils.CollectionUtil;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.repository.AbstractRepository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import java.util.Map;

/**
 * kettle的ktr脚本执行器
 *
 * @author lyf
 */
@Slf4j
public class TransExecute {

    /**
     * ktr执行单元，所有的ktr执行都调用该方法
     *
     * @param tm ktr元数据
     * @param params ktr需要的命名参数
     * @param args 命令行参数
     */
    private static void executeTrans(TransMeta tm, Map<String, String> params, String[] args) {
        try {
            // 通过元数据获取ktr的实例
            Trans trans = new Trans(tm);
            // 传入ktr需要的变量
            if (CollectionUtil.isNotEmpty(params)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    trans.setParameterValue(entry.getKey(), entry.getValue());
                }
            }
            // 开始执行ktr，该方法还可以传入命令行参数args
            trans.execute(args);
            // 线程等待，直到ktr执行完成
            trans.waitUntilFinished();
            // 判断执行过程中是否有错误
            if (trans.getErrors() > 0) {
                throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, trans.getResult().getLogText());
            }
        } catch (UnknownParamException e) {
            String msg = "未知参数异常";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        } catch (KettleException e) {
            log.error(GlobalStatusEnum.KETTLE_ERROR.getDesc(), e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "kettle执行失败");
        }
    }

    /**
     * 运行单个ktr
     *
     * @param fullPathName ktr全路径名
     * @param params ktr需要的命名参数
     * @param logLevel 日志级别
     */
    public static void run(String fullPathName, Map<String, String> params, LogLevel logLevel) {
        try {
            // 通过ktr全路径名获取ktr元数据
            TransMeta tm = new TransMeta(fullPathName);
            // 设置日志级别
            if (logLevel != null) {
                tm.setLogLevel(logLevel);
            }
            // 开始执行ktr
            executeTrans(tm, params, null);
        } catch (KettleXMLException e) {
            String msg = "解析XML文件获取元数据异常";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        } catch (KettleMissingPluginsException e) {
            String msg = "kettle插件缺失异常";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
    }

    /**
     * 运行资源库中的ktr
     *
     * @param rep 资源库对象
     * @param relDir ktr所在目录的相对路径
     * @param transName ktr名称
     * @param versionLabel 版本号，传入null表示执行最新的ktr
     * @param params ktr需要的命名参数
     * @param logLevel 日志级别
     */
    public static void run(AbstractRepository rep, String relDir, String transName, String versionLabel, Map<String, String> params, LogLevel logLevel) {
        try {
            // 根据相对目录地址获取ktr所在目录信息
            RepositoryDirectoryInterface rdi = rep.loadRepositoryDirectoryTree().findDirectory(relDir);
            // 在指定资源库的目录下找到要执行的转换
            TransMeta tm = rep.loadTransformation(transName, rdi, new ProgressNullMonitorListener(), true, versionLabel);
            // 设置日志级别
            if (logLevel != null) {
                tm.setLogLevel(logLevel);
            }
            // 开始执行ktr
            executeTrans(tm, params, null);
        } catch (KettleException e) {
            String msg = "获取ktr的元数据失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
    }
}
