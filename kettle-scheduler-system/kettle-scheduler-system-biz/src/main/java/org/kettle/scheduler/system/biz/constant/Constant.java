package org.kettle.scheduler.system.biz.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置在yml文件中的常量数据
 * @author lyf
 */
@Component
@ConfigurationProperties(prefix = "constant")
@Data
public class Constant {

    /**
     * 设置sessionId
     */
    public static String mySession;

    /**
     * 加密盐
     */
    public static String salt;

	/**
	 * 加密次数
	 */
	public static Integer hashIterations;

	public void setMySession(String mySession) {
		Constant.mySession = mySession;
	}

	public void setSalt(String salt) {
        Constant.salt = salt;
    }

	public void setHashIterations(Integer hashIterations) {
		Constant.hashIterations = hashIterations;
	}
}
