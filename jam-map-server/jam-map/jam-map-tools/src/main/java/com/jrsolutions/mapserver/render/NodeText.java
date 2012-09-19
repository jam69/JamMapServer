package com.jrsolutions.mapserver.render;

import java.awt.BasicStroke;
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
	private int size=16;
	private int offx=0;
	private int offy=0;
	private Font font;
	private String attrName;
	private int zmin;
	
	private int MARGEN_X=0;
	private int MARGEN_Y=0;
	
	enum Effect {none, box, underlined, linked};
	enum BoxType {rect, rounded, oval,box3d,
		         rectFilled,roundedFilled,ovalFilled, box3dFilled };
	
	private Effect effect = Effect.none;
	private BoxType boxType;
	private Color boxColor;

	public NodeText(int offx, int offy,int color,String attrName,int zmin){
		this.offx=offx;
		this.offy=offy;
		this.color=new Color(color);
		this.attrName=attrName;
		this.zmin=zmin;
		
	}
	

	@Override
	public void paint(Mapper m, Graphics2D g,Point p,double angle, Entity ent) {
		paint(m,g,p,angle,p.getX(),p.getY(), ent);
	}
	
	@Override
	public void paint(Mapper m, Graphics2D g,Point p, Entity ent) {
		
		if(m.getZoomLevel()<zmin)return;
		Object obj=ent.getAttr(attrName);
		if(obj!=null){
			int px= m.posX(p.getX())+offx;
			int py= m.posY(p.getY())+offy;
			
			g.setColor(color);
			g.drawString(obj.toString(), px, py);

			}
	}
	
	public void paint(Mapper m, Graphics2D g, Point p, double angulo, double mx, double my,Entity ent) {
		Object obj=ent.getAttr(attrName);
		if(obj==null)return;
		String str=obj.toString(); // Formatter
		if(str.length()==0)return;
		double x=p.getX();
		double y=p.getY();
		
		int tx=m.posX(p.getX());
		int ty=m.posY(p.getY());
		g.setColor(Color.red);
		g.drawLine(tx,ty,
				(int)(tx+Math.cos(angulo)*30),
				(int)(ty+Math.sin(angulo)*30));
		
		
		
		
		double factor = m.factorEscala();
		// con un tamaño de fuente inferior a 0.2 no actualiza el tamaño de la fuente
		//float tam;
		//if ((tam = size / (float) factor) <= 1)
		//	return;
		float tam=size; // por ahora los textos no se escalan
		
		Font origen = font!=null ? font: g.getFont();
		
		g.setColor(color);
		Font deriv = origen.deriveFont(tam);
		g.setFont(deriv);
		Rectangle2D bounds = null;
		
		//			if (txtc.hasEfecto())
		//			{
		//          Lo necesitamos para pintar luego el texto,por eso lo sacamos del if
		TextLayout txl = new TextLayout(str, deriv, g.getFontRenderContext());
		bounds = txl.getBounds();
		//			}
		//BasicStroke bbs2 = setStroke(bbs);
		AffineTransform af1 = g.getTransform();
		AffineTransform af2 = new AffineTransform();
//		af2.rotate(angulo, m.posX(x,y), m.posY(x, y));
//		af2.translate(m.posX(x + offx, y + offy), m.posY(x + offx, y + offy));
        af2.translate(tx, ty);	
		g.transform(af2);

		g.setColor(Color.red);g.drawString("XXXXXXXXXXXXXXXXX", 0,0);
		
		
		if (angulo != 0.0) {
			//Font deriv2=deriv.deriveFont(deriv.getTransform().getRotateInstance(angulo));
			Font deriv2 = deriv.deriveFont(AffineTransform.getRotateInstance(angulo));
			g.setFont(deriv2);
		}
		if (effect != Effect.none) {
			double _x = MARGEN_X / factor;
			double _xf = _x + bounds.getWidth();
			double _y = MARGEN_Y / factor;
			double _yf = _y;

			if (effect==Effect.box) { // Caja
				double _yc = bounds.getHeight() + MARGEN_Y / factor;
				double alt1 = bounds.getHeight() + 2 * MARGEN_Y / factor;
				switch (boxType) {
					case rect :
						g.drawRect((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1);
						break;
					case rounded : //El angulo de redondeo esta a capon.
						g.drawRoundRect((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1, 10, 20);
						break;
					case oval :
						g.drawOval((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1);
						break;
					case box3d :
						g.draw3DRect((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1, true);
						break;
					case rectFilled :
						g.setColor(boxColor);
						g.fillRect((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1);
						g.setColor(color);
						break;
					case roundedFilled : //El angulo de rendondeo esta a capon.
						g.setColor(boxColor);
						g.fillRoundRect((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1, 10, 20);
						g.setColor(color);
						break;
					case ovalFilled :
						g.setColor(boxColor);
						g.fillOval((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1);
						g.setColor(color);
						break;
					case box3dFilled :
						g.setColor(boxColor);
						g.fill3DRect((int) - _x, (int) - _yc, (int) (_xf + _x), (int) alt1, true);
						g.setColor(color);
						break;
				}
			}

			if (effect==Effect.underlined) {
				g.drawLine((int) - _x, (int) _y, (int) _xf, (int) _y);
			}
			if (effect== Effect.linked) {
				double cx = m.posX(mx, my);
				double cy = m.posY(mx, my);
				Point2D.Double pAux = new Point2D.Double();
				try {
					af2.inverseTransform(new Point2D.Double(cx, cy), pAux);
				} catch (NoninvertibleTransformException e) {
					e.printStackTrace();
				}
				// Calculamos el punto mas cercano a la entidad
				double d1 = ((pAux.x - _x) * (pAux.x - _x)) + ((pAux.y - _y) * (pAux.y - _y));
				double d2 = ((pAux.x - _xf) * (pAux.x - _xf)) + ((pAux.y - _yf) * (pAux.y - _yf));
				if (d1 < d2) {
					g.drawLine((int) _x, (int) _y, (int) pAux.x, (int) pAux.y);
				} else {
					g.drawLine((int) _xf, (int) _yf, (int) pAux.x, (int) pAux.y);
				}
			}
	//		setStroke(bbs2);
		}
		g.setColor(color);
		txl.draw(g, 0, 0);
		//		TextLayout layout = new TextLayout(str, gr.getFont(),gr2.getFontRenderContext());
		//		layout.draw(gr2,0,0);
		//        bounds = layout.getBounds();

		//      Volvemos a dejar el graphics como estaba
		g.setTransform(af1);
	}

}