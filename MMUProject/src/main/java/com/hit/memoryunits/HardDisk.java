package com.hit.memoryunits;

import java.io.FileNotFoundException;
import java.io.IOException;

public class HardDisk {
	
	private HardDisk() {
		
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
