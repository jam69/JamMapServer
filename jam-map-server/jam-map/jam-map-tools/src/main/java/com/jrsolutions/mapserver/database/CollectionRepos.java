package com.jrsolutions.mapserver.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jrsolutions.mapserver.database.datadefinition.TableDescriptor;
import com.jrsolutions.mapserver.geometry.Rect;

public class CollectionRepos implements DataRepos {

	private final Rect env=new Rect();
	private final List<Entity> world=new ArrayList<Entity>();
	
	
	
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
		return getIterator(); // TODO
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
        
        public void add(Entity entity){
            world.add(entity);
        }

}
