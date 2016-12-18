package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.gis.IMapper;

public class NodoSimb extends NodeSimbology {

	private Color color=Color.white;
	private int  size=5;
	private int offx=0;
	private int offy=0;
	private int justif=0;
	private int id;
	
	public final static int CIRCULO = 0;
	public final static int RECT = 1;
	public final static int CRUZ = 2;
	public final static int EQUIS = 3;

	public NodoSimb(int id){
		this.id=id;
	}
	public NodoSimb(int id,int color){
		this.id=id;
		this.color=new Color(color);
	}
	
	@Override
	public void paint(IMapper m, Graphics2D g,Point p,double angle, Entity ent) {
		// TODO
		paint(m,g,p,ent);
	}
	
	@Override
	public void paint(IMapper m, Graphics2D g,Point p, Entity ent) {
		
                int pp[]=m.pos(p.getX(),p.getY());
		int px= pp[0];
		int py= pp[1];
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
