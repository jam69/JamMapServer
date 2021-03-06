package com.jrsolutions.mapserver.gis;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrsolutions.mapserver.MapUtil;
import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.render.GeomLayer;


public class Mapa {
	
	private static Logger log=LoggerFactory.getLogger(Mapa.class);

	private String name;
	private ArrayList<GeomLayer> layers=new ArrayList<GeomLayer>();
		
	public Mapa(String name){
		log.info("Mapa "+name+" creando...");
		this.name=name;
	}
	
	public void load(File scriptFile) throws FileNotFoundException{
		String name=scriptFile.getName();
		int p=name.indexOf(".");
		if(p>0){
			load(name.substring(p),new FileInputStream(scriptFile));
		}else{
			
		}
	}
		
	public void load(String fileExt,InputStream input){	
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine jsEngine = mgr.getEngineByExtension(fileExt);
		jsEngine.put("mapa", this);
		jsEngine.put("util", new MapUtil());
		try{
			Reader reader=new InputStreamReader(input);
			jsEngine.eval(reader);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}    
		log.info("Mapa "+name+" creado");
	}
	
	public String getName(){
		return name;
	}
	
	public void add(GeomLayer layer){
		layers.add(layer);
	}
	
	public void pinta(IMapper mapper,Graphics2D gr){
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
