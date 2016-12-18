package com.jrsolutions.mapserver.index;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.logging.Logger;



public class FixedRecordFile<Header,Record> {

	private static final Logger log = Logger.getLogger("FixedRecordFile");

	 
	public enum Mode { ReadOnly, ReadWrite } ;
	
	private final File file;
	private RandomAccessFile rf;
	
	    
	private final int headerSize;
	private final int recordSize;
	
	
	
	public FixedRecordFile(String path,int hs, int rs){
		//log.setLevel(Level.WARNING);
		file=new File(path);
		headerSize=hs;
		recordSize=rs;
	}
	
	public void open(String mode) throws IOException{
		log.info("Open:"+mode);
		rf =new RandomAccessFile(file, mode);
	//	rf.setLength(0); // lo truncamos
	}
	public void close() throws IOException{
		log.info("Close");
		rf.close();
	}
	public void flush() throws IOException{
		log.info("Flush");
		//fc.force(true);
		// nothing to flush()
	}
	public Header readHeader() throws IOException,ClassNotFoundException{
		log.info("ReadHeader");
		rf.seek(0);
		byte[] header=new byte[headerSize];
		rf.readFully(header);
		return (Header)getObject(header);
	}
	public Record readRecord(long n) throws IOException,ClassNotFoundException{
		log.info("ReadRecord:"+n);
		rf.seek(headerSize+n*recordSize);
		byte[] record=new byte[recordSize];
		try {
			rf.readFully(record);
		} catch (EOFException e) {
			System.out.println("EOF leyendo registro "+0);
			e.printStackTrace();
		}
		return (Record)getObject(record);
	
	}
	public void writeHeader(Header header)throws IOException{
		log.info("WriteHeader");
		System.out.println("WH:");
		rf.seek(0);
		rf.write(getBytes(header));
	}
	public void writeRecord(long n, Record record)throws IOException{
		log.info("WriteRecord:"+n);
		long newLength=headerSize+(n+1)*recordSize;
		if(newLength>rf.length()){
			rf.setLength(newLength);
			log.info("LongFile:"+rf.length());
		}
		rf.seek(headerSize+ n * recordSize);
		rf.write(getBytes(record));
		
	}
	
	public static byte[] getBytes(Object obj) throws IOException{
	      ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	      ObjectOutputStream oos = new ObjectOutputStream(bos); 
	      oos.writeObject(obj);
	      oos.flush(); 
	      oos.close(); 
	      bos.close();
	      byte [] data = bos.toByteArray();
	      log.info("Bytes:"+obj.getClass().getName()+": "+data.length);
	      return data;
	  }

	public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException{
		  ByteArrayInputStream bis = new ByteArrayInputStream(bytes); 
	      ObjectInputStream ois = new ObjectInputStream(bis); 
	      Object obj=ois.readObject();
	      ois.close();
	      return obj;
	  }

}
