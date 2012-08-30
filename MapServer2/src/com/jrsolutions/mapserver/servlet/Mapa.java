package com.jrsolutions.mapserver.servlet;

import java.awt.Graphics2D;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Mapa {

	private String name;
	private ArrayList<GeomLayer> layers=new ArrayList<GeomLayer>();
		
	public Mapa(String name){
		this.name=name;
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
		jsEngine.put("mapa", this);
		try {
			Reader reader=new InputStreamReader(this.getClass().getResourceAsStream("/startup.js"));
			jsEngine.eval(reader);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}    
	}
	
	public String getName(){
		return name;
	}
	
	public void add(GeomLayer layer){
		layers.add(layer);
	}
	
	public void pinta(Mapper mapper,Graphics2D gr,int zoom){
		for(GeomLayer layer:layers){
			if(layer.getMaxZoom() >= zoom && layer.getMinZoom() <= zoom){
				layer.render(mapper,gr); // zoom TOFIX
			}
		}
	}
	/*
	public ShpLayer createShpLayer(String file,boolean alfa){
		return new ShpLayer(mapper,file,alfa);
	}
	
	
	public POILayer createPOILayer(String file,String x, String y,String attr){
		return new POILayer(mapper,file,x,y,attr);
	}
	
	public ShpRepos createShpRepos(String file,boolean alfa){
		return new ShpRepos(file,alfa);
	}
	
	public GeomStyles createStyles(int a, int b, int c){
		GeomStyles s=new GeomStyles();
		s.surfaceColor=new Color(a);
		s.lineColor=new Color(b);
		s.nodeColor=new Color(c);
		return s;
	}
	
	public GeometryAttrPainter createAttrGeomPainter(){
		return new GeometryAttrPainter();
	
	}
	public PaintAttr createPaintAttr(String fieldName,int offx,int offy,int color,int minZ,int maxZ){
		return new PaintAttr(fieldName,offx,offy,color,minZ,maxZ);
	}
	
	
	*/
}
