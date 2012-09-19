package com.jrsolutions.mapserver.geometry;

import java.util.Iterator;

public class LineString extends Geometry{

	private Point[] puntos;
	
	public LineString(){
		puntos=new Point[0];
	}
	/**
	 * Only create the array, don't fill the points, so 
	 *  {@link #getNumPoints()} it's not valid
	 *  
	 * @param n Number of points to allocate
	 */
	public LineString(int n){
		puntos=new Point[n];
	}
	
	public void addPoint(Point p){
		Point[] old=puntos;
		puntos=new Point[puntos.length+1];
		System.arraycopy(old,0,puntos,0,old.length);
		puntos[old.length]=p;
		env=null;
	}
	
	public LineString(int nd, double[] xyArray) {
		puntos=new Point[xyArray.length/nd];
		Point pto=null;
		for(int i=0;i<xyArray.length;i++){
			if(i%nd==0){
				pto=new Point(2); 
				puntos[i/nd]=pto;
			}
			pto.setComp(i%nd,xyArray[i]);			
		}
		env=null;
	}
	
	public Point[] getPoints(){
		return puntos;
	}
	
	public Point getPoint(int n){
		if( n<0 || n >= puntos.length){
			return null;
		}
		return puntos[n];
	}
	
	public int getNumPoints(){
		return puntos.length;
	}

	public double[] getCoordArray() {
		double[] res=new double[puntos.length*2];
		for(int i=0;i<puntos.length;i++){
			res[i*2]=puntos[i].getX();
			res[i*2+1]=puntos[i].getY();
		}
		return res;
	}

	public LineString creaParal(double distancia) {

		double x1,y1,x2,y2;

		/*Angulo respecto de la horizontal de los dos segmentos de recta que comparten
		un mismo vertice.
		*/
		double a1,a2;

		/*Beta es el angulo diferencia entre a1 y a2, y Gamma es el resultado de sumar
		a a1 Beta/2, es decir, una direccion intermedia entre a1 y a2*/
		double Beta,Gamma;

		/*M es la longitud Distancia proyectada sobre la direccion Gamma
		*/
		double M;

		/*Cuando un vertice pertenece a dos segmentos, a la hora de obtener
		su correspondiente vertice paralelo, el vertice se obtiene desplazando
		el punto original una distancia M a lo largo de la direccion Gamma, siendo
		Gamma el angulo medio entre a1 y a2. Cuando las direcciones a1 y a2 son iguales
		(pertenecen a una misma recta) M es igual a Distancia. Esto hay que tenerlo en
		cuenta para evitar el error M = Distancia / sen(Beta/2) cuando sen(Beta/2)=0.
		*/

		LineString lineaParal= new LineString(getNumPoints());
		Point ptoAnt = getPoint(0);
		double ox=ptoAnt.getX();
		double oy=ptoAnt.getY();
		Point pto = getPoint(1);
		double x=pto.getX();
		double y=pto.getY();
		double Pi= Math.PI;
		if (ox == x) {
			a1= Pi/2.0;
			if (y < oy) a1= -a1;
		}
		else
			a1= Math.atan2(y-oy, x-ox);

		x2= ox+distancia*(Math.sin(a1));
		y2= oy-distancia*(Math.cos(a1));
		ox= x;
		oy= y;
		lineaParal.addPoint(new Point(x2,y2));
		for (int i=2;i<getNumPoints();i++) {
			pto = getPoint(i);
			x=pto.getX();
			y=pto.getY();
			if (ox == x) {
				a2= Pi/2.0;
				if (y < oy) a2= -a2;
			}
			else
				a2= Math.atan2(y-oy, x-ox);
			Beta= a2-a1-Pi;
			//Si Beta/2=0 (o tiende a serlo)----> M=Distancia (o tiende a serlo)

			double tolerancia = 0.01;//Valor por debajo del cual considerararemos el angulo
			//cero

			if (Math.abs((Beta%(2*Pi)))<tolerancia)
				 M=distancia;
			else
				 M= distancia/(Math.sin(Beta/2.0));

			Gamma= a1+Pi+(Beta/2.0);
			x2= ox+M*(Math.cos(Gamma));
			y2= oy+M*(Math.sin(Gamma));
			a1= a2;
			ox= x;
			oy= y;
			lineaParal.addPoint(new Point(x2,y2));
		}
		pto = getPoint(getNumPoints()-2);
		x=pto.getX();
		y=pto.getY();
		if (x == ox) {
			a2= Pi/2.0;
			if (oy < y)
				a2= -a2;
		}
		else
			a2= Math.atan2(oy-y, ox-x);
		x1= ox+distancia*(Math.sin(a2));
		y1= oy-distancia*(Math.cos(a2));
		lineaParal.addPoint(new Point(x1,y1));
		return lineaParal;
	}
	
	@Override
	public Rect calcEnvelope() {
		env=new Rect();
		for(int i=0;i<puntos.length;i++){
			env.extend(puntos[i]);
		}
		return env;
	}
	
	public double getPerimeter(){
		double longi=0;
		for(int i=0;i <getNumPoints()-1 ;i++){
			longi+=getPoint(i).distance(getPoint(i+1));
		}
		return  longi;
	}

	/**
	 * Returns the area of the Ring.
	 * It's always positive.
	 * @return
	 */
	public double getArea() {
		return Math.abs(getArea2());
	}
	
	/**
	 * Returns the area of the Ring. It can be negative if is CounterClockWise
	 * 
	 * @return
	 */
	public double getArea2() {
		double p1x=getPoint(0).getX();
		double p1y=getPoint(0).getY();
		double p2x,p2y;
		double sumatorio=0;
		for(int i=1;i<getNumPoints();i++)
		{
			p2x=getPoint(i).getX();
			p2y=getPoint(i).getY();
			sumatorio+=(p1x * p2y) - (p1y * p2x);
			p1x=p2x;
			p1y=p2y;
		}
		double superficie = sumatorio / 2;
		return superficie;
	}

	@Override
	public String toString(){
		StringBuffer sb=new StringBuffer("LineString:["+puntos.length+"]->");
		for(int i=0;i<puntos.length;i++){
			sb.append("("+puntos[i].getX()+","+puntos[i].getY()+")");
		}
		return sb.toString();
	}
	
	public Iterator<Point> iterator(){
		return new LineIterator();
	}
	
	class LineIterator implements Iterator<Point>{

		int currentPos;
		
		public boolean hasNext() {
			return currentPos<puntos.length;
		}

		public Point next() {
			return puntos[currentPos++];
		}

		public void remove() {
			throw new IllegalArgumentException();
		}
		
	}
}
