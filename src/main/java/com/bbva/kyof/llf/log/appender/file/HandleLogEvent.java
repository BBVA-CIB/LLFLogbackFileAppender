package com.bbva.kyof.llf.log.appender.file;

import com.bbva.kyof.llf.log.appender.file.model.LogEvent;
import com.lmax.disruptor.EventHandler;

/**
 * <p>
 * The process get LogEvent of the Ring buffer. 
 * </p>
 * 
 * <p>
 * After, use the instance of the class WriteLogEvent to write logEvent to file.
 * </p>
 * 
 * @author sgb
 * 
 */
public class HandleLogEvent implements EventHandler<LogEvent>
{
	/** Instance used to write on File */
	private final transient WriteLogEvent writeLogEvent;
	/** Parameters of the HybridFileAppender */
	private final transient FileAppenderParameters parameters;
	 
	/**
	 * Constructor for a MessageSender
	 * 
	 * @param params - WriteLogEvent class to write to file
	 * @param params - parameters of HybridFileAppender
	 */
	public HandleLogEvent(final WriteLogEvent writeLog, final FileAppenderParameters params)
	{
		//Initialize  FileAppenderParams and WirteLogEvent
		this.writeLogEvent = writeLog;
		this.parameters = params;
	}
	
	/**
	 * Write the logEvent to File depend on Message Mode
	 * @param logEvent - LogEvent to write
	 */
	private void writeToFile(final LogEvent logEvent)
	{
		switch (this.parameters.getLogEventMode())
		{
		case ONLY_MESSAGE:
			this.writeLogEvent.writeToFileMessageMode(logEvent);

			break;
		case ALLCOMPLETE:
			this.writeLogEvent.writeToFileAllCompleteMode(logEvent);
			
			break;
		case LOGBACKLAYOUT:
			this.writeLogEvent.writeToFileLogBackLayoutMode(logEvent);
			
			break;
		default:
			this.writeLogEvent.writeToFileMessageMode(logEvent);
		}
	}
	
	/**
	 * Method that takes the Ring Event Log Buffer. 
	 * Once it does, the file type by instantiating the class WriteLogEvent
	 * @param logEvent - log Event to analyze
	 * @param sequence - number of sequence of the LogEvent from Ring Buffer
	 * @param isLast - true if is the last logEvent in the Ring Buffer
	 * @throws Exception
	 */
	@Override
	public void onEvent(final LogEvent logEvent, final long sequence, final boolean isLast)
	{
		this.writeToFile(logEvent);
	}
}
