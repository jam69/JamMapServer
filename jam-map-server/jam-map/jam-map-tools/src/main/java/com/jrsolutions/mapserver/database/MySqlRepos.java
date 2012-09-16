package com.jrsolutions.mapserver.database;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;

import com.jrsolutions.mapserver.database.datadefinition.FieldDescriptor;
import com.jrsolutions.mapserver.database.datadefinition.TableDescriptor;
import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.WKTParser;
import com.jrsolutions.mapserver.geometry.WKTParser.WKTParseException;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.geometry.WKBReader;
import com.jrsolutions.mapserver.geometry.WKTWriter;


public class MySqlRepos implements DataRepos{

	private static final Logger log=Logger.getLogger("MySQL");
	
	private Connection connection;
	private TableDescriptor description=new TableDescriptor();
	private String geoName="geom";
	
	public MySqlRepos(String url,String tName){
		description.setDatabaseURL(url);
		description.setName(tName);
	}
	
	@Override
	public void create(TableDescriptor description){
		executeSQL(buildCreateCommand(description));
	}
	
	@Override
	public boolean save(Entity entity){
		return executeSQL(buildInsertCommand(entity));
	}

	private boolean executeSQL(String cmd){
		try {
		//	System.out.println("Execute SQL:"+cmd);
			//url=description.getDatabaseURL();
			Connection con=getConnect();
			con.setAutoCommit(true);
			Statement st=con.createStatement();
			st.execute(cmd);
			st.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.severe("SQLERROR:\n\tcmd=["+cmd+"]\n\tCausa:"+e.getMessage()+"\n");
		    return false;
		}
	}
	
	@Override
	public Iterator<Entity> getIterator() {
		System.out.println("Iterator");
		return new SQLIterator("select AsText(geom),NAME from "+description.getName() );
	}

	@Override
	public Iterator<Entity> getIterator(Rect r) {
		log.info("Iterator "+r);
		String cmd="select asText(geom),NAME from "
				+ description.getName()
	            + " where "
				+ "MBRintersects("
				+  geoName
				+ ",GeomFromText('polygon(("
				+ r.getXMin()+" "+r.getYMin()
				+ ","
				+ r.getXMax()+" "+r.getYMin()
				+ ","
				+ r.getXMax()+" "+r.getYMax()
				+ ","
				+ r.getXMin()+" "+r.getYMax()
				+ ","
				+ r.getXMin()+" "+r.getYMin()
				+ "))'))"
				;
		return new SQLIterator(cmd);
	}

	class SQLIterator implements Iterator<Entity>{

		private Connection connection;
		private Statement st;
		private ResultSet rs;
		private ResultSetMetaData meta;
		
		public SQLIterator(String query){
			log.info("QUERY:"+query+Thread.currentThread().getId());
			try {
				connection = DriverManager.getConnection(description.getDatabaseURL());
				st=connection.createStatement(
						java.sql.ResultSet.TYPE_FORWARD_ONLY,
						java.sql.ResultSet.CONCUR_READ_ONLY);
				st.setFetchSize(Integer.MIN_VALUE);
				rs=st.executeQuery(query);
				meta=rs.getMetaData();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		@Override
		public boolean hasNext() {
			try {
				boolean b= rs.next();
				if(b)return true;
				rs.close();
				st.close();
				connection.close();
//				System.out.println(" Close:"+Thread.currentThread().getId());
				return false;
			} catch (SQLException e) {
//				System.out.println("TClose:"+Thread.currentThread().getId()+e.getMessage());
				
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public Entity next() {
			try {
//				System.out.println("NNEXT::::");
				Entity ent=new Entity();
				//for(int i=0;i<meta.getColumnCount();i++){
				for(int i=0;i<2;i++){
					if( i==0){
						
						try {
							WKTParser p=new WKTParser(rs.getString(i+1));
							Geometry g=p.getGeom();
							ent.setGeom(g);
//							System.out.println("             --<"+g.toString());
						} catch (WKTParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						ent.setAttr(meta.getColumnName(i+1), rs.getObject(i+1));
					}
				}
				return ent;
			} catch (SQLException e) {
				log.severe(" Next:"+Thread.currentThread().getId()+" "+e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}

	
	private Connection getConnect() throws SQLException{
		if (connection!=null )
			return connection;
		// localhost root:root mapas
		connection = DriverManager.getConnection(description.getDatabaseURL());
		return connection;
	}
	
	private String getTypeDef(FieldDescriptor fd){
		StringBuffer sb=new StringBuffer();
		switch(fd.getType()){
		case Decimal:
				sb.append("NUMERIC("+fd.getLon()+","+fd.getPrec()+")");break;
		case Real:
			    sb.append("DOUBLE");break;
		case Varchar:
				sb.append("VARCHAR("+fd.getLon()+")");break;
		case Geometry:
				sb.append("GEOMETRY NOT NULL"); break;
		case Logical:
				sb.append("BOOLEAN");break;
		case Date:
				sb.append("DATE");break;
		}
		return sb.toString();
	}
	
	private String buildCreateCommand(TableDescriptor description){
		StringBuffer cmd=new StringBuffer();
		//CREATE TABLE geom (g GEOMETRY NOT NULL, SPATIAL INDEX(g)) ENGINE=MyISAM;

		cmd.append("CREATE TABLE ");
		cmd.append(this.description.getName()); // Usamos el nombre del constructor no el que nos pasan
		cmd.append(" ( ");
		boolean first=true;
		for(FieldDescriptor fd:description.getFields()){
			if(!first){
				cmd.append(",");
			}else{
				first=false;
			}
			cmd.append(fd.getName());
			String tDef=getTypeDef(fd);
			cmd.append(" "+tDef);
		}
		cmd.append(", SPATIAL INDEX(geom) ) ENGINE=MyISAM ;");
		System.out.println("CreateCommand:"+cmd.toString());
		return cmd.toString();
	}
		
	private String buildInsertCommand(Entity entity){
		StringBuffer sb=new StringBuffer();
		sb.append("INSERT "+description.getName()+" set geom= ");
		WKTWriter w=new WKTWriter(entity.getGeom());
		sb.append("GeomFromText(\""+w.getString()+"\")");
		for(int i=0;i<entity.getNumAttr();i++){
			sb.append(",");
			sb.append(entity.getAttrName(i));
			sb.append("=");
			Object obj=entity.getAttrValue(i);
			if(obj instanceof String){
				String a=obj.toString().replace('\"', '\'');
				sb.append("\""+a+"\"");
			}else sb.append( obj.toString() );
		}
//		System.out.println("Insert:"+sb.toString());
		return sb.toString();
	}
		/*
		String cmd="CREATE TABLE name ( campo1 VARCHAR(30), campo2 DATE, campo3 ";
		
		 * SQLTypes:
		 * DECIMAL, NUMERIC, DEC o FIXED  (m,n).. M total of numbers, D decimal numbers
		 * FLOAT, DOUBLE, DOUBLE_PRECISION o REAL
		 * 
		 * CREATE TABLE geom (g GEOMETRY NOT NULL, SPATIAL INDEX(g)) ENGINE=MyISAM;
		 * ALTER TABLE geom ADD SPATIAL INDEX(g);
		 * CREATE SPATIAL INDEX sp_index ON geom (g);
		 * 
		 * Types:
		 * GEOMETRY,
		 * POINT,LINESTRING,POLYGON
		 * MULTIPOINT,MULTILINESTRING,MULTIPOLYGON,GEOMETRYCOLLECTION
		 * 
		 * Funciones: 
		 *  AsBinary() o AsWKB()
		 *  AsText() o AsWKT()
		 *  GeomFromText(txt,srid), PointFromText(), LineFromText(),...
		 *  GeomFromWKB(),PointFromWKB(),LineFromWKB()
		 *  
		 *  Dimension(g)  NumDimensiones
		 *  Envelope(g)   Return a Polygon with the Maximum Bounding Rectangle
		 *  GeometryType(g)
		 *  
		 *  Point:  
		 *     X(p), Y(p)
		 *  LineString:
		 *     EndPoint(ls) returns Point
		 *     GLength(ls)  returns double   
		 * 	   NumPoints(ls)
		 *     PointN(ls,n)  return Point
		 *     StartPoint(ls) return Point
		 *  MultiLineString:
		 *     GLength(mls)
		 *     IsClosed(mls)  return boolean
		 *  Polygon:
		 *     Area(poly) return double
		 *     ExteriorRing(poly) return LineString
		 *     InteriorRingN(poly,n) return LineString
		 *     NumInteriorRings(poly)
		 *  MultiPolygon:
		 *     Area(mp)
		 *     Centroid(mp) return point (perhaps not in the surface)
		 *     PointOnSurface(mp) return point (that is on the surface)         
		 *  GeometryCollection
		 *     GeometryN(gc,n)
		 *     NumGeometries(gc)
		 *     
		 *  Spatial Operators----------------
		 *  Buffer(g,d) 
		 *  ConvexHull(g)
		 *  Difference(g1,g2)
		 *  Intersection(g1,g2)
		 *  SymDifference(g1,g2)
		 *  Union(g1,g2)
		 *  Testing------------
		 *  MBRContains(g1,g2)
		 *  MBRDisjoint(g1,g2)
		 *  MBREqual(g1,g2)
		 *  MBRIntersects(g1,g2)
		 *  MBROverlap(g1,g2)
		 *  MBRTouches(g1,g2)
		 *  MBRWithin(g1,g2)
		 *  Contains(g1,g2)
		 *  Crosses(g1,g2)
		 *  Disjoint(g1,g2)
		 *  Equals(g1,g2)
		 *  Intersects(g1,g2)
		 *  Ovelaps(g1,g2)
		 *  Touches(g1,g2)
		 *  Within(g1,g2)     
    
		 */
	
	
	

	@Override
	public Rect getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}


	public void test() throws SQLException{
	// Do JDBC work to load driver, create connection, statement, etc.
	// Then....
		Connection conn = null;
	
		    conn =
		       DriverManager.getConnection("jdbc:mysql://localhost/test?" +
		                                   "user=monty&password=greatsqldb");
	Statement statement=conn.createStatement();
		    String query = "SELECT geom FROM province";
	ResultSet resultSet = statement.executeQuery(query);
	 
	while(resultSet.next()) {
	 
	    //MySQL geometries are returned in JDBC as binary streams.  The
	    //stream will be null if the record has no geometry.
	    InputStream inputStream = resultSet.getBinaryStream("geom");
	   // Geometry geometry = getGeometryFromInputStream(inputStream);
	    // do something with geometry...
	}
	}
	
	//Here’s the code that does the actual InputStream to Geometry conversion:

	private Geometry getGeometryFromInputStream(InputStream inputStream) throws Exception {
	 
	     Geometry dbGeometry = null;
	 
	     if (inputStream != null) {
	 
	         //convert the stream to a byte[] array
	         //so it can be passed to the WKBReader
	         byte[] buffer = new byte[255];
	 
	         int bytesRead = 0;
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         while ((bytesRead = inputStream.read(buffer)) != -1) {
	             baos.write(buffer, 0, bytesRead);
	         }
	 
	         byte[] geometryAsBytes = baos.toByteArray();
	 
	         if (geometryAsBytes.length < 5) {
	             throw new Exception("Invalid geometry inputStream - less than five bytes");
	         }
	 
	         //first four bytes of the geometry are the SRID,
	         //followed by the actual WKB.  Determine the SRID
	         //here
	         byte[] sridBytes = new byte[4];
	         System.arraycopy(geometryAsBytes, 0, sridBytes, 0, 4);
	         boolean bigEndian = (geometryAsBytes[4] == 0x00);
	 
	         int srid = 0;
	         if (bigEndian) {
	            for (int i = 0; i < sridBytes.length; i++) {
	               srid = (srid << 8) + (sridBytes[i] & 0xff);
	            }
	         } else {
	            for (int i = 0; i < sridBytes.length; i++) {
	              srid += (sridBytes[i] & 0xff) << (8 * i);
	            }
	         }
	 
	         //use the JTS WKBReader for WKB parsing
	         WKBReader wkbReader = new WKBReader();
	 
	         //copy the byte array, removing the first four
	         //SRID bytes
	         byte[] wkb = new byte[geometryAsBytes.length - 4];
	         System.arraycopy(geometryAsBytes, 4, wkb, 0, wkb.length);
	         dbGeometry = wkbReader.read(wkb);
	         dbGeometry.setSRID(srid);
	     }
	 
	     return dbGeometry;
	 }

	@Override
	public TableDescriptor getDescription(){
		// TODO
		return null;
	}
	
	
	@Override
	public void open(){
		// TODO
	}
	@Override
	public void close(){
		// TODO
	}
	

}
