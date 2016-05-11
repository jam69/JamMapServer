package com.jrsolutions.swing;

import com.jrsolutions.mapserver.gis.IMapper;
import com.jrsolutions.mapserver.gis.Mapa;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class MapComponent extends JPanel {

    private Mapa map;
    private IMapper mapper;

    public MapComponent(IMapper mapper, Mapa map) {
        this.map = map;
        this.mapper = mapper;
        setOpaque(true);
        setBackground(Color.black);

        setFocusable(true);
        addKeyListener(new MapperKeyAdapter(mapper,this));

    }

    @Override
    public void paint(Graphics g) {
        System.out.println("---R-e-p-i-n-t-a---" + mapper.getZoom());
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
//		super.paintComponent(g);
        map.pinta(mapper, (Graphics2D) g);
    }
}
