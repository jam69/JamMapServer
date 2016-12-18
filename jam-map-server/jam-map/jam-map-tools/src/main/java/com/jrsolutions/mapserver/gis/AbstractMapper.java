/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.gis;

import java.awt.Graphics;
import java.awt.Shape;

import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Rect;

/**
 *
 * @author joseantoniomartin
 */
public abstract class AbstractMapper implements IMapper {

    protected int ancho;
	protected int alto;

    protected Rect orig;
	protected Rect r;

    abstract protected void adjust();

      public abstract void drawLine(double x, double y, double x2, double y2, Graphics g);
 

    /**
     * Devuelve una copia de las coordenadas de zoom actual.
     * @return Una copia del zoom actual.
     */
      public abstract Rect getZoom();

    

      public abstract Shape mapLine(LineString line);

      public abstract int[] pos(double X, double Y);

    
    /**
     * Indicamos que tama�o tiene nuestra ventana.
     * Suponemos que nuestra ventana es de tama�o (0,0)-(ancho,alto)
     * Es fundamental que estos datos sean correctos.
     *
     * @param ancho Ancho en unidades(pixels) de la ventana.
     * @param alto  Alto  en unidades(pixels) de la ventana.
     */
      public abstract void setSize(int ancho, int alto);

      public abstract void setZoomLevel(int z);
      public abstract int getZoomLevel();
      public abstract double getZoomFactor();

    /**
     * Le mandamos una nuevas coordenadas de zoom.
     * Las coordenadas se ajustan a la relacion de aspecto de la ventana.
     *
     * @param rec Nuevas coordenadas.
     */
    public abstract void zoom(Rect rec);
    
    public void setRotation(double rot){

    }
    public double getRotation(){
        return 0;
    }


    	protected void adaptarCoord() {
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

    public void reset()
    {
        if(orig!=null){
            r=new Rect(orig);
            adjust();
        }
    }
    
}
