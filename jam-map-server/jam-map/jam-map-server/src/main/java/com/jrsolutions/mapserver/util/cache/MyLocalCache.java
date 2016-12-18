package com.jrsolutions.mapserver.util.cache;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MyLocalCache implements MyCache {

	private final Map<String, BufferedImage> cacheLocal = new HashMap<String, BufferedImage>();

	public MyLocalCache() {
	}

	public BufferedImage getImage(String key) {
		return cacheLocal.get(key);
	}

	public void putImage(String key, BufferedImage img) {
		cacheLocal.put(key, img);
	}
}
