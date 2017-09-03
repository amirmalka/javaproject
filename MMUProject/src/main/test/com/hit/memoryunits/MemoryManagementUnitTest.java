package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class MemoryManagementUnitTest {

	@Test
	public void test() {
		int ramCapacity = 5;
		IAlgoCache<Long, Long> algoCache = new LRUAlgoCacheImpl<Long, Long>(ramCapacity);
		MemoryManagementUnit mmu = new MemoryManagementUnit(ramCapacity, algoCache);
		//System.out.println(Arrays.toString(new byte[]{2,3,77}));
	}
}
