package com.jrsolutions.mapserver.condition;

import com.jrsolutions.mapserver.database.Entity;

public abstract class Condition {

	abstract public boolean check(Entity ent);
}
