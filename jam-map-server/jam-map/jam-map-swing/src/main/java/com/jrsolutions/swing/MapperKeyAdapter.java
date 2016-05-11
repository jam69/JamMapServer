/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrsolutions.swing;

import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.gis.IMapper;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;

/**
 *
 * @author joseantoniomartin
 */
public class MapperKeyAdapter extends KeyAdapter {

    private final IMapper mapper;
    private final JComponent comp;
    private final Rect origin;

    public MapperKeyAdapter(IMapper mapper,JComponent comp) {
        this.mapper = mapper;
        this.comp=comp;
        origin = mapper.getZoom();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Pressed:" + e.getKeyCode());
        Rect r = mapper.getZoom();
        switch (e.getKeyCode()) {
            case 38: // UP 
            {
                System.out.println("Key Up:" + KeyEvent.VK_UP);
                System.out.println("Key Up:" + KeyEvent.VK_KP_UP);
                double dy = (r.getYMax() - r.getYMin()) / 2;
                r.setYMin(r.getYMin() + dy);
                r.setYMax(r.getYMax() + dy);
            }
            break;
            case 40: // Down
            {
                System.out.println("Key down:" + KeyEvent.VK_DOWN);
                System.out.println("Key down:" + KeyEvent.VK_KP_DOWN);
                double dy = (r.getYMax() - r.getYMin()) / 2;
                r.setYMin(r.getYMin() - dy);
                r.setYMax(r.getYMax() - dy);
            }
            break;
            case 37: // Left
            {
                System.out.println("Key left:" + KeyEvent.VK_LEFT);
                System.out.println("Key left:" + KeyEvent.VK_KP_LEFT);
                double dx = (r.getXMax() - r.getXMin()) / 2;
                r.setXMin(r.getXMin() - dx);
                r.setXMax(r.getXMax() - dx);
            }
            break;
            case 39: // right
            {
                System.out.println("Key Right:" + KeyEvent.VK_RIGHT);
                System.out.println("Key Right:" + KeyEvent.VK_KP_RIGHT);
                double dx = (r.getXMax() - r.getXMin()) / 2;
                r.setXMin(r.getXMin() + dx);
                r.setXMax(r.getXMax() + dx);
            }
            break;
            case 93:
            case 107:
            case 521: // +
            {
                System.out.println("Key +:" + KeyEvent.VK_PLUS);
                System.out.println("Key +:" + KeyEvent.VK_ADD);
                double dx = (r.getXMax() - r.getXMin()) / 2;
                r.setXMin(r.getXMin() - dx);
                r.setXMax(r.getXMax() + dx);
                double dy = (r.getYMax() - r.getYMin()) / 2;
                r.setYMin(r.getYMin() - dy);
                r.setYMax(r.getYMax() + dy);

            }
            break;
            case 47:
            case 109: // -
            case 45: {
                System.out.println("Key -:" + KeyEvent.VK_MINUS);
                System.out.println("Key -:" + KeyEvent.VK_SUBTRACT);
                double dx = (r.getXMax() - r.getXMin()) / 4;
                r.setXMin(r.getXMin() + dx);
                r.setXMax(r.getXMax() - dx);
                double dy = (r.getYMax() - r.getYMin()) / 4;
                r.setYMin(r.getYMin() + dy);
                r.setYMax(r.getYMax() - dy);

            }
            break;
            case 36: // Home
                r = new Rect(origin);
                break;
        }
        mapper.zoom(r);
        comp.repaint();
    }

}
