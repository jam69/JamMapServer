package com.jrsolutions.mapserver.database;

import java.util.Map;

import com.jrsolutions.mapserver.geometry.Geometry;

public class Entity {

	private Geometry geom;
	private Map<String,Object> attrs;
	
	public void setGeom(Geometry geom){
		this.geom=geom;
	}
	
	public Geometry getGeom(){
		return geom;
	}
	
	public Object getAttr(String name){
		return attrs.get(name);
	}
	public void setAttr(String name,Object value){
		attrs.put(name,value);
	}
	public void setAllAttrs(Map<String,Object>map){
		this.attrs=map;
	}
}
