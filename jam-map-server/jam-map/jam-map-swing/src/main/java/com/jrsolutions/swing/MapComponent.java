package com.jrsolutions.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;

public class MapComponent extends JPanel{

	private final String url;
	private int zoom;
	private double lat;
	private double lon;
	private final CacheConfig cacheConfig;
	
	private final ArrayList<ImageGetter> threads=new ArrayList<ImageGetter>();
	
	public  MapComponent(String url,double lat, double lon, int zoom){
		setOpaque(true);
		this.url=url;
		this.zoom=zoom;
		this.lat=lat;
		this.lon=lon;
		cacheConfig = new CacheConfig();  
		cacheConfig.setMaxCacheEntries(1000);
	//	cacheConfig.setMaxObjectSizeBytes(8192);

		
	}
	
	@Override
	public void paintComponent(Graphics g){
		//super.paintComponent(g);
		for(ImageGetter th:threads){
			th.cancel();
			th.interrupt();
		}
		threads.clear();
		int w=getWidth();
		int h=getHeight();
		int ntx=w/256+1;
		int nty=h/256+1;
		int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
		int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
		//http://localhost:8080/jam-map-server/TileServlet/17/64222/49418.png
		zoom=17;
		xtile=64222;
		ytile=49418;
		int dx1=0;
		for(int i=0;i<ntx;i++){
			int dy1=0;	
			for(int j=0;j<nty;j++){
				new ImageGetter(xtile+i,ytile+j,zoom,g,dx1,dy1).start();
				dy1+=256;
			}
			dx1+=256;
		}
	}
	
	class ImageGetter extends Thread{
	
		private final int x;
		private final int y;
		private final int z;
		private final Graphics g;
		private final int px;
		private final int py;
		private HttpGet  get ;
		public ImageGetter(int x,int y,int z,Graphics g,int px, int py){
			this.x=x;
			this.y=y;
			this.z=z;
			this.g=g;
			this.px=px;
			this.py=py;
			System.out.println(">>"+zoom+"/"+x+"/"+y+" ->"+px+","+py);
		}
	
		public void cancel(){
			get.abort();
		}
		public void run() {
			try {
				HttpClient client = new CachingHttpClient(new DefaultHttpClient(), cacheConfig);
				//HttpClient client = new DefaultHttpClient();
				String urlExt=url+"/"+z+"/"+x+"/"+y+".png";
				System.out.println("URL="+urlExt);
				HttpGet  get = new HttpGet (urlExt);
				HttpResponse response = client.execute(get);
				int resultCode = response.getStatusLine().getStatusCode();
				if(resultCode==200){
					System.out.println("Recibido");
					if(isInterrupted() || get.isAborted())return;
					HttpEntity entity = response.getEntity();
					BufferedImage img=ImageIO.read( entity.getContent());
					if(isInterrupted()||get.isAborted())return;
					getGraphics().drawImage(img,px,py,MapComponent.this);
					System.out.println("<<"+zoom+"/"+x+"/"+y+" ->"+px+","+py);
				}else{
					System.out.println("Recibido "+resultCode);
				}
				

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	
	
	public static String getTileNumber(final double lat, final double lon, final int zoom) {
		   int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
		   int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
		    return("" + zoom + "/" + xtile + "/" + ytile);
		   }
		 
	static double tile2lon(int x, int z) {
	     return x / Math.pow(2.0, z) * 360.0 - 180;
	  }
	 
	  static double tile2lat(int y, int z) {
	    double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
	    return Math.toDegrees(Math.atan(Math.sinh(n)));
	  }
	  
}
