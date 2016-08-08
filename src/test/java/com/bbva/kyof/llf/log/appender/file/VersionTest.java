package com.bbva.kyof.llf.log.appender.file;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Carlos on 15/04/2014.
 */
public class VersionTest extends TestCase
{
	@Test
	public void testGetVersion() throws Exception
	{
		Assert.assertEquals(Version.getVersion(), "1.2.0");
	}
}
