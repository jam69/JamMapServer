package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;

abstract public class NodeSimbology {

	abstract public void paint(Mapper m, Graphics2D g,Point p, Entity ent);
}
