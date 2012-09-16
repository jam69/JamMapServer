package com.jrsolutions.mapserver.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class TileServlet
 */
public class TileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s2=request.getPathInfo();
//		System.out.println("s2="+s2);
		long nano=System.currentTimeMillis();
		
		ImageGen imgGen=new ImageGen();
		BufferedImage image=imgGen.getImage(s2);
	   
//		System.out.println("getImage("+s2+") "+(System.currentTimeMillis()-nano)+" ms");
		response.setContentType("image/png");
		ImageIO.write(image,"png",response.getOutputStream());
		response.getOutputStream().close();
	}

}
