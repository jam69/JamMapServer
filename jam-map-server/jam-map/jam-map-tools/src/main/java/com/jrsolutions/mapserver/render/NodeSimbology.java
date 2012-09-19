package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;

abstract public class NodeSimbology {

	/**
	 * Draw the Point without angle
	 * 
	 * @param m
	 * @param g
	 * @param p
	 * @param ent
	 */
	abstract public void paint(Mapper m, Graphics2D g,Point p, Entity ent);

	/**
	 * Draw the point with angle
	 * 
	 * @param m
	 * @param g
	 * @param p
	 * @param angle
	 * @param ent
	 */
	abstract public void paint(Mapper m, Graphics2D g,Point p,double angle, Entity ent);
}
