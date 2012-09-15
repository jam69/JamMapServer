package com.jrsolutions.mapserver.geometry;

import java.util.Iterator;

public class LineString extends Geometry{

	private Point[] puntos;
	
	public LineString(){
		puntos=new Point[0];
	}
	
	public void addPoint(Point p){
		Point[] old=puntos;
		puntos=new Point[puntos.length+1];
		System.arraycopy(old,0,puntos,0,old.length);
		puntos[old.length]=p;
		env=null;
	}
	
	public LineString(int nd, double[] xyArray) {
		puntos=new Point[xyArray.length/nd];
		Point pto=null;
		for(int i=0;i<xyArray.length;i++){
			if(i%nd==0){
				pto=new Point(2); 
				puntos[i/nd]=pto;
			}
			pto.setComp(i%nd,xyArray[i]);			
		}
		env=null;
	}
	
	public Point[] getPoints(){
		return puntos;
	}
	
	public Point getPoint(int n){
		if( n<0 || n >= puntos.length){
			return null;
		}
		return puntos[n];
	}
	
	public int getNumPoints(){
		return puntos.length;
	}

	public double[] getCoordArray() {
		double[] res=new double[puntos.length*2];
		for(int i=0;i<puntos.length;i++){
			res[i*2]=puntos[i].getX();
			res[i*2+1]=puntos[i].getY();
		}
		return res;
	}

	@Override
	public Rect calcEnvelope() {
		env=new Rect();
		for(int i=0;i<puntos.length;i++){
			env.extend(puntos[i]);
		}
		return env;
	}
	

	public String toString(){
		StringBuffer sb=new StringBuffer("LineString:["+puntos.length+"]->");
		for(int i=0;i<puntos.length;i++){
			sb.append("("+puntos[i].getX()+","+puntos[i].getY()+")");
		}
		return sb.toString();
	}
	public Iterator<Point> iterator(){
		return new LineIterator();
	}
	
	class LineIterator implements Iterator<Point>{

		int currentPos;
		
		public boolean hasNext() {
			return currentPos<puntos.length;
		}

		public Point next() {
			return puntos[currentPos++];
		}

		public void remove() {
			throw new IllegalArgumentException();
		}
		
	}
}
