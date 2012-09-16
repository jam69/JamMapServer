package com.jrsolutions.mapserver.geometry;

public class MultiPoint extends MultiGeometry{

	public MultiPoint(){
		super();
	}
	public MultiPoint(Point[] ptos){
		super(ptos);
	}
	
	public void addLineString(LineString p){
		throw new IllegalArgumentException();
	}
	
	public void addPolygon(Polygon p){
		throw new IllegalArgumentException();
	}
	
	public void addGeom(Geometry p){
		if(p instanceof Point){
			addPoint((Point)p);
		}else{
			throw new IllegalArgumentException();
		}
	}

}
