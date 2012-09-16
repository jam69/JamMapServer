package com.jrsolutions.mapserver.database.datadefinition;

public class FieldDescriptor {

	public enum Type {
		Decimal,
		Real,
		Varchar,
		Logical,
		Date,
		Geometry
	};
	
	private String name;
	private String description;
	private Type type;
	private int lon;
	private int prec;
	
	
	public FieldDescriptor(String name,Type type){
		this.name=name;
		this.type=type;
	}
	
	public FieldDescriptor(String name,Type type,int lon){
		this.name=name;
		this.type=type;
		this.lon=lon;
	}
	
	public FieldDescriptor(String name,Type type,int lon,int prec){
		this.name=name;
		this.type=type;
		this.lon=lon;
		this.prec=prec;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getLon() {
		return lon;
	}
	public void setLon(int lon) {
		this.lon = lon;
	}
	public int getPrec() {
		return prec;
	}
	public void setPrec(int prec) {
		this.prec = prec;
	}
	
	
}
