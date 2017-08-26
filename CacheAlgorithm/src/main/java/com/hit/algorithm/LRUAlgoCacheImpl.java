package com.hit.algorithm;
import java.util.HashMap;

public class LRUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K,V> {
	
		
		private HashMap<K, NodeLRU<K,V> > cache;
		private NodeLRU<K,V> leastRecentlyUsed;
		private NodeLRU<K,V> mostRecentlyUsed;

	
	LRUAlgoCacheImpl(int capacity){
		super(capacity);
		
		cache = new HashMap<K, NodeLRU<K,V> >(capacity);
		leastRecentlyUsed = new NodeLRU<K,V>(null, null, null, null);
		mostRecentlyUsed = leastRecentlyUsed;
	}
	
	public V getElement(K key){
		
		//Check if the key exists in the HashMap
		if(!cache.containsKey(key))
			return null;
		
		NodeLRU<K, V> tempNode = cache.get(key);
		
		 //If given key is the MRU in the Doubly Linked List
		 if (tempNode.key == mostRecentlyUsed.key)
	            return mostRecentlyUsed.value;
		 
		 //The key is in the left side of the MRU
		 
		 //Receive the next and previous nodes
		 NodeLRU<K,V> nextNode = tempNode.next;
		 NodeLRU<K,V>prevNode = tempNode.previous;
		 
		 //If given key is the LRU in the Doubly Linked List
		 if (tempNode.key == leastRecentlyUsed.key){
	            nextNode.previous = null;
	            leastRecentlyUsed = nextNode;
	     }
		 
		 //If given key is in the middle of the Doubly Linked List
		 else {
	            prevNode.next = nextNode;
	            nextNode.previous = prevNode;
	        }
		 
		 //Re-add our item as the MRU 
		 tempNode.previous = mostRecentlyUsed; //Save the current MRU
		 mostRecentlyUsed.next = tempNode;	
		 mostRecentlyUsed = tempNode;
	     mostRecentlyUsed.next = null;
	     
	     //Return the value
	     return tempNode.value;
	}
	
	public  V putElement(K key, V value){
		//If the cache contains only the newNode, then LRU=MRU
		if(cache.size()  == 0) {
			NodeLRU<K,V> newNode = new NodeLRU<K,V>(null, null, key, value);
			leastRecentlyUsed = mostRecentlyUsed = newNode;
			cache.put(key, newNode);
			return null;
		}

		if (cache.containsKey(key)){
			cache.remove(key);
		}
		NodeLRU<K,V> newNode = new NodeLRU<K,V>(mostRecentlyUsed, null, key, value);
		mostRecentlyUsed.next = newNode;
		mostRecentlyUsed = newNode;
		cache.put(key, newNode);
		//Check if the capacity size has been reached - Page Fault Occurs 
		if(cache.size() > capacity) {
			 NodeLRU<K,V> saveNode = cache.remove(leastRecentlyUsed.key);
			 leastRecentlyUsed = leastRecentlyUsed.next;
			 leastRecentlyUsed.previous = null;
			 return saveNode.value;
		 }
		return null; //There is no node which needs to be replaced
	}
	
	public void removeElement(K key){
		if (!cache.containsKey(key)) 
			return;
		NodeLRU<K,V> tempNode = cache.get(key);
		
		if(cache.size() == 1) {
			cache.remove(key);
			mostRecentlyUsed = leastRecentlyUsed = null;
		}
		else if (tempNode == mostRecentlyUsed) {
			tempNode.previous.next = null;
			mostRecentlyUsed  = tempNode.previous;
			cache.remove(key);
		}
		
		else if(tempNode == leastRecentlyUsed) {
			tempNode.next.previous = null;
			leastRecentlyUsed = tempNode.next;
			cache.remove(key);
		}
		
		else {
			tempNode.previous.next = tempNode.next;
			tempNode.next.previous = tempNode.previous;
			cache.remove(key);
		}
	
	}
	
	public int getCacheSize(){
		return capacity;
	}
	
	public int getCacheCurrentSize(){
		return cache.size();
	}
}

