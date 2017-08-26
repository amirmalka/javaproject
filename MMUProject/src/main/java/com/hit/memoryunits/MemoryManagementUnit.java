package com.hit.memoryunits;
import com.hit.algorithm.*;

public class MemoryManagementUnit {
	 private RAM myRAM;
	 private IAlgoCache<Long,Long> myAlgoCache;
	 
	MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo) {
		this.myRAM = new RAM(ramCapacity);
		this.myAlgoCache = algo ;
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds) throws java.io.IOException{
		
	}
}


