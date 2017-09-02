package com.hit.memoryunits;

import java.io.FileNotFoundException;
import java.io.IOException;

public class HardDisk {
	private final int _SIZE;
	public final int DEFAULT_FILE_NAME;
	
	private HardDisk() {
		_SIZE = 1;
	}
	
	public static HardDisk getInstance()
	{
		return null;
	}

	public Page<byte[]> pageFault(java.lang.Long pageId) throws FileNotFoundException, IOException {
		return null;
		
	}
	
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, IOException {
		return moveToHdPage;
		
	}
}
