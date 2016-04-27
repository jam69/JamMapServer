package com.jrsolutions.swing;

import com.jrsolutions.mapserver.database.CollectionRepos;
import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.geometry.LineString;
import com.jrsolutions.mapserver.geometry.Point;
import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.render.GeomLayer;
import com.jrsolutions.mapserver.render.IMapper3D;
import com.jrsolutions.mapserver.render.Mapa;
import com.jrsolutions.mapserver.render.PaintFilter;
import com.jrsolutions.mapserver.render.Simbology;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class Main extends JFrame implements ActionListener,ChangeListener{
	

	
	private MapComponent mapComponent;
	private Mapa mapa;
        private IMapper3D mapper;
	
	public Main(){
		super("JAM-SWING-CLIWNT");
        mapa=new Mapa("algo");
        //mapper=new PerspectiveMapper();
        mapper=new TransformMapper();
        mapper.setSize(600,400 );
       // mapper.zoom(new Rect(300000,300000,600000,600000));
       mapper.zoom(new Rect(439291.27,4473254.64,441291.27,4475254.64));
       mapper.zoom(new Rect(40.4166754,-3.7038902,40.4168754,-3.7036902));
        
       CollectionRepos repos=new CollectionRepos();
        GeomLayer layer=new GeomLayer(repos);
        Simbology simb=new Simbology();
        simb.addLineColor(Color.green.getRGB());
        layer.addFilter(new PaintFilter(null,simb));
        mapa.add(layer);
        LineString line=new LineString();
        line.addPoint(new Point(400000,400000));
        line.addPoint(new Point(400000,500000));
        line.addPoint(new Point(500000,500000));
        line.addPoint(new Point(500000,400000));
        line.addPoint(new Point(400000,400000));
        line.addPoint(new Point(500000,500000));
        Entity e=new Entity();
        e.setGeom(line);
        repos.add(e);

        LineString line2=new LineString();
        line2.addPoint(new Point(410000,410000));
        line2.addPoint(new Point(410000,490000));
        line2.addPoint(new Point(490000,490000));
        line2.addPoint(new Point(490000,410000));
        line2.addPoint(new Point(410000,410000));
        line2.addPoint(new Point(490000,490000));
        Entity e2=new Entity();
        e2.setGeom(line2);
        repos.add(e2);
	}
	
	public static void main(String args[]){
		Main main=new Main();
		main.open();
	}
	
	public void open(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel p=new JPanel(new BorderLayout());
		setContentPane(p);
		mapComponent=new MapComponent(mapper,mapa);
         JPanel botonera=new JPanel();
		botonera.setLayout(new BoxLayout(botonera,BoxLayout.LINE_AXIS));
		p.add(botonera,BorderLayout.NORTH);
		p.add(mapComponent,BorderLayout.CENTER);
		
		
	
		JButton repinta=new JButton("Refresh");
		repinta.addActionListener(this);
		botonera.add(repinta);

        JSlider slider=new JSlider(-180, 180, 10);
        slider.addChangeListener(this);
        botonera.add(slider);
		
		mapComponent.setFocusable(true);
		mapComponent.requestFocus();
		
		setSize(600,400);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        mapComponent.repaint();
	}

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSlider s=(JSlider)e.getSource();
        mapper.setRotation(s.getValue());
        mapComponent.repaint();
    }

}