package com.jrsolutions.mapserver.geometry;

public abstract class Geometry {

	private int srid=0;
	
	public int getDimensionality() {
		return 2;  // pa que mas
	}

//	public abstract Envelope getEnvelope();

	protected Rect env;
	
	abstract public Rect calcEnvelope();
	
	public Rect getEnvelope(){
		if(env==null){
			env=calcEnvelope();
		}
		return env;
	}
	public int getSRID(){
		return srid;
	}
	public void setSRID(int srid){
		// check SRID
		this.srid=srid;
	}
}
