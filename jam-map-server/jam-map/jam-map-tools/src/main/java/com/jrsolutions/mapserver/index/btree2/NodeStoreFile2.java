package com.jrsolutions.mapserver.index.btree2;
/**
 * This class serve the nodes from a file to the BTree2 class.
 * Use a FixedRecordFile
 * It has a deferred write and a cache of nodes.
 * 
 * The file flushes when the dirty list is bigger than MaxDirty
 * 
 * It needs a better cache algorithm (now is a simple Map)
 * 
 * It needs a concurrent test to be multi-thread safe
 * 
 * 
 * @see FixedRecordFile
 * @see BTree2
 * @see BTree2Header
 * @see BTree2Node
 * 
 */


import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jrsolutions.mapserver.index.FixedRecordFile;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class NodeStoreFile2<Node> implements NodeStore<Node> {

	private FixedRecordFile<BTree2Header,Node> file;
    private BTree2Header header;
    
    private final Map<Long,Node> dirty= new HashMap<Long,Node>();
    //private final Map<Long,Node> cached= new HashMap<Long,Node>();
    private Cache cache;
    private boolean dirtyHeader;
	
    
	private int maxDirty = 50;
	private int maxTime = 30;
    
	public NodeStoreFile2(String path) throws IOException{
		try {
			cache=CacheManager.getInstance().getCache("memory");
		} catch (CacheException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			file=new FixedRecordFile<BTree2Header, Node>(path,256,1024);
			file.open("rw");
			try {
				header=file.readHeader();
				dirtyHeader=false;
			} catch (EOFException e) {
				header=new BTree2Header();
				header.root=-1;
				header.HT=0;
				header.N=0;
				header.nRecords=0;
				file.writeHeader(header);
				dirtyHeader=false;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public long getHeader(String prop) {
		if(prop.equals("root"))    return(header.root);
		if(prop.equals("height"))  return(header.HT);
		if(prop.equals("size"))    return(header.N);
		return -1;
	}

	@Override
	public void setHeader(String prop,long n) {
		if(prop.equals("root"))    header.root=n;
		if(prop.equals("height"))  header.HT=n;
		if(prop.equals("size"))    header.N=n;
		dirtyHeader=true;
	}

	@Override
	public long createNode(Node n) {
		long v=header.nRecords;
		setDirty(v,n);
		header.nRecords++;
		return v;
	}

	@Override
	public Node getNode(long n) {
		try {
			Node node=dirty.get(n);
			if(node!=null){
				return node;
			}else{
				Element element=cache.get(n);
				node=(Node)element.getObjectValue();
				if(node!=null){
					return node;
				}else{
					node=file.readRecord(n);
					cache.put(new Element(n,node));
					return node; 
				}
			}
		} catch (CacheException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;	
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void flush() {
		try {			
			if(dirtyHeader){
				file.writeHeader(header);
			}
			for(long v:dirty.keySet()){
				file.writeRecord(v, dirty.get(v));
			}
			dirty.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close(){
		flush();
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dump(String s) {
		System.out.println("-----------DUMP:"+s+"----------------");
		//BTree2Header h=file.readHeader();
		System.out.println("Header\n"+header);
		for(int i=0;i<header.nRecords;i++){
			//Node n=file.readRecord(i);
			Node n=getNode(i);
			System.out.println("Rec:"+i+":"+n);
		}
		System.out.println("------- End DUMP:"+s+"----------------");
	}
	

	@Override
	public void setDirty(long n, Node node) {
		dirty.put(n,node);
		try {
			cache.remove(n);
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(dirty.size()>maxDirty){
			flush();
		}
	}

	@Override
	public void dumpDirty() {
		// TODO Auto-generated method stub
	}


}
