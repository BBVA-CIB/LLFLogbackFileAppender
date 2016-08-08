package com.bbva.kyof.llf.log.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Helper class to create the string representation of the time
 */
public final class TimeUtils
{
	/** Private constructor to avoid instantiation */
	private TimeUtils()
	{
		// Empty constructor
	}

	/**
	 * Get the date in String format: year, mouth, day, hour, minute, second and millisecond
	 * @param timestamp the time in time stamp format
	 * @return String - date in String Format
	 */
	public static String getTimeString(final long timestamp)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp);
		
		return Integer.toString(calendar.get(Calendar.YEAR))
				+ "-" + Integer.toString(calendar.get(Calendar.MONTH)+1)
				+ "-" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
				+ " " + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY))
				+ ":" + Integer.toString(calendar.get(Calendar.MINUTE))
				+ ":" + Integer.toString(calendar.get(Calendar.SECOND))
				+ ":" + Integer.toString(calendar.get(Calendar.MILLISECOND));
	}
}
