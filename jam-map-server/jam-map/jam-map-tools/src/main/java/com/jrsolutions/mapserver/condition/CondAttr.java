package com.jrsolutions.mapserver.condition;

import com.jrsolutions.mapserver.database.Entity;

public class CondAttr extends Condition{

	private final String attrName;
	private final Object value;
	
	public CondAttr(String attrName,Object value){
		this.attrName=attrName;
		this.value=value;
	}
	@Override
	public boolean check(Entity ent) {
		return value.equals(ent.getAttr(attrName));
	}

}
