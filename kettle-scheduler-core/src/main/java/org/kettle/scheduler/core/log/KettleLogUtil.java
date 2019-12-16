package org.kettle.scheduler.core.log;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.LoggingBuffer;

import java.util.List;

/**
 * kettle日志操作工具
 * @author lyf
 */
public class KettleLogUtil {

	/**
	 * 获取当前任务执行日志
	 * 如果使用{@code appender.getBuffer(logChannelId, true);}方式获取错误日志的时候会存在历史记录
	 * 因此从写获取日志方法只获取当前任务执行日志
	 * @param logChannelId 日志通道ID
	 * @param includeGeneral 是否包括一般日志
	 * @param minTimeBoundary 获取某个时间点之后的日志
	 * @return {@link String} 日志
	 */
	public static String getLogText(String logChannelId, boolean includeGeneral, long minTimeBoundary) {
		StringBuilder eventBuffer = new StringBuilder(10000);
		LoggingBuffer appender = KettleLogStore.getAppender();
		List<KettleLoggingEvent> loggingEvents = appender.getLogBufferFromTo(logChannelId, includeGeneral, 0, appender.getLastBufferLineNr()+1);
		loggingEvents.stream().filter(event -> event.timeStamp > minTimeBoundary-1000).forEach(event -> {
			eventBuffer.append(appender.getLayout().format(event)).append(Const.CR);
		});
		return eventBuffer.toString();
	}
}
