
package com.jrsolutions.mapserver.geometry;

/**
 * Obtiene una geometria a partir de su representacion WKT.
 * 
 *
 */
public class WKTParser {

	private int p;
	private final String str;
	private Geometry geom;

	public WKTParser(String str) throws WKTParseException{
		p = 0;
		this.str = str;
		parseGeometry();
	}
	
	public Geometry getGeom(){
		return geom;
	}

	private Geometry parseGeometry() throws WKTParseException {
		try {
		int op=p;
		p = str.indexOf("(",p);
		String ng = str.substring(op, p).toLowerCase().trim();
		if (ng.equals("geometry")) {
		} else if (ng.equals("point")) {
			geom = parsePoint();
		} else if (ng.equals("linestring")) {
			geom = parseLineString();
		} else if (ng.equals("polygon")) {
			geom = parsePolygon();
		} else if (ng.equals("multipoint")) {
			geom = parseMultiPoint();
		} else if (ng.equals("multilinestring")) {
			geom = parseMultiLineString();
		} else if (ng.equals("multipolygon")) {
			geom = parseMultiPolygon();
		} else if (ng.equals("geometrycollection")) {
			geom = parseMultiGeometry();
		}
//		char nt=nextChar();
//		if(nt==','){
//			String aux=str.substring(p);
//			int srid=Integer.parseInt(aux);
//			geom.setSRID(srid);
//		}
		return geom;
		}catch(ArrayIndexOutOfBoundsException ex){
			throw new WKTParseException("Error de formato "+formatError());
		}
	}

	private LineString parseLineString() throws WKTParseException{
		char nt = nextChar();
		if (nt != '(') { // error
			throw new WKTParseException("Falta '(' al leer una linea "+formatError());
		}
		LineString linea = new LineString();
		Point p;
		while (nt != ')') {
			p = parseCoord();
			linea.addPoint(p);
			nt = nextChar();
			if (nt != ')' && nt != ',') {
				//error
				throw new WKTParseException("Falta ')' o ',' al leer una linea "+formatError());
			}
		}
		return linea;
	}
	
	private MultiPoint parseMultiPoint() throws WKTParseException{
		char nt = nextChar();
		if (nt != '(') { // error
			throw new WKTParseException("Falta '(' al leer una linea "+formatError());
		}
		MultiPoint mp = new MultiPoint();
		while (nt != ')') {
			Point p=parseCoord();
			mp.addPoint(p);
			nt = nextChar();
			if (nt != ')' && nt != ',') {
				//error
				throw new WKTParseException("Falta ')' o ',' al leer una linea "+formatError());
			}
		}
		return mp;
	}

	private MultiLineString parseMultiLineString() throws WKTParseException{
		char nt = nextChar();
		if (nt != '(') { // error
			throw new WKTParseException("Falta '(' al leer una linea "+formatError());
		}
		MultiLineString mp = new MultiLineString();
		while (nt != ')') {
			LineString p=parseLineString();
			mp.addLineString(p);
			nt = nextChar();
			if (nt != ')' && nt != ',') {
				//error
				throw new WKTParseException("Falta ')' o ',' al leer una linea "+formatError());
			}
		}
		return mp;
	}
	
	private MultiPolygon parseMultiPolygon() throws WKTParseException{
		char nt = nextChar();
		if (nt != '(') { // error
			throw new WKTParseException("Falta '(' al leer una linea "+formatError());
		}
		MultiPolygon mp = new MultiPolygon();
		while (nt != ')') {
			Polygon p=parsePolygon();
			mp.addPolygon(p);
			nt = nextChar();
			if (nt != ')' && nt != ',') {
				//error
				throw new WKTParseException("Falta ')' o ',' al leer una linea "+formatError());
			}
		}
		return mp;
	}

	private MultiGeometry parseMultiGeometry() throws WKTParseException{
		char nt = nextChar();
		if (nt != '(') { // error
			throw new WKTParseException("Falta '(' al leer una linea "+formatError());
		}
		MultiGeometry mp = new MultiGeometry();
		while (nt != ')') {
			Geometry p=parseGeometry();
			mp.addGeom(p);
			nt = nextChar();
			if (nt != ')' && nt != ',') {
				//error
				throw new WKTParseException("Falta ')' o ',' al leer una linea "+formatError());
			}
		}
		return mp;
	}
	
	private Polygon parsePolygon() throws WKTParseException {
			char nt = nextChar();
			if (nt != '(') { // error
				throw new WKTParseException("Falta '(' al leer un polygono "+formatError());
			}
			Polygon superf = new Polygon();
			LineString ext=parseLineString();
			superf.addRing(ext);
			nt=nextChar();
			while (nt == ','){
					//System.out.println(" Tenemos interiores....");				
				LineString inte=parseLineString();
				superf.addRing(inte);
				nt=nextChar();
			}
			if (nt != ')' ) {
				//error
				throw new WKTParseException("Falta ')' al final del poligono "+formatError());
			}
			return superf;
		}

	private Point parsePoint() throws WKTParseException{
		char nt = nextChar();
		if (nt != '(') { // error
			throw new WKTParseException("Falta '(' al leer un Nodo "+formatError());
		}
		Point p = parseCoord();
		nextChar();
		return p;
	}

	private Point parseCoord() throws WKTParseException{
		while( Character.isWhitespace(str.charAt(p)))p++;
		int pos = str.indexOf(" ", p);
		String x = str.substring(p, pos);
		p = pos;
		int posA = str.indexOf(")", p);
		int posB = str.indexOf(",", p);
		if (posA == -1 && posB == -1) {
			// error
			throw new WKTParseException("Falta ')' o ',' al leer un punto "+formatError());
		}
		if (posA == -1)
			pos = posB;
		else if (posB == -1)
			pos = posA;
		else
			pos = posA < posB ? posA : posB;
		String y = str.substring(p, pos);
		p = pos;
		
		double dx;
		try {
			dx=Double.parseDouble(x);
		}catch (NumberFormatException ex){
			throw new WKTParseException("No es un double ["+x+"] "+formatError());
		}
		double dy;
		try {
			dy=Double.parseDouble(y);
		}catch (NumberFormatException ex){
			throw new WKTParseException("No es un double ["+y+"] "+formatError());
		}
		return new Point(dx,dy);
	}
	
	private char nextChar() {
		while( p<str.length() && Character.isWhitespace(str.charAt(p)))p++;
		if(p==str.length()){
			return 0;
		}
		return str.charAt(p++);
	}
	
	private String formatError(){
		return "["+str.substring((p<30?p:p-30),p)+"<--"+str.substring(p+1,p+30)+"]";
	}



public class WKTParseException extends Exception{

	public WKTParseException(String s){
		super(s);	
	}
}

} // end class parser