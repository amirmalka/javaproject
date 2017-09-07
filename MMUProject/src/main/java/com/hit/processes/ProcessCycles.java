package com.hit.processes;

import java.util.List;

public class ProcessCycles {
	
	private List<ProcessCycle> listOfProcessCycles;
	
	public ProcessCycles(List<ProcessCycle> processCycles) {
		this.listOfProcessCycles = processCycles;
		
	}
	
	public List<ProcessCycle> getProcessCycles() {
		return this.listOfProcessCycles;
	}
	
	public void setProcessCycles(List<ProcessCycle> processCycles) {
		this.listOfProcessCycles = processCycles;
		
	}
	
	public String toString() {
		return "ProcessCycles [processCycles=" + listOfProcessCycles + "]";
	}
}
