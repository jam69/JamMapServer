/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: Õâ¸öÀàÓÃÓÚ±íÊ¾DBFÎÄ¼þÖÐµÄ¶ÁÐ´Òì³£</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */

package com.jrsolutions.mapserver.database.shp.javadbf;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <p>Title: java·ÃÎÊDBFÎÄ¼þµÄ½Ó¿Ú</p>
 * <p>Description: Õâ¸öÀàÓÃÓÚ±íÊ¾DBFÎÄ¼þÖÐµÄ¶ÁÐ´Òì³£</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */
public class JDBFException
    extends Exception {
  /**
   * Ê¹ÓÃÒ»¸ö×Ö·û´®À´¹¹ÔìJDBFException
   * @param s Òì³£µÄÄÚÈÝ
   */
  public JDBFException(String s) {
    this(s, null);
  }

  /**
   * Ê¹ÓÃÒ»¸öÒì³£À´¹¹ÔìJDBFException
   * @param throwable ÒªÅ×³öµÄÒì³£
   */
  public JDBFException(Throwable throwable) {
    this(throwable.getMessage(), throwable);
  }

  /**
   * ¹¹Ôìº¯Êý
   * @param s Òì³£µÄÄÚÈÝ
   * @param throwable Ò»ÖÖÒì³£
   */
  public JDBFException(String s, Throwable throwable) {
    super(s);
    detail = throwable;
  }

  /**
   * »ñÈ¡Òì³£µÄ¾ßÌåÄÚÈÝ
   * @return Òì³£JDBFExceptionµÄ¾ßÌåÄÚÈÝ
   */
  public String getMessage() {
    if (detail == null) {
      return super.getMessage();
    }
    else {
      return super.getMessage();
    }
  }

  /**
   * Êä³öÒì³£ÖÁÆÁÄ»
   * @param printstream
   */
  public void printStackTrace(PrintStream printstream) {
    if (detail == null) {
      super.printStackTrace(printstream);
      return;
    }
    PrintStream printstream1 = printstream;
    printstream1.println(this);
    detail.printStackTrace(printstream);
    return;
  }

  /**
   * @see printStackTrace(PrintStream printstream)
   */
  public void printStackTrace() {
    printStackTrace(System.err);
  }
  /**
   * @see printStackTrace(PrintStream printstream)
   * @param printwriter
   */
  public void printStackTrace(PrintWriter printwriter) {
    if (detail == null) {
      super.printStackTrace(printwriter);
      return;
    }
    PrintWriter printwriter1 = printwriter;

    printwriter1.println(this);
    detail.printStackTrace(printwriter);
    return;
  }

  /**
   * Òì³£Ï¸½Ú
   */
  private Throwable detail;
}
