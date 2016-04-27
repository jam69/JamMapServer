/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: ²âÊÔDBFÎÄ¼þµÄ¶ÁÐ´</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */

package com.jrsolutions.mapserver.database.shp.javadbf;

import java.util.Date;

public class TestWrite
{

    public TestWrite()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        JDBField[] fields = {
            new JDBField("ID", 'C', 8, 0),
            new JDBField("Name", 'C', 32, 0),
            new JDBField("TestN", 'N', 20, 0), //µÚÈý¸ö²ÎÊýÖµÒ»¶¨²»´óÓÚ20
            new JDBField("TestF", 'F', 20, 6), //FÀàÐÍÓëNÀàÐÍÍ¬,ÇÒµÚËÄ¸ö²ÎÊýÖµÓÐÐ¡ÊýÎ»Êý£¬·ñÔò»á½Ø¶Ì
            new JDBField("TestD", 'D', 8, 0)
        };
        //DBFReader dbfreader = new DBFReader("E:\\hexiong\\work\\project\\book2.dbf");
        DBFWriter dbfwriter = new DBFWriter("E:\\hexiong\\work\\project\\testwrite.dbf", fields);

        Object[][] records = {
            {"1", "hexiong", new Integer(500), new Double(500.123), new Date() },
            {"2", "hefang", new Integer(600), new Double(600.234), new Date() },
            {"3", "heqiang", new Integer(700), new Double(700.456), new Date() }
        };

        for (int i=0; i<records.length; i++){
          dbfwriter.addRecord(records[i]);
        }
        dbfwriter.close();
    }
}
