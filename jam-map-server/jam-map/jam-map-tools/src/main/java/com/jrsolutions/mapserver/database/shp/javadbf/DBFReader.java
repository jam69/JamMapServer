/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: Õâ¸öÀàÓÃÓÚ±íÊ¾DBFÎÄ¼þÖÐµÄ¶Á²Ù×÷</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */

package com.jrsolutions.mapserver.database.shp.javadbf;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DBFReader {
  /**
   * ¹¹Ôìº¯Êý
   * @param s dbfÎÄ¼þµÄÎÄ¼þÃû
   * @throws JDBFException ÎÄ¼þÃ»ÓÐÕÒµ½Ê±»áÅ×³öÒì³£
   */
  public DBFReader(String s) throws JDBFException {
    stream = null;
    fields = null;
    nextRecord = null;
    nFieldCount = 0;
    try {
      init(new FileInputStream(s));
    }
    catch (FileNotFoundException filenotfoundexception) {
      throw new JDBFException(filenotfoundexception);
    }
  }
  /**
   * Ê¹ÓÃinputstreamÀ´¹¹ÔìDBFReader
   * @param inputstream ÊäÈëÁ÷
   * @throws JDBFException
   */
  public DBFReader(InputStream inputstream) throws JDBFException {
    stream = null;
    fields = null;
    nextRecord = null;
    init(inputstream);
  }

  /**
   * ³õÊ¼»¯¶Á²Ù×÷
   * @param inputstream ÊäÈëÁ÷£¬¿ÉÒÔÊÇÎÄ¼þÊäÈëÁ÷£¬Ò²¿ÉÒÔÊÇ±ðµÄÊäÈëÁ÷
   * @throws JDBFException µ±·¢ÉúÎÄ¼þIOÒì³£Ê±»áÅ×³ö
   */
  private void init(InputStream inputstream) throws JDBFException {
    try {
      stream = new DataInputStream(inputstream);
      int i = readHeader();
      fields = new JDBField[i];
      int j = 1;
      for (int k = 0; k < i; k++) {
        fields[k] = readFieldHeader();
        if (fields[k] != null) {
          nFieldCount++;
          j += fields[k].getLength();
        }
      }

      /*
      if(stream.read() < 1)
          throw new JDBFException("Unexpected end of file reached.");
      */
      nextRecord = new byte[j];
      try {
        stream.readFully(nextRecord);
      }
      catch (EOFException eofexception) {
        nextRecord = null;
        stream.close();
      }
      //ÅÐ¶Ï0x20»ò0x2aÊÇ·ñÎ»ÓÚnextRecordµ±ÖÐ
      int pos = 0;
      boolean hasBegin = false;
      for (int p = 0; p < j; p++) {
        if (nextRecord[p] == 0X20 || nextRecord[p] == 0X2A) {
          hasBegin = true;
          pos = p;
          break;
        }
      }
      if (pos > 0) {
        byte[] others = new byte[pos];
        stream.readFully(others);

        //½«nextRecordÖÐµÄ×Ö½ÚÅ²¶¯pos¸öÎ»ÖÃ
        for (int p = 0; p < j - pos; p++) {
          nextRecord[p] = nextRecord[p + pos];
        }
        for (int p = 0; p < pos; p++) {
          nextRecord[j - p - 1] = others[pos - p - 1];
        }
      }

    }
    catch (IOException ioexception) {
      throw new JDBFException(ioexception);
    }
  }

  /**
   * ¶ÁÈ¡dbfÎÄ¼þµÄÎÄ¼þÍ·
   * @return dbfÎÄ¼þÖÐÒ»¸ö±íµÄ×î´ó×Ö¶ÎÊý£¬µ«²»Ò»¶¨ÊÇÓÐÐ§¸öÊý
   * @throws IOException
   * @throws JDBFException
   */
  private int readHeader() throws IOException, JDBFException {
    byte abyte0[] = new byte[16];
    try {
      stream.readFully(abyte0);
    }
    catch (EOFException eofexception) {
      throw new JDBFException("Unexpected end of file reached.");
    }
    int i = abyte0[8];
    if (i < 0)
      i += 256;
    i += 256 * abyte0[9];
    i = --i / 32;
    i--;
    try {
      stream.readFully(abyte0);
    }
    catch (EOFException eofexception1) {
      throw new JDBFException("Unexpected end of file reached.");
    }
    return i;
  }

  /**
   * ¶ÁÈ¡Ò»¸ö×Ö¶Î
   * @return Ò»¸ö×Ö¶ÎµÄÃèÊö JDBField,Èç¹û×Ö¶ÎÃèÊöÒÔ0X0D»ò0X00¿ªÍ·£¬ÄÇÃ´¾Í·µ»ØÒ»
   * ¸önullÖµ
   * @see JDBField
   * @throws IOException
   * @throws JDBFException
   */
  private JDBField readFieldHeader() throws IOException, JDBFException {
    byte abyte0[] = new byte[16];
    try {
      stream.readFully(abyte0);
    }
    catch (EOFException eofexception) {
      throw new JDBFException("Unexpected end of file reached.");
    }
    //Èç¹û×Ö¶Î¶¨ÒåÒÔ'0D'¿ªÍ·£¬ÔòÊÇÎÞÐ§×Ö¶Î£¬·µ»ØÒ»¸ö¿ÕµÄJDBField
    //
    if (abyte0[0] == 0X0D || abyte0[0] == 0X00) {
      stream.readFully(abyte0);
      return null;
    }

    //»ñÈ¡×Ö¶ÎÃû
    StringBuffer stringbuffer = new StringBuffer(10);
    int i = 0;
    for (i = 0; i < 10; i++) {
      if (abyte0[i] == 0)
        break;
      //stringbuffer.append( (char) abyte0[i]);
    }
    stringbuffer.append(new String(abyte0, 0, i));

    char c = (char) abyte0[11];
    try {
      stream.readFully(abyte0);
    }
    catch (EOFException eofexception1) {
      throw new JDBFException("Unexpected end of file reached.");
    }

    int j = abyte0[0];
    int k = abyte0[1];
    if (j < 0)
      j += 256;
    if (k < 0)
      k += 256;
    return new JDBField(stringbuffer.toString(), c, j, k);
  }

  /**
   * »ñÈ¡ÓÐÐ§×Ö¶Î¸öÊý
   * @return ±íÖÐ×Ö¶ÎµÄ¸öÊý
   */
  public int getFieldCount() {
    return nFieldCount; //fields.length;
  }
  /**
   * »ñÈ¡µÚi¸ö×Ö¶Î£¬i´Ó0¿ªÊ¼¼Ç
   * @param i ×Ö¶ÎÐòºÅ
   * @return JDBField µÚi¸ö×Ö¶Î
   * @see JDBField
   */
  public JDBField getField(int i) {
    return fields[i];
  }

  /**
   * ÊÇ·ñ»¹ÓÐÏÂÒ»Ìõ¼ÇÂ¼
   * @return Èç¹ûnextRecord²»¿Õ£¬Ôò·µ»ØÕæ
   */
  public boolean hasNextRecord() {
    return nextRecord != null;
  }
  /**
   * ¶ÁÈ¡dbfÎÄ¼þÖÐµÄÏÂÒ»Ìõ¼ÇÂ¼
   * @return Ò»¸ö¶ÔÏóÊý×é
   * @throws JDBFException
   */
  public Object[] nextRecord() throws JDBFException {
    if (!hasNextRecord())
      throw new JDBFException("No more records available.");
    //Object aobj[] = new Object[fields.length];
    Object aobj[] = new Object[nFieldCount];
    int i = 1;
    for (int j = 0; j < aobj.length; j++) {
      int k = fields[j].getLength();
      StringBuffer stringbuffer = new StringBuffer(k);
      stringbuffer.append(new String(nextRecord, i, k));
      aobj[j] = fields[j].parse(stringbuffer.toString());
      i += fields[j].getLength();
    }

    try {
      stream.readFully(nextRecord);
    }
    catch (EOFException eofexception) {
      nextRecord = null;
    }
    catch (IOException ioexception) {
      throw new JDBFException(ioexception);
    }
    return aobj;
  }

  /**
   * ¹Ø±ÕÕû¸öÎÄ¼þ
   * @throws JDBFException
   */
  public void close() throws JDBFException {
    nextRecord = null;
    try {
      stream.close();
    }
    catch (IOException ioexception) {
      throw new JDBFException(ioexception);
    }
  }

  private DataInputStream stream;
  private JDBField fields[];
  private byte nextRecord[];
  /**
   * ÓÐÐ§µÄ×Ö¶Î¸öÊý
   */
  private int nFieldCount;
}