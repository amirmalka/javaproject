package com.hit.processes;

import java.util.List;


public class RunConfiguration {
	
	private List<ProcessCycles> listOfProcessCycles;
	
	public RunConfiguration(List<ProcessCycles> processesCycles) {
		this.listOfProcessCycles = processesCycles;
	}
	
	public List<ProcessCycles> getProcessesCycles() {
		return this.listOfProcessCycles;
	}
	
	public void setProcessesCycles(List<ProcessCycles> processesCycles) {
		this.listOfProcessCycles = processesCycles;
	}
	
	public String toString() {
		return "RunConfiguration [processesCycles=" + listOfProcessCycles + "]";
	}
}
