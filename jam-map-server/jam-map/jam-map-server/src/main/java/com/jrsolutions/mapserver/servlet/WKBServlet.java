package com.jrsolutions.mapserver.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jrsolutions.mapserver.geometry.Rect;

/**
 * Servlet implementation class TileServlet
 */
public class WKBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WKBServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long nano = System.currentTimeMillis();
		String xmin = request.getParameter("xmin");
		String xmax = request.getParameter("xmax");
		String ymin = request.getParameter("ymin");
		String ymax = request.getParameter("ymax");
		double x, y, X, Y;
		x = Double.parseDouble(xmin);
		y = Double.parseDouble(ymin);
		X = Double.parseDouble(xmax);
		Y = Double.parseDouble(ymax);
		VectorOutput vOut = new VectorOutput();
		vOut.dumpWKB(new Rect(x, y, X, Y), response.getOutputStream());
		response.setContentType("binary");
		response.getOutputStream().close();
		log("IMAG:" + (System.currentTimeMillis() - nano) + " ms");
	}

}
