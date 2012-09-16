package com.jrsolutions.mapserver.database;

import java.util.Iterator;

import com.jrsolutions.mapserver.database.datadefinition.TableDescriptor;
import com.jrsolutions.mapserver.geometry.Rect;

public class GeoHashSQLRepos implements DataRepos {

	@Override
	public Rect getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Entity> getIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Entity> getIterator(Rect env) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableDescriptor getDescription(){
		// TODO
		return null;
	}
	
	@Override
	public void create(TableDescriptor description){
    	//TODO
    }
	
	@Override
	public void open(){
		// TODO
	}
	@Override
	public void close(){
		// TODO
	}
	@Override
	public boolean save(Entity entity){
		// TODO
		return false;
	}

}
