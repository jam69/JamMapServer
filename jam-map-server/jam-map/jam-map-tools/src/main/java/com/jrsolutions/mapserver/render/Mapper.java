
/*------------------------------------------------------------------
 * $RCSFile:$    $Revision: 1.8 $
 * $Date: 2003/05/06 08:54:28 $
 *
 * $Source: /export2/env/ssf/rep/onis/java/src3/core/com/soluzionasf/onis/oniscanvas/Zoomer.java,v $
 *
 *------------------------------------------------------------------*/
package com.jrsolutions.mapserver.render;

import java.awt.Graphics;

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
public class Mapper {
		
	private int ancho;
	private int alto;
	
	private Rect r;

	private int zoomLevel;
	
	public void setZoomLevel(int z){
		zoomLevel=z;
	}
	public int getZoomLevel(){
		return zoomLevel;
	}
	
	public Mapper() {
		
	}
	
	public Mapper clone(){
		Mapper m= new Mapper();
		m.setSize(ancho, alto);
		m.zoom(r.clone());
		return m;
	}
	/**
         * Indicamos que tamaño tiene nuestra ventana.
         * Suponemos que nuestra ventana es de tamaño (0,0)-(ancho,alto)
         * Es fundamental que estos datos sean correctos.
         *
         * @param ancho Ancho en unidades(pixels) de la ventana.
         * @param alto  Alto  en unidades(pixels) de la ventana.
	*/
	public void setSize(int ancho, int alto) {
		this.ancho=ancho;
		this.alto=alto;
//		adaptarCoord();
	}
	
	/**
         * Le mandamos una nuevas coordenadas de zoom.
         * Las coordenadas se ajustan a la relacion de aspecto de la ventana.
         *
         * @param rec Nuevas coordenadas.
	*/
	public void zoom(Rect rec) {
		r=new Rect(rec);
		//adaptarCoord();
	}
	
	/**
         * Devuelve una copia de las coordenadas de zoom actual.
         * @return Una copia del zoom actual.
	*/
	public Rect getZoom() {
		return new Rect(r); //enviamos una copia para que no lo cambien
	}
	
	
	
	/**
         * Devuelve la escala que estamos utilizando para dibujar.
         * Este valor son los metros que representa cada pixel.
         * Si supieramos el tamaño del pixel (unidad de representacion) podríamos
         * tener la escala real.
         *
         * Esta funcion tiene sentido porque se mantiene la misma escala en
         * horizontal y en vertical.
         *
         * @return la escala.
	@roseuid 3CA83E47008C
	*/
	public double factorEscala() {
		return  (r.getXMax()-r.getXMin())/ancho;
	}
	
	/**
         * Devuelve la coordenada X real, que corresponde a la coordenada dada
         * en las unidades de la ventana.
         * @param cx Posicion horizontal del pixel.
         * @return Coordenada horizontal real del pixel.
	@roseuid 3CA83E47008D
	*/
	public double coordX(int cx) {
		return  r.getXMin() + (double)(cx*(r.getXMax()-r.getXMin()))/ancho;
	}
	
	/**
         * Devuelve la coordenada Y real, que corresponde a la coordenada dada
         * en las unidades de la ventana.
         * @param cy Posicion Vertical del pixel.
         * @return Coordenada vertical real del pixel.
	@roseuid 3CA83E47009C
	*/
	public double coordY(int cy) {
		return  r.getYMax() - (double)(cy*(r.getYMax()-r.getYMin()))/alto;
	}
	
	/**
         * Correspondencia en coordenadas reales de la distancia indicada en pixels.
         * En realida es simplemente c*factorEscala().
         *
         * @param c distancia en pixels.
         * @return distancia en metros.
	@roseuid 3CA83E47009E
	*/
	public double coordXY(int c) {
		// es igual hacerlo con x que con y
		return (double)(c*factorEscala());
	}
	
	/**
         * Devuelve la coordenada de ventana correspondiente a la coordenada real
         * que se le pasa.
         *
         * Puede que devuelva valores fuera de la ventana actual (negativos o
         * mayores que el tamaño de la ventana.
         *
         * @param cx Coordenada horizontal real.
         * @return coordenada de ventana que le corresponde.
	@roseuid 3CA83E4700AB
	*/
	public int posX(double cx) {
		return (int)Math.round((cx-r.getXMin())*ancho/(r.getXMax()-r.getXMin()));
	}
	
	/**
	 * Some transformations require both coordinates
	 * 
	 * (for now, we ignore Y )
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public int posX(double X,double Y){
		return posX(X);
	}
	/**
         * Devuelve la coordenada de ventana correspondiente a la coordenada real
         * que se le pasa.
         *
         * Puede que devuelva valores fuera de la ventana actual (negativos o
         * mayores que el tamaño de la ventana.
         *
         * @param cy Coordenada vertical real.
         * @return coordenada de ventana que le corresponde.
	@roseuid 3CA83E4700BB
	*/
	public int posY(double cy) {
		return (int)(Math.round((r.getYMax()-cy)*alto/(r.getYMax()-r.getYMin())));
	}
	/**
	 * Some transformations require both coordinates
	 * (for now, we ignore X)
	 * 
	 * @param X
	 * @param Y
	 * @return
	 */
	public int posY(double X, double Y){
		return posY(Y);
	}
	/**
     *  Metodo que adapta las coordenadas para que se mantenga la misma
     *  escala en horizontal y en vertical Y que este dentro del limite
	 *  de zoom Original.
   	 */
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

	public void drawLine(double x, double y, double x2, double y2, Graphics g) {
		g.drawLine(posX(x),posY(y),posX(x2),posY(y2));
	}
}
