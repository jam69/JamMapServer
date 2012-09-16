package com.jrsolutions.mapserver.geometry;


public class Rect {

	double xmin,xmax,ymin,ymax;
	
	public Rect(){
		xmin=ymin=Double.MAX_VALUE;
		xmax=ymax=-Double.MAX_VALUE;
	}
	public Rect(Rect r){
		this(r.xmin,r.ymin,r.xmax,r.ymax);
	}
	
	public Rect(double xmin,double ymin,double xmax,double ymax){
		if(xmin < xmax){
			this.xmin=xmin;
			this.xmax=xmax;
		}else{
			this.xmin=xmax;
			this.xmax=xmin;
		}
		if(ymin < ymax ){
			this.ymin=ymin;
			this.ymax=ymax;
		}else{
			this.ymin=ymax;
			this.ymax=ymin;
		}
	}
	public void extend(Point p){
		double x=p.getX();
		if(x>xmax)xmax=x;
		if(x<xmin)xmin=x;
		double y=p.getY();
		if(y>ymax)ymax=y;
		if(y<ymin)ymin=y;
	}
	public void extend(Rect e){
		if(e.getXMax()>xmax)xmax=e.getXMax();
		if(e.getXMin()<xmin)xmin=e.getXMin();
		if(e.getYMax()>ymax)ymax=e.getYMax();
		if(e.getYMin()<ymin)ymin=e.getYMin();
	}
	public double getXMin() {
		return xmin;
	}

	public double getYMin() {
		return ymin;
	}

	public double getXMax() {
		return xmax;
	}

	public double getYMax() {
		return ymax;
	}

	public void setXMin(double minX) {
		this.xmin = minX;
	}

	public void setXMax(double maxX) {
		this.xmax = maxX;
	}

	public void setYMin(double minY) {
		this.ymin = minY;
	}

	public void setYMax(double maxY) {
		this.ymax = maxY;
	}

	public String toString(){
		return "["+xmin+","+ymin+" "+xmax+","+ymax+"]";
	}

	public Rect clone(){
		return new Rect(xmin,ymin,xmax,ymax);
	}
		
	public void amplia(double dx,double dy){
		xmin-=dx;
		ymin-=dy;
		xmax+=dx;
		ymax+=dy;
	}
	public void amplia(Rect r){
		if(xmin>r.xmin)xmin=r.xmin;
		if(ymin>r.ymin)ymin=r.ymin;
		if(xmax<r.xmax)xmax=r.xmax;
		if(ymax<r.ymax)ymax=r.ymax;
	}
	static public Rect noSize(){
		return new Rect(Double.MAX_VALUE,Double.MAX_VALUE,Double.MIN_VALUE,Double.MIN_VALUE);
	}
	static public Rect maxSize(){
		return new Rect(Double.MIN_VALUE,Double.MIN_VALUE,Double.MAX_VALUE,Double.MAX_VALUE);
	}
	
	public boolean intersect(Rect envelope) {
		return  
		     xmin<envelope.getXMax() 
		  && xmax>envelope.getXMin()
		  && ymin<envelope.getYMax()
		  && ymax>envelope.getYMin();
		
	}
	public boolean equals(Object r2){
		Rect r=(Rect)r2;
		return  xmin==r.xmin
		     && xmax==r.xmax
		     && ymin==r.ymin
		     && ymax==r.ymax;
	}
	public boolean contains(double cx, double cy) {
		return xmin<cx && xmax>cx && ymin<cy && ymax>cy;
	}

}
