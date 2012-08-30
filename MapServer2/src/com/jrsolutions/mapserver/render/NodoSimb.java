package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.servlet.Mapper;

public class NodoSimb extends NodeSimbology {

	private Color color;
	private int  size;
	private int offx=0;
	private int offy=0;
	private int justif=0;
	private int id;
	
	public final static int CIRCULO = 0;
	public final static int RECT = 1;
	public final static int CRUZ = 2;
	public final static int EQUIS = 3;
	
	@Override
	public void paint(Mapper m, Graphics2D g,Point p, Entity ent) {
		
		int px= m.posX(p.getX());
		int py= m.posY(p.getY());
		g.setColor(color);
			int  s2=size/2;
			switch(id){
			case CIRCULO:g.drawArc(px-s2,py-s2,size,size,0,360); break;
			case RECT   :g.drawRect(px-s2,py-s2,size,size); break;
			case CRUZ   :g.drawLine(px-s2,py,px+s2,py);g.drawLine(px,py-s2,px,py+s2); break;
			case EQUIS  :g.drawLine(px-s2,py-s2,px+s2,py+s2);g.drawLine(px-s2,py+s2,px+s2,py-s2); break;
			}

		}

	}
