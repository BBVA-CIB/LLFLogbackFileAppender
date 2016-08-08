package com.bbva.kyof.llf.log.appender.file;

import ch.qos.logback.classic.Level;
import com.bbva.kyof.llf.log.appender.file.model.LogEventMode;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class FileAppenderParametersTest
{
	@Test
	public void testParameters() throws Exception
	{
		FileAppenderParameters parameters = new FileAppenderParameters();

		parameters.setRingSize(2000);
		Assert.assertEquals(parameters.getRingSize(), 2000);

		parameters.setFileName("log.txt");
		Assert.assertEquals(parameters.getFileName(), "log.txt");

		parameters.setBufferSize(3000);
		Assert.assertEquals(parameters.getBufferSize(), 3000);

		parameters.setLogEventMode(LogEventMode.ALLCOMPLETE.getStringValue());
		Assert.assertEquals(parameters.getLogEventMode(), LogEventMode.ALLCOMPLETE);

		parameters.setLogEventMode(LogEventMode.ALLCOMPLETE);
		Assert.assertEquals(parameters.getLogEventMode(), LogEventMode.ALLCOMPLETE);

		parameters.setHybridMode(FileAppenderMode.ASYNCHRONOUS.toString());
		Assert.assertEquals(parameters.getHybridMode(), FileAppenderMode.ASYNCHRONOUS);

		parameters.setHybridMode(FileAppenderMode.ASYNCHRONOUS);
		Assert.assertEquals(parameters.getHybridMode(), FileAppenderMode.ASYNCHRONOUS);

		parameters.setIsDiscard(true);
		Assert.assertEquals(parameters.isDiscard(), true);

		parameters.setDiscardPercent(30);
		Assert.assertEquals(parameters.getDiscardPercent(), 30);

		parameters.setCleanLogLevel(Level.ERROR.toString());
		Assert.assertEquals(parameters.getCleanLogLevel(), Level.ERROR);

		parameters.setCleanLogLevel(Level.ERROR);
		Assert.assertEquals(parameters.getCleanLogLevel(), Level.ERROR);

		parameters.setCleanLogLevel(Level.ALL);
		Assert.assertEquals(parameters.getCleanLogLevel(), Level.ALL);
	}
}
