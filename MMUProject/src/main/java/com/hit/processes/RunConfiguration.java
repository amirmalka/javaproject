package com.hit.processes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class RunConfiguration {
	private static final String CONFIG_FILE_NAME = null;

	public RunConfiguration(List<ProcessCycles> processesCycles) {
		// TODO Implement
		try {
			new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE_NAME)), ProcessCycles.class);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<ProcessCycles> getProcessesCycles() {
		// TODO Implement
		return null;
	}
	
	public void setProcessesCycles(java.util.List<ProcessCycles> processesCycles) {
		// TODO Implement
	}
	
	public String toString() {
		// TODO Implement
		return "";
	}
}
