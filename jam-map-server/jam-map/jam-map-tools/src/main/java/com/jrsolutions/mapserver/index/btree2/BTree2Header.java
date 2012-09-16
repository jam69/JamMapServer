package com.jrsolutions.mapserver.index.btree2;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BTree2Header 
	//implements Serializable{
	implements Externalizable{
		private static final long serialVersionUID = 1688051067248118094L;
		long root;
		long HT;
		long N;
		long nRecords;
		
		public String toString(){
			StringBuffer sb=new StringBuffer();
			sb.append("Root:"+root+"\n");
			sb.append("HT:"+HT+"\n");
			sb.append("N:"+N+"\n");
			sb.append("nRecords:"+nRecords+"\n");
			return sb.toString();
		}
		
//		private void writeObject(ObjectOutputStream out) throws IOException{
//			out.writeLong(root);
//			out.writeLong(HT);
//			out.writeLong(N);
//			out.writeLong(nRecords);
//		}
//		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//			root=in.readLong();
//			HT=in.readLong();
//			N=in.readLong();
//			nRecords=in.readLong();
//		}
//		
		public void writeExternal(ObjectOutput out) throws IOException{
			out.writeLong(root);
			out.writeLong(HT);
			out.writeLong(N);
			out.writeLong(nRecords);
		}
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
			root=in.readLong();
			HT=in.readLong();
			N=in.readLong();
			nRecords=in.readLong();			
		}
}	
