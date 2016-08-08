package com.bbva.kyof.llf.log.appender.file;

/**
 * This class is a simple class that stores the version of the application
 */
public final class Version
{
	/**  Version of the project */
	private static final String VERSION_NUMBER = "1.2.0";

	/** Empty private constructor to avoid instantiation */
	private Version()
	{
		// Empty constructor
	}

	/** Returns the application version */
	public static String getVersion()
	{
		return VERSION_NUMBER;
	}
}
