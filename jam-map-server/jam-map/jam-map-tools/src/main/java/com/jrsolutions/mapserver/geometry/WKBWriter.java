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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Writes a {@link Geometry} into Well-Known Binary format.
 * Supports use of an {@link OutStream}, which allows easy use
 * with arbitary byte stream sinks.
 * <p>
 * The WKB format is specified in the OGC Simple Features for SQL specification.
 * This implementation supports the extended WKB standard for representing
 * 3-dimensional coordinates.  The presence of 3D coordinates is signified
 * by setting the high bit of the wkbType word.
 * <p>
 * Empty Points cannot be represented in WKB; an
 * {@link IllegalArgumentException} will be thrown if one is
 * written. The WKB specification does not support representing {@link LinearRing}s;
 * they will be written as {@link LineString}s.
 * <p>
 * This class is designed to support reuse of a single instance to read multiple
 * geometries. This class is not thread-safe; each thread should create its own
 * instance.
 *
 * @see WKBReader
 */
public class WKBWriter
{
//  public static String bytesToHex(byte[] bytes)
//  {
//    StringBuffer buf = new StringBuffer();
//    for (int i = 0; i < bytes.length; i++) {
//      byte b = bytes[i];
//      buf.append(toHexDigit((b >> 4) & 0x0F));
//      buf.append(toHexDigit(b & 0x0F));
//    }
//    return buf.toString();
//  }
//
//  private static char toHexDigit(int n)
//  {
//    if (n < 0 || n > 15)
//      throw new IllegalArgumentException("Nibble value out of range: " + n);
//    if (n <= 9)
//      return (char) ('0' + n);
//    return (char) ('A' + (n - 10));
//  }
//
  private int outputDimension = 2;
  private int byteOrder;
  private ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
//  private OutStream byteArrayOutStream = new OutputStreamOutStream(byteArrayOS);
//  // holds output data values
  private byte[] buf = new byte[8];

  /**
   * Creates a writer that writes {@link Geometry}s with
   * output dimension = 2 and BIG_ENDIAN byte order
   */
  public WKBWriter() {
    this(2, ByteOrderValues.BIG_ENDIAN);
  }

  /**
   * Creates a writer that writes {@link Geometry}s with
   * the given output dimension (2 or 3) and BIG_ENDIAN byte order
   *
   * @param outputDimension the dimension to output (2 or 3)
   */
  public WKBWriter(int outputDimension) {
    this(outputDimension, ByteOrderValues.BIG_ENDIAN);
  }

  /**
   * Creates a writer that writes {@link Geometry}s with
   * the given output dimension (2 or 3) and byte order
   *
   * @param outputDimension the dimension to output (2 or 3)
   * @param byteOrder the byte ordering to use
   */
  public WKBWriter(int outputDimension, int byteOrder) {
    this.outputDimension = outputDimension;
    this.byteOrder = byteOrder;

    if (outputDimension < 2 || outputDimension > 3)
      throw new IllegalArgumentException("Output dimension must be 2 or 3");
  }

  /**
   * Writes a {@link Geometry} into a byte array.
   *
   * @param geom the geometry to write
   * @return the byte array containing the WKB
   */
  public byte[] write(Geometry geom)
  {
    try {
      byteArrayOS.reset();
      write(geom, byteArrayOS);
    }
    catch (IOException ex) {
      throw new RuntimeException("Unexpected IO exception: " + ex.getMessage());
    }
    return byteArrayOS.toByteArray();
  }

  /**
   * Writes a {@link Geometry} to an {@link OutStream}.
   *
   * @param geom the geometry to write
   * @param os the out stream to write to
   * @throws IOException if an I/O error occurs
   */
  public void write(Geometry geom, ByteArrayOutputStream os) throws IOException
  {
    if (geom instanceof Point)
      writePoint((Point) geom, os);
    // LinearRings will be written as LineStrings
    else if (geom instanceof LineString)
      writeLineString((LineString) geom, os);
    else if (geom instanceof Polygon)
      writePolygon((Polygon) geom, os);
    else if (geom instanceof MultiPoint)
      writeGeometryCollection(WKBConstants.wkbMultiPoint, (MultiPoint) geom, os);
    else if (geom instanceof MultiLineString)
      writeGeometryCollection(WKBConstants.wkbMultiLineString,
          (MultiLineString) geom, os);
    else if (geom instanceof MultiPolygon)
      writeGeometryCollection(WKBConstants.wkbMultiPolygon,
          (MultiPolygon) geom, os);
    else if (geom instanceof MultiGeometry)
      writeGeometryCollection(WKBConstants.wkbGeometryCollection,
          (MultiGeometry) geom, os);
    else {
      //Assert.shouldNeverReachHere("Unknown Geometry type");
    }
  }

  private void writePoint(Point pt, ByteArrayOutputStream os) throws IOException
  {
    writeByteOrder(os);
    writeGeometryType(WKBConstants.wkbPoint, os);
    writeCoordinate(pt, os);
  }

  private void writeLineString(LineString line, ByteArrayOutputStream os)
      throws IOException
  {
    writeByteOrder(os);
    writeGeometryType(WKBConstants.wkbLineString, os);
    writeCoordinateSequence(line, true, os);
  }

  private void writePolygon(Polygon poly, ByteArrayOutputStream os) throws IOException
  {
    writeByteOrder(os);
    writeGeometryType(WKBConstants.wkbPolygon, os);
    writeInt(poly.getNumRings(), os);
    for (int i = 0; i < poly.getNumRings(); i++) {
      writeCoordinateSequence(poly.getRing(i), true,os);
    }
  }

  private void writeGeometryCollection(int geometryType, MultiGeometry gc,
		  ByteArrayOutputStream os) throws IOException
  {
    writeByteOrder(os);
    writeGeometryType(geometryType, os);
    writeInt(gc.getNumGeometries(), os);
    for (int i = 0; i < gc.getNumGeometries(); i++) {
      write(gc.getGeom(i), os);
    }
  }

  private void writeByteOrder(ByteArrayOutputStream os) throws IOException
  {
    if (byteOrder == ByteOrderValues.LITTLE_ENDIAN)
      buf[0] = WKBConstants.wkbNDR;
    else
      buf[0] = WKBConstants.wkbXDR;
    os.write(buf,0, 1);
  }

  private void writeGeometryType(int geometryType, ByteArrayOutputStream os)
      throws IOException
  {
    int flag3D = (outputDimension == 3) ? 0x80000000 : 0;
    int typeInt = geometryType | flag3D;
    writeInt(typeInt, os);
  }

  private void writeInt(int intValue, ByteArrayOutputStream os) throws IOException
  {
    ByteOrderValues.putInt(intValue, buf, byteOrder);
    os.write(buf,0, 4);
  }

  private void writeCoordinateSequence(LineString seq, boolean writeSize, ByteArrayOutputStream os)
      throws IOException
  {
    if (writeSize)
      writeInt(seq.getNumPoints(), os);

    for (int i = 0; i < seq.getNumPoints(); i++) {
      writeCoordinate(seq.getPoint(i), os);
    }
  }

  private void writeCoordinate(Point seq,  ByteArrayOutputStream os)
  throws IOException
  {
    ByteOrderValues.putDouble(seq.getX(), buf, byteOrder);
    os.write(buf,0, 8);
    //os.write(buf);
    ByteOrderValues.putDouble(seq.getY(), buf, byteOrder);
    os.write(buf,0, 8);
     }
}