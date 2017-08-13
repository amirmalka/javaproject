package com.hit.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

public class IAlgoCacheTest{
	/*
	 * This unit test will check the following regarding each cache algorithm:
	 * Get an existing key - its value needs to be returned
	 * Get an non-existing key - null value needs to be returned
	 * Put an element to a non-full cache - verify cache's values.
	 * Put an element to a full cache - a page fault needs to be performed - check the key which is replaced depends on the Algorithm.
	 * Remove a non-existing element from the cache - nothing happens and the currentSame should stay the same, the keys stay the same in the cache
	 * Remove an existing element from the cache - currentSame needs to be decremented by 1 and the key must be removed from the cache
	 * Check time complexity of each algorithm (necessary?)
	 */
	@Test
	public void testLRU() {
		int casheSize = 4;
		LRUAlgoCacheImpl<Integer,Integer> testCache = new LRUAlgoCacheImpl<Integer,Integer>(casheSize);
		for(int i=1;i<casheSize+1;i++) {
			testCache.putElement(i, i+casheSize);
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
		 testCache.putElement(5, 5+casheSize);
		 
		//Check LRU Current Capacity:
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		//Current State in cache: LRU:#1, MRU:#5
		//Put an additional element in the cache- causes a page fault:
		 testCache.putElement(6, 6+casheSize);
		 //element #1 needs to be paged:
		 assertEquals(null, testCache.getElement(1));
		 assertEquals(null, testCache.getElement(4));
		 assertEquals(Integer.valueOf(6+casheSize), testCache.getElement(6)); 
		 assertEquals(Integer.valueOf(5+casheSize), testCache.getElement(5));
		 assertEquals(Integer.valueOf(3+casheSize), testCache.getElement(3));
		 assertEquals(Integer.valueOf(2+casheSize), testCache.getElement(2));
		//Put an additional element in the cache- causes a page fault:
		 testCache.putElement(7, 7+casheSize);
		 assertEquals(null, testCache.getElement(6));
		 assertEquals(Integer.valueOf(7+casheSize), testCache.getElement(7));
		 
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
		int casheSize = 4;
		Random<Integer,Integer> testCache = new Random<Integer,Integer>(casheSize);
		for(int i=1;i<casheSize+1;i++) {
			testCache.putElement(i, i+casheSize);
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
		 testCache.putElement(5, 5+casheSize);
		 
		//Check LRU Current Capacity:
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		//Put an additional element in the cache- causes a page fault:
		 testCache.putElement(6, 6+casheSize);
		 assertEquals(4, testCache.getCacheCurrentSize());
		 
		 //Remove all elements from the cache
		 if(testCache.getElement(1)!=null)
			 testCache.removeElement(1);
		 if (testCache.getElement(2)!=null)
			 testCache.removeElement(2);
		 if (testCache.getElement(3)!=null)
			 testCache.removeElement(3);
		 if (testCache.getElement(5)!=null)
			 testCache.removeElement(5);
		 testCache.removeElement(6);
		 
		 assertEquals(0, testCache.getCacheCurrentSize());
	}

}
