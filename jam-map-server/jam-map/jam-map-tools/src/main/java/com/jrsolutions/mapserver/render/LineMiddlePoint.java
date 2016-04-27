package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;

public class LineMiddlePoint extends LineSimbology{

	private final NodeSimbology nodeSimb;
	
	public LineMiddlePoint(NodeSimbology nodeSimb){
		this.nodeSimb=nodeSimb;
	}
	
	@Override
	public void paint(IMapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
		int n=line.getNumPoints();
		for(int i=1;i<n;i++){
			Point p0=line.getPoint(i-1);
			Point p1=line.getPoint(i);
			double ang0=Math.atan2(
					-(p1.getY()-p0.getY()),
					(p1.getX()-p0.getX()));
			Point mPoint=new Point( 
					(p0.getX()+p1.getX())/2.0 ,
					(p0.getY()+p1.getY())/2.0 ) ;
			nodeSimb.paint(mapper, g, mPoint,ang0, ent);
		}	
	}
}

