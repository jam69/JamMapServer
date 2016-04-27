package com.jrsolutions.mapserver.index.btree2;

import java.io.IOException;

/*************************************************************************
 *  Compilation:  javac BTree.java
 *  Execution:    java BTree
 *
 *  B-tree.
 *
 * Copyright © 2002–2010, Robert Sedgewick and Kevin Wayne. 
 * 
 *  Limitations
 *  -----------
 *   -  Assumes M is even and M >= 4
 *   -  should b be an array of children or list (it would help with
 *      casting to make it a list)
 *   -  No implemnts the remove Key operation   
 *
 *************************************************************************/


public class BTree2<Key extends Comparable<Key>, Value>  {
    private static final int M = 4;    // max children per B-tree node = M-1

   // private BTree2Node<Key,Value> root;             // root of the B-tree
    private long nRoot;
    private int HT;                // height of the B-tree
    private long N;                 // number of key-value pairs in the B-tree

    
    private final NodeStore<BTree2Node<Key, Value>> store;

    // constructor
    public BTree2(NodeStore<BTree2Node<Key, Value>> store) {
    	this.store=store;
    	nRoot=store.getHeader("root");
    	if(nRoot<0){
    		BTree2Node<Key,Value> root = new BTree2Node<Key,Value>(0,M);
    		nRoot=store.createNode(root);
    		store.setHeader("root",nRoot);
    	}else{
    		//root=store.getNode(nRoot);
    		HT=(int)store.getHeader("height");
    		N=store.getHeader("size");
    	}
    }
 
    // return number of key-value pairs in the B-tree
    public long size() { 
    	return N;
    }

    public void setSize(long n){
    	N=n;
    	store.setHeader("size",N);
    }
    // return height of B-tree
    public int height() { 
    	return HT;
    }

    
    public void setHeight(int h){
    	HT=h;
    	store.setHeader("height",HT);
    }

    // search for given key, return associated value; return null if no such key
    public Value get(Key key) {
    	return search(nRoot, key, HT); 
    }
    
    private Value search(long nx, Key key, int ht) {
    	BTree2Node<Key,Value> x=store.getNode(nx);
        BTree2Entry<Key,Value>[] children = x.getChildren();

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.getM(); j++) {
                if (eq(key, children[j].getKey())) return (Value) children[j].getValue();
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.getM(); j++) {
                if (j+1 == x.getM() || less(key, children[j+1].getKey()))
                    return search(children[j].getNext(), key, ht-1);
            }
        }
        return null;
    }


    // insert key-value pair
    // add code to check for duplicate keys
    public void put(Key key, Value value) {
    	System.out.println("put("+key+","+value+")");
        long u = insert(nRoot, key, value, HT); 
        N++;
        if (u <0) return;

        // need to split root
        BTree2Node<Key,Value> root=store.getNode(nRoot);
        BTree2Node<Key,Value> t = new BTree2Node<Key,Value>(2,M);
        long nt=store.createNode(t);
        BTree2Entry<Key,Value> a1=new BTree2Entry<Key,Value>(
        		root.getChildren()[0].getKey(), 
        		(Value)null, 
        		nRoot);
        t.getChildren()[0] = a1;
        BTree2Node<Key,Value> uNode=store.getNode(u);
        BTree2Entry<Key,Value> a2= new BTree2Entry<Key,Value>(
        		uNode.getChildren()[0].getKey(), 
        		null,
        		u);
        t.getChildren()[1] = a2;
        
        store.setDirty(nRoot,root);
        store.setDirty(nt,t);
   //     root = t;
        nRoot= nt;
        store.setHeader("root",nt);
        setHeight(HT+1);
    }


    private long insert(long nh, Key key, Value value, int ht) {
        int j;
        BTree2Node<Key,Value> h=store.getNode(nh);
        BTree2Entry<Key,Value> t = new BTree2Entry<Key,Value>(key, value, -1);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.getChildren()[j].getKey())) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    long u = insert(h.children[j++].next, key, value, ht-1);
                    if (u  < 0) return -1;
                    BTree2Node<Key,Value> uNode=store.getNode(u);
                    t.key = uNode.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--) h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) {
        	store.setDirty(nh,h);
        	return -1;
        }else{
        	long s=split(h);
        	store.setDirty(nh,h);
        	return s;        
        }
        
        
    }

    public void flush(){
    	store.flush();
    }
    public void close(){
    	store.close();
    }
    public void dumpDirty(){
    	store.dumpDirty();
    }
    // split node in half
    private long split(BTree2Node<Key,Value> h) {
        System.out.println("--split("+h);
    	BTree2Node<Key,Value> t = new BTree2Node<Key,Value>(M/2,M);
        long nt=store.createNode(t);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j]; 
        store.setDirty(nt, t);
        return nt;    
    }

    // for debugging
    public void dump(String s){
    	System.out.println("====================");
    	store.dump(s);
    	System.out.println("----------------------");
    }
    public String toString() {
    	return toString(nRoot, HT, "") + "\n";
    }
    
    private String toString(long nh, int ht, String indent) {
        String s = "";
        BTree2Node<Key,Value> h=store.getNode(nh);
        BTree2Entry<Key,Value>[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s += indent + children[j].key + " " + children[j].value + "\n";
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s += indent + "(" + children[j].key + ")\n";
                s += toString(children[j].next, ht-1, indent + "     ");
            }
        }
        return s;
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }


   /*************************************************************************
    *  test client
    *************************************************************************/
    public static void main(String[] args) {
//        BTree2<String, String> st = new BTree2<String, String>(
//        		new NodeStoreMemory<BTree2Node<String,String>>());
      BTree2<String, String> st;
      
	try {
		st = new BTree2<String, String>(
			new NodeStoreFile2<BTree2Node<String,String>>("test.ndx"));
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}

//      st.put("www.cs.princeton.edu", "128.112.136.12");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu",    "128.112.128.15");
        st.put("www.yale.edu",         "130.132.143.21");
        st.put("www.simpsons.com",     "209.052.165.60");
        st.put("www.apple.com",        "17.112.152.32");
        st.put("www.amazon.com",       "207.171.182.16");
        st.put("www.ebay.com",         "66.135.192.87");
        st.put("www.cnn.com",          "64.236.16.20");
        st.put("www.google.com",       "216.239.41.99");
        st.put("www.nytimes.com",      "199.239.136.200");
        st.put("www.microsoft.com",    "207.126.99.140");
        st.put("www.dell.com",         "143.166.224.230");
        st.put("www.slashdot.org",     "66.35.250.151");
        st.put("www.espn.com",         "199.181.135.201");
        st.put("www.weather.com",      "63.111.66.11");
        st.put("www.yahoo.com",        "216.109.118.65");

        System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
        System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
        System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
        System.out.println("apple.com:         " + st.get("www.apple.com"));
        System.out.println("ebay.com:          " + st.get("www.ebay.com"));
        System.out.println("dell.com:          " + st.get("www.dell.com"));
        System.out.println();

        System.out.println("size:    " + st.size());
        System.out.println("height:  " + st.height());
        System.out.println(st);
        st.dump("FIN");
        
        System.out.println();
        st.close();
    }

}

