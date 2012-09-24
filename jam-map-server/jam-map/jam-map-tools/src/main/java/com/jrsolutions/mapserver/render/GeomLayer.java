package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jrsolutions.mapserver.condition.CondAttr;
import com.jrsolutions.mapserver.condition.CondTrue;
import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.database.Entity;

public class GeomLayer {

	private DataRepos repos;
	private List<PaintFilter> filtros=new ArrayList<PaintFilter>();
	
	protected int maxZoom;	
	protected int minZoom;

	public GeomLayer(DataRepos repos){
		this.repos=repos;
	}
	
	public void addFilter(PaintFilter pf){
		filtros.add(pf);
	}
	
	public void setZoomLimits(int z1,int z2){
		setMinZoom(z1);
		setMaxZoom(z2);
	}
	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}

	public int getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(int minZoom) {
		this.minZoom = minZoom;
	}

	public void where(String attr_name, String attr_value,Simbology simb){
		addFilter(new PaintFilter( new CondAttr(attr_name,attr_value),simb));
    }
	public void setDefault(Simbology simb){
		addFilter(new PaintFilter( new CondTrue(),simb));
    }
	
	public void render(Mapper m,Graphics2D g){
		Iterator<Entity> it=repos.getIterator(m.getZoom().increase(.001,.001));
		while(it.hasNext()){
			Entity ent=it.next();
			for(PaintFilter f:filtros){
				f.paint(m,g,ent);
			}
		}
	}
	
}
