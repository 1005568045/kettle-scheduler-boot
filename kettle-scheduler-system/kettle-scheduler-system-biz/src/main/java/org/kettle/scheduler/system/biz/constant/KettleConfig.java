package org.kettle.scheduler.system.biz.constant;

import lombok.Data;
import org.kettle.scheduler.common.utils.FileUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 配置在yml文件中的常量数据
 * @author lyf
 */
@Component
@ConfigurationProperties(prefix = "kettle")
@Data
public class KettleConfig {

    /**
     * 日志文件输出路径
     */
    public static String logFilePath;

    /**
     * kettle编码设置
     */
    public static Charset encoding;

    public void setLogFilePath(String logFilePath) {
        KettleConfig.logFilePath = FileUtil.replaceSeparator(logFilePath);;
    }

    public void setEncoding(Charset encoding) {
        KettleConfig.encoding = encoding;
    }
}
