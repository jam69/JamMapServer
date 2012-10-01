/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */
package com.jrsolutions.mapserver.geometry;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Reads a {@link Geometry}from a byte stream in Well-Known Binary format.
 * Supports use of an {@link InStream}, which allows easy use
 * with arbitary byte stream sources.
 * <p>
 * This class reads the format describe in {@link WKBWriter}.  It also partiually handles
 * the Extended WKB format used by PostGIS (by reading SRID values)
 * <p>
 * This class is designed to support reuse of a single instance to read multiple
 * geometries. This class is not thread-safe; each thread should create its own
 * instance.
 *
 * @see WKBWriter
 */
public class WKBReader
{
//  /**
//   * Converts a hexadecimal string to a byte array.
//   *
//   * @param hex a string containing hex digits
//   */
//  public static byte[] hexToBytes(String hex)
//  {
//    int byteLen = hex.length() / 2;
//    byte[] bytes = new byte[byteLen];
//
//    for (int i = 0; i < hex.length() / 2; i++) {
//      int i2 = 2 * i;
//      if (i2 + 1 > hex.length())
//        throw new IllegalArgumentException("Hex string has odd length");
//
//      int nib1 = hexToInt(hex.charAt(i2));
//      int nib0 = hexToInt(hex.charAt(i2 + 1));
//      byte b = (byte) ((nib1 << 4) + (byte) nib0);
//      bytes[i] = b;
//    }
//    return bytes;
//  }
//
//  private static int hexToInt(char hex)
//  {
//    int nib = Character.digit(hex, 16);
//    if (nib < 0)
//      throw new IllegalArgumentException("Invalid hex digit");
//    return nib;
//  }
//
//  private static final String INVALID_GEOM_TYPE_MSG
//  = "Invalid geometry type encountered in ";
//
//  private GeometryFactory factory;
//  private PrecisionModel precisionModel;
//  // default dimension - will be set on read
  private int inputDimension = 2;
  private boolean hasSRID = false;
  private int SRID = 0;
//  private ByteOrderDataInStream dis = new ByteOrderDataInStream();
  private DataInputStream dis; // = new ByteArrayInputStream();
  private double[] ordValues;
  private byte byteOrder;

  public	class WKBParseException extends Exception{
		public WKBParseException(){
			super();
		}
		public WKBParseException(String s){
			super(s);
		}
	}

  /**
   * Reads a single {@link Geometry} from a byte array.
   *
   * @param bytes the byte array to read from
   * @return the geometry read
   * @throws WKBParseException if a parse exception occurs
   */
  public Geometry read(byte[] bytes) throws WKBParseException
  {
    // possibly reuse the ByteArrayInStream?
    // don't throw IOExceptions, since we are not doing any I/O
    try {
      return read(new DataInputStream(new ByteArrayInputStream(bytes)));
    }
    catch (IOException ex) {
      throw new RuntimeException("Unexpected IOException caught: " + ex.getMessage());
    }
  }

  /**
   * Reads a {@link Geometry} from an {@link InStream).
   *
   * @param is the stream to read from
   * @return the Geometry read
   * @throws IOException
   * @throws WKBParseException
   */
  public Geometry read(DataInputStream is)
  throws IOException, WKBParseException
  {
    dis=is;
    Geometry g = readGeometry();
    setSRID(g);
    return g;
  }

  private Geometry readGeometry()
  throws IOException, WKBParseException
  {
    // determine byte order
    byteOrder = dis.readByte();
    // default is big endian
  //  if (byteOrder == WKBConstants.wkbNDR)
  //    dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);

    int typeInt = dis.readInt();
    int geometryType = typeInt & 0xff;
    // determine if Z values are present
    boolean hasZ = (typeInt & 0x80000000) != 0;
    inputDimension =  hasZ ? 3 : 2;
    // determine if SRIDs are present
    hasSRID = (typeInt & 0x20000000) != 0;

    if (hasSRID) {
      SRID = dis.readInt();
    }

    // only allocate ordValues buffer if necessary
    if (ordValues == null || ordValues.length < inputDimension)
      ordValues = new double[inputDimension];

    switch (geometryType) {
      case WKBConstants.wkbPoint :
        return readPoint();
      case WKBConstants.wkbLineString :
        return readLineString();
      case WKBConstants.wkbPolygon :
        return readPolygon();
      case WKBConstants.wkbMultiPoint :
        return readMultiPoint();
      case WKBConstants.wkbMultiLineString :
        return readMultiLineString();
      case WKBConstants.wkbMultiPolygon :
        return readMultiPolygon();
      case WKBConstants.wkbGeometryCollection :
        return readGeometryCollection();
    }
    throw new WKBParseException("Unknown WKB type " + geometryType);
    //return null;
  }

  /**
   * Sets the SRID, if it was specified in the WKB
   *
   * @param g the geometry to update
   * @return the geometry with an updated SRID value, if required
   */
  private Geometry setSRID(Geometry g)
  {
    if (SRID != 0)
      g.setSRID(SRID);
    return g;
  }

  private Point readPoint() throws IOException
  {
    readCoordinate();
    return new Point(ordValues[0],ordValues[1]);
  }

   private LineString readLineString() throws IOException
  {
    int size = dis.readInt();
    LineString pts = readCoordinateSequence(size);
    return pts;
  }

  private Polygon readPolygon() throws IOException
  {
    int numRings = dis.readInt();
    LineString[] rings=new LineString[numRings];
    for (int i = 0; i < numRings ; i++) {
      rings[i] = readLineString();
    }
    return new Polygon(rings);
  }

  private MultiPoint readMultiPoint() throws IOException, WKBParseException
  {
    int numGeom = dis.readInt();
    Point[] geoms = new Point[numGeom];
    for (int i = 0; i < numGeom; i++) {
      Geometry g = readGeometry();
      if (! (g instanceof Point))
        throw new WKBParseException("Parse Error" + "MultiPoint");
      geoms[i] = (Point) g;
    }
    return new MultiPoint(geoms);
  }

  private MultiLineString readMultiLineString() throws IOException, WKBParseException
  {
    int numGeom = dis.readInt();
    LineString[] geoms = new LineString[numGeom];
    for (int i = 0; i < numGeom; i++) {
      Geometry g = readGeometry();
      if (! (g instanceof LineString))
        throw new WKBParseException("Parse Error" + "MultiLineString");
      geoms[i] = (LineString) g;
    }
    return new MultiLineString(geoms);
  }

  private MultiPolygon readMultiPolygon() throws IOException, WKBParseException
  {
    int numGeom = dis.readInt();
    Polygon[] geoms = new Polygon[numGeom];
    for (int i = 0; i < numGeom; i++) {
      Geometry g = readGeometry();
      if (! (g instanceof Polygon))
        throw new WKBParseException("ParseError" + "MultiPolygon");
      geoms[i] = (Polygon) g;
    }
    return new MultiPolygon(geoms);
  }

  private MultiGeometry readGeometryCollection() throws IOException, WKBParseException
  {
    int numGeom = dis.readInt();
    Geometry[] geoms = new Geometry[numGeom];
    for (int i = 0; i < numGeom; i++) {
       geoms[i]= readGeometry();
    }
    return new MultiGeometry(geoms);
  }

  private LineString readCoordinateSequence(int size) throws IOException
  {
    Point[] pts=new Point[size];
    for (int i = 0; i < size; i++) {
      readCoordinate();
      pts[i]=new Point(ordValues[0],ordValues[1]);
    }
    return new LineString(pts);
  }

  /**
   * Reads a coordinate value with the specified dimensionality.
   * Makes the X and Y ordinates precise according to the precision model
   * in use.
   */
  private void readCoordinate() throws IOException
  {
    for (int i = 0; i < inputDimension; i++) {
        ordValues[i] = dis.readDouble();
       }
  }

}