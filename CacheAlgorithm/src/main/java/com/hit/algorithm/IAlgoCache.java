package com.hit.algorithm;

public interface IAlgoCache<K,V> {
	
	/*getElement Returns the value to which the specified key is mapped, or null if this cache contains no mapping for the key.*/
	public V getElement(K key);
	
	/*putElement Associates the specified value with the specified key in this cache according to the current algorithm*/
	public V putElement(K key, V value);
	
	/*removeElement Removes the mapping for the specified key from this map if present.*/
	public void removeElement(K key);

}
