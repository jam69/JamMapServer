
/*------------------------------------------------------------------
 * $RCSFile:$    $Revision: 1.8 $
 * $Date: 2003/05/06 08:54:28 $
 *
 * $Source: /export2/env/ssf/rep/onis/java/src3/core/com/soluzionasf/onis/oniscanvas/Zoomer.java,v $
 *
 *------------------------------------------------------------------*/
package com.jrsolutions.swing;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.gis.IMapper3D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.PerspectiveTransform;



/**
***************************************************************************
 * Mappea... convierte las coordenadas reales en los pixels de la ventana
 *
 *
 ***********************************************************************
*/
public class Transform3DMapper implements IMapper3D {
		
	private int ancho;
	private int alto;
	
	private Rect r;

    private double rotation=0;

	private int zoomLevel;
	
    @Override
	public void setZoomLevel(int z){
		zoomLevel=z;
	}
    @Override
	public int getZoomLevel(){
		return 14;//zoomLevel;
	}

    public void setRotation(double rot){
        this.rotation=rot;
        adjust();
    }

    public double getRotation(){
        return rotation;
    }
	public Transform3DMapper() {
		
	}
	

	/**
         * Indicamos que tama�o tiene nuestra ventana.
         * Suponemos que nuestra ventana es de tama�o (0,0)-(ancho,alto)
         * Es fundamental que estos datos sean correctos.
         *
         * @param ancho Ancho en unidades(pixels) de la ventana.
         * @param alto  Alto  en unidades(pixels) de la ventana.
	*/
    @Override
	public void setSize(int ancho, int alto) {
		this.ancho=ancho;
		this.alto=alto;
//		adaptarCoord();
        if(r!=null){
            adjust();
        }
	}
                public int getWidth(){
            return ancho;
        }
	 public int getHeight(){
            return alto;
        }
	
	/**
         * Le mandamos una nuevas coordenadas de zoom.
         * Las coordenadas se ajustan a la relacion de aspecto de la ventana.
         *
         * @param rec Nuevas coordenadas.
	*/
    @Override
	public void zoom(Rect rec) {
		r=new Rect(rec);
		adaptarCoord();
        adjust();
	}

    @Override
	public Rect getZoom() {
		return new Rect(r); //enviamos una copia para que no lo cambien
	}
	
	
	

	private double coordX(int cx) {
		return  r.getXMin() + (double)(cx*(r.getXMax()-r.getXMin()))/ancho;
	}
	
	/**
         * Devuelve la coordenada Y real, que corresponde a la coordenada dada
         * en las unidades de la ventana.
         * @param cy Posicion Vertical del pixel.
         * @return Coordenada vertical real del pixel.
	@roseuid 3CA83E47009C
	*/
	private double coordY(int cy) {
		return  r.getYMax() - (double)(cy*(r.getYMax()-r.getYMin()))/alto;
	}


    //private final PerspectiveTransform tr =new PerspectiveTransform();
    private final AffineTransform tr =new AffineTransform();
    private PerspectiveTransform pt;

    
    private void adjust(){
        tr.setToIdentity();
//        tr.translate(ancho/2,alto/2);
//        tr.rotate(Math.toRadians(rotation));
//        tr.translate(-ancho/2,-alto/2);
        tr.scale(1,-1);
        tr.translate(0,-alto);
        double a=ancho/(r.getXMax()-r.getXMin());
        double b=alto/ (r.getYMax()-r.getYMin());
        tr.scale(a,b);
        tr.translate(-r.getXMin(), -r.getYMin());
        System.out.println(" tr="+tr);
        PerspectiveTransform tt=new PerspectiveTransform(tr);
        System.out.println(" pt="+tt);
        PerspectiveTransform pr=gira(rotation);
        //tt.preConcatenate(pr);
        System.out.println(" pt2="+tt);
        pt=pr;
    }

    public int[] pos(double cx, double cy){
   //     System.out.print("Entra:("+cx+","+cy+")");
       
        Point2D p1=new Point2D.Double(cx,cy);
        Point2D p2=tr.transform(p1,null);
        
       // System.out.println("PT:"+pt);

    //    System.out.println("Point2D:"+p2);
        Point2D p3=pt.transform(p2,null);
        
        
//        double[][]m=pt.getMatrix((double[][])null);
//        double x=m[0][0]*cx+m[0][1]*cy+m[0][2];
//        double y=m[1][0]*cx+m[1][1]*cy+m[1][2];
//        double w=m[2][0]*cx+m[2][1]*cy+m[2][2];
//        double xx=x/w;
//        double yy=y/w;
        
        
            int[] ret=new int[2];
        ret[0]=(int)p3.getX();
        ret[1]=(int)p3.getY();    
     //    System.out.println("Point2D--:"+p3);
     //   System.out.println(" Sale:("+ret[0]+","+ret[1]+")");
        return ret;
    }

    PerspectiveTransform gira(double rotation){
        double a=Math.toRadians(rotation);
//        PerspectiveTransform pt=new PerspectiveTransform(
//                1.,  0.,  0.,
//                0., Math.cos(a),  -Math.sin(a),
//                0., Math.sin(a),  Math.cos(a)
//        );
        pt=PerspectiveTransform.getQuadToQuad(
                0, alto, 
                0, 0, 
                ancho, 0, 
                ancho, alto, 
                
                0, alto, 
                80, 100, 
                ancho-80, 100,
                ancho,alto
                );
        System.out.println(" gira="+pt);
        test(0,0);
        test(0,alto);
        test(ancho,alto);
        test(ancho,0);
    
            return pt;
    }
    
    private void test(int x,int y){
        Point2D k=new Point2D.Double(x,y);
        System.out.println("("+x+","+y+")->("+pt.transform(k,null));
    }
    public int[] pos(double X, double Y,double Z){
       return pos(X,Y);
	}

      @Override
	public int posX(double X,double Y){
		return pos(X,Y)[0];
	}
      @Override
    public int posY(double X, double Y)
    {
        return pos(X,Y)[1];
    }

    @Override
    public int posY(double X,double Y,double Z){
		return pos(X,Y)[1];
	}
      @Override
    public int posX(double X, double Y, double Z)
    {
    return pos(X,Y)[0];
    }

    
//    @Override
//	public Shape mapLine(LineString line){
//		GeneralPath res=new GeneralPath(GeneralPath.WIND_EVEN_ODD,line.getNumPoints());
//		Point p=line.getPoint(0);
//		// res.moveTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//		res.moveTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//		for(int i=1;i<line.getNumPoints();i++){
//			p=line.getPoint(i);
//			// res.lineTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//			res.lineTo( posX(p.getX(),p.getY()),posY(p.getX(),p.getY()));
//		}
//		return res;
//	}
	

	/**
     *  Metodo que adapta las coordenadas para que se mantenga la misma
     *  escala en horizontal y en vertical Y que este dentro del limite
	 *  de zoom Original.
   	 */
	private void adaptarCoord() {
		//Redimensiona las coordenadas xmin,xmax,ymin,ymax para que
		//tengan la relacion de aspecto de la ventana de visualizacion.
		double rmx,rmy;
        final double tamx = r.getXMax()-r.getXMin();
        final double tamy = r.getYMax()-r.getYMin();
        if ((tamx/ancho) > (tamy/alto)) {
			rmx=(tamx/2.0);
			rmy=(alto*tamx/2.0)/ancho;
		}
		else{
			rmy=(tamy/2.0);
			rmx=(ancho*tamy/2.0)/alto;
		}

		double xCentroR=(r.getXMin()+r.getXMax())/2.0;
		double yCentroR=(r.getYMin()+r.getYMax())/2.0;
		r.setXMin(xCentroR-rmx);
		r.setXMax(xCentroR+rmx);
		r.setYMin(yCentroR-rmy);
		r.setYMax(yCentroR+rmy);
	}

//    @Override
//	public void drawLine(double x, double y, double x2, double y2, Graphics g) {
//		g.drawLine(posX(x,y),posY(x,y),posX(x2,y2),posY(x2,y2));
//	}
//
//    int SX=600;
//    int SY=400;
//    
//    Point2D.Float corPix(int x0, int y0, int x1, int y1, int x2, int y2, int x3,
//int y3, int x, int y ) {
//  
//  return intersectLines(
//    ((SY-y)*x0 + (y)*x3)/SY, ((SY-y)*y0 + y*y3)/SY,
//    ((SY-y)*x1 + (y)*x2)/SY, ((SY-y)*y1 + y*y2)/SY,
//    ((SX-x)*x0 + (x)*x1)/SX, ((SX-x)*y0 + x*y1)/SX,
//    ((SX-x)*x3 + (x)*x2)/SX, ((SX-x)*y3 + x*y2)/SX);
//}
//
//int det(int a, int b, int c, int d) {
//  return a*d-b*c;
//}
//
//Point2D.Float intersectLines( int x1, int y1, int x2, int y2,
//int x3, int y3, int x4, int y4) {
//  int d = det(x1-x2,y1-y2,x3-x4,y3-y4);
//  
//  if (d==0)
//    d = 1;
//  Point2D p;
//  int x = det(det(x1,y1,x2,y2),x1-x2,det(x3,y3,x4,y4),x3-x4)/d;
//  int y = det(det(x1,y1,x2,y2),y1-y2,det(x3,y3,x4,y4),y3-y4)/d;
//  return new Point2D.Float(x,y);
//}

//   private void evaluatePixel()
//    {
//        const float2 ofst    = float2(0.0, 0.0);
//        const float2 imgSize = float2(2050.0, 1700.0); // image size
//        const float M = 2.2;
//        const float MDZ = 1.0 + 2.0 / (M - 1.0);
//        const float H = 1.0 + MDZ;
//
//        float2 c2D = float2(2.0, - 2.0) * (outCoord() / imgSize) + float2(- 1.0, 1.0);
//
//        float dz = 1.0 + (1.0 + c2D.y) / (M - c2D.y);
//
//        float2 c3D = float2(c2D.x, c2D.y + 1.0) * float2(dz, dz);
//        c3D.x /= 1.7 * MDZ;
//        c3D.y = c3D.y / H - 0.5;
//
//        if(outCoord().x < imgSize.x && outCoord().y < imgSize.y){
//            dst = sampleLinear(src, (c3D + float2(0.5, 0.5)) * imgSize + ofst);
//        } else{
//            dst.rgb = float3(0.0, 0.0, 0.0);
//            dst.a = 0.0;
//        }
//
//    }
//    private void tra(Graphics g){
//          int h = 600;//frameA.getHeight();
//		int w = 400;//frameB.getWidth();
//
//			g.setColor(Color.LIGHTBLUE);
//			g.fillRect(0,0,w,h);
//
//		Point3D topLeftA3D, topRightA3D, bottomLeftA3D, bottomRightA3D;
//		Point3D topLeftB3D, topRightB3D, bottomLeftB3D, bottomRightB3D;
//		BasicProjection p = new BasicProjection( w, h );
//		double t = progress + 4f/8f;
//		double k = Math.PI/2.0;
//        double centerY = h/2.0;
//			double z = Point2D.distance(centerY, centerY, 0, 0);
//			double j = -z*Math.sin(2*Math.PI/8);
//			topLeftA3D = new Point3D.Double( 0, centerY - z*Math.cos(k*t), z*Math.sin(k*t)+j );
//			bottomLeftA3D = new Point3D.Double( 0, centerY - z*Math.cos(k*t+k), z*Math.sin(k*t+k)+j );
//			topRightA3D = new Point3D.Double( w, centerY - z*Math.cos(k*t), z*Math.sin(k*t)+j );
//			bottomRightA3D = new Point3D.Double( w, centerY - z*Math.cos(k*t+k), z*Math.sin(k*t+k)+j );
//			topLeftB3D = new Point3D.Double( 0, centerY - z*Math.cos(k*t-k), z*Math.sin(k*t-k)+j );
//			bottomLeftB3D = new Point3D.Double( 0, centerY - z*Math.cos(k*t), z*Math.sin(k*t)+j );
//			topRightB3D = new Point3D.Double( w, centerY - z*Math.cos(k*t-k), z*Math.sin(k*t-k)+j );
//			bottomRightB3D = new Point3D.Double( w, centerY - z*Math.cos(k*t), z*Math.sin(k*t)+j );
//
//     			g3.setPaint(shadowA);
//				g3.fill(getBounds(topLeftA, topRightA, bottomLeftA, bottomRightA));
//			}


  



}
