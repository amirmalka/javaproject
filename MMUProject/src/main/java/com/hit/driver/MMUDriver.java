package com.hit.driver;

import java.util.List;

import com.hit.algorithm.IAlgoCache;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;

public class MMUDriver {
	public MMUDriver() {
		// TODO Implement
	}
	
	public static void main(java.lang.String[] args) {
		CLI cli = new CLI(System.in, System.out); 
		new Thread(cli).start();
	}
	
	public static void start(java.lang.String[] command) {
		IAlgoCache<Long, Long> algo = null; int capacity = 0;
		/**     
		 *  Initialize capacity and algorithm
		 */
		
		/**
		 * Build MMU and initial RAM (content)
		 */
		MemoryManagementUnit mmu = new MemoryManagementUnit(capacity, algo);
		RunConfiguration runConfig = readConfigurationFile();
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		List<Process> processes = createProcesses(processCycles, mmu);
		runProcesses(processes);
	}
	
	public static void runProcesses(java.util.List<Process> applications) {
		// TODO Implement
	}
	
	public static List<Process> createProcesses(
			List<ProcessCycles> applicationsScenarios,
			MemoryManagementUnit mmu) {
		// TODO Implement
		return null;
	}
	
	public static RunConfiguration readConfigurationFile() {
		// TODO Implement
		return null;
	}
}
