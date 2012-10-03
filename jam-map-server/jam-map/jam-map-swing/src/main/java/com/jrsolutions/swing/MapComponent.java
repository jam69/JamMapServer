package com.jrsolutions.swing;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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

	private final CacheConfig cacheConfig;
	
	private TreeMap<String,ImageGetter> threads=new TreeMap<String,ImageGetter>();
	
	private int xt=64222;
	private int yt=49418;
	private int z=17;
	


	public  MapComponent(String url){
		setOpaque(true);
		this.url=url;
		cacheConfig = new CacheConfig();  
		cacheConfig.setMaxCacheEntries(1000);
	//	cacheConfig.setMaxObjectSizeBytes(8192);
		setFocusable(true);
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Pressed:"+e.getKeyCode());
				switch(e.getKeyCode()){
				case 38: // UP
					 yt--;
					 break;
				case 40: // Down
					yt++;
					break;
				case 37: // Left
					xt++;
					break;
				case 39: // right
					xt--;
					break;
				case 107:
				case 521: // +
					xt=xt*2;
					yt=yt*2;
					z ++;
					break;
				case 109: // -
				case 45:
					xt= xt/2;
					yt= yt/2;
					z --;
					break;
				case 36: // Home
					xt=64222;
					yt=49418;
					z=17;
					break;
				}
				repaint();
			}
			
		});
		
		}
	
	@Override
	public void paint(Graphics g){
		System.out.println("---R-e-p-i-n-t-a---");
		super.paintComponent(g);
//		for(ImageGetter th:threads.values()){
//			if(!th.flag){
//				th.cancel();
//				th.interrupt();
//			//	threads.remove(th.key);
//			}
//			th.flag=false;
//		}
//		threads.clear();
		int w=getWidth();
		int h=getHeight();
		int ntx=w/256+1;
		int nty=h/256+1;
		//http://localhost:8080/jam-map-server/TileServlet/17/64222/49418.png
		int zoom=z;
		int xtile=xt;
		int ytile=yt;
		System.out.println(">--");
		int dx1=0;
		for(int i=0;i<ntx;i++){
			int dy1=0;	
			for(int j=0;j<nty;j++){
				String key=zoom+"/"+(xtile+i)+"/"+(ytile+j);
				ImageGetter mg=threads.get(key);
				if(mg!=null){
					mg.repos(g,dx1,dy1);
					System.out.println("-++++++++REPOS++++++++++++++++++++++++++++++++++++++++++++URL="+key);
				}else{
					System.out.println("---------GET -------------------------------------------- URL="+key);
					ImageGetter ig=new ImageGetter(key,dx1,dy1);
					threads.put(key, ig);
					ig.start();	
				}
				dy1+=256;
			}
			dx1+=256;
		}
	}
	
	class ImageGetter extends Thread{
	
		private int px;
		private int py;
		private final String key;
		private BufferedImage img=null;
		
		public ImageGetter(String key,int px, int py){
			this.px=px;
			this.py=py;
			this.key=key;
		}
		
		public void repos(Graphics g,int px,int py){
			this.px=px;
			this.py=py;
			if(img!=null){
				g.drawImage(img,px,py,MapComponent.this);
			}
		}
	
		public void run() {
			try {
				HttpClient client = new CachingHttpClient(new DefaultHttpClient(), cacheConfig);
//				HttpClient client = new DefaultHttpClient();
				String urlExt=url+"/"+key+".png";
				HttpGet  get = new HttpGet (urlExt);
				HttpResponse response = client.execute(get);
				int resultCode = response.getStatusLine().getStatusCode();
				if(resultCode==200){
					HttpEntity entity = response.getEntity();
					img=ImageIO.read( entity.getContent());
					getGraphics().drawImage(img,px,py,MapComponent.this);
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
	
	
	
//	public static String getTileNumber(final double lat, final double lon, final int zoom) {
//		   int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
//		   int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
//		    return("" + zoom + "/" + xtile + "/" + ytile);
//		   }
//		 
//	static double tile2lon(int x, int z) {
//	     return x / Math.pow(2.0, z) * 360.0 - 180;
//	  }
//	 
//	  static double tile2lat(int y, int z) {
//	    double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
//	    return Math.toDegrees(Math.atan(Math.sinh(n)));
//	  }
//	  
}
