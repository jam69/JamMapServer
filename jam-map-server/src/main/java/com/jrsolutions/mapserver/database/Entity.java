package com.jrsolutions.mapserver.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jrsolutions.mapserver.geometry.Geometry;

public class Entity {

	private Geometry geom;
	private Map<String,Object> attrs=new HashMap<String,Object>();
	private ArrayList<String>attrNames=new ArrayList<String>();
	
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
		if(!attrNames.contains(name)) attrNames.add(name);
	}
	public void setAllAttrs(Map<String,Object>map){
		this.attrs=map;
		attrNames=new ArrayList<String>(map.keySet());
	}
	public int getNumAttr(){
		return attrNames.size();
	}
	public String getAttrName(int i){
		return attrNames.get(i);
	}
	public Object getAttrValue(int i){
		return attrs.get(attrNames.get(i));
	}
}
