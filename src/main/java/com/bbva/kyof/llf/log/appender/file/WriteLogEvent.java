package com.bbva.kyof.llf.log.appender.file;

import com.bbva.kyof.llf.log.appender.file.model.LogEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Class that receives and writes logEvents to file depending on write mode function
 * that is using
 * 
 * @author sgb
 * 
 */
public class WriteLogEvent
{
	/** Specific type of encoding for Strings*/
	private static final String ENCODING = "UTF-8";
	/**HybridFileAppender*/
	private final transient IErrorLogger errorLogger;
	/** Name of the file to write into*/
	private final transient String fileName;
	/** BufferByte to save the LogEvent */
	private final transient ByteBuffer byteBuffer;
	/** Channel where writing the event logs */
	private transient FileChannel fileChannel;
	/** FileOutputStream using by FileChannel */
	private transient FileOutputStream fileOutPutStream;
	
	/**
	 * Constructor for a WriteLogEvent
	 * 
	 * @param fileName - name of the file to write into
	 * @param bufferSize size of the buffer used to write to file
	 * @param errorLogger - logger to store the errors
	 * @throws FileNotFoundException
	 */
	public WriteLogEvent(final String fileName, final int bufferSize, final IErrorLogger errorLogger)
	{
		this.errorLogger = errorLogger;
		this.fileName = fileName;
		try
		{
			//Initialize Channel
			this.fileOutPutStream = new FileOutputStream(this.fileName);
			this.fileChannel = fileOutPutStream.getChannel();
		}
		catch (FileNotFoundException e) 
		{
			errorLogger.onError("openFile(" + this.fileName
		+ ") call failed.", e);
		}
		//Initialize Buffer Directly -Faster-
		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
	}
	
	/**
	 * Stop and close the channel.
	 * @throws IOException
	 */
	public void stop()
	{
		try 
		{
			this.fileChannel.close();
			this.fileOutPutStream.close();
		} 
		catch (IOException e)
		{
			this.errorLogger.onError("close Channel(" + this.fileName
					+ ") call failed.", e);
		}
	}
	
	/**
	 * Write the LogEvent to file if you are using ONLY_MESSAGE mode
	 * @param logEvent - logEvent to write
	 * @throws IOException
	 */
	public void writeToFileMessageMode(final LogEvent logEvent)  
	{
		try
		{
			//Create the Structure of Message
			String infoLogLevel = "Log Level: ";
			String infoMessage = " Message: ";
			
			final byte [] logLevel = infoLogLevel.getBytes(Charset.forName(ENCODING));
			final byte [] message = infoMessage.getBytes(Charset.forName(ENCODING));
			
			this.byteBuffer.put(logLevel);
			this.byteBuffer.put(logEvent.getLogLevel().toString().getBytes(Charset.forName(ENCODING)));
			this.byteBuffer.put((byte)',');
			
			this.byteBuffer.put(message);
			this.byteBuffer.put(logEvent.getMessage().getBytes(Charset.forName(ENCODING)));
			
			this.byteBuffer.put((byte) '\n');

			//Calculate the space, write to file and clear buffer to next logEvent
			this.byteBuffer.flip();
			this.fileChannel.write(this.byteBuffer);
			this.byteBuffer.clear();
		}
		catch (IOException e) 
		{
			this.errorLogger.onError("Writing to (" + this.fileName
		+ ") failed.", e);
		}
	}
	
	/**
	 * Write the LogEvent to file if you are using ALLCOMPLETE mode
	 * 
	 * @param logEvent - logEvent to write to file
	 * @throws IOException
	 */
	public void writeToFileAllCompleteMode(final LogEvent logEvent)
	{
		try
		{
			this.byteBuffer.put(logEvent.getTimeString().getBytes(Charset.forName(ENCODING)));
			this.byteBuffer.put((byte)',');

			this.byteBuffer.put(logEvent.getThread().getBytes(Charset.forName(ENCODING)));
			this.byteBuffer.put((byte)',');
			
			this.byteBuffer.put(logEvent.getLoggerName().getBytes(Charset.forName(ENCODING)));
			this.byteBuffer.put((byte)',');
			
			this.byteBuffer.put(logEvent.getLogLevel().toString().getBytes(Charset.forName(ENCODING)));
			this.byteBuffer.put((byte)',');
			
			this.byteBuffer.put(logEvent.getMessage().getBytes(Charset.forName(ENCODING)));
			
			this.byteBuffer.put((byte) '\n');

			//Calculate the space, write to file and clear buffer to next logEvent
			this.byteBuffer.flip();
			this.fileChannel.write(this.byteBuffer);
			this.byteBuffer.clear();
		}
		catch (IOException e) 
		{
			this.errorLogger.onError("Writing to (" + this.fileName
		+ ") failed.", e);
		}
	}
	
	/**
	 * Write the LogEvent to file if you are using LOGBACKLAYOUT mode
	 * 
	 * @param logEvent - logEvent to write to file
	 * @throws IOException
	 */
	public void writeToFileLogBackLayoutMode(final LogEvent logEvent)
	{
		try
		{
			this.byteBuffer.put(logEvent.getMessage().getBytes(Charset.forName(ENCODING)));
			
			//Calculate the space, write to file and clear buffer to next logEvent
			this.byteBuffer.flip();
			this.fileChannel.write(this.byteBuffer);
			this.byteBuffer.clear();
		}
		catch (IOException e) 
		{
			this.errorLogger.onError("Writing to (" + this.fileName
		+ ") failed.", e);
		}
	}
}
