package com.jrsolutions.mapserver.geometry;

public class MultiLineString extends MultiGeometry {

	public MultiLineString(){
		super();
	}
	public MultiLineString(LineString[] lines){
		super(lines);
	}
}
