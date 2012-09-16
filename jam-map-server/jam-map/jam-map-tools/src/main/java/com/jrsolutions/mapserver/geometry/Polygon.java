package com.jrsolutions.mapserver.geometry;

import java.util.Iterator;

/**
 * One polygon is formed for a Exterior Ring and optionaly, interior Rings.
 * 
 * The exterior Ring is the first ring in the array.
 * 
 * @author jamartinm
 *
 */
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
	
	public void setExterior(LineString p){
		if(rings.length>0){
			rings[0]=p;
		}else{
			addRing(p);
		}
	}
	public LineString getExterior(){
		if(rings.length>0){
			return rings[0];
		}else{
			return null;
		}
	}
	public LineString[] getRingArray() {
		return rings;
	}
	public int getNumRings(){
		return rings.length;
	}
	public LineString getRing(int n){
		if(n<0 || n>=rings.length){
			return null;
		}
		return rings[n];
	}
	
	@Override
	public Rect calcEnvelope(){
		// Only needs calc the exterior Ring for the MBR
//		for(int i=0;i<rings.length;i++){
//			env.extend(rings[i].getEnvelope());
//		}
		if(rings.length>0){
			env= rings[0].getEnvelope();
		}else{
			env=new Rect();
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
