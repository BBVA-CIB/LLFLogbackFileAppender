package com.bbva.kyof.llf.log.appender.file.model;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class LogEventModeTest
{
	@Test
	public void testGetIntValue() throws Exception
	{
		Assert.assertEquals(LogEventMode.LOGBACKLAYOUT.getIntValue(), 2);
	}

	@Test
	public void testGetStringValue() throws Exception
	{
		Assert.assertEquals(LogEventMode.LOGBACKLAYOUT.getStringValue(), "LOGBACKLAYOUT");
	}

	@Test
	public void testGetStringValueFromInt() throws Exception
	{
		Assert.assertEquals(LogEventMode.getStringValueFromInt(LogEventMode.LOGBACKLAYOUT.getIntValue()), LogEventMode.LOGBACKLAYOUT.getStringValue());
	}

	@Test
	public void testFromIntValue() throws Exception
	{
		Assert.assertEquals(LogEventMode.fromIntValue(2), LogEventMode.LOGBACKLAYOUT);
	}
}
