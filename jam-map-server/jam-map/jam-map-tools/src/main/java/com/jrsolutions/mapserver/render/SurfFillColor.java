package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Polygon;
import com.jrsolutions.mapserver.gis.IMapper;

public class SurfFillColor extends SurfaceSimbology{

	private Color color;
	
	public SurfFillColor(Color color){
		this.color=color;
	}
	
	@Override
	public void paint(IMapper mapper, Graphics2D g, Polygon s, Entity ent) {
		Iterator<LineString> it=s.iterator();
		while(it.hasNext()){
			paint(mapper,g,it.next(),ent);
		}
	}
	
	public void paint(IMapper mapper,Graphics2D g, LineString line,Entity ent){
		Point[] p=line.getPoints();
		int[]x=new int[p.length];
		int[]y=new int[p.length];
		for(int i=0;i<line.getNumPoints();i++){
                        int[]pp=mapper.pos(p[i].getX(),p[i].getY());
			x[i]=pp[0];
			y[i]=pp[1];								
		}
		g.setColor(color);
		g.fillPolygon(x, y,p.length);		
	}
}
