
/*------------------------------------------------------------------
 * $RCSFile:$    $Revision: 1.8 $
 * $Date: 2003/05/06 08:54:28 $
 *
 * $Source: /export2/env/ssf/rep/onis/java/src3/core/com/soluzionasf/onis/oniscanvas/Zoomer.java,v $
 *
 *------------------------------------------------------------------*/
package com.jrsolutions.mapserver.gis;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Rect;



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
public class TransformMapper extends AbstractMapper3D {
		
	private int ancho;
	private int alto;

    private Rect orig;
	private Rect r;

    private double rotation=0;

    private final AffineTransform tr=new AffineTransform();

    private int zoomLevel; // TODO debe ser una funcion de R y ancho-alto
	
    @Override
	public void setZoomLevel(int z){
		zoomLevel=z;
	}
    @Override
	public int getZoomLevel(){
		return 8;
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
	
	/**
         * Indicamos que tama�o tiene nuestra ventana.
         * Suponemos que nuestra ventana es de tama�o (0,0)-(ancho,alto)
         * Es fundamental que estos datos sean correctos.
         *
         * @param ancho Ancho en unidades(pixels) de la ventana.
         * @param alto  Alto  en unidades(pixels) de la ventana.
	*/
    @Override
	public void setSize(int ancho, int alto) {
		this.ancho=ancho;
		this.alto=alto;
        if(r!=null){
            adaptarCoord();
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
        if(orig==null){
            orig=new Rect(rec);
        }
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
	
	
	
	/**
         * Devuelve la escala que estamos utilizando para dibujar.
         * Este valor son los metros que representa cada pixel.
         * Si supieramos el tama�o del pixel (unidad de representacion) podr�amos
         * tener la escala real.
         *
         * Esta funcion tiene sentido porque se mantiene la misma escala en
         * horizontal y en vertical.
         *
         * @return la escala.
	*/
    @Override
	public double getZoomFactor() {
		return  (r.getXMax()-r.getXMin())/ancho;
	}
	
	/**
         * Devuelve la coordenada X real, que corresponde a la coordenada dada
         * en las unidades de la ventana.
         * @param cx Posicion horizontal del pixel.
         * @return Coordenada horizontal real del pixel.
	@roseuid 3CA83E47008D
	*/
	private double coordX(int cx) {
		return  r.getXMin() + (double)(cx*(r.getXMax()-r.getXMin()))/ancho;
	}
	
	/**
         * Devuelve la coordenada Y real, que corresponde a la coordenada dada
         * en las unidades de la ventana.
         * @param cy Posicion Vertical del pixel.
         * @return Coordenada vertical real del pixel.
	@roseuid 3CA83E47009C
	*/
	private double coordY(int cy) {
		return  r.getYMax() - (double)(cy*(r.getYMax()-r.getYMin()))/alto;
	}

   
    protected void adjust(){
        tr.setToIdentity();
        tr.translate(ancho/2,alto/2);
        tr.rotate(Math.toRadians(rotation));
        tr.translate(-ancho/2,-alto/2);
        double a=ancho/(r.getXMax()-r.getXMin());
        double b=alto/ (r.getYMax()-r.getYMin());
        tr.scale(a,b);
        tr.translate(-r.getXMin(), -r.getYMin());
        System.out.println(" tr="+tr);
    }

    public int[] pos(double cx, double cy){
        System.out.print("Entra:("+cx+","+cy+")");
        Point2D p1=new Point2D.Double(cx,cy);
        Point2D p2=tr.transform(p1,null);
        int[] ret=new int[2];
        ret[0]=(int)p2.getX();
        ret[1]=(int)p2.getY();
        System.out.println(" Sale:("+ret[0]+","+ret[1]+")");
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

    
    @Override
	public Shape mapLine(LineString line){
		GeneralPath res=new GeneralPath(GeneralPath.WIND_EVEN_ODD,line.getNumPoints());
		Point p=line.getPoint(0);
		// res.moveTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
		res.moveTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
		for(int i=1;i<line.getNumPoints();i++){
			p=line.getPoint(i);
			// res.lineTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
			res.lineTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
		}
		return res;
	}
	

	/**
     *  Metodo que adapta las coordenadas para que se mantenga la misma
     *  escala en horizontal y en vertical Y que este dentro del limite
	 *  de zoom Original.
   	 */


    @Override
	public void drawLine(double x, double y, double x2, double y2, Graphics g) {
		g.drawLine(posX(x,y),posY(x,y),posX(x2,y2),posY(x2,y2));
	}


    @Override
    public void pan(int direction)
    {
        double dx = (r.getXMax() - r.getXMin()) / 2.;
        double dy = (r.getYMax() - r.getYMin()) / 2.;
        switch (direction)
        {
            case 8: // UP
                r.setYMin(r.getYMin() + dy);
                r.setYMax(r.getYMax() + dy);
                break;
            case 2: // DOWN
                r.setYMin(r.getYMin() - dy);
                r.setYMax(r.getYMax() - dy);
            case 4: // LEFT
                r.setXMin(r.getXMin() - dx);
                r.setXMax(r.getXMax() - dx);
            case 6: // RIGHT
                r.setXMin(r.getXMin() + dx);
                r.setXMax(r.getXMax() + dx);
            case 1: // LEFT_DOWN
                r.setXMin(r.getXMin() - dx);
                r.setXMax(r.getXMax() - dx);
                r.setYMin(r.getYMin() - dy);
                r.setYMax(r.getYMax() - dy);
            case 3: // right_down
                r.setYMin(r.getYMin() - dy);
                r.setYMax(r.getYMax() - dy);
                r.setXMin(r.getXMin() + dx);
                r.setXMax(r.getXMax() + dx);
            case 7: // left_up
                r.setXMin(r.getXMin() - dx);
                r.setXMax(r.getXMax() - dx);
                r.setYMin(r.getYMin() + dy);
                r.setYMax(r.getYMax() + dy);

            case 9: // right_up
                r.setYMin(r.getYMin() + dy);
                r.setYMax(r.getYMax() + dy);
                r.setXMin(r.getXMin() + dx);
                r.setXMax(r.getXMax() + dx);
        }
        adjust();
    }

     @Override
    public void zoom(int direction)
    {
        double dx = (r.getXMax() - r.getXMin()) / 4.;
        double dy = (r.getYMax() - r.getYMin()) / 4.;
        switch (direction)
        {
            case 1: // IN
                r.setXMin(r.getXMin() + dx);
                r.setXMax(r.getXMax() - dx);
                r.setYMin(r.getYMin() + dy);
                r.setYMax(r.getYMax() - dy);
                break;
                case -1: // OUT
                r.setXMin(r.getXMin() - dx);
                r.setXMax(r.getXMax() + dx);
                r.setYMin(r.getYMin() - dy);
                r.setYMax(r.getYMax() + dy);
                break;
        }
        adjust();
    }

    @Override
    public void reset()
    {
        if(orig!=null){
            r=new Rect(orig);
            adjust();
        }
    }


}
