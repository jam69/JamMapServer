package com.jrsolutions.mapserver.render;

import com.jrsolutions.mapserver.gis.IMapper;
import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;

public class LineRepPoint extends LineSimbology{

	private final NodeSimbology nodeSimb;
	private final double offset;
	private final double dist;

	public LineRepPoint(double offset, double distance,NodeSimbology nodeSimb){
		this.nodeSimb=nodeSimb;
		this.offset=offset;
		this.dist=distance;
	}

	@Override
	public void paint(IMapper mapper, Graphics2D g, LineString linea, Entity ent,boolean closed) {	 
		if(linea.getNumPoints()==0)return;
		Point ptoAnt = linea.getPoint(0);
		double ox=ptoAnt.getX();
		double oy=ptoAnt.getY();
		double x,y;
		double lon,d,dx,dy,vx,vy;
		Point ptoRep;
		double angulo;
		d= offset;
		for (int i=1;i<linea.getNumPoints();i++){
			Point pto = linea.getPoint(i);
			x=pto.getX();
			y=pto.getY();
			lon=Math.sqrt(Math.pow(x-ox,2.0) + Math.pow(oy-y,2.0));
			dx= dist*(x-ox)/lon;
			dy= dist*(y-oy)/lon;
			vx= ox+d*(x-ox)/lon;
			vy= oy+d*(y-oy)/lon;
			angulo=Math.atan2((y-oy), (x-ox));
			while (d < lon){
				ptoRep=new Point(vx,vy);
				nodeSimb.paint(mapper, g, ptoRep,angulo, ent);
				vx+= dx;
				vy+=dy;
				d+=dist;
			}
			d= d - lon;
			ox= x;
			oy= y;
		}
	}


}

