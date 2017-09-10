package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class MemoryManagementUnitTest {
	private final int RAM_CAPACITY = 3;
	
	private MemoryManagementUnit prepareHD() {
		// Prepare HD File with contents
		HardDisk hd = HardDisk.getInstance();
		HashMap<Long, Page<byte[]>> hdMap = new HashMap<Long, Page<byte[]>>();
		hdMap.put(Long.valueOf(1), new Page<byte[]>(Long.valueOf(1), "Amir".getBytes()));
		hdMap.put(Long.valueOf(2), new Page<byte[]>(Long.valueOf(2), "Ran".getBytes()));
		hdMap.put(Long.valueOf(3), new Page<byte[]>(Long.valueOf(3), "Keren".getBytes()));
		hdMap.put(Long.valueOf(4), new Page<byte[]>(Long.valueOf(4), "Sapir".getBytes()));
		hdMap.put(Long.valueOf(5), new Page<byte[]>(Long.valueOf(5), "Daniel".getBytes()));
		hdMap.put(Long.valueOf(6), new Page<byte[]>(Long.valueOf(6), "Amos".getBytes()));
		hdMap.put(Long.valueOf(7), new Page<byte[]>(Long.valueOf(7), "Yair".getBytes()));
		
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(hd.DEFAULT_FILE_NAME));
			out.writeObject(hdMap);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IAlgoCache<Long, Long> algoCache = new LRUAlgoCacheImpl<Long, Long>(RAM_CAPACITY);
		MemoryManagementUnit mmu = new MemoryManagementUnit(RAM_CAPACITY, algoCache);
		return mmu;
	}
	
	@Test
	public void throwsNothing() {
		MemoryManagementUnit mmu = prepareHD();
		Page<byte[]>[] pages = null;
		try {
			pages = mmu.getPages(new Long[]{Long.valueOf(1), Long.valueOf(2), Long.valueOf(3)});
		} catch (IOException e) {
			fail("Exception thrown from getPages while it shouldnt: " + e.getMessage());
		}
		
		assertEquals("Amir" , new String(pages[0].getContent()));
		assertEquals("Ran" ,new String(pages[1].getContent()));
		assertEquals("Keren", new String(pages[2].getContent()));
		
		// Update content of page IDs 1,2,3
		pages[0].setContent("ABC".getBytes());
		pages[1].setContent("DEF".getBytes());
		pages[2].setContent("GHI".getBytes());
		
		// Request other 3 pages (all existing are going to be written to HD) 
		try {
			pages = mmu.getPages(new Long[]{Long.valueOf(4), Long.valueOf(5), Long.valueOf(6)});
		} catch (IOException e) {
			fail("Exception thrown from getPages while it shouldnt" + e.getMessage());
		}
		
		assertEquals("Sapir" , new String(pages[0].getContent()));
		assertEquals("Daniel" ,new String(pages[1].getContent()));
		assertEquals("Amos", new String(pages[2].getContent()));

		// Update page ID 5
		pages[1].setContent("Nissim".getBytes());
		
		try {
			pages = mmu.getPages(new Long[]{Long.valueOf(1), Long.valueOf(2), Long.valueOf(3)});
		} catch (IOException e) {
			fail("Exception thrown from getPages while it shouldnt"+ e.getMessage());
		}
		
		// Validate that updated pages are retrieved from HD correctly
		assertEquals("ABC", new String(pages[0].getContent()));
		assertEquals("DEF", new String(pages[1].getContent()));
		assertEquals("GHI", new String(pages[2].getContent()));
		
		// Shutdown MMU, and restart to re-check the values
		mmu.shutdown();
		
		IAlgoCache<Long, Long> algoCache = new LRUAlgoCacheImpl<Long, Long>(RAM_CAPACITY);
		mmu = new MemoryManagementUnit(RAM_CAPACITY, algoCache);
		
		try {
			pages = mmu.getPages(new Long[]{Long.valueOf(1), Long.valueOf(5), Long.valueOf(7)});
		} catch (IOException e) {
			fail("Exception thrown from getPages while it shouldnt"+ e.getMessage());
		}
		assertEquals("ABC", new String(pages[0].getContent()));
		assertEquals("Nissim", new String(pages[1].getContent()));
		assertEquals("Yair", new String(pages[2].getContent()));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	  
	@Test
	public void throwsGetMorePagesThanRamCapacity() throws IOException{
		thrown.expect(IOException.class);
		thrown.expectMessage("Amount of requested pages exceeds RAM capacity");
		MemoryManagementUnit mmu = prepareHD();
		mmu.getPages(new Long[]{Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Long.valueOf(5)});		
	}
	
	@Test
	public void throwsIOExceptionForMissingPageId() throws IOException{
		thrown.expect(IOException.class);
		thrown.expectMessage("out of HD bounds");
		MemoryManagementUnit mmu = prepareHD();
		mmu.getPages(new Long[]{Long.valueOf(1), Long.valueOf(2), Long.valueOf(10)});		
	}
}
