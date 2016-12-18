/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.tiles;

import java.awt.Graphics2D;

import com.jrsolutions.mapserver.gis.IMapper;
import com.jrsolutions.mapserver.gis.Layer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 *
 * @author joseantoniomartin
 */
public class TileLayer implements Layer {

    private Cache ehCache;
    
    @Override
    public void render(IMapper m, Graphics2D g) {

        int z=17;  // TOFIX
        double lat=0;  //TOFIX
        double lon=0; // TOFIX

        System.out.println("---R-e-p-i-n-t-a---");
        int w = m.getWidth();
        int h = m.getHeight();

        int ntx = w / 256 + 1;
        int nty = h / 256 + 1;
        //http://localhost:8080/jam-map-server/TileServlet/17/64222/49418.png
        int zoom = z;
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
       
        System.out.println(">--");
        int dx1 = 0;
        for (int i = 0; i < ntx; i++) {
            int dy1 = 0;
            for (int j = 0; j < nty; j++) {
                String key = zoom + "/" + (xtile + i) + "/" + (ytile + j);
                ImageGetter mg = null;
                if (ehCache != null) {
                    Element element = ehCache.get(key);
                    if (element != null) {
                        mg = (ImageGetter) element.getObjectValue();
                    }
                }
                if (mg != null) {
                    mg.repos(g, dx1, dy1);
                    System.out.println("-++++++++REPOS++++++++++++++++++++++++++++++++++++++++++++URL=" + key);
                } else {
                    System.out.println("---------GET -------------------------------------------- URL=" + key);
                    ImageGetter ig = new ImageGetter(key, dx1, dy1,null);
                    if (ehCache != null) {
                        ehCache.put(new Element(key, ig));
                    }
                    //executor.execute(ig);
                    ig.run(); // TOFIX
                }
                dy1 += 256;
            }
            dx1 += 256;
        }
    }
 
    
    
    public static String getTileNumber(final double lat, final double lon, final int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
        return ("" + zoom + "/" + xtile + "/" + ytile);
    }

    static double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    static double tile2lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }
    
     int tileSize = 256;
     double initialResolution = 2 * Math.PI * 6378137 / tileSize;
        // 156543.03392804062 for tileSize 256 pixels
     double originShift = 2 * Math.PI * 6378137 / 2.0;
        // 20037508.342789244
        
    double[] LatLonToMeters( double lat, double lon ){
        //Converts given lat/lon in WGS84 Datum to XY in Spherical Mercator EPSG:900913"

        double mx = lon * originShift / 180.0;
        double my = Math.log( Math.tan((90 + lat) * Math.PI / 360.0 )) / (Math.PI / 180.0);

        my = my * originShift / 180.0;
        return new double[]{mx,my};
        //return mx, my;
    }

    double[] MetersToLatLon( double mx, double my ){
        //Converts XY point from Spherical Mercator EPSG:900913 to lat/lon in WGS84 Datum"

        double lon = (mx / originShift) * 180.0;
        double lat = (my / originShift) * 180.0;

        lat = 180 / Math.PI * (2 * Math.atan( Math.exp( lat * Math.PI / 180.0)) - Math.PI / 2.0);
        //return lat, lon
        return new double[]{lat,lon};
    }
}
