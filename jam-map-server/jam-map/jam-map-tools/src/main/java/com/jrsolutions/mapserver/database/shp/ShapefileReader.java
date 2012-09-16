// Copyright (C) Oracle Corporation 1999.        All Rights Reserved.
/*------------------------------------------------------------------
 * $RCSFile:$    $Revision: 1.2 $
 * $Date: 2003/05/06 08:54:10 $
 *
 * $Source: /export2/env/ssf/rep/onis/java/src3/core/com/soluzionasf/onis/datosgraficos/adapters/ShapefileReader.java,v $
 *
 * Equipo:  I+D (SGD)
 * $Log: ShapefileReader.java,v $
 * Revision 1.2  2003/05/06 08:54:10  sf000023
 * Quitamos Warnings, optimizamos imports.
 *
 * Revision 1.1.1.1  2002/04/18 14:41:45  sf000023
 * no message
 *
 *------------------------------------------------------------------*/

package com.jrsolutions.mapserver.database.shp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.GeometryFactory;
import com.jrsolutions.mapserver.geometry.InvalidGeometryException;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.MultiLineString;
import com.jrsolutions.mapserver.geometry.MultiPoint;
import com.jrsolutions.mapserver.geometry.MultiPolygon;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Polygon;
import com.jrsolutions.mapserver.geometry.Rect;

class IndexRecord {
	int offset;
	int contentLen;
}

public class ShapefileReader {
	public static final int AV_NULL = 0;
	public static final int AV_POINT = 1;
	public static final int AV_ARC = 3;
	public static final int AV_POLYGON = 5;
	public static final int AV_MULTIPOINT = 8;
	protected int nRecords;
	protected int type;
	protected byte recBuffer[];
	protected double partBuffer[];
	protected int bufferLen;
	protected int partBufferLen;
	protected int cnt;
	protected String namePrefix;
	protected ShapefileHdr mainFileHdr;
	protected ShapefileHdr idxFileHdr;
	protected RandomAccessFile mainFIS;
	protected RandomAccessFile idxFIS;

	class ShapeIterator implements Iterator<Geometry>{

		int currentPos;
		GeometryFactory gf=new GeometryFactory();
		
		public boolean hasNext() {
			return currentPos<numRecords();
		}

		public Geometry next() {

			try {
				byte[] bA =getGeometryBytes(currentPos++);
				Geometry g = getGeometry(bA, gf);
				return g;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidGeometryException e) {
				e.printStackTrace();
			}
			return null;
		}

		public void remove() {
			throw new IllegalArgumentException();
		}
		
	}
	
	public Iterator<Geometry> iterator(){
		return new ShapeIterator();
	}
		
	
	/**
	@roseuid 3CA83F76009C
	*/
	public static Geometry getGeometry(byte recBuffer[], GeometryFactory gF)
		throws InvalidGeometryException {
		int type = makeIntLittleEndian(recBuffer, 8);

		return getGeometry(recBuffer, gF, type, 12);
	}

	/**
	@roseuid 3CA83F7600AB
	*/
	protected static Geometry getGeometry(
		byte recBuffer[],
		GeometryFactory gF,
		int type,
		int off)
		throws InvalidGeometryException {
		switch (type) {
			case AV_POINT :
				return makePoint(recBuffer, gF, off);
			case AV_MULTIPOINT :
				return makeMPoint(recBuffer, gF, off);
			case AV_ARC :
			case AV_POLYGON :
				return makePoly(recBuffer, gF, type, off);
			case AV_NULL :
				return null;
			default :
				throw new InvalidGeometryException(
					"Type not recognized: " + type);
		}
	}

	/**
	@roseuid 3CA83F7600BD
	*/
	protected static Point makePoint(byte[] b, GeometryFactory gF, int off)
		throws InvalidGeometryException {
		double x = makeDoubleLittleEndian(b, off);
		off += 8;
		double y = makeDoubleLittleEndian(b, off);
		off += 8;

		return gF.createPoint(x, y);
	}

	/**
	@roseuid 3CA83F7600CD
	*/
	protected static MultiPoint makeMPoint(
		byte[] b,
		GeometryFactory gF,
		int off)
		throws InvalidGeometryException {
		off += 32; //skip bounding box part

		int numPoints = makeIntLittleEndian(b, off);
		Point[] points = new Point[numPoints];

		off += 4;
		for (int i = 0; i < numPoints; i++) {
			double x = makeDoubleLittleEndian(b, off);
			off += 8;
			double y = makeDoubleLittleEndian(b, off);
			off += 8;
			points[i] = gF.createPoint(x, y);
		}

		return (MultiPoint) gF.createGeometryCollection(points);
	}

	/**
	@roseuid 3CA83F7600EA
	*/
	protected static Geometry makePoly(
		byte[] b,
		GeometryFactory gF,
		int type,
		int off)
		throws InvalidGeometryException {
		//the boundbox part
		off += 4 * 8;

		int numParts = makeIntLittleEndian(b, off);
		int[] parts = new int[numParts + 1];

		off += 4;
		int numPoints = makeIntLittleEndian(b, off);
		off += 4;
		parts[numParts] = numPoints;
		for (int i = 0; i < numParts; i++) {
			parts[i] = makeIntLittleEndian(b, off);
			off += 4;
		}

		if (type == AV_POLYGON) { // construct a polygon
			LineString[] rings = new LineString[numParts];
			for (int i = 0; i < numParts; i++) {
				int partSize = parts[i + 1] - parts[i];
				double[] xyArray = new double[2 * partSize];
				for (int j = 0; j < partSize * 2; j++) {
					xyArray[j] = makeDoubleLittleEndian(b, off);
					off += 8;
				}
				rings[i] = gF.createLineString(2, xyArray);
			}
			return gF.createPolygon(rings);
		} else if (numParts == 1) { // construct a lineString
			int partSize = parts[1] - parts[0];
			double[] xyArray = new double[2 * partSize];
			for (int j = 0; j < partSize * 2; j++) {
				xyArray[j] = makeDoubleLittleEndian(b, off);
				off += 8;
			}
			return gF.createLineString(2, xyArray);
		} else { // construct a multiLineString
			LineString[] lineStrings = new LineString[numParts];
			for (int i = 0; i < numParts; i++) {
				int partSize = parts[i + 1] - parts[i];
				double[] xyArray = new double[2 * partSize];
				for (int j = 0; j < partSize * 2; j++) {
					xyArray[j] = makeDoubleLittleEndian(b, off);
					off += 8;
				}
				lineStrings[i] = gF.createLineString(2, xyArray);
			}
			return gF.createGeometryCollection(lineStrings);
		}
	}

	/**
	@roseuid 3CA83F7600FB
	*/
	public static void swapBytes(byte[] buf, int offset, int len) {
		byte tmp;
		for (int i = 0; i < len / 2; i++) {
			tmp = buf[offset + i];
			buf[offset + i] = buf[offset + len - 1 - i];
			buf[offset + len - 1 - i] = tmp;
		}
	}

	/**
	@roseuid 3CA83F76010B
	*/
	protected static int makeIntBigEndian(byte[] b, int off) {
		return (int)
			(((((int) b[off + 0]) << 24) & 0xff000000)
				| ((((int) b[off + 1]) << 16) & 0x00ff0000)
				| ((((int) b[off + 2]) << 8) & 0x0000ff00)
				| (((int) b[off + 3]) & 0x000000ff));
	}

	/**
	@roseuid 3CA83F76011A
	*/
	protected static int makeIntLittleEndian(byte[] b, int off) {
		return (int)
			(((((int) b[off + 3]) << 24) & 0xff000000)
				| ((((int) b[off + 2]) << 16) & 0x00ff0000)
				| ((((int) b[off + 1]) << 8) & 0x0000ff00)
				| (((int) b[off + 0]) & 0x000000ff));
	}

	/**
	@roseuid 3CA83F760129
	*/
	protected static double makeDoubleBigEndian(byte[] b, int off) {
		long tmp =
			(((((long) b[off + 0]) << 56) & 0xff00000000000000L)
				| ((((long) b[off + 1]) << 48) & 0x00ff000000000000L)
				| ((((long) b[off + 2]) << 40) & 0x0000ff0000000000L)
				| ((((long) b[off + 3]) << 32) & 0x000000ff00000000L)
				| ((((long) b[off + 4]) << 24) & 0x00000000ff000000L)
				| ((((long) b[off + 5]) << 16) & 0x0000000000ff0000L)
				| ((((long) b[off + 6]) << 8) & 0x000000000000ff00L)
				| (((long) b[off + 7]) & 0x00000000000000ffL));
		return Double.longBitsToDouble(tmp);
	}

	/**
	@roseuid 3CA83F760139
	*/
	protected static double makeDoubleLittleEndian(byte[] b, int off) {
		long tmp =
			(((((long) b[off + 7]) << 56) & 0xff00000000000000L)
				| ((((long) b[off + 6]) << 48) & 0x00ff000000000000L)
				| ((((long) b[off + 5]) << 40) & 0x0000ff0000000000L)
				| ((((long) b[off + 4]) << 32) & 0x000000ff00000000L)
				| ((((long) b[off + 3]) << 24) & 0x00000000ff000000L)
				| ((((long) b[off + 2]) << 16) & 0x0000000000ff0000L)
				| ((((long) b[off + 1]) << 8) & 0x000000000000ff00L)
				| (((long) b[off + 0]) & 0x00000000000000ffL));
		return Double.longBitsToDouble(tmp);
	}

	/**
	@roseuid 3CA83F760148
	*/
	public ShapefileReader(String name) throws IOException {
		type = AV_NULL;
		nRecords = 0;
		mainFIS = idxFIS = null;
		namePrefix = null;
		bufferLen = 512;
		recBuffer = new byte[bufferLen];
		partBufferLen = bufferLen / 8 + 1;
		partBuffer = new double[partBufferLen * 2];

		mainFileHdr = new ShapefileHdr();
		idxFileHdr = new ShapefileHdr();
		cnt = 0;

		openShapefile(name);
	}

	/**
	* open a shapefile with the given name. ready to
	* fetch individual shape records upon success return.
	@roseuid 3CA83F76014A
	*/
	protected void openShapefile(String name) throws IOException {
		String fullName, fullIdxName;
		namePrefix = new String(name);

		if (name.lastIndexOf(".shp") < 0) {
			fullName = new String(name + ".shp");
			namePrefix = new String(name);
		} else {
			fullName = new String(name);
			namePrefix =
				new String(name.substring(0, name.lastIndexOf(".shp")));
		}

		fullIdxName =
			new String(
				fullName.substring(0, fullName.lastIndexOf(".shp")) + ".shx");

		//open .shp file (the main shape file)
		mainFIS = new RandomAccessFile(fullName, "r");
		mainFIS.read(recBuffer, 0, ShapefileHdr.size);
		mainFileHdr.fromBuffer(recBuffer);

		//open .shx file (the index file)
		idxFIS = new RandomAccessFile(fullIdxName, "r");
		idxFIS.read(recBuffer, 0, ShapefileHdr.size);
		idxFileHdr.fromBuffer(recBuffer);

		type = mainFileHdr.shapeType;
		nRecords = (idxFileHdr.fileLength * 2 - 100) / 8;
	}

	/**
	@roseuid 3CA83F760157
	*/
	public void closeShapefile() throws IOException {
		if (mainFIS != null)
			mainFIS.close();
		if (idxFIS != null)
			idxFIS.close();
	}

	/**
	@roseuid 3CA83F760158
	*/
	public byte[] getGeometryBytes(int nth)
		throws IOException, InvalidGeometryException {
		IndexRecord idxRec = new IndexRecord();
		mainRecordOffset(nth, idxRec);
		int byte_off = idxRec.offset;
		int byte_len = idxRec.contentLen + 8; /* 8 is the main rec hdr */

		byte returnBuffer[] = new byte[byte_len];
		mainFIS.seek(byte_off);
		mainFIS.read(returnBuffer, 0, byte_len);

		return returnBuffer;
	}

	/**
	* make an double out of the byte array starting at offset 'off'.
	@roseuid 3CA83F760167
	*/
	protected long idxRecordOffset(int nth) {
		return ShapefileHdr.size + nth * 8;
	}

	/**
	@roseuid 3CA83F760169
	*/
	protected boolean mainRecordOffset(int nth, IndexRecord idxRec) {
		try {
			idxFIS.seek(idxRecordOffset(nth));
		} catch (IOException e) {
			System.err.println("Error: " + e);
			return false;
		}

		try {
			idxRec.offset = idxFIS.readInt() * 2;
		} catch (IOException e) {
			System.err.println("Error: " + e);
			return false;
		}

		try {
			idxRec.contentLen = idxFIS.readInt() * 2;
		} catch (IOException e) {
			System.err.println("Error: " + e);
			return false;
		}

		return true;
	}

	/**
	@roseuid 3CA83F760178
	*/
	public int numRecords() {
		return nRecords;
	}

	/**
	@roseuid 3CA83F760179
	*/
	public static byte[] exportGeometry(Geometry geom, int num)
		throws InvalidGeometryException {
		if (geom.getDimensionality() != 2)
			throw new InvalidGeometryException("not yet supported");

		if (geom instanceof Point)
			return exportPoint((Point) geom, num);
		else if (geom instanceof MultiPoint)
			return exportMultiPoint((MultiPoint) geom, num);
		else if (geom instanceof LineString)
			return exportLineString((LineString) geom, num);
		else if (geom instanceof MultiLineString)
			return exportMultiLineString((MultiLineString) geom, num);
//		else if (geom instanceof Polygon)
//			return exportPolygon((Polygon) geom, num);
		else if (geom instanceof MultiPolygon)
			return exportMultiPolygon((MultiPolygon) geom, num);
		else
			throw new InvalidGeometryException(
				"Geometry type " + geom.getClass() + " not supported");
	}

	/**
	@roseuid 3CA83F760187
	*/
	protected static byte[] exportPoint(Point geom, int num) {
		byte b[] = initRecordHeader(8 + 20, num);
		// record //////////////////////////////////////////////////////////////////
		exportIntLittleEndian(AV_POINT, b, 8 + 0); // point
		exportDoubleLittleEndian(((Point) geom).getX(), b, 8 + 4); // x
		exportDoubleLittleEndian(((Point) geom).getY(), b, 8 + 12); // y

		return b;
	}

	/**
	@roseuid 3CA83F760197
	*/
	protected static byte[] exportMultiPoint(MultiPoint geom, int num) {
		int numPoints = geom.getNumGeometries();

		byte b[] = initRecordHeader(8 + 40 + 16 * numPoints, num);
		// record //////////////////////////////////////////////////////////////////
		exportIntLittleEndian(AV_MULTIPOINT, b, 8 + 0); // MultiPoint
		exportMBB(geom, b, 8 + 4); // Min Bounding Box
		exportIntLittleEndian(numPoints, b, 8 + 36); // number of points

		Geometry gA[] = geom.getGeometryArray(); // array of all points
		for (int i = 0;
			i < numPoints;
			i++) { // for each point in the MultiPoint
			exportDoubleLittleEndian(
				((Point) gA[i]).getX(),
				b,
				8 + 40 + 16 * i);
			// x[i]
			exportDoubleLittleEndian(
				((Point) gA[i]).getY(),
				b,
				8 + 48 + 16 * i);
			// y[i]
		}

		return b;
	}

	/**
	@roseuid 3CA83F7601A6
	*/
	protected static byte[] exportLineString(LineString geom, int num) {
		double coordArray[] = geom.getCoordArray();
		int numPoints = coordArray.length / geom.getDimensionality();

		byte[] b = initRecordHeader(8 + 44 + 1 * 4 + numPoints * 16, num);
		// record //////////////////////////////////////////////////////////////////
		exportIntLittleEndian(AV_ARC, b, 8 + 0); // LineString becomes ARC
		exportMBB(geom, b, 8 + 4); // Min Bounding Box
		exportIntLittleEndian(1, b, 8 + 36);
		// number of parts; a LineString has one part only
		exportIntLittleEndian(numPoints, b, 8 + 40); // number of points

		exportIntLittleEndian(0, b, 8 + 44);
		// index to the first and only part

		for (int i = 0; i < 2 * numPoints; i++) // for each point coordinate
			exportDoubleLittleEndian(coordArray[i], b, 8 + 48 + 8 * i);

		return b;
	}

	/**
	@roseuid 3CA83F7601B5
	*/
	protected static byte[] exportMultiLineString(
		MultiLineString geom,
		int num) {
		LineString lsA[] = (LineString[]) geom.getGeometryArray();
		byte b[] = exportLineStrings(geom, num, lsA);
		exportIntLittleEndian(AV_ARC, b, 8 + 0); // MultiLineString becomes ARC
		return b;
	}

	/**
	@roseuid 3CA83F7601C5
	*/
//	protected static byte[] exportPolygon(
//		oracle.sdoapi.geom.Polygon geom,
//		int num) {
//		LineString lsA[] = (LineString[]) geom.getRingArray();
//		byte b[] = exportLineStrings(geom, num, lsA);
//		exportIntLittleEndian(AV_POLYGON, b, 8 + 0); // Polygon
//		return b;
//	}

	/**
	@roseuid 3CA83F7601D4
	*/
	protected static byte[] exportMultiPolygon(MultiPolygon geom, int num) {
		Polygon polys[] = (Polygon[]) geom.getGeometryArray();
		LineString lsAA[][] = new LineString[polys.length][];
		int numLineStrings = 0;
		for (int poly = 0;
			poly < polys.length;
			poly++) { // get the line strings of all polygons
			lsAA[poly] = (LineString[]) polys[poly].getRingArray();
			numLineStrings += lsAA[poly].length;
		}
		LineString lsA[] = new LineString[numLineStrings];
		for (int poly = 0, lineString = 0;
			poly < polys.length;
			poly++) { // copy them into a flat array
			for (int i = 0; i < lsAA[poly].length; i++)
				lsA[lineString++] = lsAA[poly][i];
		}
		byte b[] = exportLineStrings(geom, num, lsA);
		exportIntLittleEndian(AV_POLYGON, b, 8 + 0); // Polygon
		return b;
	}

	/**
	@roseuid 3CA83F7601E4
	*/
	protected static byte[] exportLineStrings(
		Geometry geom,
		int num,
		LineString lsA[]) { // geom is either MultiLineString or Polygon
		//LineString lsA[] = (LineString[])geom.getRingArray(); // will be done by caller
		int numParts = lsA.length;
		double coordArrays[][] = new double[numParts][];
		int numPoints = 0;

		for (int i = 0; i < numParts; i++) {
			coordArrays[i] = lsA[i].getCoordArray();
			numPoints += coordArrays[i].length / lsA[i].getDimensionality();
		}
		byte[] b =
			initRecordHeader(8 + 44 + numParts * 4 + numPoints * 16, num);
		// record //////////////////////////////////////////////////////////////////
		//exportInt(..., b, 8+0);                   // will be done by caller
		exportMBB(geom, b, 8 + 4); // Min Bounding Box
		exportIntLittleEndian(numParts, b, 8 + 36);
		// number of parts; a LineString has one part only
		exportIntLittleEndian(numPoints, b, 8 + 40); // number of points

		int indexPart = 0;
		for (int part = 0, pos = 0; part < numParts; part++) {
			exportIntLittleEndian(indexPart, b, (8 + 44 + 4 * part));
			// index to the according part
			indexPart += coordArrays[part].length
				/ lsA[part].getDimensionality();
			for (int posInThisPart = 0;
				posInThisPart < coordArrays[part].length;
				posInThisPart++, pos++)
				exportDoubleLittleEndian(
					coordArrays[part][posInThisPart],
					b,
					8 + 44 + 4 * numParts + 8 * pos);
		}

		return b;
	}

	/**
	@roseuid 3CA83F7601F4
	*/
	protected static void exportMBB(Geometry geom, byte b[], int offset) {
		Rect env = geom.getEnvelope(); // Min Bounding Box
		exportDoubleLittleEndian(env.getXMin(), b, offset);
		exportDoubleLittleEndian(env.getYMin(), b, offset + 8);
		exportDoubleLittleEndian(env.getXMax(), b, offset + 16);
		exportDoubleLittleEndian(env.getYMax(), b, offset + 24);
	}

	/**
	@roseuid 3CA83F760204
	*/
	protected static byte[] initRecordHeader(int length, int recordNum) {
		byte b[] = new byte[length];
		exportIntBigEndian(recordNum, b, 0); // record number
		exportIntBigEndian((length - 8) / 2, b, 4);
		// record length in 16 bit words without record header

		return b;
	}

	/**
	@roseuid 3CA83F760214
	*/
	protected static void exportIntBigEndian(int val, byte[] b, int off) {
		b[off + 0] = (byte) ((val & 0xff000000) >> 24);
		b[off + 1] = (byte) ((val & 0x00ff0000) >> 16);
		b[off + 2] = (byte) ((val & 0x0000ff00) >> 8);
		b[off + 3] = (byte) ((val & 0x000000ff));
	}

	/**
	@roseuid 3CA83F760223
	*/
	protected static void exportIntLittleEndian(int val, byte[] b, int off) {
		b[off + 3] = (byte) ((val & 0xff000000) >> 24);
		b[off + 2] = (byte) ((val & 0x00ff0000) >> 16);
		b[off + 1] = (byte) ((val & 0x0000ff00) >> 8);
		b[off + 0] = (byte) ((val & 0x000000ff));
	}

	/**
	@roseuid 3CA83F760234
	*/
	protected static void exportDoubleBigEndian(
		double dVal,
		byte[] b,
		int off) {
		long val = Double.doubleToLongBits(dVal);
		b[off + 0] = (byte) ((val & 0xff00000000000000L) >> 56);
		b[off + 1] = (byte) ((val & 0x00ff000000000000L) >> 48);
		b[off + 2] = (byte) ((val & 0x0000ff0000000000L) >> 40);
		b[off + 3] = (byte) ((val & 0x000000ff00000000L) >> 32);
		b[off + 4] = (byte) ((val & 0x00000000ff000000L) >> 24);
		b[off + 5] = (byte) ((val & 0x0000000000ff0000L) >> 16);
		b[off + 6] = (byte) ((val & 0x000000000000ff00L) >> 8);
		b[off + 7] = (byte) ((val & 0x00000000000000ffL));
	}

	/**
	@roseuid 3CA83F760245
	*/
	protected static void exportDoubleLittleEndian(
		double dVal,
		byte[] b,
		int off) {
		long val = Double.doubleToLongBits(dVal);
		b[off + 7] = (byte) ((val & 0xff00000000000000L) >> 56);
		b[off + 6] = (byte) ((val & 0x00ff000000000000L) >> 48);
		b[off + 5] = (byte) ((val & 0x0000ff0000000000L) >> 40);
		b[off + 4] = (byte) ((val & 0x000000ff00000000L) >> 32);
		b[off + 3] = (byte) ((val & 0x00000000ff000000L) >> 24);
		b[off + 2] = (byte) ((val & 0x0000000000ff0000L) >> 16);
		b[off + 1] = (byte) ((val & 0x000000000000ff00L) >> 8);
		b[off + 0] = (byte) ((val & 0x00000000000000ffL));
	}

	/**
	@roseuid 3CA83F760261
	*/
	public static void createShapefile(
		byte byteArrays[][],
		String shapefileName)
		throws IOException, InvalidGeometryException {
		FileOutputStream idx = new FileOutputStream(shapefileName + ".shx");
		FileOutputStream data = new FileOutputStream(shapefileName + ".shp");

		writeFileHeaders(data, idx, byteArrays);

		byte idxRec[] = new byte[8];
		for (int geomNum = 0, mainOffset = ShapefileHdr.size / 2;
			geomNum < byteArrays.length;
			geomNum++) {
			// write idx record
			exportIntBigEndian(mainOffset, idxRec, 0);
			exportIntBigEndian((byteArrays[geomNum].length - 8) / 2, idxRec, 4);
			mainOffset += byteArrays[geomNum].length / 2;
			idx.write(idxRec);
			// write data record
			exportIntBigEndian(geomNum, byteArrays[geomNum], 0);
			// record number
			data.write(byteArrays[geomNum]);
		}
		data.close();
		idx.close();
	}

	/**
	@roseuid 3CA83F760271
	*/
	protected static void writeFileHeaders(
		FileOutputStream data,
		FileOutputStream idx,
		byte byteArrays[][])
		throws IOException, InvalidGeometryException {
		int fileSize = 0;
		int geomType, fileGeomType = 0;
		double geomXmin, geomYmin, geomXmax, geomYmax;
		double fileXmin = Double.MAX_VALUE,
			fileYmin = Double.MAX_VALUE,
			fileXmax = Double.MIN_VALUE,
			fileYmax = Double.MIN_VALUE;
		for (int geomNum = 0; geomNum < byteArrays.length; geomNum++) {
			fileSize += byteArrays[geomNum].length;
			geomType = makeIntBigEndian(byteArrays[geomNum], 8);
			if (fileGeomType == 0)
				fileGeomType = geomType;
			else if (fileGeomType != geomType)
				throw new InvalidGeometryException();
			switch (geomType) {
				case AV_POINT :
					geomXmin =
						makeDoubleLittleEndian(byteArrays[geomNum], 8 + 4);
					geomYmin =
						makeDoubleLittleEndian(byteArrays[geomNum], 8 + 12);
					if (geomXmin < fileXmin)
						fileXmin = geomXmin;
					if (geomYmin < fileYmin)
						fileYmin = geomYmin;
					if (geomXmin > fileXmax)
						fileXmax = geomXmin;
					if (geomYmin > fileYmax)
						fileYmax = geomYmin;
					break;
				case AV_MULTIPOINT :
				case AV_ARC :
				case AV_POLYGON :
					geomXmin =
						makeDoubleLittleEndian(byteArrays[geomNum], 8 + 4);
					geomYmin =
						makeDoubleLittleEndian(byteArrays[geomNum], 8 + 12);
					geomXmax =
						makeDoubleLittleEndian(byteArrays[geomNum], 8 + 20);
					geomYmax =
						makeDoubleLittleEndian(byteArrays[geomNum], 8 + 28);
					if (geomXmin < fileXmin)
						fileXmin = geomXmin;
					if (geomYmin < fileYmin)
						fileYmin = geomYmin;
					if (geomXmax > fileXmax)
						fileXmax = geomXmax;
					if (geomYmax > fileYmax)
						fileYmax = geomYmax;
					break;
				case AV_NULL :
					break;
				default :
					}
		}
		byte b[] = new byte[100];
		// main file header ////////////////////////////////////////////////////////
		exportIntBigEndian(9994, b, 0); // File code
		exportIntBigEndian(0, b, 4); // unused
		exportIntBigEndian(0, b, 8); // unused
		exportIntBigEndian(0, b, 12); // unused
		exportIntBigEndian(0, b, 16); // unused
		exportIntBigEndian(0, b, 20); // unused
		exportIntBigEndian((fileSize + ShapefileHdr.size) / 2, b, 24);
		// file size incl header
		exportIntBigEndian(1000, b, 28); // version
		exportIntBigEndian(fileGeomType, b, 32); // shape type
		exportDoubleBigEndian(fileXmin, b, 36); // Xmin
		exportDoubleBigEndian(fileYmin, b, 44); // Ymin
		exportDoubleBigEndian(fileXmax, b, 52); // Xmax
		exportDoubleBigEndian(fileYmax, b, 60); // Ymax
		exportDoubleBigEndian(0, b, 68); // Zmin
		exportDoubleBigEndian(0, b, 76); // Zmax
		exportDoubleBigEndian(0, b, 84); // Mmin
		exportDoubleBigEndian(0, b, 92); // Mmax
		data.write(b);

		// index file header ///////////////////////////////////////////////////////
		exportIntBigEndian(
			(8 * byteArrays.length + ShapefileHdr.size) / 2,
			b,
			24);
		// file size incl header
		idx.write(b);
	}
}

class ShapefileHdr {
	public int fileCode;
	public int u1;
	public int u2;
	public int u3;
	public int u4;
	public int u5;
	public int fileLength;
	public int version;
	public int shapeType;
	public double Xmin;
	public double Ymin;
	public double Xmax;
	public double Ymax;
	public double Zmin;
	public double Zmax;
	public double Mmin;
	public double Mmax;
	public static final int size = 100;

	/**
	@roseuid 3CA83F75036B
	*/
	public void fromBuffer(byte[] buf) throws IOException {
		//order sensitive??
		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(buf));

		ShapefileReader.swapBytes(buf, 28, 4);
		ShapefileReader.swapBytes(buf, 32, 4);
		for (int i = 36; i <= 92; i += 4)
			ShapefileReader.swapBytes(buf, i, 8);

		fileCode = dis.readInt();
		for (int i = 0; i < 5; i++)
			dis.readInt();
		fileLength = dis.readInt();
		version = dis.readInt();
		shapeType = dis.readInt();

		Xmin = dis.readDouble();
		Ymin = dis.readDouble();
		Xmax = dis.readDouble();
		Ymax = dis.readDouble();
		Zmin = dis.readDouble();
		Zmax = dis.readDouble();
		Mmin = dis.readDouble();
		Mmax = dis.readDouble();
	}
}
