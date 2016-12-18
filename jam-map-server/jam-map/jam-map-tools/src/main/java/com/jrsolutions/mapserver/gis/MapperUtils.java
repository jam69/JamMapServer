/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.gis;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;

/**
 *
 * @author joseantoniomartin
 */
public class MapperUtils {

    public static Point2D mapPoint(IMapper mapper, Point p) {
        int[] r = mapper.pos(p.getX(), p.getY());
        return new Point2D.Double(r[0], r[1]);
    }

    public static Shape mapLine(IMapper mapper, LineString line) {
        GeneralPath res = new GeneralPath(GeneralPath.WIND_EVEN_ODD, line.getNumPoints());
        Point2D p = mapPoint(mapper, line.getPoint(0));
        res.moveTo(p.getX(), p.getY());
        for (int i = 1; i < line.getNumPoints(); i++) {
            p = mapPoint(mapper, line.getPoint(i));
            res.lineTo(p.getX(), p.getY());
        }
        return res;
    }
    
    
    
}
