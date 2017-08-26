package com.hit.algorithm;
import java.util.HashMap;
import java.util.LinkedHashSet;


public class NFUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K,V> {

	private NodeNFU<K> head = null;
	private HashMap<K, V> valueHash = null;
	private HashMap<K, NodeNFU<K>> nodeHash = null;
	
	public NFUAlgoCacheImpl(int capacity) {
		super(capacity);
	    valueHash = new HashMap<K, V>();
	    nodeHash = new HashMap<K, NodeNFU<K>>();
	}
	
	public V getElement(K key) {
		if (valueHash.containsKey(key)) {
	        increaseCount(key);
	        return valueHash.get(key);
	    }
	    return null;
	}

	private void addToHead(K key) {
	    if (head == null) {
	        head = new NodeNFU<K>(0);
	        head.keys.add(key);
	    } else if (head.count > 0) {
	    	NodeNFU<K> node = new NodeNFU<K>(0);
	        node.keys.add(key);
	        node.next = head;
	        head.prev = node;
	        head = node;
	    } else {
	        head.keys.add(key);
	    }
	    nodeHash.put(key, head);      
	}
	
	private void increaseCount(K key) {
		NodeNFU<K> node = nodeHash.get(key);
	    node.keys.remove(key);
	    
	    if (node.next == null) {
	        node.next = new NodeNFU<K>(node.count+1);
	        node.next.prev = node;
	        node.next.keys.add(key);
	    } else if (node.next.count == node.count+1) {
	        node.next.keys.add(key);
	    } else {
	    	NodeNFU<K> tmp = new NodeNFU<K>(node.count+1);
	        tmp.keys.add(key);
	        tmp.prev = node;
	        tmp.next = node.next;
	        node.next.prev = tmp;
	        node.next = tmp;
	    }
	
	    nodeHash.put(key, node.next);
	    if (node.keys.size() == 0) 
	    	removeNode(node);
	}
	
	private V removeNotFrequentElement() {
	    if (head == null) 
	    	return null;
	    K old = null;
	    for (K n: head.keys) {
	        old = n;
	        break;
	    }
	    V oldValue = valueHash.get(old);
	    head.keys.remove(old);
	    if (head.keys.size() == 0) 
	    	removeNode(head);
	    nodeHash.remove(old);
	    valueHash.remove(old);
	    return oldValue;
	}
	
	private void removeNode(NodeNFU<K> node) {
	    if (node.prev == null) {
	        head = node.next;
	    } else {
	        node.prev.next = node.next;
	    } 
	    if (node.next != null) {
	        node.next.prev = node.prev;
	    }
	}
	
	public V putElement(K key, V value) {
		V valueToReturn = null;
		if (capacity == 0) 
	    	return valueToReturn;
	    if (valueHash.containsKey(key)) {
	        valueHash.put(key, value);
	    } else {
	        if (valueHash.size() < capacity) {
	            valueHash.put(key, value);
	        } else {
	            valueToReturn = removeNotFrequentElement();
	            valueHash.put(key, value);
	        }
	        addToHead(key);
	    }
	    increaseCount(key);
	    return valueToReturn;
	}

	public void removeElement(K key) {
		if (!valueHash.containsKey(key))
			return;
		valueHash.remove(key);
		NodeNFU<K> node = nodeHash.get(key);
		node.keys.remove(key);
		if (node.keys.size() == 0) 
	    	removeNode(head);
	}
	
	public int getCacheSize(){
		return capacity;
	}
	
	public int getCacheCurrentSize(){
		return valueHash.size();
	}
}
