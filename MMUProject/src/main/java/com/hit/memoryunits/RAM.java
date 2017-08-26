package com.hit.memoryunits;

import java.util.HashMap;
import java.util.Map;

public class RAM
{
	private int capacity;
	private Map<Long, Page<byte[]>> memory;
	
	public RAM(int initialCapacity) {
		capacity = initialCapacity;
		memory = new HashMap<Long, Page<byte[]>>(initialCapacity);
	}
	
	public Map<Long,Page<byte[]>> getPages(){
		return new HashMap<Long, Page<byte[]>>(memory);
	}
	
	public void setPages(Map<Long,Page<byte[]>> pages) {
		
	}
	public Page<byte[]> getPage(Long pageId){
		if (!memory.containsKey(pageId))
			return null;
		return memory.get(pageId);
		
	}
	public void addPage(Page<byte[]> addPage) {
		memory.put(addPage., value)
	}
	public void removePage(Page<byte[]> removePage) {
		
	}
	public Page<byte[]>[] getPages(Long[] pageIds){
		return null;
		
	}
	public void addPages(Page<byte[]>[] addPages) {
		
	}
	public void removePages(Page<byte[]>[] removePages) {
		
	}
	
	public int getInitialCapacity() {
		return 0;
	}
	
	public void setInitialCapacity(int initialCapacity) {
		
	}
	
}
