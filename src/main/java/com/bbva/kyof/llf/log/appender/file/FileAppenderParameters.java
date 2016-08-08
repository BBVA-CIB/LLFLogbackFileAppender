package com.bbva.kyof.llf.log.appender.file;

import com.bbva.kyof.llf.log.appender.file.model.LogEventMode;

import ch.qos.logback.classic.Level;

/**
 * General parameters for an HybridFileAppender
 * 
 * @author sgb
 * 
 */
public final class FileAppenderParameters
{
	/** Hybrid Mode */
	private static final FileAppenderMode DEFAULT_HYDBRID_MODE = FileAppenderMode.SYNCHRONOUS;
	/** Default status of discard mode */
	private static final boolean DEFAULT_IS_DISCARD = false;
	/** Default RingBuffer size */
	private static final int DEFAULT_RING_SIZE = 8192;
	/** Name of mode to create of eventLog */
	private static final LogEventMode DEFAULT_LOG_EVENT_MODE = LogEventMode.ONLY_MESSAGE;
	/** Size of File/Memory */
	private static final int DEFAULT_BUFFER_SIZE = 512;
	/** Default percentage when Discard mode is enabled */
	private static final int DEFAULT_DISCARD_PERCENT = 85;
	/** Default value to clean when use clean log mode */
	private static final Level DEFAULT_CLEAN_LOG_LEVEL = Level.TRACE;
	/** Variable to set the size of Ring Buffer */
	private int ringSize = DEFAULT_RING_SIZE;
	/** Variable to set the size of Byte Buffer */
	private int bufferSize = DEFAULT_BUFFER_SIZE ;
	/** Variable to set or activate Discard mode */
	private boolean discard = DEFAULT_IS_DISCARD;
	/** Variable to set percentage for Discard mode */
	private int discardPercent = DEFAULT_DISCARD_PERCENT;
	/** Variable to set name of file */
	private String fileName = null;
	/** Variable to set message mode */
	private LogEventMode logEventMode = DEFAULT_LOG_EVENT_MODE;
	/**  Variable to set Hybrid mode  */
	private FileAppenderMode hybridMode = DEFAULT_HYDBRID_MODE;
	/** Variable to set what level log want to discard */
	private Level cleanLogLevel = DEFAULT_CLEAN_LOG_LEVEL;

	/** Returns the ring size */
	public int getRingSize()
	{
		return ringSize;
	}

	/** Returns true if discard mode is active */
	public boolean isDiscard()
	{
		return discard;
	}

	/** Return the percentaje of the ring to start discarding elements */
	public int getDiscardPercent()
	{
		return discardPercent;
	}

	/** Set the log level in which discard mode is allowed */
	public Level getCleanLogLevel()
	{
		return cleanLogLevel;
	}

	/** Get the name of the file to write the log into */
	public String getFileName()
	{
		return this.fileName;
	}

	/** Return the size of the buffer to write into disk */
	public int getBufferSize()
	{
		return this.bufferSize;
	}

	/** Return the log event mode */
	public LogEventMode getLogEventMode()
	{
		return this.logEventMode;
	}

	/** Return the hybrid mode to use if active */
	public FileAppenderMode getHybridMode()
	{
		return this.hybridMode;
	}

	/** Sets the ring buffer size to store events */
	public void setRingSize(final int ringSize)
	{
		this.ringSize = ringSize;
	}

	/** Set the file name to store the log */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName.trim();
	}

	/** Sets the size of the buffer to store into files */
	public void setBufferSize(final int bufferSize)
	{
		this.bufferSize = bufferSize;
	}

	/** Sets the log event mode as an string */
	public void setLogEventMode(final String logEventMode)
	{
		this.logEventMode = LogEventMode.valueOf(logEventMode);
	}

	/** Set the log event mode as an enum */
	public void setLogEventMode(final LogEventMode logEventMode)
	{
		this.logEventMode = logEventMode;
	}

	/** Set the hybrid mode as an String */
	public void setHybridMode(final String hybirdMode)
	{
		this.hybridMode = FileAppenderMode.valueOf(hybirdMode);
	}

	/** Set the hybrid mode as an enum */
	public void setHybridMode(final FileAppenderMode hybridMode)
	{
		this.hybridMode = hybridMode;
	}

	/** Set the discard mode */
	public void setIsDiscard(final boolean isDiscard)
	{
		this.discard = isDiscard;
	}

	/** Set the discar percent */
	public void setDiscardPercent(final int discardPercent)
	{
		this.discardPercent = discardPercent;
	}

	/** Set the clean log level */
	public void setCleanLogLevel(final String cleanLogLevel)  
	{
		final Level levelLog = Level.valueOf(cleanLogLevel);

		if(cleanLogLevel.equalsIgnoreCase(levelLog.toString()))
		{
			this.cleanLogLevel = levelLog;
		}
		else
		{
			if(this.discard)
			{
				this.cleanLogLevel = null;
			}
		}
	}

	/** Set the clean log level as an enum */
	public void setCleanLogLevel(final Level cleanLogLevel)
	{
		this.cleanLogLevel = cleanLogLevel;
	}
}
