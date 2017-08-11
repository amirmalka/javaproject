package com.hit.algorithm;
import java.util.HashMap;

public class LRUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K,V> {
	
		private int currentSize;
		private HashMap<K, Node<K,V> > cache;
		private Node<K,V> leastRecentlyUsed;
		private Node<K,V> mostRecentlyUsed;

	
	LRUAlgoCacheImpl(int capacity)
	{
		super(capacity);
		this.currentSize = 0;
		cache = new HashMap<K, Node<K,V> >(capacity);
		leastRecentlyUsed = new Node<K,V>(null, null, null, null);
		mostRecentlyUsed = leastRecentlyUsed;
	}
	
	public V getElement(K key)
	{
		//Check if the key exists in the HashMap
		if(!cache.containsKey(key))
			return null;
		
		Node<K, V> tempNode = cache.get(key);
		
		 //If given key is the MRU in the Doubly Linked List
		 if (tempNode.key == mostRecentlyUsed.key)
	            return mostRecentlyUsed.value;
		 
		 //The key is in the left side of the MRU
		 
		 //Receive the next and previous nodes
		 Node<K,V> nextNode = tempNode.next;
		 Node<K,V>prevNode = tempNode.previous;
		 
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
	
	public  V putElement(K key, V value)
	{
		//If the key already exists in the cache, no element needs to be replaced 
		 if (cache.containsKey(key)){
	            return null; 
	     }
		 
		 //Else, lets put the new element as the MRU
		 Node<K,V> newNode = new Node<K,V>(mostRecentlyUsed, null, key, value);
		 mostRecentlyUsed.next = newNode;
		 cache.put(key, newNode);
		 mostRecentlyUsed = newNode;
		 
		 //Check if the previous capacity size has been reached
		 if(currentSize == capacity) {
			 Node<K,V> saveNode = cache.remove(leastRecentlyUsed.key);
			 leastRecentlyUsed = leastRecentlyUsed.next;
			 leastRecentlyUsed.previous = null;
			 return saveNode.value;
		 }
		 
		 //If the capacity size hasn't been reached, update the current Size of the cache
		 else if (currentSize < capacity) {
			 //If the cache contains only the newNode, then LRU=MRU
			 if(currentSize ==0)
				 leastRecentlyUsed = newNode;
			 currentSize++;
		 }
		 
		 return value; //Return the current value
	}
	
	public void removeElement(K key)
	{
		if (cache.containsKey(key)) {
			Node<K,V> tempNode = cache.get(key);
			if (tempNode == mostRecentlyUsed) {
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
	}
}
