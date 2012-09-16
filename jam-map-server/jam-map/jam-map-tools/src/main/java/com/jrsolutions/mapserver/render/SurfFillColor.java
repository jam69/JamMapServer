package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Polygon;

public class SurfFillColor extends SurfaceSimbology{

	private Color color;
	
	public SurfFillColor(Color color){
		this.color=color;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, Polygon s, Entity ent) {
		Iterator<LineString> it=s.iterator();
		while(it.hasNext()){
			paint(mapper,g,it.next(),ent);
		}
	}
	
	public void paint(Mapper mapper,Graphics2D g, LineString line,Entity ent){
		Point[] p=line.getPoints();
		int[]x=new int[p.length];
		int[]y=new int[p.length];
		for(int i=0;i<line.getNumPoints();i++){
			x[i]=mapper.posX(p[i].getX());
			y[i]=mapper.posY(p[i].getY());								
		}
		g.setColor(color);
		g.fillPolygon(x, y,p.length);		
	}
}
