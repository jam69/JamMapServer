package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Polygon;

public class SurfCentroidPoint extends SurfaceSimbology{

	private final NodeSimbology ns;
	
	public SurfCentroidPoint(NodeSimbology ns){
		this.ns=ns;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, Polygon s, Entity ent) {
		paint(mapper,g,s.getExterior(),ent);
	}
	
	public void paint(Mapper mapper,Graphics2D g, LineString line,Entity ent){
//		Point p=line.getCentroid();
//		System.out.println(">m>"+p);
//		System.out.println("<o<"+line.getPoint(0));
		ns.paint(mapper,g,line.getCentroid(),ent);
	}
}