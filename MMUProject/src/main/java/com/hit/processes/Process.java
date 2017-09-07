package com.hit.processes;


import java.util.List;
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
		return this.processId;
	}
	
	public void setId(int id) {
		this.processId = id;
	}
	
	@Override
	public Boolean call() throws Exception {
		Page<byte[]>[] pagesFromMmu;
		
		//Iterate each element in the ProcessCycle list
		try {
			for(ProcessCycle processCycle : processCycles.getProcessCycles()) { 
				pagesFromMmu = mmu.getPages(processCycle.getPages().toArray(new Long[processCycle.getPages().size()]));
				List<Long>processCyclePages = processCycle.getPages();
				for(int i=0;i<processCyclePages.size();i++) {
					pagesFromMmu[i].setContent(processCycle.getData().get(i));
				}
				Thread.sleep(processCycle.getSleepMs());
			}
			return true;
		}
		catch (InterruptedException e){
			e.printStackTrace();
			return false;
		}
	}
}
