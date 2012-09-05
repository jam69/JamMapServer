package com.jrsolutions.mapserver.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

}
