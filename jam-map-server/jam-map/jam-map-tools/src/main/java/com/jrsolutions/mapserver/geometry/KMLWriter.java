package com.jrsolutions.mapserver.geometry;

import java.util.Iterator;
/**
 * 
 * 
 * 
 * @see https://developers.google.com/kml/documentation/kmlreference?hl=es
 *
 * @author jamartinm
 *
 */
public class KMLWriter {

	private final StringBuffer sb=new StringBuffer();
	private boolean pretty;
	
	public KMLWriter(boolean pretty){
		this.pretty=pretty;
	}
	public KMLWriter(){
		this.pretty=true;
	}
	
	public String writeHeader(){
		return "<kml xmlns=\"http://www.opengis.net/kml/2.2\" " +
				"xmlns:gx=\"http://www.google.com/kml/ext/2.2\">"+
				"   <Document>";
	}
	public String writeFooter(){
		return "</kml>";
	}
	public String write(Geometry geom){
		sb.delete(0,-1);
		if(geom==null) return "";
		writeGeometry(geom,0);
		return sb.toString();
	}
	
	public void writeGeometry(Geometry geom,int tab){
		if(geom instanceof Point){
			writePoint((Point)geom,tab+1);
		}else if (geom instanceof LineString){
			writeLineString((LineString)geom,tab+1);
		}else if (geom instanceof Polygon){
			writePolygon((Polygon)geom,tab+1);
		}else if (geom instanceof MultiLineString ){
			writeMultiGeometry((MultiLineString)geom,tab+1);
		}else if (geom instanceof MultiPoint){
			writeMultiGeometry((MultiPoint)geom,tab+1);
		}else if (geom instanceof MultiPolygon){
			writeMultiGeometry((MultiPolygon)geom,tab+1);
		}else if (geom instanceof MultiGeometry){
			writeMultiGeometry((MultiGeometry)geom,tab+1);
		}else{
			System.err.println("No se que geometria es "+geom.getClass().getName());
		}
	}

	private String newLine(){
		return pretty? "\n" : ""; 
	}
		
	private String tabs(int n){
		if(!pretty)return "";
		switch(n){
		case 1: return  "\t";
		case 2: return  "\t\t";
		case 3: return  "\t\t\t";
		case 4: return  "\t\t\t\t";
		case 5: return  "\t\t\t\t\t";
		case 6: return  "\t\t\t\t\t\t";
		case 7: return  "\t\t\t\t\t\t\t";
		case 8: return  "\t\t\t\t\t\t\t\t";
		default: return "\t\t\t\t\t\t\t\t\t";
		}
	}
	
	private void writePoint(Point p,int tab){
		sb.append(tabs(tab)+"<Point>"+newLine())   // id=ID
		// <extrude>0</extrude>
		// <altitudemode>
			.append(tabs(tab+1)+"<coordinates>"+newLine())
			.append(tabs(tab+2)+p.getX()).append(",").append(p.getY()+newLine())
			.append(tabs(tab+1)+"</coordinates>"+newLine())
			.append(tabs(tab)+"</Point>"+newLine());
	}
	
	private void writeLineString(LineString line,int tab){
		sb.append(tabs(tab)+"<LineString>"+newLine()); // id=ID
			// <extrude>
			// <altitudeMode>
			// <tessellate>
		sb.append(tabs(tab+1)+"<coordinates>"+newLine());
		Iterator<Point> it=line.iterator();
		while(it.hasNext()){
			Point p=it.next();
			sb.append(p.getX()).append(",").append(p.getY()).append(" "+newLine());
		}
		sb.append(tabs(tab+1)+"</coordinates>"+newLine());
		sb.append(tabs(tab)+"</LineString>"+newLine());
	}

	private void writeLinearRing(LineString line,int tab){
		sb.append(tabs(tab)+"<LinearRing>"+newLine()); // id=ID
			// <extrude>
			// <altitudeMode>
			// <tessellate>
		sb.append(tabs(tab+1)+"<coordinates>"+newLine());
		Iterator<Point> it=line.iterator();
		while(it.hasNext()){
			Point p=it.next();
			sb.append(tabs(tab+2)+p.getX()).append(",").append(p.getY()).append(" "+newLine());
		}
		sb.append(tabs(tab+1)+"</coordinates>"+newLine());
		sb.append(tabs(tab)+"</LinearRing>"+newLine());
	}

	private void writePolygon(Polygon poly,int tab){
		sb.append(tabs(tab)+"<Polygon>"+newLine()); // id=ID
		LineString outer=poly.getExterior();
		sb.append(tabs(tab+1)+"<outerBoundaryIs>"+newLine());
		writeLinearRing(outer,tab+2);
		sb.append(tabs(tab+1)+"</outerBoundaryIs>"+newLine());
		for(int i=1;i<poly.getNumRings();i++){
			sb.append(tabs(tab+1)+"<innerBoundaryIs>"+newLine());
			writeLinearRing(poly.getRing(i),tab+2);
			sb.append(tabs(tab+1)+"</innerBoundaryIs>"+newLine());
		}
		sb.append(tabs(tab)+"</Polygon>"+newLine());
	}
	
	private void writeMultiGeometry(MultiGeometry multi,int tab){
		sb.append(tabs(tab)+"<MultiGeometry>"+newLine());
		for(int i=0;i<multi.getNumGeometries();i++){
			writeGeometry(multi.getGeom(i),tab+1);
		}
		sb.append(tabs(tab)+"</MultiGeometry>"+newLine());
	}
		
}
