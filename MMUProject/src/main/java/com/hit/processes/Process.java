package com.hit.processes;

import java.util.concurrent.Callable;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;

public class Process implements Callable<Boolean> {
	private MemoryManagementUnit mmu;
	private int processId;
	private ProcessCycles processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles) {
		// TODO Implement
		this.processId = id;
		this.mmu = mmu;
		this.processCycles = processCycles;
	}
	
	public int getId() {
		// TODO Implement
		return this.processId;
	}
	
	public void setId(int id) {
		this.processId = id;
	}
	
	@Override
	public Boolean call() throws Exception {
		Page<byte[]>[] pageFromMmu;
		
		return true;
	}

}
