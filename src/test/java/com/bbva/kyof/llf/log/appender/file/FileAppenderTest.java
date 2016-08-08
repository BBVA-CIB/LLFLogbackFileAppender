package com.bbva.kyof.llf.log.appender.file;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.bbva.kyof.llf.log.appender.file.model.LogEventMode;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class FileAppenderTest
{
	@Test
	public void testStartValidation() throws Exception
	{
		FileAppender appender = new FileAppender();
		appender.start();
		Assert.assertFalse(appender.isStarted());
	}

	@Test
	public void testStart() throws Exception
	{
		FileAppender appender = new FileAppender();

		appender.setLogEventMode(LogEventMode.ALLCOMPLETE.getStringValue());
		Assert.assertEquals(appender.getLogEventMode(), LogEventMode.ALLCOMPLETE.toString());

		appender.setHybridMode(FileAppenderMode.ASYNCHRONOUS.toString());
		Assert.assertEquals(appender.getHybridMode(), FileAppenderMode.ASYNCHRONOUS.toString());

		appender.setIsDiscard(true);
		Assert.assertEquals(appender.isDiscard(), true);

		appender.setDiscardPercent(30);
		Assert.assertEquals(appender.getDiscardPercent(), 30);

		appender.setCleanLogLevel(Level.ERROR.toString());
		Assert.assertEquals(appender.getCleanLogLevel(), Level.ERROR.toString());

		appender.setRingSize(2048);
		Assert.assertEquals(appender.getRingSize(), 2048);

		appender.setFileName("log.txt");
		Assert.assertEquals(appender.getFileName(), "log.txt");

		appender.setBufferSize(3000);
		Assert.assertEquals(appender.getBufferSize(), 3000);

		appender.start();

		Assert.assertTrue(appender.isStarted());

		LoggingEvent event = new LoggingEvent();
		event.setLevel(Level.DEBUG);
		event.setLoggerName("logger");
		event.setMessage("message");

		appender.append(event);


		appender.stop();


		Assert.assertFalse(appender.isStarted());
	}

	@Test
	public void testStop() throws Exception
	{

	}

	@Test
	public void testAppend() throws Exception
	{

	}

	@Test
	public void testOnError() throws Exception
	{

	}

	@Test
	public void testOnError1() throws Exception
	{

	}
}
