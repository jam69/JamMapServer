package com.jrsolutions.mapserver.util.cache;

import java.awt.image.BufferedImage;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;


public class MyEhCache implements MyCache{
	
	private static Logger log=Logger.getLogger(MyEhCache.class);
	
	static {
		log.info("Iniciando Static");
	}
	
	private static final CacheManager  cacheManager  = new CacheManager();

	private Ehcache cacheEh;
	
	public MyEhCache(){
		log.info("Arrancando");
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