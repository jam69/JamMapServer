package com.jrsolutions.swing;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class MapComponent extends JPanel {

	private final Properties properties = new Properties();
	private final String url;

	private final CacheConfig cacheConfig;
	private Cache ehCache;
	private TreeMap<String, ImageGetter> cache;

	private int xt = 64222;
	private int yt = 49418;
	private int z = 17;

	private ExecutorService executor;

	public void setProperties(Properties props) {
		properties.putAll(props);
	}

	public MapComponent(String url, Properties properties) {
		setOpaque(true);
		this.url = url;
		this.properties.putAll(properties);
		String cacheP = properties.getProperty("cache");
		if (cacheP != null && cacheP.equalsIgnoreCase("http")) {
			cacheConfig = new CacheConfig();
			cacheConfig.setMaxCacheEntries(1000);
			// cacheConfig.setMaxObjectSizeBytes(8192);
		} else {
			cacheConfig = null;
		}
		if (cacheP != null && cacheP.equalsIgnoreCase("ehcache")) {
			CacheManager cacheManager = new CacheManager();
			ehCache = cacheManager.getCache("tiles");
		}
		if (cacheP != null && cacheP.equalsIgnoreCase("memory")) {
			cache = new TreeMap<String, ImageGetter>();
		}
		int numThreads = Integer.parseInt(properties.getProperty("numThreads", "1"));
		executor = Executors.newFixedThreadPool(numThreads);

		setFocusable(true);
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Pressed:" + e.getKeyCode());
				switch (e.getKeyCode()) {
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
					xt = xt * 2;
					yt = yt * 2;
					z++;
					break;
				case 109: // -
				case 45:
					xt = xt / 2;
					yt = yt / 2;
					z--;
					break;
				case 36: // Home
					xt = 64222;
					yt = 49418;
					z = 17;
					break;
				}
				repaint();
			}

		});

	}

	@Override
	public void paint(Graphics g) {
		System.out.println("---R-e-p-i-n-t-a---");
		super.paintComponent(g);
		int w = getWidth();
		int h = getHeight();
		int ntx = w / 256 + 1;
		int nty = h / 256 + 1;
		// http://localhost:8080/jam-map-server/TileServlet/17/64222/49418.png
		int zoom = z;
		int xtile = xt;
		int ytile = yt;
		System.out.println(">--");
		int dx1 = 0;
		for (int i = 0; i < ntx; i++) {
			int dy1 = 0;
			for (int j = 0; j < nty; j++) {
				String key = zoom + "/" + (xtile + i) + "/" + (ytile + j);
				ImageGetter mg = null;
				if (cache != null) {
					mg = cache.get(key);
				}
				if (ehCache != null) {
					Element element = ehCache.get(key);
					if (element != null) {
						mg = (ImageGetter) element.getObjectValue();
					}
				}
				if (mg != null) {
					mg.repos(g, dx1, dy1);
					System.out.println("-++++++++REPOS++++++++++++++++++++++++++++++++++++++++++++URL=" + key);
				} else {
					System.out.println("---------GET -------------------------------------------- URL=" + key);
					ImageGetter ig = new ImageGetter(key, dx1, dy1);
					if (cache != null) {
						cache.put(key, ig);
					}
					if (ehCache != null) {
						ehCache.put(new Element(key, ig));
					}
					executor.execute(ig);
				}
				dy1 += 256;
			}
			dx1 += 256;
		}
	}

	class ImageGetter implements Runnable {

		private int px;
		private int py;
		private final String key;
		private BufferedImage img = null;

		public ImageGetter(String key, int px, int py) {
			this.px = px;
			this.py = py;
			this.key = key;
		}

		public void repos(Graphics g, int px, int py) {
			this.px = px;
			this.py = py;
			if (img != null) {
				g.drawImage(img, px, py, MapComponent.this);
			}
		}

		public void run() {
			try {
				HttpClient client = getHttpClient();
				String urlExt = url + "/" + key + ".png";
				HttpGet get = new HttpGet(urlExt);
				HttpResponse response = client.execute(get);
				int resultCode = response.getStatusLine().getStatusCode();
				if (resultCode == 200) {
					HttpEntity entity = response.getEntity();
					img = ImageIO.read(entity.getContent());
					getGraphics().drawImage(img, px, py, MapComponent.this);
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

	private HttpClient getHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (properties.getProperty("proxy.host") != null) {
			String proxyServer = properties.getProperty("proxy.host");
			int proxyPort = Integer.parseInt(properties.getProperty("proxy.port", "8080"));
			if (properties.getProperty("user") != null) {
				List authpref = new ArrayList();
				authpref.add(AuthPolicy.BASIC);
				httpClient.getParams().setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
				String proxyUser = properties.getProperty("proxy.user");
				String proxyPassword = properties.getProperty("proxy.password");
				CredentialsProvider credsProvider = httpClient.getCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(proxyServer, proxyPort),
						new UsernamePasswordCredentials(proxyUser, proxyPassword));
				httpClient.setCredentialsProvider(credsProvider);
			}
			HttpHost proxy = new HttpHost(proxyServer, proxyPort,
					(proxyServer.indexOf("https") != 0) ? "http" : "https");
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		if (cacheConfig != null) {
			return new CachingHttpClient(httpClient, cacheConfig);
		} else {
			return httpClient;
		}
	}

	// public static String getTileNumber(final double lat, final double lon,
	// final int zoom) {
	// int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
	// int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat))
	// + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
	// return("" + zoom + "/" + xtile + "/" + ytile);
	// }
	//
	// static double tile2lon(int x, int z) {
	// return x / Math.pow(2.0, z) * 360.0 - 180;
	// }
	//
	// static double tile2lat(int y, int z) {
	// double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
	// return Math.toDegrees(Math.atan(Math.sinh(n)));
	// }
	//
}
