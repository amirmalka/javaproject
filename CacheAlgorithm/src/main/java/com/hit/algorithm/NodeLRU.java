package com.hit.algorithm;

public class NodeLRU<T, U> {
	NodeLRU<T, U> previous;
	NodeLRU<T, U> next;
	T key;
	U value;
	
	public NodeLRU(NodeLRU<T, U> previous, NodeLRU<T, U> next, T key, U value){
        this.previous = previous;
        this.next = next;
        this.key = key;
        this.value = value;
    }
}


