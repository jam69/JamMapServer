package com.jrsolutions.mapserver.index.btree2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NodeStoreMemory<Node> implements NodeStore<Node> {

	private ArrayList<Node> lista;
	private long nRoot=-1;
	private Set<Long> dirty =new HashSet<Long>();
	private boolean dirtyHead=false;
	
	public NodeStoreMemory(){
		lista=new ArrayList<Node>();	
	}
	
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#getRoot()
	 */
	@Override
	public long getHeader(String s){
		if (s=="root") return nRoot;
		return -1;
	}
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#setRoot(long)
	 */
	@Override
	public void setHeader(String p,long n){
		if(p=="root")nRoot=n;
		dirtyHead=true;
	}
	
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#createNode(Node)
	 */
	@Override
	public long createNode(Node n){
		int s=lista.size();
		lista.add(n);
		setDirty(s,n);
		return s;
	}
	
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#getNode(long)
	 */
	@Override
	public Node getNode(long n){
		return lista.get((int)n);
	}
	
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#setDirty(long)
	 */
	@Override
	public void setDirty(long n,Node node){
		dirty.add(n);
	}
	
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#flush()
	 */
	@Override
	public void flush(){
		dirty.clear();
		dirtyHead=false;
	}
	
	@Override
	public void close(){
		flush();
	}
	
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#dump(java.lang.String)
	 */
	@Override
	public void dump(String s){
		System.out.println("ROOT:"+nRoot);
		for(int i=0;i<lista.size();i++){
			System.out.println(s+">"+i+":"+lista.get(i).toString());
		}
		dumpDirty();
	}
	/* (non-Javadoc)
	 * @see com.jrsolutions.mapserver.index.btree2.NStore#dumpDirty()
	 */
	@Override
	public void dumpDirty(){
		System.out.println("Dirty:");
		for(long i:dirty){
			System.out.print(" "+i);
		}
		System.out.println();
	}
}
