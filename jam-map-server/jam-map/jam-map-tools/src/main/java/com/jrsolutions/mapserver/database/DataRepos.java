package com.jrsolutions.mapserver.database;

import java.util.Iterator;

import com.jrsolutions.mapserver.database.datadefinition.TableDescriptor;
import com.jrsolutions.mapserver.geometry.Rect;

public interface DataRepos {

	public Rect getEnvelope();
	
	public Iterator<Entity> getIterator();
	public Iterator<Entity> getIterator(Rect env);
	
	public TableDescriptor getDescription();
	
	public void create(TableDescriptor description);
	
	public void open();
	public void close();
	public boolean save(Entity entity);
	
	
}
