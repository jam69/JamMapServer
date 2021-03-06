package com.jrsolutions.mapserver.render;

import com.jrsolutions.mapserver.condition.Condition;
import com.jrsolutions.mapserver.database.Entity;

public abstract class EntityFilter {

    protected final Condition cond;

    public EntityFilter(Condition cond) {
        this.cond = cond;
    }

    boolean checkCond(Entity ent) {
        if (cond == null) {
            return true;
        }
        return cond.check(ent);
    }

}
