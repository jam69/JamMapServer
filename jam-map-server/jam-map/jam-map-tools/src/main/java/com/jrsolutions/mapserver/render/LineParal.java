package com.jrsolutions.mapserver.render;

import java.awt.Graphics2D;
import java.awt.Shape;

import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.gis.IMapper;
import com.jrsolutions.mapserver.gis.MapperUtils;

public class LineParal extends LineSimbology{

	private final LineSimbology lineSimb;
	private final float d;
	
	public LineParal(float d,LineSimbology lineSimb){
		this.lineSimb=lineSimb;
		this.d=d;
	}
	
	@Override
	public void paint(IMapper mapper, Graphics2D g, LineString line, Entity ent,boolean closed) {
	
		Shape r=MapperUtils.mapLine(mapper,line);
		 
//		LineString paral=line.creaParal(d); // TODO
	    lineSimb.paint(mapper, g, line, ent,closed);
//	    
	}
	
//	public GeneralPath creaParal(GeneralPath r,double distancia) {
//
//		double x1,y1,x2,y2;
//
//		/*Angulo respecto de la horizontal de los dos segmentos de recta que comparten
//		un mismo vertice.
//		*/
//		double a1,a2;
//
//		/*Beta es el angulo diferencia entre a1 y a2, y Gamma es el resultado de sumar
//		a a1 Beta/2, es decir, una direccion intermedia entre a1 y a2*/
//		double Beta,Gamma;
//
//		/*M es la longitud Distancia proyectada sobre la direccion Gamma
//		*/
//		double M;
//
//		/*Cuando un vertice pertenece a dos segmentos, a la hora de obtener
//		su correspondiente vertice paralelo, el vertice se obtiene desplazando
//		el punto original una distancia M a lo largo de la direccion Gamma, siendo
//		Gamma el angulo medio entre a1 y a2. Cuando las direcciones a1 y a2 son iguales
//		(pertenecen a una misma recta) M es igual a Distancia. Esto hay que tenerlo en
//		cuenta para evitar el error M = Distancia / sen(Beta/2) cuando sen(Beta/2)=0.
//		*/
//
//		PathIterator pit=r.getPathIterator(null);
//		//LineString lineaParal= new LineString(getNumPoints());
//		LineString lineaParal= new LineString();
//		Point ptoAnt = r.getPoint(0);
//		double ox=ptoAnt.getX();
//		double oy=ptoAnt.getY();
//		Point pto = getPoint(1);
//		double x=pto.getX();
//		double y=pto.getY();
//		double Pi= Math.PI;
//		if (ox == x) {
//			a1= Pi/2.0;
//			if (y < oy) a1= -a1;
//		}
//		else
//			a1= Math.atan2(y-oy, x-ox);
//
//		x2= ox+distancia*(Math.sin(a1));
//		y2= oy-distancia*(Math.cos(a1));
//		ox= x;
//		oy= y;
//		lineaParal.addPoint(new Point(x2,y2));
//		for (int i=2;i<getNumPoints();i++) {
//			pto = getPoint(i);
//			x=pto.getX();
//			y=pto.getY();
//			if (ox == x) {
//				a2= Pi/2.0;
//				if (y < oy) a2= -a2;
//			}
//			else
//				a2= Math.atan2(y-oy, x-ox);
//			Beta= a2-a1-Pi;
//			//Si Beta/2=0 (o tiende a serlo)----> M=Distancia (o tiende a serlo)
//
//			double tolerancia = 0.01;//Valor por debajo del cual considerararemos el angulo
//			//cero
//
//			if (Math.abs((Beta%(2*Pi)))<tolerancia)
//				 M=distancia;
//			else
//				 M= distancia/(Math.sin(Beta/2.0));
//
//			Gamma= a1+Pi+(Beta/2.0);
//			x2= ox+M*(Math.cos(Gamma));
//			y2= oy+M*(Math.sin(Gamma));
//			a1= a2;
//			ox= x;
//			oy= y;
//			lineaParal.addPoint(new Point(x2,y2));
//		}
//		pto = getPoint(getNumPoints()-2);
//		x=pto.getX();
//		y=pto.getY();
//		if (x == ox) {
//			a2= Pi/2.0;
//			if (oy < y)
//				a2= -a2;
//		}else{
//			a2= Math.atan2(oy-y, ox-x);
//		}
//		x1= ox+distancia*(Math.sin(a2));
//		y1= oy-distancia*(Math.cos(a2));
//		lineaParal.addPoint(new Point(x1,y1));
//		
//		System.out.println("LINEA:"+this);
//		System.out.println("PARAL:"+lineaParal);
//		
//		return lineaParal;
//	}


}

