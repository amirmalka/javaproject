package com.hit.processes;

import java.util.concurrent.Callable;

import com.hit.memoryunits.MemoryManagementUnit;

public class Process implements Callable<Boolean> {
	private MemoryManagementUnit mmu;
	private int ProcessId;
	private ProcessCycles processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles) {
		// TODO Implement
	}
	
	public int getId() {
		// TODO Implement
		return 0;
	}
	
	public void setId(int id) {
		// TODO Implement
	}
	
	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
