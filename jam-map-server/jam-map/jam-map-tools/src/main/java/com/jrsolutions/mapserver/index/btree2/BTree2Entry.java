package com.jrsolutions.mapserver.index.btree2;

import java.io.Serializable;

// internal nodes: only use key and next
// external nodes: only use key and value
public class BTree2Entry<Key,Value> 
	implements Serializable {
		
	
	private static final long serialVersionUID = 6059068606564862854L;
	public Comparable<Key> key;
    public Value value;
    public long next;     // helper field to iterate over array entries
    
    public BTree2Entry(Comparable<Key> key, Value value, long next) {
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
    
    public long getNext(){
    	return next;
    }
    
//	public void writeExternal(ObjectOutput out) throws IOException{
//		out.writeObject(key);
//		out.writeObject(value);
//		out.writeObject(next);
//	}
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
//		key=(Comparable<Key>) in.readObject();
//		value=(Value)in.readObject();
//		next=in.readLong();
//	}

    
    
}
