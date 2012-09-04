package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.servlet.Mapper;

public class NodeText extends NodeSimbology {

	private Color color=Color.white;
	private int  size=5;
	private int offx=0;
	private int offy=0;
	private int justif=0;
	private int postion;
	private Font font;
	private String attrName;
	

	public NodeText(int offx, int offy,int color,String attrName){
		this.offx=offx;
		this.offy=offy;
		this.color=new Color(color);
		this.attrName=attrName;
	}

	@Override
	public void paint(Mapper m, Graphics2D g,Point p, Entity ent) {
		
		Object obj=ent.getAttr(attrName);
		if(obj!=null){
			int px= m.posX(p.getX())+offx;
			int py= m.posY(p.getY())+offy;
			
			g.setColor(color);
			g.drawString(obj.toString(), px, py);

			}
	}
}