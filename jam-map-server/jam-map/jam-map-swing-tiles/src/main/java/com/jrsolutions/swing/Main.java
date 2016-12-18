package com.jrsolutions.swing;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

public class Main extends JFrame {

	private static final String PROPS_FILE_NAME = "maps.properties";

	private static final String SERVER_URL = "http://localhost:8080/jam-map-server/TileServlet";

	private final Properties properties;

	public Main() {
		super("JAM-SWING-CLIWNT");
		properties = new Properties();
		try {
			for (Object p : System.getProperties().keySet()) {
				System.out.println(" " + p + " " + System.getProperty((String) p));
			}
			properties.load(new FileReader("map.properties"));
		} catch (IOException ex) {
			System.err.println(("No puedo abrire el fichero " + PROPS_FILE_NAME));
		}

	}

	public static void main(String args[]) {
		Main main = new Main();
		if (args.length > 1) {
			try {
				main.properties.load(new FileReader(args[0]));
			} catch (IOException ex) {
				System.err.println(("No puedo abrire el fichero " + args[0]));
			}
		}
		main.open();
	}

	public void open() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		MapComponent mapComponent = new MapComponent(SERVER_URL, properties);
		setContentPane(mapComponent);
		setSize(300, 400);
		setVisible(true);
	}
}