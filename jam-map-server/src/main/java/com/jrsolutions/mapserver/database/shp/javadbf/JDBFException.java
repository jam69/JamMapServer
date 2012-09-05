/**
 * <p>Title: java����DBF�ļ��Ľӿ�</p>
 * <p>Description: ��������ڱ�ʾDBF�ļ��еĶ�д�쳣</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */

package com.jrsolutions.mapserver.database.shp.javadbf;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <p>Title: java����DBF�ļ��Ľӿ�</p>
 * <p>Description: ��������ڱ�ʾDBF�ļ��еĶ�д�쳣</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ict</p>
 * @author : He Xiong
 * @version 1.0
 */
public class JDBFException
    extends Exception {
  /**
   * ʹ��һ���ַ���������JDBFException
   * @param s �쳣������
   */
  public JDBFException(String s) {
    this(s, null);
  }

  /**
   * ʹ��һ���쳣������JDBFException
   * @param throwable Ҫ�׳����쳣
   */
  public JDBFException(Throwable throwable) {
    this(throwable.getMessage(), throwable);
  }

  /**
   * ���캯��
   * @param s �쳣������
   * @param throwable һ���쳣
   */
  public JDBFException(String s, Throwable throwable) {
    super(s);
    detail = throwable;
  }

  /**
   * ��ȡ�쳣�ľ�������
   * @return �쳣JDBFException�ľ�������
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
   * ����쳣����Ļ
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
   * �쳣ϸ��
   */
  private Throwable detail;
}
