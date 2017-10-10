package com.hit.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NFUAlgoCacheImpl;
import com.hit.algorithm.Random;
import com.hit.driver.CLI;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model{
	public int numProcesses;
	public int ramCapacity;
	private List<String> commands;
	public IAlgoCache<Long,Long> algo; 	 	
	private MemoryManagementUnit mmu;
	private static final String CONFIG_FILE_NAME = "./src/main/resources/com/hit/config/Configuration.json";
	
	public MMUModel() {
		
	}

	public void setConfiguration(List<String> configuration) {
		// TODO: Implement
	}
	
	@Override
	public void start(String[] command) {
		IAlgoCache<Long, Long> algo = null;
		
		//Initialize capacity and algorithm
		this.ramCapacity = Integer.parseInt(command[1]);
		switch (command[0]) {
			case CLI.LRU:
				algo = new LRUAlgoCacheImpl<Long, Long>(ramCapacity);
				break;
			case CLI.NFU:
				algo = new NFUAlgoCacheImpl<Long, Long>(ramCapacity);
				break;
			case CLI.RANDOM:
				algo = new Random<Long, Long>(ramCapacity);
		}
		
		// Build MMU and initial RAM (content)
		mmu = new MemoryManagementUnit(ramCapacity, algo);
		RunConfiguration runConfig = readConfigurationFile();
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		
		List<Process> processes = createProcesses(processCycles, mmu);
		numProcesses = processes.size();
		runProcesses(processes);
		
		readAndGetCommands(MMULogger.DEFAULT_FILE_NAME);
		setChanged();
		notifyObservers(commands);
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
	
	private boolean readAndGetCommands(String logFile) {
		commands = new ArrayList<String>();
		// Open the file
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(logFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			//Read File Line By Line
			
			while ((strLine = br.readLine()) != null)   {
				commands.add(strLine);
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		//Close the input stream
		return true;
	}
}
