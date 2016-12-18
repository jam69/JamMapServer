package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Polygon;
import com.jrsolutions.mapserver.gis.IMapper;

public class SurfCentroidPoint extends SurfaceSimbology{

	private final NodeSimbology ns;
	
	public SurfCentroidPoint(NodeSimbology ns){
		this.ns=ns;
	}
	
	@Override
	public void paint(IMapper mapper, Graphics2D g, Polygon s, Entity ent) {
		paint(mapper,g,s.getExterior(),ent);
	}
	
	public void paint(IMapper mapper,Graphics2D g, LineString line,Entity ent){
//		Point p=line.getCentroid();
//		System.out.println(">m>"+p);
//		System.out.println("<o<"+line.getPoint(0));
		ns.paint(mapper,g,line.getCentroid(),ent);
	}
}
