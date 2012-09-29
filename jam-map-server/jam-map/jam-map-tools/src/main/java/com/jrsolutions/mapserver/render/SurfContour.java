package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Polygon;

public class SurfContour extends SurfaceSimbology{

	private final LineSimbology ns;
	
	public SurfContour(LineSimbology ns){
		this.ns=ns;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, Polygon s, Entity ent) {
		ns.paint(mapper,g,s.getExterior(),ent,true);
	}
	
}
