
/*------------------------------------------------------------------
 * $RCSFile:$    $Revision: 1.8 $
 * $Date: 2003/05/06 08:54:28 $
 *
 * $Source: /export2/env/ssf/rep/onis/java/src3/core/com/soluzionasf/onis/oniscanvas/Zoomer.java,v $
 *
 *------------------------------------------------------------------*/
package com.jrsolutions.swing;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.gis.IMapper3D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;



/**
***************************************************************************
 * Mappea... convierte las coordenadas reales en los pixels de la ventana
 *
 * @see VentanaOnis
 * @version $Revision: 1.8 $
 * @author  ONIS
 *
 ***********************************************************************
*/
public class TransformMapper implements IMapper3D {
		
	private int ancho;
	private int alto;
	
	private Rect r;

    private double rotation=0;

    private final AffineTransform tr=new AffineTransform();



    private int zoomLevel;
	
    @Override
	public void setZoomLevel(int z){
		zoomLevel=z;
	}
    @Override
	public int getZoomLevel(){
		return 14;
	}

    public void setRotation(double rot){
        this.rotation=rot;
        adjust();
    }

    public double getRotation(){
        return rotation;
    }
	public TransformMapper() {
		
	}
	

    @Override
	public void setSize(int ancho, int alto) {
		this.ancho=ancho;
		this.alto=alto;
//		adaptarCoord();
        if(r!=null){
            adjust();
        }
	}
        
                public int getWidth(){
            return ancho;
        }
	 public int getHeight(){
            return alto;
        }
	
	/**
         * Le mandamos una nuevas coordenadas de zoom.
         * Las coordenadas se ajustan a la relacion de aspecto de la ventana.
         *
         * @param rec Nuevas coordenadas.
	*/
    @Override
	public void zoom(Rect rec) {
		r=new Rect(rec);
		adaptarCoord();
        adjust();
	}
	
	/**
         * Devuelve una copia de las coordenadas de zoom actual.
         * @return Una copia del zoom actual.
	*/
    @Override
	public Rect getZoom() {
		return new Rect(r); //enviamos una copia para que no lo cambien
	}
	
	

//	public double factorEscala() {
//		return  (r.getXMax()-r.getXMin())/ancho;
//	}
	

	private double coordX(int cx) {
		return  r.getXMin() + (double)(cx*(r.getXMax()-r.getXMin()))/ancho;
	}

	private double coordY(int cy) {
		return  r.getYMax() - (double)(cy*(r.getYMax()-r.getYMin()))/alto;
	}
	
        
        private void adaptarCoord() {
		//Redimensiona las coordenadas xmin,xmax,ymin,ymax para que
		//tengan la relacion de aspecto de la ventana de visualizacion.
		double rmx,rmy;
        final double tamx = r.getXMax()-r.getXMin();
        final double tamy = r.getYMax()-r.getYMin();
        if ((tamx/ancho) > (tamy/alto)) {
			rmx=(tamx/2.0);
			rmy=(alto*tamx/2.0)/ancho;
		}
		else{
			rmy=(tamy/2.0);
			rmx=(ancho*tamy/2.0)/alto;
		}

		double xCentroR=(r.getXMin()+r.getXMax())/2.0;
		double yCentroR=(r.getYMin()+r.getYMax())/2.0;
		r.setXMin(xCentroR-rmx);
		r.setXMax(xCentroR+rmx);
		r.setYMin(yCentroR-rmy);
		r.setYMax(yCentroR+rmy);
	}
   
    private void adjust(){
        tr.setToIdentity();
        tr.translate(ancho/2,alto/2);
        tr.rotate(Math.toRadians(rotation));
        tr.translate(-ancho/2,-alto/2);
        tr.scale(1,-1);
        tr.translate(0,-alto);
        double a=ancho/(r.getXMax()-r.getXMin());
        double b=alto/ (r.getYMax()-r.getYMin());
        tr.scale(a,b);
        tr.translate(-r.getXMin(), -r.getYMin());
        System.out.println(" tr="+tr);
    }

    public int[] pos(double cx, double cy){
   //     System.out.print("Entra:("+cx+","+cy+")");
        Point2D p1=new Point2D.Double(cx,cy);
        Point2D p2=tr.transform(p1,null);
        int[] ret=new int[2];
        ret[0]=(int)p2.getX();
        ret[1]=(int)p2.getY();
     //   System.out.println(" Sale:("+ret[0]+","+ret[1]+")");
        return ret;
    }

    public int[] pos(double X, double Y,double Z){
       return pos(X,Y);
	}

      @Override
	public int posX(double X,double Y){
		return pos(X,Y)[0];
	}
      @Override
    public int posY(double X, double Y)
    {
        return pos(X,Y)[1];
    }

    @Override
    public int posY(double X,double Y,double Z){
		return pos(X,Y)[1];
	}
      @Override
    public int posX(double X, double Y, double Z)
    {
    return pos(X,Y)[0];
    }

    
//    @Override
//	public Shape mapLine(LineString line){
//		GeneralPath res=new GeneralPath(GeneralPath.WIND_EVEN_ODD,line.getNumPoints());
//		Point p=line.getPoint(0);
//		// res.moveTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//		res.moveTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//		for(int i=1;i<line.getNumPoints();i++){
//			p=line.getPoint(i);
//			// res.lineTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//			res.lineTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//		}
//		return res;
//	}
	

//    @Override
//	public void drawLine(double x, double y, double x2, double y2, Graphics g) {
//		g.drawLine(posX(x,y),posY(x,y),posX(x2,y2),posY(x2,y2));
//	}

  



}
