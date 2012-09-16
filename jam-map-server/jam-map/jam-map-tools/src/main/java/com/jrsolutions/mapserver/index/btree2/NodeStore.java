package com.jrsolutions.mapserver.index.btree2;


public interface NodeStore<Node> {

	public abstract long getHeader(String propName);

	public abstract void setHeader(String propName,long n);

	public abstract long createNode(Node n);

	public abstract Node getNode(long n) ;

	public abstract void setDirty(long pos,Node n) ;

	public abstract void flush();
	
	public abstract void close();

	public abstract void dump(String s);

	public abstract void dumpDirty();

}