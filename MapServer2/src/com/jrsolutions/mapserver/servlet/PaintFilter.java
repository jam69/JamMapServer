package com.jrsolutions.mapserver.servlet;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.condition.Condition;
import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.render.Simbology;

public class PaintFilter extends EntityFilter {

	private final Simbology simbology;

	public PaintFilter(Condition cond,Simbology simb){
		super(cond);
		this.simbology=simb;
	}
	public void paint(Mapper m, Graphics2D g,Entity ent){
		    if(checkCond(ent)) paint(m, g, ent);
     }
}
