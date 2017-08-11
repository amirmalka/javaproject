package com.hit.algorithm;
import java.util.HashMap;


public class LRUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K,V> {
	
		private int capacity;
		private int currentSize;
		private HashMap<K, Node<K,V> > cache;
		private Node<K,V> leastRecentlyUsed;
		private Node<K,V> mostRecentlyUsed;

	
	LRUAlgoCacheImpl(int capacity)
	{
		super(capacity);
		this.currentSize = 0;
		cache = new HashMap<K, Node<K,V> >();
		leastRecentlyUsed = new Node<K,V>(null, null, null, null);
		mostRecentlyUsed = leastRecentlyUsed;
	}
	
	public V getElement(K key)
	{
		Node<K, V> tempNode = cache.get(key);
		/*If given key is not in the HashMap*/
		 if (tempNode == null)
	            return null;
		 /*If given key is the MRU in the Doubly Linked List*/
		 else if (tempNode.key == mostRecentlyUsed.key)
	            return mostRecentlyUsed.value;
		 
		 /*The key is in the left side of the MRU*/
		 
		 /*Receive the next and previous nodes*/
		 Node<K,V> nextNode = tempNode.next;
		 Node<K,V>prevNode = tempNode.previous;
		 
		 /*If given key is the LRU in the Doubly Linked List*/
		 if (tempNode.key == leastRecentlyUsed.key){
	            nextNode.previous = null;
	            leastRecentlyUsed = nextNode;
	     }
		 
		 /*If given key is in the middle of the Doubly Linked List*/
		 else if (tempNode.key != mostRecentlyUsed.key){
	            prevNode.next = nextNode;
	            nextNode.previous = prevNode;
	        }
		 
		 /*Re-add our item as the MRU */
		 tempNode.previous = mostRecentlyUsed;
		 mostRecentlyUsed.next = tempNode;
		 mostRecentlyUsed = tempNode;
	     mostRecentlyUsed.next = null;
	     
	     /*Return the value*/
	     return tempNode.value;
	}
	
	public  V putElement(K key, V value)
	{
		/*If the key already exists in the cache, no element needs to be replaced */
		 if (cache.containsKey(key)){
	            return value; //Return the current value
	     }
		 
		 /*Else, lets put the new element in the MRU of the doubly linked list*/
		 Node<K,V> newNode = new Node<K,V>(mostRecentlyUsed, null, key, value);
		 mostRecentlyUsed.next = newNode;
		 cache.put(key, newNode);
		 mostRecentlyUsed = newNode;
		 
		 /*Check if the capacity size has been reached*/
		 if(currentSize == capacity) {
			 Node<K,V> saveNode = cache.remove(leastRecentlyUsed.key);
			 leastRecentlyUsed = leastRecentlyUsed.next;
			 leastRecentlyUsed.previous = null;
			 return saveNode.value;
		 }
		 
		 /*If the capacity size hasn't been reached, update the current Size of the cache*/
		 else if (currentSize < capacity) {
			 /*If the cache is empty*/
			 if(currentSize ==0)
				 leastRecentlyUsed = newNode;
			 currentSize++;
		 }
		 
		 return value; //Return the current value
	}
	
	public void removeElement(K key)
	{
		if (cache.containsKey(key))
			cache.remove(key);
	}
}
