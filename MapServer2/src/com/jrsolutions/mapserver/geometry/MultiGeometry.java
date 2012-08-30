package com.jrsolutions.mapserver.geometry;

import java.util.Iterator;

public class MultiGeometry extends Geometry{

	private  Geometry[] geoms;
	
	public MultiGeometry(){
		geoms=new Geometry[0];
	}
	public MultiGeometry(Geometry[] geoms){
		this.geoms=geoms;
	}
	public int getNumGeometries() {
		return geoms.length;
	}
	public Geometry[] getGeometryArray() {
		return geoms;
	}

	public void addPoint(Point p){
		Geometry[] old=geoms;
		geoms=new Geometry[geoms.length+1];
		System.arraycopy(old,0,geoms,0,old.length);
		geoms[old.length]=p;
		env=null;
	}
	public void addLineString(LineString p){
		Geometry[] old=geoms;
		geoms=new Geometry[geoms.length+1];
		System.arraycopy(old,0,geoms,0,old.length);
		geoms[old.length]=p;
		env=null;
	}
	public void addPolygon(Polygon p){
		Geometry[] old=geoms;
		geoms=new Geometry[geoms.length+1];
		System.arraycopy(old,0,geoms,0,old.length);
		geoms[old.length]=p;
		env=null;
	}
	public void addGeom(Geometry p){
		Geometry[] old=geoms;
		geoms=new Geometry[geoms.length+1];
		System.arraycopy(old,0,geoms,0,old.length);
		geoms[old.length]=p;
		env=null;
	}
	
//	public Envelope getEnvelope(){
//		Envelope env=new Envelope();
//		for(int i=0;i<geoms.length;i++){
//			env.extend(geoms[i].getEnvelope());
//		}
//		return env;
//	}
	public Rect getEnvelope(){
		if(env==null){
			env=new Rect();
			for(int i=0;i<geoms.length;i++){
				env.extend(geoms[i].getEnvelope());
			}
		}
		return env;
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("MultiGeom: ("+geoms.length+")\n");
		for(int i=0;i<geoms.length;i++){
			sb.append("\t"+i+":"+geoms[i].toString()+"\n");
		}
		return sb.toString();
	}
	public Iterator<Geometry> iterator(){
		return new GeomIterator();
	}
	
	class GeomIterator implements Iterator<Geometry>{

		int currentPos;
		
		public boolean hasNext() {
			return currentPos<geoms.length;
		}

		public Geometry next() {
			return geoms[currentPos++];
		}

		public void remove() {
			throw new IllegalArgumentException();
		}
		
	}
	
	
}
