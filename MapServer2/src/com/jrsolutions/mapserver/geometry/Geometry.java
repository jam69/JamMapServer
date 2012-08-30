package com.jrsolutions.mapserver.geometry;

public abstract class Geometry {

	public int getDimensionality() {
		return 2;  // pa que mas
	}

//	public abstract Envelope getEnvelope();

	protected Rect env;
	public Rect getEnvelope(){
		return env;
	}
}
