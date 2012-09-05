package com.jrsolutions.mapserver.geometry;

public class Point extends Geometry {

	private double[] coord;

	public Point(int nd){
		coord=new double[nd];
	}
	
	public Point(double x,double y){
		coord=new double[2];
		coord[0]=x;
		coord[1]=y;
	}
	public Point(double x,double y,double z){
		coord=new double[3];
		coord[0]=x;
		coord[1]=y;
		coord[2]=z;
	}
	
	public double getX() {
		return coord[0];
	}

	public void setX(double x) {
		coord[0] = x;
		env=null;
	}

	public double getY() {
		return coord[1];
	}

	public void setY(double y) {
		coord[1]=y;
		env=null;
	}
		
//	public Envelope getEnvelope() {
//		return new Envelope(coord[0],coord[1],coord[0],coord[1]);
//	}
	public Rect getEnvelope() {
		if(env==null){
			env=new Rect(coord[0],coord[1],coord[0],coord[1]);
		}
		return env;
	}
	
	public String toString(){
		return "Point:("+coord[0]+","+coord[1]+")";
	}
	public void setComp(int i, double d) {
		coord[i]=d;		
	}

	public int getDimensionality() {
		return coord.length;
	}
	
}
