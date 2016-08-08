package com.bbva.kyof.llf.log.appender.file.model;

/**
 * Enum class that represent the log event mode to define the format of the stored messages
 */
public enum LogEventMode 
{
	/** ONLY_MESSAGE mode variable */
	ONLY_MESSAGE(0, "ONLY_MESSAGE"), 
	/** ALLCOMPLETE mode variable */
	ALLCOMPLETE(1, "ALLCOMPLETE"), 
	/** LOGBACKLAYOUT mode variable */
	LOGBACKLAYOUT(2, "LOGBACKLAYOUT"),
	/** UNKNOWN mode variable */
	UNKNOWN(3, "UNKNOWN");
	
	/** intValue variable */
	private int intValue;
	/** String value variable */
	private String stringValue;

	/**
	 * Create a new log event mode
	 *
	 * @param value numeric value of the enum
	 * @param stringValue string value of the enum
	 */
	private LogEventMode(final int value, final String stringValue)
	{
		this.intValue = value;
		this.stringValue = stringValue;
	}

	/** Returns the numeric representation of the enum */
	public int getIntValue()
	{
		return intValue;
	}

	/** Returns the string representation of the enum */
	public String getStringValue()
	{
		return stringValue;
	}

	/** Get the string representation of the enum from the numeric representation */
	public static String getStringValueFromInt(final int value)
	{
		String stringValue;
		switch (value)
		{
			case 0:
				stringValue = ONLY_MESSAGE.stringValue;
				break;
			case 1:
				stringValue = ALLCOMPLETE.stringValue;
				break;
			case 2:
				stringValue =  LOGBACKLAYOUT.stringValue;
				break;
			default:
				stringValue = UNKNOWN.stringValue;
				break;
		}
		return stringValue;
	}

	/** Return the enum from the numeric representation */
	public static LogEventMode fromIntValue(final int value)
	{
		LogEventMode logEventMode = null;
		switch (value)
		{
			case 0:
				logEventMode = ONLY_MESSAGE;
				break;
			case 1:
				logEventMode = ALLCOMPLETE;
				break;
			case 2:
				logEventMode = LOGBACKLAYOUT;
				break;
			default:
				logEventMode = UNKNOWN;
				break;
		}
		return logEventMode;
	}
}
