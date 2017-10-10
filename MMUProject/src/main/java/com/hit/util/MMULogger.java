package com.hit.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Level;


public class MMULogger {
	private static MMULogger instance = null;
	public final static String DEFAULT_FILE_NAME = "logs/log.txt";
	private FileHandler handler;
	
	private MMULogger() {
		try {
			File file = new File(DEFAULT_FILE_NAME);
			if(file.exists())
				renameCurrentLog();
			handler = new FileHandler(DEFAULT_FILE_NAME, false);
		} catch (SecurityException | IOException e) {
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
	
	
	public void rotateLog() {
		try {
			handler.close();
			renameCurrentLog();
			handler = new FileHandler(DEFAULT_FILE_NAME, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renameCurrentLog() {
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		File newFile = new File("logs/"+ dateFormat.format(date) + ".txt") ;
		File oldFile = new File(DEFAULT_FILE_NAME);
		oldFile.renameTo(newFile);
	}
}
