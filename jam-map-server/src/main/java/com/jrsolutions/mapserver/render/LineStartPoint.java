package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.servlet.Mapper;

public class LineStartPoint extends LineSimbology{

	private final NodeSimbology nodeSimb;
	
	public LineStartPoint(NodeSimbology nodeSimb){
		this.nodeSimb=nodeSimb;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
	    Point[] p=line.getPoints();	
	    if(p.length==0)return;
	    Point p0=p[0];
	    nodeSimb.paint(mapper, g, p0, ent);
	}
}

