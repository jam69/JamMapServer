package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.servlet.Mapper;

public class LineColor extends LineSimbology{

	private final Color c;
	
	public LineColor(Color c){
		this.c=c;
	}
	
	@Override
	public void paint(Mapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
			g.setColor(c);
			Iterator<Point> it=line.iterator();		
		    Point p=null;
		    Point first=null;
			while(it.hasNext()){
				Point p2=(Point)it.next();
				if(p!=null){
					mapper.drawLine(p.getX(),p.getY(),p2.getX(),p2.getY(), g);	
				}else{
					first=p2;
				}
				p=p2;					
			}
			if(closed && first!=null){
				mapper.drawLine(p.getX(),p.getY(),first.getX(),first.getY(), g);
			}		
		}
	}


