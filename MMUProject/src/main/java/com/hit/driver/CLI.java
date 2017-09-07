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
	private static final String WELCOME_MSG = "Welcome to the MMU CLI";
	private static final String START_MSG = "Please enter required algorithm and RAM capcity";
	private static final String NOT_STARTED_MSG = "Maybe try to start first?";
	private static final String EXIT_MSG = "Thank you";
	private static final String INVALID_MSG = "Not a valid command";
	private PrintStream printStream;
    private Scanner scanner;

	public CLI(InputStream in, OutputStream out) {
		this.printStream = new PrintStream(out);
		this.scanner = new Scanner(in);
	}
	
	private boolean isNumeric(String str) {
		return str != null && str.chars().allMatch(Character::isDigit);
	}
	
	private int getPositiveInteger(String str) {
		if (str != null && isNumeric(str)) {
			try {
				return Integer.parseInt(str);
			}
			catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}
	
	public void run() {
		boolean isStarted = false;
		printStream.println(WELCOME_MSG);
		while (true) {
			String[] inputLine = this.scanner.nextLine().split("\\s+");
			
			if (inputLine.length == 0)
				inputLine = new String[] {""};
			
			switch (inputLine[0]) {
				case START:
					if (inputLine.length == 1) {
						isStarted = true;
						printStream.println(START_MSG);
						break;
					}
				case STOP:
					if (inputLine.length == 1) {
						printStream.println(EXIT_MSG);
						return;
					}
				case LRU:
				case NFU:
				case RANDOM:
					if (isStarted && inputLine.length == 2 && getPositiveInteger(inputLine[1]) != 0) {
						MMUDriver.start(inputLine);
						break;
					}
				default: {
					printStream.println(INVALID_MSG);
					if (!isStarted)
						printStream.println(NOT_STARTED_MSG);
					printHelper();
				}
			}
		}
	}
	
	public void printHelper() {
		printStream.print("! Supported commands: " + START + " | " + STOP + " (control) or ");
		printStream.println(LRU + " | " + NFU + " | " + RANDOM + " <integer> (config)");
	}
	
	public void write(String string) {
	}
}
