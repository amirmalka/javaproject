package com.hit.view;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;

public class MMUCommand {
	public static final String TYPE_PAGE_FAULT = "PF";
	public static final String TYPE_PAGE_REPLACEMENT = "PR";
	public static final String TYPE_GET_PAGES = "GP";
	public static final String TYPE_PROC_NUM = "PN";
	public static final String TYPE_RAM_CAPACITY = "RC";
	private String type;
	private int processId;
	private Long pageId;
	private List<String> data;
	private Long mth;
	private Long mtr;

	private MMUCommand()
	{
		data = new ArrayList<String>();
	}

	public Long getPageId() {
		return pageId;
	}
	
	public String getType() {
		return type;
	}

	public int getProcessId() {
		return processId;
	}

	public List<String> getData() {
		return data;
	}

	public Long getMth() {
		return mth;
	}

	public Long getMtr() {
		return mtr;
	}

	public static MMUCommand parseCommand(String line) {
		MMUCommand cmd = new MMUCommand();
		cmd.type = line.substring(0, 2);
		
		switch (cmd.type) {		
			case TYPE_PAGE_FAULT:
				if (parsePageFault(line, cmd))
					return cmd;
			break;
			case TYPE_GET_PAGES:
				if (parseGetPages(line, cmd))
					return cmd;
			break;
			case TYPE_PAGE_REPLACEMENT:
				if (parsePageReplacement(line, cmd)) 
					return cmd;
			break;
			case TYPE_PROC_NUM:
			case TYPE_RAM_CAPACITY:
				return cmd;
			default:
				return null;
		}
		return null;
	}
	
	private static boolean parsePageReplacement(String line, MMUCommand cmd) {
		String pattern = "PR:MTH\\s+(\\d+)\\s+MTR\\s+(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			try {
				cmd.mth = Long.parseLong(m.group(1));
				cmd.mtr = Long.parseLong(m.group(2));
			}
			catch (Exception ex) {
				return false;
			}
			return true;
		}
		return false;
	}

	private static boolean parseGetPages(String line, MMUCommand cmd) {
		String pattern = "GP:P(\\d+)\\s+(\\d+)\\s+(\\[.*\\])";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find()) {
			try {
				cmd.processId = Integer.parseInt(m.group(1));
				cmd.pageId = Long.parseLong(m.group(2));
				String[] test = (new Gson()).fromJson(m.group(3),  String[].class);
				cmd.data = Arrays.asList(test);
			}
			catch (Exception ex) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	private static boolean parsePageFault(String line, MMUCommand cmd) {
		String pattern = "PF:(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		
		if (m.find()) {
			try {
				cmd.pageId = Long.parseLong(m.group(1));
			}
			catch (NumberFormatException ex) {
				return false;
			}
			return true;
		}
		return false;
	}
}
