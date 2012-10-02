package com.jrsolutions.swing;

import javax.swing.JFrame;



public class Main extends JFrame{
	
	private final String SERVER_URL="http://localhost:8080/jam-map-server/TileServlet";
	
	public Main(){
		super("JAM-SWING-CLIWNT");
	}
	
	public static void main(String args[]){
		Main main=new Main();
		main.open();
	}
	
	public void open(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		MapComponent mapComponet=new MapComponent(SERVER_URL,-3.6,40.0,16);
		setContentPane(mapComponet);
		setSize(300,400);
		setVisible(true);
	}
}