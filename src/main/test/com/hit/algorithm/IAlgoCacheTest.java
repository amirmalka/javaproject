package com.hit.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

public class IAlgoCacheTest{
	
	@Test
	public void testLRU() {
		int cacheSize = 4;
		LRUAlgoCacheImpl<Integer,Integer> testCache = new LRUAlgoCacheImpl<Integer,Integer>(cacheSize);
		for(int i=1;i<cacheSize+1;i++) {
			testCache.putElement(i, i*100);
		}
		
		//Check LRU Total Capacity:
		 assertEquals(4, testCache.getCacheSize());
		 
		//Check LRU Current Capacity:
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		 //Remove  element #4 from the cache
		 testCache.removeElement(4);
		 
		//Check LRU Current Capacity:
		 assertEquals(3, testCache.getCacheCurrentSize());
		 
		//Put  element #5 in the cache
		 testCache.putElement(5, 5*100);
		 
		//Check LRU Current Capacity:
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		//Current State in cache: LRU:#1, MRU:#5
		//Put an additional element in the cache- causes a page fault:
		 assertEquals(Integer.valueOf(100),testCache.putElement(6, 6*100));
		 assertEquals(Integer.valueOf(6*100), testCache.getElement(6)); 
		 assertEquals(Integer.valueOf(5*100), testCache.getElement(5));
		 assertEquals(Integer.valueOf(3*100), testCache.getElement(3));
		 assertEquals(Integer.valueOf(2*100), testCache.getElement(2));
		//Put an additional element in the cache- causes a page fault:
		 assertEquals(Integer.valueOf(600),testCache.putElement(7, 7*100));
		 assertNull(testCache.getElement(6));
		 
		 //Remove all element from the cache
		 testCache.removeElement(5);
		 assertEquals(3, testCache.getCacheCurrentSize());
		 testCache.removeElement(3);
		 assertEquals(2, testCache.getCacheCurrentSize());
		 testCache.removeElement(2);
		 assertEquals(1, testCache.getCacheCurrentSize());
		 testCache.removeElement(7);
		 assertEquals(0, testCache.getCacheCurrentSize());
	}
	
	
	@Test 
	public void testRandom() {
		int cacheSize = 4;
		Random<Integer,Integer> testCache = new Random<Integer,Integer>(cacheSize);
		for(int i=1;i<cacheSize+1;i++) {
			testCache.putElement(i, i*100);
		}
		
		//Check LRU Total Capacity:
		 assertEquals(4, testCache.getCacheSize());
		 
		//Check LRU Current Capacity:
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		 //Remove element #4 from the cache
		 testCache.removeElement(4);
		 
		//Check LRU Current Capacity:
		 assertEquals(3, testCache.getCacheCurrentSize());
		 
		//Put element #5 in the cache
		 testCache.putElement(5, 5*100);
		 
		//Check LRU Current Capacity:
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		//Put an additional element in the cache- causes a page fault:
		 System.out.println(testCache.putElement(6, 6*100));
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		 //Remove all elements from the cache
		 for(int i=0;i<7;i++)
			 testCache.removeElement(i);
		 
		 assertEquals(0, testCache.getCacheCurrentSize());
	}
	
	@Test 
	public void testNFU() {
		int cacheSize = 4;
		NFUAlgoCacheImpl<Integer, Integer> testCache = new NFUAlgoCacheImpl<Integer,Integer>(cacheSize);
		assertNull(testCache.putElement(1, 100));
		assertNull(testCache.putElement(2, 200));
		assertNull(testCache.putElement(3, 300));
		assertNull(testCache.putElement(4, 400));
		assertEquals(Integer.valueOf(100), testCache.getElement(1));
		assertEquals(Integer.valueOf(200), testCache.getElement(2));
		assertEquals(Integer.valueOf(200), testCache.getElement(2));
		assertEquals(Integer.valueOf(300),testCache.putElement(5, 500));
		assertEquals(Integer.valueOf(500), testCache.getElement(5));
		assertEquals(Integer.valueOf(500), testCache.getElement(5));
		assertEquals(Integer.valueOf(500), testCache.getElement(5));
		assertEquals(Integer.valueOf(500), testCache.getElement(5));
		assertEquals(Integer.valueOf(500), testCache.getElement(5));
		assertEquals(Integer.valueOf(500), testCache.getElement(5));
		assertEquals(Integer.valueOf(400),testCache.putElement(6, 600));
		assertEquals(Integer.valueOf(600), testCache.getElement(6));
		assertEquals(Integer.valueOf(100),testCache.putElement(7, 700));
		assertEquals(Integer.valueOf(700),testCache.putElement(8, 800));
		assertEquals(4, testCache.getCacheCurrentSize());
		testCache.removeElement(2);
		assertEquals(3, testCache.getCacheCurrentSize());
		testCache.removeElement(6);
		assertEquals(2, testCache.getCacheCurrentSize());
		testCache.removeElement(5);
		assertEquals(1, testCache.getCacheCurrentSize());
		testCache.removeElement(8);
		assertEquals(0, testCache.getCacheCurrentSize());
		assertNull(testCache.putElement(7, 700));
		assertEquals(1, testCache.getCacheCurrentSize());
		assertNull(testCache.putElement(7, 700));
		assertEquals(1, testCache.getCacheCurrentSize());
		testCache.removeElement(7);
		assertNull(testCache.getElement(7));
		assertEquals(0, testCache.getCacheCurrentSize());
		testCache.removeElement(7);
		assertEquals(0, testCache.getCacheCurrentSize());
		assertNull(testCache.putElement(7, 700));
	}
}
