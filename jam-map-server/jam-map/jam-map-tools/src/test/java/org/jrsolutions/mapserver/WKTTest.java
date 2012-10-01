package org.jrsolutions.mapserver;

import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.WKTParser;
import com.jrsolutions.mapserver.geometry.WKTWriter;
import com.jrsolutions.mapserver.geometry.WKTParser.WKTParseException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class WKTTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WKTTest( String testName )
    {
        super( testName );
    }

    public void testApp() {
        test("POINT(15 20)");
        test("LINESTRING(0 0,10 10,20 25,50 60)");
        test("POLYGON((0 0,10 0,10 10,0 10,0 0),(5 5,7 5,7 7,5 7,5 5))");
        test("MULTIPOINT(0 0,20 20,60 60)");
        test("MULTILINESTRING((10 10,20 20),(15 15,30 15))");
        test("MULTIPOLYGON(((0 0,10 0,10 10,0 10,0 0)),((5 5,7 5,7 7,5 7,5 5)))");
        test("GEOMETRYCOLLECTION(POINT(10 10),POINT(30 30),LINESTRING(15 15,20 20))");
    }
    
    private void test(String txt){
    	try {
			WKTParser parser=new WKTParser(txt);
			Geometry g=parser.getGeom();
			WKTWriter writer=new WKTWriter(g);
			String txt2=writer.getString();
			assertEquals(txt,txt2);
			System.out.println(txt);
		} catch (WKTParseException e) {
			// TODO Auto-generated catch block
			assertTrue(false);
		}
    }
    
}
