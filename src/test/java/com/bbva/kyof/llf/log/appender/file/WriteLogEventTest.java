package com.bbva.kyof.llf.log.appender.file;

import ch.qos.logback.classic.Level;
import com.bbva.kyof.llf.log.appender.file.model.LogEvent;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class WriteLogEventTest implements IErrorLogger
{
	@Test
	public void testWriteToFileMessageMode() throws Exception
	{
		WriteLogEvent writeLog = new WriteLogEvent("file.log", 2056, this);

		LogEvent event = LogEvent.createEmptyEvent();
		event.createOnlyMsgEvent("message", Level.ERROR);
		writeLog.writeToFileMessageMode(event);
		writeLog.stop();

		// Make sure there are no exceptions
		Assert.assertTrue(true);
	}

	@Test
	public void testWriteToFileAllCompleteMode() throws Exception
	{
		WriteLogEvent writeLog = new WriteLogEvent("file.log", 2056, this);

		LogEvent event = LogEvent.createEmptyEvent();
		event.createCompleteEvent("message", Level.ERROR, "thread", "something");
		writeLog.writeToFileAllCompleteMode(event);
		writeLog.stop();

		// Make sure there are no exceptions
		Assert.assertTrue(true);
	}

	@Test
	public void testWriteToFileLogBackLayoutMode() throws Exception
	{
		WriteLogEvent writeLog = new WriteLogEvent("file.log", 2056, this);

		LogEvent event = LogEvent.createEmptyEvent();
		event.createOnlyMsgEvent("message", Level.ERROR);
		writeLog.writeToFileLogBackLayoutMode(event);
		writeLog.stop();

		// Make sure there are no exceptions
		Assert.assertTrue(true);
	}

	@Override
	public void onError(String msg, Throwable cause)
	{

	}
}
