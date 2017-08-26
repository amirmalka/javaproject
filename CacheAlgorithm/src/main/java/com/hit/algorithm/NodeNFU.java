package com.hit.algorithm;

import java.util.LinkedHashSet;

class NodeNFU<T> {
    public int count = 0;
    public LinkedHashSet<T> keys = null;
    public NodeNFU<T> prev = null, next = null;
    
    public NodeNFU(int count) {
        this.count = count;
        keys = new LinkedHashSet<T>();
        prev = next = null;
    }
}
