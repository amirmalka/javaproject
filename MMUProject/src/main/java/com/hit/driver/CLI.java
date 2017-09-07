package com.hit.driver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CLI implements Runnable {
	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String LRU = "LRU";
	public static final String NFU = "NFU";
	public static final String RANDOM = "RANDOM";
	private static final String START_MSG = "Please enter required algorithm and RAM capcity";
	private static final String EXIT_MSG = "Thank you";
	private static final String INVALID_MSG = "Not a valid command";
	

	public CLI(InputStream in, OutputStream out) {
		// TODO Implement
		System.out.println(START_MSG);
		Scanner scan = new Scanner(in);
		if (scan.hasNext()) {
			String first = scan.next();
			if (first.equals(START)) {
				
			}
			else if (first.equals(STOP))
			{
				
			}
		}
		scan.close();
	}
	
	public void run() {
		// TODO Implement
	}
	
	public void write(String string) {
		// TODO Implement
	}
}
