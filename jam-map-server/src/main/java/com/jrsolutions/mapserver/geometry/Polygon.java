package com.jrsolutions.mapserver.geometry;

import java.util.Iterator;

public class Polygon extends Geometry{

	private LineString[] rings;
	
	public Polygon(){
		this.rings= new LineString[0];
	}
	
	public Polygon(LineString[] rings){
		this.rings=rings;
	}
	
	public void addRing(LineString p){
		LineString[] old=rings;
		rings=new LineString[rings.length+1];
		System.arraycopy(old,0,rings,0,old.length);
		rings[old.length]=p;
		env=null;
	}
	
	public LineString[] getRingArray() {
		return rings;
	}
	
//	public Envelope getEnvelope(){
//		Envelope env=new Envelope();
//		for(int i=0;i<rings.length;i++){
//			env.extend(rings[i].getEnvelope());
//		}
//		return env;
//	}
	public Rect getEnvelope(){
		if(env==null){
			env=new Rect();
			for(int i=0;i<rings.length;i++){
				env.extend(rings[i].getEnvelope());
			}
		}
		return env;
	}

	
	public String toString(){
		StringBuffer sb=new StringBuffer("Polygon:["+rings.length+"]\n");
		for(int i=0;i<rings.length;i++){
			sb.append(i+">"+rings[i].toString()+"\n");
		}
		return sb.toString();
	}

	public Iterator<LineString> iterator(){
		return new RingIterator();
	}
	
	class RingIterator implements Iterator<LineString>{

		int currentPos;
		
		public boolean hasNext() {
			return currentPos<rings.length;
		}

		public LineString next() {
			return rings[currentPos++];
		}

		public void remove() {
			throw new IllegalArgumentException();
		}
		
	}
}
