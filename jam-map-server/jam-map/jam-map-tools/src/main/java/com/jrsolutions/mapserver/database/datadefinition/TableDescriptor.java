package com.jrsolutions.mapserver.database.datadefinition;

import java.util.ArrayList;
import java.util.List;

public class TableDescriptor {

	private String name;
	private String description;
	
	private String databaseURL;
	
	private final List<FieldDescriptor> fields=new ArrayList<FieldDescriptor>();

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

	public String getDatabaseURL() {
		return databaseURL;
	}

	public void setDatabaseURL(String databaseURL) {
		this.databaseURL = databaseURL;
	}

	public List<FieldDescriptor> getFields() {
		return fields;
	}
	
	public void addField(FieldDescriptor fd){
		fields.add(fd);
	}
	
}
