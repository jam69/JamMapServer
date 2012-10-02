package com.jrsolutions.mapserver.servlet;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.jrsolutions.mapserver.MapUtil;
import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.render.GeomLayer;
import com.jrsolutions.mapserver.render.Mapper;

public class Mapa {

	private String name;
	private ArrayList<GeomLayer> layers=new ArrayList<GeomLayer>();
		
	public Mapa(String name){
		this.name=name;
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
		jsEngine.put("mapa", this);
		jsEngine.put("xxxx", new MapUtil());
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
	
	public void pinta(Mapper mapper,Graphics2D gr){
		int zoom=mapper.getZoomLevel();
		for(GeomLayer layer:layers){
			if(layer.getMaxZoom() >= zoom && layer.getMinZoom() <= zoom){
				layer.render(mapper,gr); // zoom TOFIX
			}
		}
	}

	public void dumpWKB(Rect r,OutputStream out) throws IOException{
		int zoom=16; //mapper.getZoomLevel();
		for(GeomLayer layer:layers){
			if(layer.getMaxZoom() >= zoom && layer.getMinZoom() <= zoom){
				layer.dumpWKB(r,out); // zoom TOFIX
			}
		}
	}
	
	// Create poiRepos
	
	public GeomLayer createGeomLayer(DataRepos dataRepos){
		GeomLayer gLayer=new GeomLayer(dataRepos);
		layers.add(gLayer);
		return gLayer;
	}
	
	}
