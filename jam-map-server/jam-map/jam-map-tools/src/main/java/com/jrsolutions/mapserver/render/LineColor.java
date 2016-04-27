package com.jrsolutions.mapserver.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Iterator;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;

public class LineColor extends LineSimbology{

	private final Color c;
	private final float thick;
	private final Stroke stroke;
	
	public LineColor(int c){
		this(c,0);
	}
	
	public LineColor(Color c){
		this(c,0);
	}

	public LineColor(int c,float thick){
		this.c=new Color(c);
		this.thick=thick;
		stroke=new BasicStroke(thick);
	}
	
	public LineColor(Color c,float thick){
		this.c=c;
		this.thick=thick;
		stroke=new BasicStroke(thick);
	}
	
	@Override
	public void paint(IMapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
		g.setColor(c);
		g.setStroke(stroke);
		g.draw(mapper.mapLine(line));
	}

	public void paint2(IMapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
			g.setColor(c);
			g.setStroke(stroke);
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


