package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Polygon;
import com.jrsolutions.mapserver.servlet.Mapper;

abstract public class SurfaceSimbology {

	abstract public void paint(Mapper m, Graphics2D g,Polygon poly, Entity ent);
}
