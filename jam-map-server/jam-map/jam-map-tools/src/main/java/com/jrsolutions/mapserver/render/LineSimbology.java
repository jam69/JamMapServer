package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;

abstract public class LineSimbology {

	abstract public void paint(Mapper m, Graphics2D g,LineString line, Entity ent,boolean closed);
}
