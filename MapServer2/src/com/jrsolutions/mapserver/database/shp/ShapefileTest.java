package com.jrsolutions.mapserver.database.shp;

import java.io.IOException;

import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.GeometryFactory;
import com.jrsolutions.mapserver.geometry.InvalidGeometryException;

public class ShapefileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShapefileTest test=new ShapefileTest();
		test.test();
	}

	public void test(){
		try{
			ShapefileReader sfh=new ShapefileReader("../mapas/cntry02/cntry02.shp");
			Geometry g1;
			GeometryFactory gF=new GeometryFactory();
			for (int i = 0; i < sfh.numRecords(); i++)
			{
			//  System.out.println("Converting geometry #" + (i+1));
			  // read Shapefile geometry

				try
				{
					byte[] bA =sfh.getGeometryBytes(i);
					g1 = ShapefileReader.getGeometry(bA, gF);
					System.out.println("Geom["+i+"]"+g1);
				}catch(InvalidGeometryException e2) {
						e2.printStackTrace();
//				}catch(GeometryInputTypeNotSupportedException e3) {
//						e3.printStackTrace();
//				}catch(GeometryOutputTypeNotSupportedException e4) {
//						e4.printStackTrace();
				}

			}// Fin del for
			sfh.closeShapefile();
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
}
