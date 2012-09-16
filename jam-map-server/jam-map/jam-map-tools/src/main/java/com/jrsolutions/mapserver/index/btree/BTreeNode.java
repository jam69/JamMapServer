package com.jrsolutions.mapserver.index.btree;


// helper B-tree node data type
public class BTreeNode<Key,Value> {
    public int m;                             // number of children
    public BTreeEntry<Key,Value>[] children;  // the array of children
    
    public BTreeNode(int k,int M){
    	children = new BTreeEntry[M];        // create a node with k children
    	m=k;
    }
    
    public int getM(){
    	return m;
    }
    
    public BTreeEntry<Key,Value>[] getChildren(){
    	return children;
    }
    
    
}
