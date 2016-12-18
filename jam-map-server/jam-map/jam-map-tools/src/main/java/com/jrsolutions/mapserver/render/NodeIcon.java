package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.gis.IMapper;

public class NodeIcon extends NodeSimbology {

	private static final Logger log=Logger.getLogger("NodeIcon");
	
	private BufferedImage image;
	private final int align;
	private final int zmin;
	
	
	public NodeIcon(String iconPath, int zmin, String align) {
		try {
			this.image=ImageIO.read(new File(iconPath));
		} catch (IOException e) {
			//e.printStackTrace();
			if(iconPath!=null && iconPath.length()>0){
				log.info("Icon Not Found:"+iconPath);
			}
			image=null;
		}
		this.zmin=zmin;
		this.align=strToAlign(align);
	//	System.out.println("Imagen:"+image);
	
	}

	/*
	 *  Align:
	 *  
	 *  1 | 2 | 3    NW | N | NE
	 * ---+---+---  ----+---+----
	 *  4 | 5 | 6     W | C | E
	 * ---+---+---  ----+---+----
	 *  7 | 8 | 9    SW | S | SE
	 * 
	 */
	public int strToAlign(String str){
		if(str.equalsIgnoreCase("NW"))return 1;
		if(str.equalsIgnoreCase("N")) return 2;
		if(str.equalsIgnoreCase("NE"))return 3;
		if(str.equalsIgnoreCase("W")) return 4;
		if(str.equalsIgnoreCase("C")) return 5;
		if(str.equalsIgnoreCase("E")) return 6;
		if(str.equalsIgnoreCase("SW"))return 7;
		if(str.equalsIgnoreCase("S")) return 8;
		if(str.equalsIgnoreCase("SE"))return 9;
		return 5;
	}
	
	@Override
	public void paint(IMapper m, Graphics2D g, Point p, Entity ent) {
		if(m.getZoomLevel()<zmin)return;
		if(image==null)return;
		int pp[]=m.pos(p.getX(),p.getY());
                int px=pp[0];
                int py=pp[1];        
		int tx=px;
		int ty=py;
		switch(align){
		case 1: 
			tx=px-image.getWidth();
		    ty=py-image.getHeight();
		    break;
		case 2: 
			tx=px-image.getWidth()/2;
	        ty=py-image.getHeight();
	        break;
		case 3: 
			tx=px;
	        ty=py-image.getHeight();
	        break;
		case 4: 
			tx=px-image.getWidth();
	        ty=py-image.getHeight()/2;
	        break;
		case 5: 
			tx=px-image.getWidth()/2;
	        ty=py-image.getHeight()/2;
	        break;
		case 6: 
			tx=px;
	        ty=py-image.getHeight()/2;
	        break;
		case 7: 
			tx=px-image.getWidth();
	        ty=py;
	        break;
		case 8: 
			tx=px-image.getWidth()/2;
	        ty=px;
	        break;
		case 9: 
			tx=px;
	        ty=px;
	        break;
		}
		g.drawImage(image,tx,ty,null);
	}

	@Override
	public void paint(IMapper m, Graphics2D g, Point p, double angle, Entity ent) {
		paint(m,g,p,ent);
	}

}
