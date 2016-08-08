package com.bbva.kyof.llf.log.appender.file;

/**
 * Error logger interface to decouple the file appender from the write log events
 */
public interface IErrorLogger
{
	/** Method to call when there is an error, adding the cause */
	void onError(final String msg, final Throwable cause);
}
