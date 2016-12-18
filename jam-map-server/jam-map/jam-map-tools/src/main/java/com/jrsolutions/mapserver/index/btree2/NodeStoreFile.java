package com.jrsolutions.mapserver.index.btree2;
/**
 * This class serve the nodes from a file to the BTree2 class.
 * Use a FixedRecordFile
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

public class NodeStoreFile<Node> implements NodeStore<Node> {

	private FixedRecordFile<BTree2Header,Node> file;
    private BTree2Header header;
    
    private final Map<Long,Node> dirty= new HashMap<Long,Node>();
    
	
		
	public NodeStoreFile(String path) throws IOException{
		try {
			file=new FixedRecordFile<BTree2Header, Node>(path,256,1024);
			file.open("rw");
			try {
				header=file.readHeader();
			} catch (EOFException e) {
				header=new BTree2Header();
				header.root=-1;
				header.HT=0;
				header.N=0;
				header.nRecords=0;
				file.writeHeader(header);
				
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
		try {
			System.out.println("WriteHeader:"+header);
			file.writeHeader(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			return file.readRecord(n);
		} catch (IOException e) {
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
			file.writeHeader(header);
			for(long v:dirty.keySet()){
				file.writeRecord(v, dirty.get(v));
			}
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
		try {
			System.out.println("-----------DUMP:"+s+"----------------");
			BTree2Header h=file.readHeader();
			System.out.println("Header\n"+h);
			for(int i=0;i<header.nRecords;i++){
				Node n=file.readRecord(i);
				System.out.println("Rec:"+i+":"+n);
			}
			System.out.println("------- End DUMP:"+s+"----------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void setDirty(long n, Node node) {
		//dirty.put(n,node);
		try {
			file.writeRecord(n, node);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dumpDirty() {
		// TODO Auto-generated method stub
		
	}


}
