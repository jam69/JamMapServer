package com.jrsolutions.mapserver.servlet;

import com.jrsolutions.mapserver.condition.Condition;
import com.jrsolutions.mapserver.database.Entity;

public abstract class EntityFilter {

	 protected final Condition cond;
     
	 public EntityFilter(Condition cond){
		 this.cond=cond;
	 }
	 
     boolean checkCond(Entity ent){
		 return cond.check(ent);
	 }

}
