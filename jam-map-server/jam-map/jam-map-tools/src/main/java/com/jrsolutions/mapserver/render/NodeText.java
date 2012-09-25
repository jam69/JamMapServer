package com.jrsolutions.mapserver.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.Point;

public class NodeText extends NodeSimbology {

	private Color color=Color.white;
	private int size=8;
	private int offx=0;
	private int offy=0;
	private Font font;
	private String attrName;
	private int zmin;
	private int align;

	private int MARGIN_X=5;
	private int MARGIN_Y=5;

	private boolean linked=false;

	enum BoxType {none,underlined,rect, rounded, oval,box3d,
		rectFilled,roundedFilled,ovalFilled, box3dFilled };

		private BoxType boxType=BoxType.rounded;
		private Color boxColor=Color.lightGray;

		public NodeText(int offx, int offy,int color,String attrName,int zmin,int size,String bType,String align){
			this.offx=offx;
			this.offy=offy;
			this.color=new Color(color);
			this.attrName=attrName;
			this.zmin=zmin;	
			this.size=size;
			this.boxType=strToBoxType(bType);
			this.align=strToAlign(align);
		}

		public BoxType strToBoxType(String str){
			if(str==null)return BoxType.none;
			if(str.contains("L")) linked=true;  // Efecto lateral
			if(str.contains("u")) return BoxType.underlined;
			if(str.contains("b")) return BoxType.rect;
			if(str.contains("r")) return BoxType.rounded;
			if(str.contains("o")) return BoxType.oval;
			if(str.contains("d")) return BoxType.box3d;
			if(str.contains("B")) return BoxType.rectFilled;
			if(str.contains("R")) return BoxType.roundedFilled;
			if(str.contains("O")) return BoxType.ovalFilled;
			if(str.contains("D")) return BoxType.box3dFilled;
			return BoxType.none;
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
		public void paint(Mapper m, Graphics2D g,Point p,double angle, Entity ent) {
			paint(m,g,p,angle,p.getX(),p.getY(), ent);
		}

		@Override
		public void paint(Mapper m, Graphics2D g,Point p, Entity ent) {
			paint(m,g,p,0.0,p.getX(),p.getY(), ent);

//			if(m.getZoomLevel()<zmin)return;
//			Object obj=ent.getAttr(attrName);
//			if(obj!=null){
//				int px= m.posX(p.getX())+offx;
//				int py= m.posY(p.getY())+offy;
//
//				g.setColor(color);
//				g.drawString(obj.toString(), px, py);
//
//			}
		}

		// falta probar el link, el subrayado
		// falta dibujar siempre los textos para arriba (no para abajo)
		public void paint(Mapper m, Graphics2D g, Point p, double angulo, double mx, double my,Entity ent) {
			if(m.getZoomLevel()<zmin)return;
			Object obj=ent.getAttr(attrName);
//			System.out.println(">>"+obj+"=="+ent);
			if(obj==null)return;
			String str=obj.toString(); // Formatter
			if(str.length()==0)return;
			int tx=m.posX(p.getX());
			int ty=m.posY(p.getY());
 
			float tam=size; // por ahora los textos no se escalan

			Font origen = font!=null ? font: g.getFont();

			g.setColor(color);
			Font deriv = origen.deriveFont(tam);
			g.setFont(deriv);
			
			TextLayout txl = new TextLayout(str, deriv, g.getFontRenderContext());
			Rectangle2D bounds = txl.getBounds();
					
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
			int x0=tx;
			int y0=ty;
			switch(align){
				case 1:  
					x0 = tx - (int) Math.round(bounds.getWidth());
			        y0 = ty ;
			        break;
				case 2:  
					x0 = tx - (int) Math.round(bounds.getWidth()/2);
					y0 = ty ;
					break;
				case 3:  
					x0 = tx ;
					y0 = ty ;
					break;
				case 4:  
					x0 = tx - (int) Math.round(bounds.getWidth());
					y0 = ty + (int) Math.round(bounds.getHeight()/2);
					break;
				case 5:  
					x0 = tx - (int) Math.round(bounds.getWidth()/2);
					y0 = ty + (int) Math.round(bounds.getHeight()/2);
					break;
				case 6:  
					x0 = tx ;
					y0 = ty + (int) Math.round(bounds.getHeight()/2);
					break;
				case 7:  
					x0 = tx - (int) Math.round(bounds.getWidth());
					y0 = ty + (int) Math.round(bounds.getHeight());
					break;
				case 8:  
					x0 = tx - (int) Math.round(bounds.getWidth()/2);
					y0 = ty + (int) Math.round(bounds.getHeight());
					break;
				case 9:  
					x0 = tx ;
					y0 = ty + (int) Math.round(bounds.getHeight());
					break;
			}
			
			AffineTransform af1 = g.getTransform();
			AffineTransform af2 = new AffineTransform();
			af2.translate(x0,y0);
			af2.rotate(angulo);
			g.transform(af2);

			
			if (boxType != BoxType.none || linked) {
				
				int bx0 =        - MARGIN_X;
				int bx1 = (int)  bounds.getWidth()+2*MARGIN_X;
				int by0 = (int)-(bounds.getHeight()+MARGIN_Y);
				int by1 = (int)  bounds.getHeight()+2*MARGIN_Y ;

				switch (boxType) {
				case rect :
					g.drawRect(bx0,by0,bx1,by1 );
					break;
				case rounded : //El angulo de redondeo esta a capon.
					g.drawRoundRect(bx0,by0,bx1,by1, 10, 20);
					break;
				case oval :
					g.drawOval(bx0,by0,bx1,by1);
					break;
				case box3d :
					g.draw3DRect(bx0,by0,bx1,by1, true);
					break;
				case rectFilled :
					g.setColor(boxColor);
					g.fillRect(bx0,by0,bx1,by1);
					g.setColor(color);
					break;
				case roundedFilled : //El angulo de rendondeo esta a capon.
					g.setColor(boxColor);
					g.fillRoundRect(bx0,by0,bx1,by1, 10, 20);
					g.setColor(color);
					break;
				case ovalFilled :
					g.setColor(boxColor);
					g.fillOval(bx0,by0,bx1,by1);
					g.setColor(color);
					break;
				case box3dFilled :
					g.setColor(boxColor);
					g.fill3DRect(bx0,by0,bx1,by1, true);
					g.setColor(color);
					break;

				case underlined:
					g.drawLine(bx0,by0, bx1, by0);
					break;
				case none:
					break;
				}

				if (linked) {
					// Calculamos el punto mas cercano a la entidad
					double d1 = ((bx0 - tx) * (bx0 - tx)) + ((by0 - ty) * (by0 - ty));
					double d2 = ((bx1 - tx) * (bx1 - tx)) + ((by0 - ty) * (by0 - ty));
					if (d1 < d2) {
						g.drawLine(bx0, by0, tx, ty);
					} else {
						g.drawLine(bx1, by1, tx,ty);
					}
				}
				//		setStroke(bbs2);
			}
			g.setColor(color);
			txl.draw(g, 0, 0);

			//      Volvemos a dejar el graphics como estaba
			g.setTransform(af1);
		}

}