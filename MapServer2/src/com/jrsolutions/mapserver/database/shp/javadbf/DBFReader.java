/**
 * <p>Title: java����DBF�ļ��Ľӿ�</p>
 * <p>Description: ��������ڱ�ʾDBF�ļ��еĶ�����</p>
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
   * ���캯��
   * @param s dbf�ļ����ļ���
   * @throws JDBFException �ļ�û���ҵ�ʱ���׳��쳣
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
   * ʹ��inputstream������DBFReader
   * @param inputstream ������
   * @throws JDBFException
   */
  public DBFReader(InputStream inputstream) throws JDBFException {
    stream = null;
    fields = null;
    nextRecord = null;
    init(inputstream);
  }

  /**
   * ��ʼ��������
   * @param inputstream ���������������ļ���������Ҳ�����Ǳ��������
   * @throws JDBFException �������ļ�IO�쳣ʱ���׳�
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
      //�ж�0x20��0x2a�Ƿ�λ��nextRecord����
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

        //��nextRecord�е��ֽ�Ų��pos��λ��
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
   * ��ȡdbf�ļ����ļ�ͷ
   * @return dbf�ļ���һ���������ֶ���������һ������Ч����
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
   * ��ȡһ���ֶ�
   * @return һ���ֶε����� JDBField,����ֶ�������0X0D��0X00��ͷ����ô�ͷ���һ
   * ��nullֵ
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
    //����ֶζ�����'0D'��ͷ��������Ч�ֶΣ�����һ���յ�JDBField
    //
    if (abyte0[0] == 0X0D || abyte0[0] == 0X00) {
      stream.readFully(abyte0);
      return null;
    }

    //��ȡ�ֶ���
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
   * ��ȡ��Ч�ֶθ���
   * @return �����ֶεĸ���
   */
  public int getFieldCount() {
    return nFieldCount; //fields.length;
  }
  /**
   * ��ȡ��i���ֶΣ�i��0��ʼ��
   * @param i �ֶ����
   * @return JDBField ��i���ֶ�
   * @see JDBField
   */
  public JDBField getField(int i) {
    return fields[i];
  }

  /**
   * �Ƿ�����һ����¼
   * @return ���nextRecord���գ��򷵻���
   */
  public boolean hasNextRecord() {
    return nextRecord != null;
  }
  /**
   * ��ȡdbf�ļ��е���һ����¼
   * @return һ����������
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
   * �ر������ļ�
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
   * ��Ч���ֶθ���
   */
  private int nFieldCount;
}