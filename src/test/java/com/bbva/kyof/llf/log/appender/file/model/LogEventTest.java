package com.bbva.kyof.llf.log.appender.file.model;

import ch.qos.logback.classic.Level;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class LogEventTest
{
	@Test
	public void createEventsTest()
	{
		final LogEvent event = LogEvent.createEmptyEvent();

		Assert.assertNotNull(event);
		Assert.assertEquals(event.getMessage(), null);

		event.createOnlyMsgEvent("message", Level.ERROR);

		Assert.assertEquals(event.getMessage(), "message");
		Assert.assertEquals(event.getLogLevel(), Level.ERROR);

		event.createLayoutEvent("layout", Level.INFO);
		Assert.assertEquals(event.getLogEventMode(), LogEventMode.LOGBACKLAYOUT);

		event.createCompleteEvent("complete", Level.ERROR, "threadName", "loggerName");
		Assert.assertEquals(event.getLogEventMode(), LogEventMode.ALLCOMPLETE);
		Assert.assertEquals(event.getThread(), "threadName");
		Assert.assertEquals(event.getLoggerName(), "loggerName");

		Assert.assertNotNull(event.getTimeString());
	}
}
