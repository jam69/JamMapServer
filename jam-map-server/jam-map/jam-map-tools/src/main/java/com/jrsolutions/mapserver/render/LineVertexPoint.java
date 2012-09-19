package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;

public class LineVertexPoint extends LineSimbology{

	private final NodeSimbology nodeSimb;
	
	public LineVertexPoint(NodeSimbology nodeSimb){
		this.nodeSimb=nodeSimb;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
		int n=line.getNumPoints();
		// Probably there is a better algorithm
		for(int i=1;i<(n-1);i++){
			Point p0=line.getPoint(i-1);
			Point p1=line.getPoint(i);
			Point p2=line.getPoint(i+1);
			if(p0==null || p1==null || p2==null){
				System.out.println("NULLLLLLLLLL");
			}else{
			double ang0=Math.atan2((p1.getY()-p0.getY()), (p1.getX()-p0.getX()));
			double ang1=Math.atan2((p2.getY()-p1.getY()), (p2.getX()-p1.getX()));
			nodeSimb.paint(mapper, g, p0,(ang0+ang1)/2, ent);
			}
		}	
	}
}

