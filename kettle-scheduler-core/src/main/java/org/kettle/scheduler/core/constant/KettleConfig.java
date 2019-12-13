package org.kettle.scheduler.core.constant;

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

	/**
	 * ktr或kjb文件保存路径
	 */
	public static String uploadPath;

	/**
	 * kettle所在路径，初始化会自动生成.kettle文件在该目录
	 */
	public static String kettleHome;

	/**
	 * kettle插件包所在路径 eg: D:\Development\kettle\8.3\data-integration\plugins
	 */
	public static String kettlePluginPackages;

	public void setLogFilePath(String logFilePath) {
        KettleConfig.logFilePath = FileUtil.replaceSeparator(logFilePath);;
    }

    public void setEncoding(Charset encoding) {
        KettleConfig.encoding = encoding;
    }

	public void setUploadPath(String uploadPath) {
		KettleConfig.uploadPath = uploadPath;
	}

	public void setKettleHome(String kettleHome) {
		KettleConfig.kettleHome = kettleHome;
	}

	public void setKettlePluginPackages(String kettlePluginPackages) {
		KettleConfig.kettlePluginPackages = kettlePluginPackages;
	}
}
