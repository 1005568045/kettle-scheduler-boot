package org.kettle.scheduler.core.execute;

import lombok.extern.slf4j.Slf4j;
import org.kettle.scheduler.common.utils.CollectionUtil;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.JsonUtil;
import org.kettle.scheduler.core.log.KettleLogUtil;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
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
    private static String executeTrans(TransMeta tm, Map<String, String> params, String[] args) throws KettleException {
        // 通过元数据获取ktr的实例
        Trans trans = new Trans(tm);
        // 传入ktr需要的变量
        if (CollectionUtil.isNotEmpty(params)) {
			log.info("传入kettle的参数：{}", JsonUtil.toJsonString(params));
            for (Map.Entry<String, String> entry : params.entrySet()) {
                trans.setParameterValue(entry.getKey(), entry.getValue());
            }
        }
        // 开始执行ktr，该方法还可以传入命令行参数args
        trans.execute(args);
        // 线程等待，直到ktr执行完成
        trans.waitUntilFinished();
        // 执行完成后获取日志
		String logText = KettleLogUtil.getLogText(trans.getLogChannelId(), true, trans.getLogDate().getTime());
		// 判断执行过程中是否有错误, 有错误就抛出错误日志
		if (trans.getErrors() > 0) {
            throw new KettleException(logText);
        }
        // 没有错误就返回正常执行日志
        return logText;
    }

    /**
     * 运行单个ktr
     *
     * @param fullPathName ktr全路径名
     * @param params ktr需要的命名参数
     * @param logLevel 日志级别
     */
    public static String run(String fullPathName, Map<String, String> params, LogLevel logLevel) throws KettleException {
        // 通过ktr全路径名获取ktr元数据
        TransMeta tm = new TransMeta(FileUtil.replaceSeparator(fullPathName));
        // 设置日志级别
        if (logLevel != null) {
            tm.setLogLevel(logLevel);
        }
        // 开始执行ktr
        return executeTrans(tm, params, null);
    }

    /**
     * 运行资源库中的ktr
     *
     * @param rep 资源库对象
     * @param transPath ktr所在路径
     * @param transName ktr名称
     * @param versionLabel 版本号，传入null表示执行最新的ktr
     * @param params ktr需要的命名参数
     * @param logLevel 日志级别
     */
    public static String run(AbstractRepository rep, String transPath, String transName, String versionLabel, Map<String, String> params, LogLevel logLevel) throws KettleException {
        // 根据相对目录地址获取ktr所在目录信息
        RepositoryDirectoryInterface rdi = rep.loadRepositoryDirectoryTree().findDirectory(FileUtil.getParentPath(transPath));
        // 在指定资源库的目录下找到要执行的转换
        TransMeta tm = rep.loadTransformation(FileUtil.getFileName(transName), rdi, new ProgressNullMonitorListener(), true, versionLabel);
        // 设置日志级别
        if (logLevel != null) {
            tm.setLogLevel(logLevel);
        }
        // 开始执行ktr
        return executeTrans(tm, params, null);
    }
}
