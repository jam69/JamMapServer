package com.jrsolutions.mapserver.condition;

import com.jrsolutions.mapserver.database.Entity;

public class CondTrue extends Condition {

	@Override
	public boolean check(Entity ent) {
		return true;
	}

	
}
