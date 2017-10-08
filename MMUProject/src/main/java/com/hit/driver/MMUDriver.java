package com.hit.driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NFUAlgoCacheImpl;
import com.hit.algorithm.Random;
import com.hit.controller.MMUController;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.model.MMUModel;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;
import com.hit.view.MMUView;

public class MMUDriver {
	private static final String CONFIG_FILE_NAME = "./src/main/resources/com/hit/config/Configuration.json";
	
	public static void main(String[] args) {
		CLI cli = new CLI(System.in, System.out);
		MMUModel model = new MMUModel();
		MMUView view = new MMUView();
		MMUController controller = new MMUController(model, view);
		model.addObserver(controller);
		cli.addObserver(controller);
		view.addObserver(controller);
		new Thread(cli).start();	
	}
	
	public static void start(String[] command) {
		IAlgoCache<Long, Long> algo = null;
		
		//Initialize capacity and algorithm
		int capacity = Integer.parseInt(command[1]);
		switch (command[0]) {
			case CLI.LRU:
				algo = new LRUAlgoCacheImpl<Long, Long>(capacity);
				break;
			case CLI.NFU:
				algo = new NFUAlgoCacheImpl<Long, Long>(capacity);
				break;
			case CLI.RANDOM:
				algo = new Random<Long, Long>(capacity);
		}
		
		// Build MMU and initial RAM (content)
		MemoryManagementUnit mmu = new MemoryManagementUnit(capacity, algo);
		RunConfiguration runConfig = readConfigurationFile();
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		List<Process> processes = createProcesses(processCycles, mmu);
		runProcesses(processes);
		mmu.shutdown();
	}
	
	public static void runProcesses(List<Process> applications) {
		ExecutorService executor = Executors.newCachedThreadPool();
		
        List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();

		for (Process app : applications) {
        		Future<Boolean> future = executor.submit(app);
        		futures.add(future);
        }
		for(Future<Boolean> future : futures){
            try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}   
        }
        executor.shutdown();
	}
	
	public static List<Process> createProcesses(
			List<ProcessCycles> applicationsScenarios,
			MemoryManagementUnit mmu) {
		List<Process> processList = new ArrayList<Process>();
		for (int i = 0; i < applicationsScenarios.size(); i++) {
			processList.add(new Process(i, mmu, applicationsScenarios.get(i)));
        }
        MMULogger.getInstance().write("PN:"+processList.size(), Level.INFO);

		return processList;
	}
	
	public static RunConfiguration readConfigurationFile() {
		try {
			RunConfiguration runConfiguration = new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE_NAME)), RunConfiguration.class);
			return runConfiguration;
			} 
		catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return null;
	}
}
