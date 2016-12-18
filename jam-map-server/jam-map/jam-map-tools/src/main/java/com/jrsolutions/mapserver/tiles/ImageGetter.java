/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.mapserver.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author joseantoniomartin
 */
	class ImageGetter implements Runnable{

        static final Properties properties=new Properties();
            
		private int px;
		private int py;
		private final String key;
        private final ImageObserver obs;
		private BufferedImage img=null;
		private String url;
		
		public ImageGetter(String key,int px, int py,ImageObserver obs){
			this.px=px;
			this.py=py;
			this.key=key;
            this.obs=obs;
		}
		
		public void repos(Graphics g,int px,int py){
			this.px=px;
			this.py=py;
			if(img!=null){
				g.drawImage(img,px,py,obs);
			}
		}
	
		public void run() {
			try {
				DefaultHttpClient client = getHttpClient();
				String urlExt=url+"/"+key+".png";
				HttpGet  get = new HttpGet (urlExt);
				HttpResponse response = client.execute(get);
				int resultCode = response.getStatusLine().getStatusCode();
				if(resultCode==200){
					HttpEntity entity = response.getEntity();
					img=ImageIO.read( entity.getContent());
					img.getGraphics().drawImage(img,px,py,obs);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	
	
    private DefaultHttpClient getHttpClient() {
        DefaultHttpClient httpClient=new DefaultHttpClient();
        if(properties.getProperty("proxy.host")!=null){
            String proxyServer = properties.getProperty("proxy.host");
            int proxyPort = Integer.parseInt(properties.getProperty("proxy.port","8080"));
            if(properties.getProperty("user")!=null){
                List authpref = new ArrayList();
                authpref.add(AuthPolicy.BASIC);
                httpClient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
                String proxyUser = properties.getProperty("proxy.user");
                String proxyPassword = properties.getProperty("proxy.password");
                CredentialsProvider credsProvider = httpClient.getCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(proxyServer, proxyPort), new UsernamePasswordCredentials(proxyUser, proxyPassword));
                httpClient.setCredentialsProvider(credsProvider);
            }
            HttpHost proxy = new HttpHost(proxyServer, proxyPort, (proxyServer.indexOf("https") != 0) ? "http" : "https");
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

      return httpClient;
    }
        
}
