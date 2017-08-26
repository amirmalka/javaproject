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
		memory.put(addPage.getPageId(), addPage);
	}
	
	public void removePage(Page<byte[]> removePage) {
		if (!memory.containsKey(removePage.getPageId()))
			return;
		memory.remove(removePage.getPageId());
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds){
		@SuppressWarnings("unchecked")
		Page<byte[]>[] arr = (Page<byte[]>[]) new Page<?>[pageIds.length];
		for (int i = 0; i < pageIds.length; i++)
			arr[i] = this.getPage(pageIds[i]);
		return arr;
	}
	
	public void addPages(Page<byte[]>[] addPages) {
		for (Page<byte[]> page_i : addPages)
			this.addPage(page_i);
	}
	
	public void removePages(Page<byte[]>[] removePages) {
		for (Page<byte[]> page_i : removePages)
			this.removePage(page_i);
	}
	
	public int getInitialCapacity() {
		return this.capacity;
		// TODO: implement
	}
	
	public void setInitialCapacity(int initialCapacity) {
		// TODO: Implement
	}
	
}
