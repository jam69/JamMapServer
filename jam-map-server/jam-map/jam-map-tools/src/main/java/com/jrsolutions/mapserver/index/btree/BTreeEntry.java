package com.jrsolutions.mapserver.index.btree;

// internal nodes: only use key and next
// external nodes: only use key and value
public class BTreeEntry<Key,Value> {
    
	public Comparable<Key> key;
    public Value value;
    public BTreeNode<Key,Value> next;     // helper field to iterate over array entries
    
    public BTreeEntry(Comparable<Key> key, Value value, BTreeNode<Key,Value> next) {
        this.key   = key;
        this.value = value;
        this.next  = next;
    }
    
    public Comparable<Key> getKey(){
    	return key;
    }
    
    public Value getValue(){
    	return value;
    }
    
    public BTreeNode<Key,Value> getNext(){
    	return next;
    }
    
    
}
