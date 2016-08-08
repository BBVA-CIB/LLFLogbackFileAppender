package com.bbva.kyof.llf.log.appender.file.model;

import ch.qos.logback.classic.Level;

import com.bbva.kyof.llf.log.util.TimeUtils;
import com.lmax.disruptor.EventFactory;

/** 
 * Class that represent a Log event
 */
public final class LogEvent
{
	/** Content of the message of the event Log */
	private transient String message = null;
	/** Log level from the eventLog */
	private transient Level logLevel = null;
	/** name of the Thread from eventLog */
	private transient String thread = null; 
	/** Name of the logger from eventLog */
	private transient String loggerName = null;
	/** Enum to indicate if the mode of the received log event  */
	private transient LogEventMode logEventMode = null;
	/** Content of the exactly time of the event Log (LONG) */
	private transient long timesCurrentMillis = 0;
	
	/**
	 * Constructor. Initialize all attributes.
	 */
	private LogEvent()
	{
		super();
	}
		
	/**
	 * Event Factory to create a new LogEvent for RingBuffer
	 * @return LogEvent - new empty Log Event
	 */
    public static final EventFactory<LogEvent> EVENT_FACTORY = new EventFactory<LogEvent>()
    {
		/** Create a new log event instance */
        public LogEvent newInstance()
        {
            return new LogEvent();
        }
    };
	
    /**
	 * Create new LogEvent with empty parameters. It's a new LogEvent 
	 * @return LogEvent - new empty Log Event
	 */
	public static LogEvent createEmptyEvent()
	{
		return new LogEvent();
	}
	
	/**
	 * Create new LogEvent with all parameters necessary for ONLY_MESSAGE mode.
	 * @param msg - Text message of LogEvent
	 * @param logLevel - Log Level of the LogEvent 
	 */
	public void createOnlyMsgEvent(final String msg, final Level logLevel)
	{
		this.message = msg;
		this.logLevel = logLevel;
		this.logEventMode = LogEventMode.ONLY_MESSAGE;
	}
	
	/**
	 * Create new LogEvent with all parameters necessary for ALLCOMPLETE mode.
	 * @param msg - Text message of LogEvent
	 * @param logLevel - Log Level of the LogEvent 
	 * @param thread - name of thread created the logEvent
	 * @param loggerName - name of Logger of LogEvent
	 */
	public void createCompleteEvent(final String msg, final Level logLevel, final String thread,
			final String loggerName)
	{
		this.message = msg;
		this.logLevel = logLevel;
		this.thread = thread;
		this.loggerName = loggerName;
		this.logEventMode = LogEventMode.ALLCOMPLETE;
		this.timesCurrentMillis = System.currentTimeMillis();
	}

	/**
	 * Create new LogEvent with all parameters necessary for LOGBACKLAYOUT mode.
	 * @param msg - Text message of LogEvent
	 * @param logLevel - Log Level of the LogEvent 
	 */
	public void createLayoutEvent(final String msg, final Level logLevel)
	{
		this.message = msg;
		this.logLevel = logLevel;
		this.logEventMode = LogEventMode.LOGBACKLAYOUT;
	}
	
	////////////GETTERS/////////////
	
	/**
	 * Get the content of message from LogEvent
	 * @return String - message from LogEvent
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	/**
	 * Get Log level from LogEvent
	 * @return Level class - Level from Log Event. Use method toString() to see in format text
	 */
	public Level getLogLevel()
	{
		return this.logLevel;
	}
	
	/**
	 * Get the name of Thread that execute the from LogEvent
	 * @return String - name of thread
	 */
	public String getThread()
	{
		return this.thread;
	}
	
	/**
	 * Get the name of logger
	 * @return String - name of Logger
	 */
	public String getLoggerName()
	{
		return this.loggerName;
	}
	
	/**
	 * Get the date in String format: year, mouth, day, hour, minute, second and millisecond
	 * @return String - date in String Format
	 */
	public String getTimeString()
	{
		return TimeUtils.getTimeString(this.timesCurrentMillis);
	}
	
	/**
	 * Get the content of message from LogEvent
	 * @return String - message from LogEvent
	 */
	public LogEventMode getLogEventMode()
	{
		return this.logEventMode;
	}
}
