package com.bbva.kyof.llf.log.appender.file;

/** Defines the operation HybridFileAppender Mode */
public enum FileAppenderMode
{
	/** Writes synchronously file */
	SYNCHRONOUS, 
	/** Writes asynchronously files */
	ASYNCHRONOUS, 
	/** Write in file log events asynchronously under INFO and writes 
	 * synchronously log events of type WARN and ERROR */
	MIXED
}
