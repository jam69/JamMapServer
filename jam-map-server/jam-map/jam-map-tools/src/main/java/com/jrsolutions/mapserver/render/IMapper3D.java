/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.render;

/**
 *
 * @author joseantoniomartin
 */
public interface IMapper3D extends IMapper{
    
    int[] pos(double X, double Y, double Z);

    int posX(double X, double Y);

    int posX(double X, double Y, double Z);

    int posY(double X, double Y);

    int posY(double X, double Y, double Z);
}
