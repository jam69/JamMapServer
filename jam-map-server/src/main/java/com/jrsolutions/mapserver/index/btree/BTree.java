package com.jrsolutions.mapserver.index.btree;

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
 *
 *************************************************************************/


public class BTree<Key extends Comparable<Key>, Value>  {
    private static final int M = 4;    // max children per B-tree node = M-1

    private BTreeNode<Key,Value> root;             // root of the B-tree
    private int HT;                // height of the B-tree
    private int N;                 // number of key-value pairs in the B-tree

 

    // constructor
    public BTree() { root = new BTreeNode<Key,Value>(0,M); }
 
    // return number of key-value pairs in the B-tree
    public int size() { return N; }

    // return height of B-tree
    public int height() { return HT; }


    // search for given key, return associated value; return null if no such key
    public Value get(Key key) { return search(root, key, HT); }
    
    private Value search(BTreeNode<Key,Value> x, Key key, int ht) {
        BTreeEntry<Key,Value>[] children = x.getChildren();

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
        BTreeNode<Key,Value> u = insert(root, key, value, HT); 
        N++;
        if (u == null) return;

        // need to split root
        BTreeNode<Key,Value> t = new BTreeNode<Key,Value>(2,M);
        t.getChildren()[0] = new BTreeEntry<Key,Value>(root.getChildren()[0].getKey(), null, root);
        t.getChildren()[1] = new BTreeEntry<Key,Value>(u.getChildren()[0].getKey(), null, u);
        root = t;
        HT++;
    }


    private BTreeNode<Key,Value> insert(BTreeNode<Key,Value> h, Key key, Value value, int ht) {
        int j;
        BTreeEntry<Key,Value> t = new BTreeEntry<Key,Value>(key, value, null);

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
                    BTreeNode<Key,Value> u = insert(h.children[j++].next, key, value, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--) h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else         return split(h);
    }

    // split node in half
    private BTreeNode<Key,Value> split(BTreeNode<Key,Value> h) {
        BTreeNode<Key,Value> t = new BTreeNode<Key,Value>(M/2,M);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j]; 
        return t;    
    }

    // for debugging
    public String toString() {
        return toString(root, HT, "") + "\n";
    }
    private String toString(BTreeNode<Key,Value> h, int ht, String indent) {
        String s = "";
        BTreeEntry<Key,Value>[] children = h.children;

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
        BTree<String, String> st = new BTree<String, String>();

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
        System.out.println();
    }

}

