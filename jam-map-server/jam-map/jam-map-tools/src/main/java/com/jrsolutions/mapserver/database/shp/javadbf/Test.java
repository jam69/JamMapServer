/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: ²âÊÔDBFÎÄ¼þµÄ¶ÁÐ´</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */

package com.jrsolutions.mapserver.database.shp.javadbf;

/*
FIPS_CNTRY
GMI_CNTRY
ISO_2DIGIT
ISO_3DIGIT
CNTRY_NAME
LONG_NAME
SOVEREIGN
POP_CNTRY
CURR_TYPE
CURR_CODE
LANDLOCKED
SQKM
SQMI
COLOR_MAP
*/

public class Test
{

    public Test()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        //DBFReader dbfreader = new DBFReader((new URL("http://www.svcon.com/us48st.dbf")).openStream());
        //DBFReader dbfreader = new DBFReader("F:\\work\\book2.dbf");
        DBFReader dbfreader = new DBFReader("E:\\hexiong\\work\\project\\tablemeta.dbf");
        //DBFReader dbfreader = new DBFReader("E:\\hexiongshare\\test.dbf");
        int i;
        for (i=0; i<dbfreader.getFieldCount(); i++) {
          System.out.print(dbfreader.getField(i).getName()+"  ");
        }
        System.out.print("\n");
        for(i = 0; dbfreader.hasNextRecord(); i++)
        {
            Object aobj[] = dbfreader.nextRecord();
            for (int j=0; j<aobj.length; j++)
              System.out.print(aobj[j]+"  |  ");
            System.out.print("\n");
        }

        System.out.println("Total Count: " + i);
    }
}

