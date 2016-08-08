package com.bbva.kyof.llf.log.appender.file;

import ch.qos.logback.classic.Level;
import com.bbva.kyof.llf.log.appender.file.model.LogEvent;
import com.bbva.kyof.llf.log.appender.file.model.LogEventMode;
import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class HandleLogEventTest
{
	@Test
	public void testOnEventAllComplete() throws Exception
	{
		WriteLogEvent logEventWriter = EasyMock.createMock(WriteLogEvent.class);

		FileAppenderParameters appenderParams = new FileAppenderParameters();
		appenderParams.setLogEventMode(LogEventMode.ALLCOMPLETE);

		final HandleLogEvent logEventHandler = new HandleLogEvent(logEventWriter,  appenderParams);

		LogEvent logEvent = LogEvent.createEmptyEvent();
		logEvent.createOnlyMsgEvent("message", Level.ERROR);

		logEventWriter.writeToFileAllCompleteMode(logEvent);
		EasyMock.expectLastCall().once();

		EasyMock.replay(logEventWriter);

		logEventHandler.onEvent(logEvent, 1, true);
	}

	@Test
	public void testOnEventLogbackLayout() throws Exception
	{
		WriteLogEvent logEventWriter = EasyMock.createMock(WriteLogEvent.class);

		FileAppenderParameters appenderParams = new FileAppenderParameters();
		appenderParams.setLogEventMode(LogEventMode.LOGBACKLAYOUT);

		final HandleLogEvent logEventHandler = new HandleLogEvent(logEventWriter,  appenderParams);

		LogEvent logEvent = LogEvent.createEmptyEvent();
		logEvent.createOnlyMsgEvent("message", Level.ERROR);

		logEventWriter.writeToFileLogBackLayoutMode(logEvent);
		EasyMock.expectLastCall().once();

		EasyMock.replay(logEventWriter);

		logEventHandler.onEvent(logEvent, 1, true);
	}

	@Test
	public void testOnEventOnlyMessage() throws Exception
	{
		WriteLogEvent logEventWriter = EasyMock.createMock(WriteLogEvent.class);

		FileAppenderParameters appenderParams = new FileAppenderParameters();
		appenderParams.setLogEventMode(LogEventMode.ONLY_MESSAGE);

		final HandleLogEvent logEventHandler = new HandleLogEvent(logEventWriter,  appenderParams);

		LogEvent logEvent = LogEvent.createEmptyEvent();
		logEvent.createOnlyMsgEvent("message", Level.ERROR);

		logEventWriter.writeToFileMessageMode(logEvent);
		EasyMock.expectLastCall().once();

		EasyMock.replay(logEventWriter);

		logEventHandler.onEvent(logEvent, 1, true);
	}

	@Test
	public void testOnEventUnknown() throws Exception
	{
		WriteLogEvent logEventWriter = EasyMock.createMock(WriteLogEvent.class);

		FileAppenderParameters appenderParams = new FileAppenderParameters();
		appenderParams.setLogEventMode(LogEventMode.UNKNOWN);

		final HandleLogEvent logEventHandler = new HandleLogEvent(logEventWriter,  appenderParams);

		LogEvent logEvent = LogEvent.createEmptyEvent();
		logEvent.createOnlyMsgEvent("message", Level.ERROR);

		logEventWriter.writeToFileMessageMode(logEvent);
		EasyMock.expectLastCall().once();

		EasyMock.replay(logEventWriter);

		logEventHandler.onEvent(logEvent, 1, true);
	}
}
