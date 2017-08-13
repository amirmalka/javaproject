package com.hit.algorithm;

import java.util.ArrayList;
import java.util.HashMap;


public class Random<K,V> extends AbstractAlgoCache<K,V> {
	
	private HashMap<K,V> cache;
	int currentSize;
	java.util.Random rand;
	K randomKey;
	
	ArrayList<K> keys;
	
	Random (int capacity){
		super(capacity);
		cache = new HashMap<K,V>(capacity);
		rand = new java.util.Random();
		currentSize = 0;
	}
	
	public V getElement(K key) {
	//Check if the key exists in the HashMap
		if(!cache.containsKey(key))
			return null;
		return cache.get(key);	
	}

	public V putElement(K key, V value) {
		
		
		//Check if the capacity size has been reached - Page Fault Occurs 
		if(currentSize == capacity){
			
			keys = new ArrayList<K>(cache.keySet()); //Create an array of keys depends on the keys presented in the cache
			randomKey = keys.get(rand.nextInt(keys.size())); //get() returns the element at the specified position in this array.
			V victimValue = cache.get(randomKey); //Save the victim value which will be removed from cache.
			cache.remove(randomKey); //Remove the victim key from the cache.
			cache.put(key, value); //Put the new element in the cache
			return victimValue;
		}
		
		else {
			currentSize++;
			cache.put(key, value); //Put the new element in the cache
			return null;
		}
	}

	public void removeElement(K key) {
		if (!cache.containsKey(key)) 
			return;
		cache.remove(key);
		currentSize--;
		
	}
	
	public int getCacheSize() {
		return capacity;
	}
	
	public int getCacheCurrentSize() {
		return currentSize;
	}
	
}