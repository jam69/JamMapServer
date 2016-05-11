/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.gis;

/**
 *
 * @author joseantoniomartin
 */
public abstract class AbstractMapper3D extends AbstractMapper{
    
   abstract public int[] pos(double X, double Y, double Z);

   abstract  int posX(double X, double Y);

   abstract  int posX(double X, double Y, double Z);

   abstract  int posY(double X, double Y);

   abstract  int posY(double X, double Y, double Z);
}
