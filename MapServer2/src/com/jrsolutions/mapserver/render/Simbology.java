package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.MultiGeometry;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Polygon;
import com.jrsolutions.mapserver.servlet.Mapper;

public class Simbology {

	private final List<NodeSimbology> nodePrims = new ArrayList<NodeSimbology>();
	private final List<LineSimbology> linePrims = new ArrayList<LineSimbology>();
	private final List<SurfaceSimbology> surfacePrims = new ArrayList<SurfaceSimbology>();
	  
	public void paint(Mapper m, Graphics2D g, Entity ent){
		paint(m,g,ent.getGeom(),ent);
	}
	
	public void paint(Mapper m, Graphics2D g,Geometry geom, Entity ent){
			if(geom instanceof MultiGeometry){
				MultiGeometry mg=(MultiGeometry)geom;
				Iterator<Geometry> it=mg.iterator();
				while(it.hasNext()){
					paint(m,g,it.next(),ent);
				}
			}
			if(geom instanceof Point){
				Point p=(Point)geom;
				for(NodeSimbology ns:nodePrims){
					ns.paint(m,g,p,ent);
				}
			}
			if(geom instanceof LineString){
				LineString s=(LineString)geom;
				for(LineSimbology lines:linePrims){
					lines.paint(m,g,s,ent,false);
				}
			}
			if(geom instanceof Polygon){
				Polygon p=(Polygon)geom;
				for(SurfaceSimbology surfs:surfacePrims){
					surfs.paint(m,g,p,ent);
				}
			}		
		}
		
		
	
		
	
	}

