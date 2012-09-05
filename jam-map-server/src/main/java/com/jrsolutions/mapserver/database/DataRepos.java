package com.jrsolutions.mapserver.database;

import java.util.Iterator;

import com.jrsolutions.mapserver.geometry.Rect;

public interface DataRepos {

	public Rect getEnvelope();
	
	public Iterator<Entity> getIterator();
	public Iterator<Entity> getIterator(Rect env);
	
	
	
}
