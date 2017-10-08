package com.hit.memoryunits;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

import com.hit.algorithm.*;
import com.hit.util.MMULogger;

public class MemoryManagementUnit {
	 private RAM ram;
	 private IAlgoCache<Long,Long> cache;
	 private HardDisk hd;
	 
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo) {
		this.ram = new RAM(ramCapacity);
		this.cache = algo;
		this.hd = HardDisk.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	public synchronized Page<byte[]>[] getPages(Long[] pageIds) throws IOException {
		if (pageIds.length > ram.getInitialCapacity())
			throw new IOException("Amount of requested pages exceeds RAM capacity!");
		
		Page<byte[]>[] requestedPages = (Page<byte[]>[]) new Page<?>[pageIds.length];
		
		for (int i=0; i<pageIds.length;i++) {
			Long pageId = cache.getElement(pageIds[i]) ;
			if (pageId == null) {
				if (ram.isFull()) {
					//Page Replacement
					Long pageToRemoveFromRamId = cache.putElement(pageIds[i], pageIds[i]);
					Page<byte[]> pageToRemoveFromRam = ram.getPage(pageToRemoveFromRamId);
					Page<byte[]> pageRetrievedFromHd = hd.pageReplacement(pageToRemoveFromRam, pageIds[i]);
					MMULogger.getInstance().write("PR:MTH "+ pageToRemoveFromRamId + " MTR " + pageIds[i], Level.INFO);
					if (pageRetrievedFromHd == null)
						throw new IOException("Requested page id [" + pageIds[i] + "] is out of HD bounds!");
					ram.removePage(pageToRemoveFromRam);
					ram.addPage(pageRetrievedFromHd);
				}
				else {
					// Page Fault
					MMULogger.getInstance().write("PF:" + pageIds[i], Level.INFO);
					Page <byte[]> pageRetrievedFromHd = hd.pageFault(pageIds[i]);
					if (pageRetrievedFromHd == null)
						throw new IOException("Requested page id [" + pageIds[i] + "] is out of HD bounds!");
					cache.putElement(pageIds[i], pageIds[i]);
					ram.addPage(pageRetrievedFromHd);
				}
			}
			requestedPages[i] = ram.getPage(pageIds[i]);	
		}
		return requestedPages;
	}
	
	public void shutdown() {
		int errorCount = 0;
		for (Map.Entry<Long, Page<byte[]>> pageEntry : ram.getPages().entrySet()) {
			try {
				hd.pageReplacement(pageEntry.getValue(), pageEntry.getKey());
			}
			catch (IOException e){
				++errorCount;
			}
		}
		if (errorCount > 0)
			MMULogger.getInstance().write("Shutdown of MMU encounterd " + errorCount + " errors", Level.SEVERE);
	}
}


