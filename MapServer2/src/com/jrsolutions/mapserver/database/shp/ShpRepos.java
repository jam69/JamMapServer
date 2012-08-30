package com.jrsolutions.mapserver.database.shp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.database.shp.javadbf.DBFReader;
import com.jrsolutions.mapserver.database.shp.javadbf.JDBFException;
import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.Rect;

public class ShpRepos implements DataRepos {

	private String path;
	private boolean loadAttr;
	private Rect env;
	protected final ArrayList<Entity> world=new ArrayList<Entity>();	

	
	@Override
	public Rect getEnvelope() {
		return env;
	}

	@Override
	public Iterator<Entity> getIterator() {
		return world.iterator();
	}

	@Override
	public Iterator<Entity> getIterator(Rect env) {
		// TODO Auto-generated method stub
		return world.iterator();
	}

	public ShpRepos(String path2,boolean loadAttr){
		System.out.println("Loading SHP:"+path2);
		path=path2;
		this.loadAttr=loadAttr;
		try {
			ShapefileReader shf=new ShapefileReader(path);
			
			Iterator<Geometry> it=shf.iterator();
			while(it.hasNext()){
				Geometry g=(Geometry)it.next();
				Entity ent=new Entity();
				ent.setGeom(g);
				world.add(ent);	
				env.extend(g.getEnvelope());
			}
			shf.closeShapefile();
			System.out.println("Coords:"+env+" NRec:"+ shf.numRecords());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(loadAttr){
		try {
			String path3=path.substring(0,path.length()-4)+".dbf";
			System.out.println("Loading DBF:"+path3);
			DBFReader dbf=new DBFReader(path3);
			for(int i=0;i<dbf.getFieldCount();i++){
				System.out.println("   Campo:["+dbf.getField(i).getType()+"]:"+dbf.getField(i).getName());
			}
			int cont=0;
			while(dbf.hasNextRecord()){
				Object[] rec =(Object[])dbf.nextRecord();
				Map<String,Object> ent=new HashMap<String,Object>(dbf.getFieldCount());
				for(int i=0;i<dbf.getFieldCount();i++){
					ent.put(dbf.getField(i).getName(),rec[i]);
				}
				world.get(cont++).setAllAttrs(ent);
			}
			dbf.close();
		} catch (JDBFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	}
	
}
