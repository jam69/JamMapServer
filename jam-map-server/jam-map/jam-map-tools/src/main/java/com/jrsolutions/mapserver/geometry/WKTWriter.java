package com.jrsolutions.mapserver.geometry;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class WKTWriter {

	private final StringBuffer sb;
	private final DecimalFormat fmt;
	
	public WKTWriter(Geometry geom){
		this(geom,7);
	}
	public WKTWriter(Geometry geom,int nDecimals){
		sb=new StringBuffer();
		fmt=new DecimalFormat();
		fmt.setMaximumFractionDigits(nDecimals);
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(','); 
		fmt.setDecimalFormatSymbols(otherSymbols);
		renderGeom(geom);
	}
	
	private void renderGeom(Geometry geom){
		if(geom==null)return;
		if(geom instanceof Point){
			renderPoint((Point)geom);
		}else if (geom instanceof LineString){
			renderLineString((LineString)geom);
		}else if (geom instanceof Polygon){
			renderPolygon((Polygon)geom);
		}else if (geom instanceof MultiLineString ){
			renderMultiLineString((MultiLineString)geom);
		}else if (geom instanceof MultiPoint){
			renderMultiPoint((MultiPoint)geom);
		}else if (geom instanceof MultiPolygon){
			renderMultiPolygon((MultiPolygon)geom);
		}else if (geom instanceof MultiGeometry){
			renderMultiGeometry((MultiGeometry)geom);
		}else{
			System.err.println("No se que geometria es "+geom.getClass().getName());
		}
	}
	
	public String getString(){
		return sb.toString();
	}
	
	private void renderPoint(Point p){
		sb.append("POINT(");
		renderPoint2(p);
		sb.append( ")");
	}
	private void renderPoint2(Point p){
		sb.append(fmt.format(p.getX())+" "+fmt.format(p.getY())  );
	}
	
	private void renderLineString(LineString s){
		sb.append("LINESTRING");
		renderLineString2(s);
	}
	
	private void renderLineString2(LineString s){
		if(s.getNumPoints()==0){
			sb.append(" EMPTY ");
			return;
		}
		sb.append("(");
		for(int i=0;i<s.getNumPoints();i++){
			if(i>0){
				sb.append(",");
			}
			renderPoint2(s.getPoint(i));
		}
		sb.append(")");
	}

	private void renderPolygon(Polygon p){
		sb.append("POLYGON");
		renderPolygon2(p);
	}
	
	private void renderPolygon2(Polygon p){
		if(p.getNumRings()==0){
			sb.append(" EMPTY ");
			return;
		}
		sb.append("(");
		for(int i=0;i<p.getNumRings();i++){
			if(i>0){
				sb.append(",");
			}
			renderLineString2(p.getRing(i));
		}
		sb.append(")");
	}
	
	private void renderMultiPoint(MultiPoint mp){
		sb.append("MULTIPOINT");
		if (mp.getNumGeometries()==0){
			sb.append(" EMPTY ");
			return;
		}
		sb.append("(");
		for(int i=0;i<mp.getNumGeometries();i++){
			if(i>0){
				sb.append(",");
			}
			renderPoint2((Point)mp.getGeom(i));
		}
		sb.append(")");
	}
	
	private void renderMultiLineString(MultiLineString mp){
		sb.append("MULTILINESTRING");
		if (mp.getNumGeometries()==0){
			sb.append(" EMPTY ");
			return;
		}
		sb.append("(");
		for(int i=0;i<mp.getNumGeometries();i++){
			if(i>0){
				sb.append(",");
			}
			renderLineString2((LineString)mp.getGeom(i));
		}
		sb.append(")");
	}

	private void renderMultiPolygon(MultiPolygon mp){
		sb.append("MULTIPOLYGON");
		if (mp.getNumGeometries()==0){
			sb.append(" EMPTY ");
			return;
		}
		sb.append("(");
		for(int i=0;i<mp.getNumGeometries();i++){
			if(i>0){
				sb.append(",");
			}
			renderPolygon2((Polygon)mp.getGeom(i));
		}
		sb.append(")");
	}
	
	private void renderMultiGeometry(MultiGeometry mp){
		sb.append("GEOMETRYCOLLECTION");
		if (mp.getNumGeometries()==0){
			sb.append(" EMPTY ");
			return;
		}
		sb.append("(");
		for(int i=0;i<mp.getNumGeometries();i++){
			if(i>0){
				sb.append(",");
			}
			renderGeom(mp.getGeom(i));
		}
		sb.append(")");
	}
		

}
