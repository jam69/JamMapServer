/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: Õâ¸öÀàÓÃÓÚ±íÊ¾DBFÎÄ¼þÖÐµÄ×Ö¶Î</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */

package com.jrsolutions.mapserver.database.shp.javadbf;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: Õâ¸öÀàÓÃÓÚ±íÊ¾DBFÎÄ¼þÖÐµÄ×Ö¶Î</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */
public class JDBField {
  /**
   * ¹¹Ôìº¯Êý
   * @param s ×Ö¶ÎÃû
   * @param c ×Ö¶ÎÀàÐÍ£¬Ê¹ÓÃÒ»¸ö×Ö·ûÀ´ÃèÊö
   *   'C' ×Ö·û´®ÀàÐÍ
   *   'N' ÊýÖµÀàÐÍ
   *   'D'
   *   'F' ¸¡µãÀàÐÍ
   * @param i ³¤¶È
   * @param j Ð¡ÊýÎ»Êý
   * @throws JDBFException Èç¹ûÓë×Ö¶ÎÀàÐÍ¶¨Òå²»ºÏ£¬Ôò»áÅ×³öÒì³£
   */
  public JDBField(String s, char c, int i, int j) throws JDBFException {
    if (s.length() > 10) {
      throw new JDBFException(
          "The field name is more than 10 characters long: " + s);
    }
    if (c != 'C' && c != 'N' && c != 'L' && c != 'D' && c != 'F') {
      throw new JDBFException("The field type is not a valid. Got: " + c);
    }
    if (i < 1) {
      throw new JDBFException(
          "The field length should be a positive integer. Got: " + i);
    }
    if (c == 'C' && i >= 254) {
      throw new JDBFException("The field length should be less than 254 characters for character fields. Got: " +
                              i);
    }
    if (c == 'N' && i >= 21) {
      throw new JDBFException(
          "The field length should be less than 21 digits for numeric fields. Got: " +
          i);
    }
    if (c == 'L' && i != 1) {
      throw new JDBFException(
          "The field length should be 1 characater for logical fields. Got: " +
          i);
    }
    if (c == 'D' && i != 8) {
      throw new JDBFException(
          "The field length should be 8 characaters for date fields. Got: " + i);
    }
    if (c == 'F' && i >= 21) {
      throw new JDBFException("The field length should be less than 21 digits for floating point fields. Got: " +
                              i);
    }
    if (j < 0) {
      throw new JDBFException(
          "The field decimal count should not be a negative integer. Got: " + j);
    }
    if ( (c == 'C' || c == 'L' || c == 'D') && j != 0) {
      throw new JDBFException("The field decimal count should be 0 for character, logical, and date fields. Got: " +
                              j);
    }
    if (j > i - 1) {
      throw new JDBFException(
          "The field decimal count should be less than the length - 1. Got: " +
          j);
    }
    else {
      name = s;
      type = c;
      length = i;
      decimalCount = j;
      return;
    }
  }

  /**
   * »ñÈ¡×Ö¶ÎÃû
   * @return ×Ö¶ÎÃû
   */
  public String getName() {
    return name;
  }

  /**
   * »ñÈ¡×Ö¶ÎÀàÐÍ
   * @return ×Ö¶ÎÀàÐÍ
   */
  public char getType() {
    return type;
  }

  /**
   * »ñÈ¡×Ö¶Î³¤¶È
   * @return ×Ö¶Î³¤¶È
   */
  public int getLength() {
    return length;
  }

  /**
   * »ñÈ¡×Ö¶ÎµÄÐ¡ÊýÎ»Êý
   * @return ×Ö¶ÎÐ¡ÊýÎ»Êý
   */
  public int getDecimalCount() {
    return decimalCount;
  }

  /**
   * ½«¶ÔÏó¸ñÊ½»¯ÎªÒ»¸ö×Ö·û´®
   * @param obj Á÷ÖÐµÄ¶ÔÏó
   * @return ÓÃÓÚ±íÊ¾×Ö¶ÎÖµµÄ¶ÔÏó
   * @throws JDBFException µ±¶ÁÈ¡Ê±·¢Éú´íÎóÊ±£¬Å×³öÒì³£
   */
  public String format(Object obj) throws JDBFException {
    if (type == 'N' || type == 'F') {
      if (obj == null) {
        obj = new Double(0.0D);
      }
      if (obj instanceof Number) {
        Number number = (Number) obj;
        StringBuffer stringbuffer = new StringBuffer(getLength());
        for (int i = 0; i < getLength(); i++) {
          stringbuffer.append("#");

        }
        if (getDecimalCount() > 0) {
          stringbuffer.setCharAt(getLength() - getDecimalCount() - 1, '.');
        }
        DecimalFormat decimalformat = new DecimalFormat(stringbuffer.toString());
        String s1 = decimalformat.format(number);
        int k = getLength() - s1.length();
        if (k < 0) {
          throw new JDBFException("Value " + number +
                                  " cannot fit in pattern: '" + stringbuffer +
                                  "'.");
        }
        StringBuffer stringbuffer2 = new StringBuffer(k);
        for (int l = 0; l < k; l++) {
          stringbuffer2.append(" ");

        }
        return stringbuffer2 + s1;
      }
      else {
        throw new JDBFException("Expected a Number, got " + obj.getClass() +
                                ".");
      }
    }
    if (type == 'C') {
      if (obj == null) {
        obj = "";
      }
      if (obj instanceof String) {
        String s = (String) obj;
        if (s.length() > getLength()) {
          throw new JDBFException("'" + obj + "' is longer than " + getLength() +
                                  " characters.");
        }
        StringBuffer stringbuffer1 = new StringBuffer(getLength() - s.length());
        for (int j = 0; j < getLength() - s.length(); j++) {
          stringbuffer1.append(' ');

        }
        return s + stringbuffer1;
      }
      else {
        throw new JDBFException("Expected a String, got " + obj.getClass() +
                                ".");
      }
    }
    if (type == 'L') {
      if (obj == null) {
        obj = new Boolean(false);
      }
      if (obj instanceof Boolean) {
        Boolean boolean1 = (Boolean) obj;
        return boolean1.booleanValue() ? "Y" : "N";
      }
      else {
        throw new JDBFException("Expected a Boolean, got " + obj.getClass() +
                                ".");
      }
    }
    if (type == 'D') {
      if (obj == null) {
        obj = new Date();
      }
      if (obj instanceof Date) {
        Date date = (Date) obj;
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
        return simpledateformat.format(date);
      }
      else {
        throw new JDBFException("Expected a Date, got " + obj.getClass() + ".");
      }
    }
    else {
      throw new JDBFException("Unrecognized JDBFField type: " + type);
    }
  }

  /**
   * ½«Ò»¸ö×Ö·û´®½âÎöÎª¶ÔÓ¦µÄ×Ö¶ÎÖµÀàÐÍ¶ÔÏó
   * @param s ±íÊ¾×Ö¶ÎÖµµÄ×Ö·û´®
   * @return ¶ÔÓ¦µÄ×Ö¶ÎÖµÀàÐÍ¶ÔÏó
   * @throws JDBFException ½âÎö³ö´íÊ±Å×³ö
   */
  public Object parse(String s) throws JDBFException {
    s = s.trim();
    if (type == 'N' || type == 'F') {
      if (s.equals("") || s.equals("***********")) {
        s = "0";
      }
      try {
        if (getDecimalCount() == 0) {
          return new Long(s);
        }
        else {
          return new Double(s);
        }
      }
      catch (NumberFormatException numberformatexception) {
        throw new JDBFException(numberformatexception);
      }
    }
    if (type == 'C') {
      return s;
    }
    if (type == 'L') {
      if (s.equals("Y") || s.equals("y") || s.equals("T") || s.equals("t")) {
        return new Boolean(true);
      }
      if (s.equals("N") || s.equals("n") || s.equals("F") || s.equals("f")) {
        return new Boolean(false);
      }
      else {
        throw new JDBFException("Unrecognized value for logical field: " + s);
      }
    }
    if (type == 'D') {
      SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
      try {
        if ("".equals(s)) {
          return null;
        }
        else {
          return simpledateformat.parse(s);
        }
      }
      catch (ParseException parseexception) {
        throw new JDBFException(parseexception);
      }
    }
    else {
      throw new JDBFException("Unrecognized JDBFField type: " + type);
    }
  }

  /**
   * »ñÈ¡×Ö¶ÎÃû
   * @return ×Ö¶ÎÃû
   */
  public String toString() {
    return name;
  }

  /**
   * ×Ö¶ÎÃû
   */
  private String name;
  /**
   * ×Ö¶ÎÀàÐÍ
   */
  private char type;
  /**
   * ×Ö¶Î³¤¶È
   */
  private int length;
  /**
   * ×Ö¶ÎµÄÐ¡ÊýÎ»Êý
   */
  private int decimalCount;
}
