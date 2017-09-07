package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class MemoryManagementUnitTest {

	@Test
	public void test() {
		int ramCapacity = 5;
		
		// Prepare HD File with contents
		HardDisk hd = HardDisk.getInstance();
		HashMap<Long, Page<byte[]>> hdMap = new HashMap<Long, Page<byte[]>>();
		hdMap.put(Long.valueOf(1), new Page<byte[]>(Long.valueOf(1), "Amir".getBytes()));
		hdMap.put(Long.valueOf(2), new Page<byte[]>(Long.valueOf(2), "Ran".getBytes()));
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(hd.DEFAULT_FILE_NAME));
			out.writeObject(hdMap);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IAlgoCache<Long, Long> algoCache = new LRUAlgoCacheImpl<Long, Long>(ramCapacity);
		MemoryManagementUnit mmu = new MemoryManagementUnit(ramCapacity, algoCache);
		
		try {
			Page<byte[]>[] pages = mmu.getPages(new Long[]{Long.valueOf(1), Long.valueOf(2)});
			for (int i=0; i < pages.length; i++) {
				System.out.print(Arrays.toString(pages[i].getContent()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(Arrays.toString(new byte[]{2,3,77}));
		HardDisk hd = HardDisk.getInstance();
		Long p_id1 = new Long(45);
		Long p_id2 = new Long(46);
		Page<byte[]> p1 = new Page<byte[]>(p_id1, new byte[] {1,2,3,4});
		Page<byte[]> gotPage = hd.pageReplacement(p1, p_id2);
	}
	
}
