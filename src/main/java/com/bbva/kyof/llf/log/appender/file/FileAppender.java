package com.bbva.kyof.llf.log.appender.file;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

import com.bbva.kyof.llf.log.appender.file.model.LogEvent;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;

/**
 * <p>
 * This class contains an implementation of a HybridFileAppender. For proper operation must
 * be configured logback.xml file indicating which is the location of this class.
 * You must put some parameters and configure logback file as [fileName], [logEventMode]
 * and [HybridMode]. You should configure optional parameters in logback.xml file
 * </p>
 * 
 * <p>
 * The HybridFileAppender collects all log events that occur in your application. These events 
 * are save in a file. you can do it in 3 different ways. Synchronous, Asynchronous and using 
 * the above two while
 * </p>
 * 
 * <p>
 * The HybridFileAppender has several modes that you can set in the file logback.xml.
 * </p>
 * 
 * <p>
 * When you're using the asynchronous mode, can be configured so that when the ring buffer is filled
 * to a desired amount, discard log events that are desired and its lower level.
 * </p>
 * 
 * <p>
 * You can choose 3 ways to receive the message log. All of them are captured in an object called 
 * LogEventHyb. There are MESSAGE MODE ( receive  message log and level log ), ALLCOMPLETE ( receive
 * message log, level log, thread name, time and so on ) and finally, LOGBACKLAYOUT (you can configure
 * the Layout of Logback to print the message log). All these parameters must configure in logback.xml 
 * </p>
 * 
 * 
 * @author sgb
 * 
 */
public final class FileAppender extends AppenderBase<ILoggingEvent> implements IErrorLogger
{
	/** Multiplier used to calculate percentages */
	private static final int PERCENTAGE_MULTIPLIER = 100;
	/** Percentage to represented the total size of ring */
	private static final int PERCENTAGE_REPRESENTED_TOTAL = 100;

	/** Parameters of the appender */
	private final transient FileAppenderParameters parameters;

	/** Class used to write on File */
	private transient WriteLogEvent writeLogEvent = null;

	/** Variable which prevent re-entry*/
	private transient boolean reentryFlag = false;

	/** Encoder to get a Layout from LogBack*/
	private transient PatternLayoutEncoder patternLayourEncoder = null;

	/** Thread to Get message from queue and send by LLB*/
	private transient RingBuffer<LogEvent> ringBuffer = null;

	/** True if the thread should stop */
	private transient boolean isStop = false;

	/** Batch event processor */
	private transient BatchEventProcessor<LogEvent> batchEndProcessor = null;

	/**
	 * Default constructor.
	 */
	public FileAppender()
	{
		super();
		
		//Instantiate the class will create the parameters
		this.parameters = new FileAppenderParameters();
	}

	/** Validates the configuration, returning false if there is any problem */
	private boolean validateConfig()
	{
		boolean validateConfig = true;
		if(this.parameters.getFileName()== null || this.parameters.getCleanLogLevel() == null)
		{	
			if(this.parameters.getFileName() == null)
			{
				this.addError("You must put the name in the variable [FileName] of the file where you save the LogEvents");
			}

			if(this.parameters.getCleanLogLevel() == null)
			{
				this.addError("You must put [cleanLogLevel] correctly. You can choose between TRACE - DEBUG - INFO - WARN - ERROR");
			}
			validateConfig = false;
		}
		return validateConfig;

	}

	/** Displays the configuration information on screen */
	private void displayConfigurationInfo()
	{
		//Tip off the Appender mode
		this.addInfo("Appender Mode is: " + parameters.getHybridMode().toString());

		//Tip off the Name of File
		this.addInfo("Name of File is: " + "["+ parameters.getFileName()+ "]");

		//Tip off the Buffers Size
		this.addInfo("Size of Ring Buffer is: " + "[" + parameters.getRingSize() + "]" + "Buffer Size is: "
		+ "[" + parameters.getBufferSize() + "]");

		//Tip off if DiscardMode is enabled
		if (parameters.isDiscard())
		{
			this.addInfo("Discard Mode is enable. Discard Percentage: " + this.parameters.getDiscardPercent()
					+ " Level log Discard: " + this.parameters.getCleanLogLevel().toString());
		}

		//Tip off the log event mode
		this.addInfo("Log event mode: " + parameters.getLogEventMode());
	}

	/**
	 * Initializes Ring, creates and initializes the HandleLogEvent process if you are in ASYNCHRONOUS or MIXED mode
	 */
	@Override
	public void start() 
	{
		if (!validateConfig())
		{
			return;
		}

		// Display the configuration information
		this.displayConfigurationInfo();

		//Instantiate the class will write on file
		this.writeLogEvent = new WriteLogEvent(this.parameters.getFileName(), this.parameters.getBufferSize(), this);

		if(this.parameters.getHybridMode() != FileAppenderMode.SYNCHRONOUS)
		{
			//Create the Instance HandleLogEvent to get the logEvent.
			final HandleLogEvent handleLogEvent = new HandleLogEvent(this.writeLogEvent, 
					this.parameters);

			//Create Ring Buffer
			this.ringBuffer = new RingBuffer<LogEvent>(LogEvent.EVENT_FACTORY, 
					new SingleThreadedClaimStrategy(this.parameters.getRingSize()),
					new SleepingWaitStrategy());

			this.addInfo("Ring Buffer and HandleLogEvent Created");

			//We need the Barrier and Batch Even processor
			final SequenceBarrier barrier = this.ringBuffer.newBarrier();

			//Batch Event Processor
			this.batchEndProcessor = new BatchEventProcessor<LogEvent>(this.ringBuffer,
					barrier, handleLogEvent);

			//Start the sequence on the ring
			this.ringBuffer.setGatingSequences(batchEndProcessor.getSequence());

			/* Thread that remove elements from the ring */
			final Thread ringThread = new Thread(batchEndProcessor);
			ringThread.start();
		}

		this.addInfo("RingBuffer, Barrier, and BatchEventProcessor Inicialized: HybridFileAppender is running");

		super.start();
	}

	/**
	 * Stop the process WriteLogEvent
	 */
	@Override
	public void stop() 
	{
		// Stop the sender
		this.isStop = true;

		this.writeLogEvent.stop();

		if (batchEndProcessor != null)
		{
			this.batchEndProcessor.halt();
		}

		addInfo("HybridFileAppender Stopped");

		// Call the parent method
		super.stop();
	}

	/**
	 * Return if the log event should be discarded.
	 * 
	 * The event is discarded if the percentage usage of the ring is equals or bigger than the configured usage
	 * and the log event level is lower or equals to the configured value for discard.
	 * 
	 * @param event - log event to analyze
	 * @return boolean - true if must discard log event
	 */
	private boolean shouldDiscard(final ILoggingEvent event) 
	{
		// Get the percentage usage of the ring
		final int ringPercentUsage = ((int) this.ringBuffer.remainingCapacity() * PERCENTAGE_MULTIPLIER) / this.parameters.getRingSize();

		boolean allOk = false;

		// If we went over the percentage and the event level is low enough, discard it
		if((ringPercentUsage + this.parameters.getDiscardPercent()) <= PERCENTAGE_REPRESENTED_TOTAL 
				&& event.getLevel().toInt() <= this.parameters.getCleanLogLevel().toInt())
		{
			allOk = true;
		}

		return allOk;
	}


	/** 
	 * Gets an instance of LogEvent depending on which option is selected
	 * in the event log mode
	 * @param logEvent - New LogEvent to give back depending on Message mode
	 * @param event - LogEvent to analyze
	 */
	private void initLogEventFromLogbackEvent(final ILoggingEvent event, final LogEvent logEvent)
	{
		switch(this.parameters.getLogEventMode())
		{
		case ONLY_MESSAGE:
			logEvent.createOnlyMsgEvent(event.getMessage(), event.getLevel());
			break;

		case ALLCOMPLETE:
			logEvent.createCompleteEvent(event.getMessage(), 
					event.getLevel(), event.getThreadName(), event.getLoggerName());
			break;

		case LOGBACKLAYOUT:
			if(this.patternLayourEncoder == null)
			{
				this.addError("There was an error with [encoder]. " 
			+ "No instance [LogEvent] was created ");
			}
			else
			{
				final Layout<ILoggingEvent> layoutLogBack = patternLayourEncoder.getLayout();

				logEvent.createLayoutEvent(layoutLogBack.doLayout(event),
						event.getLevel());
			}
			break;
		default:
			logEvent.createOnlyMsgEvent(event.getMessage(), event.getLevel());
		}
	}

	/** 
	 * Method that calls the function that handles file write mode depending 
	 * on the message you wish
	 * @param logEvent - LogEvent to write to file
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
	 * Creates and configures the LogEvent in writing mode function for mode SYNCHRONOUS
	 * @param event - LogEvent to write to file
	 */
	private void writeSynchronous(final ILoggingEvent event)
	{
		// Create new LogEvent with parameters
		final LogEvent logEvent = LogEvent.createEmptyEvent();
		initLogEventFromLogbackEvent(event, logEvent);

		// Write to File directly
		writeToFile(logEvent);
	}

	/** 
	 * Creates and configures the LogEvent in writing mode function for mode ASYNCHRONOUS
	 * @param event - LogEvent to write to file
	 */
	private void writeAsynchronous(final ILoggingEvent event)
	{
		// Get the sequence and LogEvent
		final long sequence = this.ringBuffer.next();
		final LogEvent logEvent = this.ringBuffer.get(sequence);

		// Create LogEvent depending on the message options -ONLY_Message, ALLCOMPLETE, LOGBACKLAYOUT
		this.initLogEventFromLogbackEvent(event, logEvent);

		// make the LogEvent available to HandleLogEvent
		this.ringBuffer.publish(sequence);
	}


	/** 
	 * Send the event depending on mode of HybridAppender
	 */
	private void sendToWriteEvent(final ILoggingEvent event)
	{
		FileAppenderMode logEventMode= this.parameters.getHybridMode();
		switch (logEventMode)
		{
		case SYNCHRONOUS:
			writeSynchronous(event);

			break;
		case ASYNCHRONOUS:
			writeAsynchronous(event);

			break;
		case MIXED:
			if(event.getLevel() == Level.ERROR || event.getLevel() == Level.WARN)
			{
				writeSynchronous(event);
			}
			else
			{
				writeAsynchronous(event);
			}
			break;
		default:
			break;
		}
	}


	/** 
	 * Send log events to the RingBuffer or Call WriteLogEvent directly
	 * @param event - LogEvent to analyze
	 */
	public void append(final ILoggingEvent event)
	{
		// prevent re-entry.
		if (this.reentryFlag || this.isStop) 
		{
			return;
		}
		try
		{
			// Put the flag a true to start 
			this.reentryFlag = true;

			//Check if we have to discard logEvents.
			if(this.parameters.getHybridMode() != FileAppenderMode.SYNCHRONOUS 
					&& shouldDiscard(event) && this.parameters.isDiscard())
			{
				return;
			}

			//Write the event 
			this.sendToWriteEvent(event);
		}
		finally
		{
			//Put the flag false to avoid re-entry
			this.reentryFlag = false;
		}
	}

	@Override
	public void onError(final String msg, final Throwable cause)
	{
		this.addError(msg, cause);
	}

	/**
	 * Returns queue the ring size to store events (use 2 power values)
	 */
	public int getRingSize()
	{
		return this.parameters.getRingSize();
	}

	/**
	 * Sets the queue ring size
	 *
	 * @param queueSize size of the ring
	 */
	public void setRingSize(final int queueSize)
	{
		parameters.setRingSize(queueSize);
	}

	/**
	 * True if it uses discard mode
	 */
	public boolean isDiscard()
	{
		return this.parameters.isDiscard();
	}

	/**
	 * Set the mode to discard events
	 */
	public void setIsDiscard(final boolean isDiscard)
	{
		parameters.setIsDiscard(isDiscard);
	}

	/**
	 * If discard mode is active, returns the percentage of the ring in which events starts to be discarded
	 */
	public int getDiscardPercent()
	{
		return this.parameters.getDiscardPercent();
	}

	/**
	 * If discard mode is active, sets the percentage of the ring in which events starts to be discarded
	 */
	public void setDiscardPercent(final int discardPercent)
	{
		parameters.setDiscardPercent(discardPercent);
	}

	/**
	 * Return the minimum log level to clean events
	 */
	public String getCleanLogLevel()
	{
		return parameters.getCleanLogLevel().toString();
	}

	/**
	 * Sets the minimum log level to clean events
	 */
	public void setCleanLogLevel(final String cleanLogLevel)
	{
		this.parameters.setCleanLogLevel(cleanLogLevel);
	}

	/**
	 * Return the encoder to display the messages in the files
	 */
	public PatternLayoutEncoder getEncoder()
	{
		return patternLayourEncoder;
	}

	/**
	 * Sets the encoder to display the messages in the files
	 */
	public void setEncoder(final PatternLayoutEncoder encoder)
	{
		this.patternLayourEncoder = encoder;
	}

	/**
	 * Get the name of the log file
	 */
	public String getFileName()
	{
		return parameters.getFileName();
	}

	/**
	 * Sets the name of the log file
	 */
	public void setFileName(final String fileName)
	{
		this.parameters.setFileName(fileName);
	}

	/**
	 * Get the size of the buffer to write into the files
	 */
	public int getBufferSize()
	{
		return parameters.getBufferSize();
	}

	/**
	 * Sets the size of the buffer to write into the files
	 */
	public void setBufferSize(final int  bufferSize)
	{
		this.parameters.setBufferSize(bufferSize);
	}

	/**
	 * Get the mode being used to deal with the log events
	 */
	public String getLogEventMode()
	{
		return parameters.getLogEventMode().toString();
	}

	/**
	 * Sets the mode to use to deal with the log events
	 */
	public void setLogEventMode(final String logEventMode)
	{
		this.parameters.setLogEventMode(logEventMode);
	}

	/**
	 * Return the hybrid mode being used
	 */
	public String getHybridMode()
	{
		return parameters.getHybridMode().toString();
	}

	/**
	 * Set the hybrid mode to use
	 */
	public void setHybridMode(final String hybridMode)
	{
		this.parameters.setHybridMode(hybridMode);
	}
}
