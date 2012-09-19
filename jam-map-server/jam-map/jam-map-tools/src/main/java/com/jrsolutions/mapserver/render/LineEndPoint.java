package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;

public class LineEndPoint extends LineSimbology{

	private final NodeSimbology nodeSimb;
	
	public LineEndPoint(NodeSimbology nodeSimb){
		this.nodeSimb=nodeSimb;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
	    int n=line.getNumPoints();
		if(n==0)return;
	    Point p1=line.getPoint(n);
	    if(n>1){
	    	Point p0=line.getPoint(n-1);
	    	double ang=Math.atan2((p1.getY()-p0.getY()), (p1.getX()-p0.getX()));
	    	nodeSimb.paint(mapper, g, p0,ang, ent);
	    }else{
	    	nodeSimb.paint(mapper, g, p1, ent);
	    }
	}
}

