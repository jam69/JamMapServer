package com.jrsolutions.mapserver.servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.render.Mapper;
import com.jrsolutions.mapserver.util.cache.MyCache;
import com.jrsolutions.mapserver.util.cache.MyEhCache;
import com.jrsolutions.mapserver.util.cache.MyLocalCache;

public class ImageGen {

	public static final String CACHE_STRATEGY= "CACHE";
	
	
	static MyCache cache;

	
static {
	
	int strategy=0;
	String v=System.getProperty(CACHE_STRATEGY);
	if(v==null){
		strategy=0;
	}else{
		if(v.equalsIgnoreCase("none")) strategy=0;
		else if (v.equalsIgnoreCase("local"))strategy=1;
		else if (v.equalsIgnoreCase("ehcache")) strategy=2;
	}
	
	switch(strategy){
	case 0: cache=null; break;
	case 1: cache=  new MyLocalCache(); break;
	case 2: cache=  new MyEhCache(); break;
	}
}
	
	
	static Mapa mapa=new Mapa("algo");

   public BufferedImage getImage(String key) {

	   BufferedImage img=cache!=null? cache.getImage(key): null;
	    
		if (img == null) {
	    	String p[]=key.split("/");
			int zoom   =Integer.parseInt(p[1]);
			int xtile  =Integer.parseInt(p[2]);
			int ytile  =Integer.parseInt(p[3].substring(0,p[3].lastIndexOf(".png")));
			
			img=getImage(zoom,xtile,ytile);
			
			if(cache!=null)	cache.putImage(key,img);
		}
		return img;
	}

  
	private  BufferedImage getImage(int z, int xtile, int ytile) {

		// int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<z) ) ;
		// int ytile = (int)Math.floor( (1 -
		// Math.log(Math.tan(Math.toRadians(lat)) + 1 /
		// Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<z) ) ;

			
			BufferedImage image = new BufferedImage(256, 256,BufferedImage.TYPE_INT_RGB);
			Graphics2D gr = (Graphics2D) image.getGraphics();
			gr.setColor(Color.black);
			gr.fillRect(0, 0, 256, 256);

			Rect r = tile2boundingBox(xtile, ytile, z);

			
//			System.out.println("IMAGE:"+r);
			Mapper mapper =new Mapper();	
			mapper.setSize(256, 256); 			
			mapper.zoom(r);
			mapper.setZoomLevel(z);

			mapa.pinta(mapper,gr);
			
		return image;
	}

	private  Rect tile2boundingBox(final int x, final int y,
			final int zoom) {
		Rect bb = new Rect();
		bb.setYMax(tile2lat(y, zoom));
		bb.setYMin(tile2lat(y + 1, zoom));
		bb.setXMax(tile2lon(x, zoom));
		bb.setXMin(tile2lon(x + 1, zoom));
		return bb;
	}

	 double tile2lon(int x, int z) {
		return x / Math.pow(2.0, z) * 360.0 - 180;
	}

	double tile2lat(int y, int z) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}

}
