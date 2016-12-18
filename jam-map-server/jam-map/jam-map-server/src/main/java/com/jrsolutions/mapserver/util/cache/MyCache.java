package com.jrsolutions.mapserver.util.cache;

import java.awt.image.BufferedImage;

public interface MyCache {
	public BufferedImage getImage(String key);

	public void putImage(String key, BufferedImage img);

}
