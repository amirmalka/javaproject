package com.hit.memoryunits;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;

import com.hit.util.MMULogger;

public class HardDisk {
	private final int _SIZE = 1000;
	public final String DEFAULT_FILE_NAME = "HD.txt";
	private static HardDisk instance = null;
	
	@SuppressWarnings("unchecked")
	private HardDisk() {
		HashMap<Long, Page<byte[]>> hd;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(DEFAULT_FILE_NAME));
			hd = (HashMap<Long,Page<byte[]>>)in.readObject();
			in.close();
		}
		catch (IOException | ClassNotFoundException ex1) {
			// File was corrupted or file was not exist - Initialize HD data and save to file
			hd = generateEmptyHdMap(_SIZE);
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DEFAULT_FILE_NAME));
				out.writeObject(hd);
				out.close();
			}
			catch (IOException ex2) {
				MMULogger.getInstance().write(ex2.getMessage(), Level.SEVERE);
			}
		}		
	}
	
	public static HardDisk getInstance()
	{
		if (instance == null)
			instance = new HardDisk();
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Page<byte[]> pageFault(java.lang.Long pageId) throws FileNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(DEFAULT_FILE_NAME));
		HashMap<Long, Page<byte[]>> hd;
		try {
			hd = (HashMap<Long, Page<byte[]>>)in.readObject();
		} catch (ClassNotFoundException e) {
			// Hard disk data is damaged - will initialize HD
			hd = generateEmptyHdMap(_SIZE);
		}
		finally {
			in.close();
		}
		
		return hd.getOrDefault(pageId, null);
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(DEFAULT_FILE_NAME));
		HashMap<Long, Page<byte[]>> hd;
		try {
			hd = (HashMap<Long, Page<byte[]>>)in.readObject();
		} catch (ClassNotFoundException e) {
			// Hard disk data is damaged - will initialize HD
			hd = generateEmptyHdMap(_SIZE);
		}
		finally {
			in.close();
		}
		
		Page<byte[]> pToReturn = hd.getOrDefault(moveToRamId, null);
		hd.put(moveToHdPage.getPageId(), moveToHdPage);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DEFAULT_FILE_NAME));
		out.writeObject(hd);
		out.close();
		return pToReturn;
	}
	
	private static HashMap<Long, Page<byte[]>> generateEmptyHdMap(int size) {
		HashMap<Long, Page<byte[]>> hd = new HashMap<Long, Page<byte[]>>(size);
		for (int i = 0 ; i < size ; i++)
			hd.put(Long.valueOf(i), new Page<byte[]>(Long.valueOf(i),new byte[4]));
		return hd;
	}
}
