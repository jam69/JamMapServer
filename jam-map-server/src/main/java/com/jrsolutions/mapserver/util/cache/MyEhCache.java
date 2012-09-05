package com.jrsolutions.mapserver.util.cache;

import java.awt.image.BufferedImage;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;


public class MyEhCache implements MyCache{
	
	private final CacheManager  cacheManager  = new CacheManager();
	Ehcache cacheEh;
	
	public MyEhCache(){
		cacheEh=cacheManager.getEhcache("uno");	
	}
	
	public BufferedImage getImage(String key){
		Element element =cacheEh.get(key);
		return element!=null ? (BufferedImage)element.getObjectValue(): null;	
	}

	public void putImage(String key,BufferedImage img){
		cacheEh.put(new Element(key,img));
	}
}