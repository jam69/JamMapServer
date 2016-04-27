package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.condition.Condition;
import com.jrsolutions.mapserver.database.Entity;

public class PaintFilter extends EntityFilter {

	private final Simbology simbology;

	public PaintFilter(Condition cond,Simbology simb){
		super(cond);
		this.simbology=simb;
	}
	public boolean paint(IMapper m, Graphics2D g,Entity ent){
		    boolean b=checkCond(ent);
		    if(b)simbology.paint(m, g, ent);
		    return b;
     }
}
