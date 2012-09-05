package com.jrsolutions.mapserver.geometry;

public class GeometryFactory {

	public Point createPoint(double x, double y) {
		return new Point(x,y);
	}

	public MultiPoint createGeometryCollection(Point[] points) {
		return new MultiPoint(points);
	}

	public LineString createLineString(int i, double[] xyArray) {
		return new LineString(i,xyArray);
	}

	public Polygon createPolygon(LineString[] rings) {
		return new Polygon(rings);
	}

	public MultiGeometry createGeometryCollection(LineString[] lineStrings) {
		return new MultiGeometry(lineStrings);
	}

}
