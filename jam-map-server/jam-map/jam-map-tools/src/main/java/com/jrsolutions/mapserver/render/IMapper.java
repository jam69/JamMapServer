/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.render;

import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Rect;
import java.awt.Graphics;
import java.awt.Shape;

/**
 *
 * @author joseantoniomartin
 */
public interface IMapper {

    void drawLine(double x, double y, double x2, double y2, Graphics g);
 

    /**
     * Devuelve una copia de las coordenadas de zoom actual.
     * @return Una copia del zoom actual.
     */
    Rect getZoom();

    int getZoomLevel();

    Shape mapLine(LineString line);

    int[] pos(double X, double Y);

    
    /**
     * Indicamos que tama�o tiene nuestra ventana.
     * Suponemos que nuestra ventana es de tama�o (0,0)-(ancho,alto)
     * Es fundamental que estos datos sean correctos.
     *
     * @param ancho Ancho en unidades(pixels) de la ventana.
     * @param alto  Alto  en unidades(pixels) de la ventana.
     */
    void setSize(int ancho, int alto);

    void setZoomLevel(int z);

    /**
     * Le mandamos una nuevas coordenadas de zoom.
     * Las coordenadas se ajustan a la relacion de aspecto de la ventana.
     *
     * @param rec Nuevas coordenadas.
     */
    void zoom(Rect rec);
    
     public void setRotation(double rot);

    public double getRotation();
    
}
