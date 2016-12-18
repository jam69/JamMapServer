package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.gis.IMapper;

abstract public class LineSimbology {

	abstract public void paint(IMapper m, Graphics2D g,LineString line, Entity ent,boolean closed);
	
}
