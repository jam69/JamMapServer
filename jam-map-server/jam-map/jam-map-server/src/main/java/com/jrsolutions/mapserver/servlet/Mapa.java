package com.jrsolutions.mapserver.servlet;

import java.awt.Graphics2D;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.database.MySqlRepos;
import com.jrsolutions.mapserver.database.shp.ShpRepos;
import com.jrsolutions.mapserver.render.GeomLayer;
import com.jrsolutions.mapserver.render.Mapper;
import com.jrsolutions.mapserver.render.Simbology;

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
	
	public void pinta(Mapper mapper,Graphics2D gr){
		int zoom=mapper.getZoomLevel();
		for(GeomLayer layer:layers){
			if(layer.getMaxZoom() >= zoom && layer.getMinZoom() <= zoom){
				layer.render(mapper,gr); // zoom TOFIX
			}
		}
	}

	// Create poiRepos
	
	public ShpRepos createShpRepos(String file,boolean alfa){
		return new ShpRepos(file);
	}
	
	public DataRepos createMySQLRepos(String url,String name){
		return new MySqlRepos(url,name);
	}
	
	public GeomLayer createGeomLayer(DataRepos dataRepos){
		GeomLayer gLayer=new GeomLayer(dataRepos);
		layers.add(gLayer);
		return gLayer;
	}
	
	public Simbology createSymboly(){
		return new Simbology();
	}
	
	}
