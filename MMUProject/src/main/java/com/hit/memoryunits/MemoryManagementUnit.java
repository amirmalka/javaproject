package com.hit.memoryunits;
import java.io.IOException;

import com.hit.algorithm.*;

public class MemoryManagementUnit {
	 private RAM ram;
	 private IAlgoCache<Long,Long> cache;
	 private HardDisk hd;
	 
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo) {
		this.ram = new RAM(ramCapacity);
		this.cache = algo;
		this.hd = HardDisk.getInstance();
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds) throws IOException {
		@SuppressWarnings("unchecked")
		Page<byte[]>[] requestedPages = (Page<byte[]>[]) new Page<?>[pageIds.length];
		
		for (int i=0; i<pageIds.length;i++) {
			Long pageId = cache.getElement(pageIds[i]) ;
			if (pageId == null) {
				if (ram.isFull()) {
					//Page Replacement
					Long pageToRemoveFromRamId = cache.putElement(pageIds[i], pageIds[i]);
					Page<byte[]> pageToRemoveFromRam = ram.getPage(pageToRemoveFromRamId);
					Page<byte[]> pageRetrievedFromHd = hd.pageReplacement(pageToRemoveFromRam, pageIds[i]);
					ram.removePage(pageToRemoveFromRam);
					ram.addPage(pageRetrievedFromHd);
				}
				else {
					// Page Fault
					Page <byte[]> pageRetrievedFromHd = hd.pageFault(pageIds[i]);
					cache.putElement(pageIds[i], pageIds[i]);
					ram.addPage(pageRetrievedFromHd);
				}
			}
			requestedPages[i] = ram.getPage(pageId);			
		}
		return requestedPages;
	}
}


