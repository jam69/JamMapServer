package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jrsolutions.mapserver.condition.CondAttr;
import com.jrsolutions.mapserver.condition.CondTrue;
import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.geometry.WKBWriter;

public class GeomLayer {

	private DataRepos repos;
	private List<PaintFilter> filtros=new ArrayList<PaintFilter>();
	
	protected int maxZoom;	
	protected int minZoom;

	public GeomLayer(DataRepos repos){
		this.repos=repos;
	}
	
	public void dumpWKB(Rect r,OutputStream out) throws IOException{
		Iterator<Entity> it=repos.getIterator(r);
		while(it.hasNext()){
			Entity ent=it.next();
			WKBWriter writer=new WKBWriter();
			byte[] buff=writer.write(ent.getGeom());
			out.write(buff);
		}
	}
	
	public GeomLayer addFilter(PaintFilter pf){
		filtros.add(pf);
		return this;
	}
	
	public GeomLayer setZoomLimits(int z1,int z2){
		setMinZoom(z1);
		setMaxZoom(z2);
		return this;
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

	public GeomLayer where(String attr_name, String attr_value,Simbology simb){
		addFilter(new PaintFilter( new CondAttr(attr_name,attr_value),simb));
		return this;
	}
	public GeomLayer where(String attr_name, String attr_value,String iconPath,int zmin,String align){
		Simbology simb=new Simbology();
		simb.addNodePrim(new NodeIcon(iconPath, zmin, align));
		addFilter(new PaintFilter( new CondAttr(attr_name,attr_value),simb));
		return this;
	}
	public GeomLayer setDefault(Simbology simb){
		addFilter(new PaintFilter( new CondTrue(),simb));
		return this;
	}
	
	/**  -- metodos de implementacion -- **/
	public void render(IMapper m,Graphics2D g){
		Iterator<Entity> it=repos.getIterator(m.getZoom().increase(.001,.001));
		while(it.hasNext()){
			Entity ent=it.next();
			for(PaintFilter f:filtros){
				f.paint(m,g,ent);
			}
		}
	}
	
}
