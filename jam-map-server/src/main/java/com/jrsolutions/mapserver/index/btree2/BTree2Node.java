package com.jrsolutions.mapserver.index.btree2;

import java.io.Serializable;


// helper B-tree node data type
public class BTree2Node<Key,Value> 
	implements Serializable {
		
	
    private static final long serialVersionUID = -7908152656362472982L;
	
    public int m;                             // number of children
    public BTree2Entry<Key,Value>[] children;  // the array of children
    
    public BTree2Node(int k,int M){
    	children = new BTree2Entry[M];        // create a node with k children
    	m=k;
    }
    
    public int getM(){
    	return m;
    }
    
    public BTree2Entry<Key,Value>[] getChildren(){
    	return children;
    }
    
    public String toString(){
    	String s="";
    	for(int i=0;i<m;i++){
    		s += children[i].key+"["+children[i].next+"]("+children[i].value+")   ";
    	}
    	return s;
    }
    public String toStringNonLeaf(){
    	String s="";
    	for(int i=0;i<m;i++){
    		s += children[i].key+"["+children[i].next+"]   ";
    	}
    	return s;
    }
    public String toStringLeaf(){
    	String s="";
    	for(int i=0;i<m;i++){
    		s += children[i].key+"("+children[i].value+")   ";
    	}
    	return s;
    }
    
//	public void writeExternal(ObjectOutput out) throws IOException{
//		out.writeInt(m);                             // number of children
//	    for(int i=0;i<m;i++){
//	    	out.writeObject(children[i]);
//	    }
//	}
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
//		m=in.readInt();
//		for(int i=0;i<m;i++){
//			children[i]=(BTree2Entry)in.readObject();
//		}
//	}
		
}
