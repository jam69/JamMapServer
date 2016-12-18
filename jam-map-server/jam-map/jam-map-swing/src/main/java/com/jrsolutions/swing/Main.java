package com.jrsolutions.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.gis.IMapper;
import com.jrsolutions.mapserver.gis.Mapa;

public class Main extends JFrame implements ActionListener, ChangeListener {

	private MapComponent mapComponent;
	private Mapa mapa;
	private IMapper mapper;

	public Main() {
		super("JAM-SWING-CLIWNT");
		mapa = new Mapa("algo");
		InputStream is = this.getClass().getResourceAsStream("/startup.js");
		mapa.load("js", is);
		// mapper=new PerspectiveMapper();
		mapper = new TransformMapper();
		// mapper=new Transform3DMapper();
		// mapper=new Mapper();
		mapper.setSize(600, 400);
		// mapper.zoom(new Rect(300000,300000,600000,600000));
		// mapper.zoom(new Rect(439291.27,4473254.64,441291.27,4475254.64));
		// mapper.zoom(new Rect(-4.1133902,40.0071754,-3.2941902,40.8263754));
		// // madrid prov
		mapper.zoom(new Rect(-3.7085902, 40.4135754, -3.6989902, 40.4199754)); // Pta
																				// Sol

	}

	public static void main(String args[]) {
		Main main = new Main();
		main.open();
	}

	public void open() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel p = new JPanel(new BorderLayout());
		setContentPane(p);
		mapComponent = new MapComponent(mapper, mapa);
		JPanel botonera = new JPanel();
		botonera.setLayout(new BoxLayout(botonera, BoxLayout.LINE_AXIS));
		p.add(botonera, BorderLayout.NORTH);
		p.add(mapComponent, BorderLayout.CENTER);

		JButton repinta = new JButton("Refresh");
		repinta.addActionListener(this);
		botonera.add(repinta);

		JSlider slider = new JSlider(-180, 180, 10);
		slider.addChangeListener(this);
		botonera.add(slider);

		mapComponent.setFocusable(true);
		mapComponent.requestFocus();

		setSize(600, 400);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mapComponent.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider s = (JSlider) e.getSource();
		mapper.setRotation(s.getValue());
		mapComponent.repaint();
	}

}
