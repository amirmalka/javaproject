package com.hit.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


import java.util.logging.Level;


public class MMULogger {
	private static MMULogger instance = null;
	public final static String DEFAULT_FILE_NAME = "logs/log.txt";
	private 	FileHandler handler;
	
	private MMULogger() {
		try {
			handler = new FileHandler(DEFAULT_FILE_NAME, false);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static MMULogger getInstance() {
		if (instance == null)
			instance = new MMULogger();
		return instance;
	}
	
	public synchronized void write(String command, Level level) {
		if (this.handler == null) {
			return;
        }
		
        OnlyMessageFormatter formatter = new OnlyMessageFormatter();
        handler.setFormatter(formatter);
        handler.publish(new LogRecord(level, command+System.getProperty("line.separator")));   
	}
	
	public class OnlyMessageFormatter extends Formatter 
	{
		public OnlyMessageFormatter() {
			super();
		}
		
		@Override
		public String format(final LogRecord record) {
			return record.getMessage();
		}
		
	}
}
