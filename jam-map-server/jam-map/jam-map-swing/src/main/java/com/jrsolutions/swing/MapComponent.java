package com.jrsolutions.swing;


import com.jrsolutions.mapserver.render.IMapper;
import com.jrsolutions.mapserver.render.Mapa;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;


public class MapComponent extends JPanel{


    private Mapa map;
    private IMapper mapper;
	
	public  MapComponent(IMapper mapper,Mapa map){
        this.map=map;
        this.mapper=mapper;
		setOpaque(true);
		setBackground(Color.red);

		setFocusable(true);
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Pressed:"+e.getKeyCode());
				switch(e.getKeyCode()){
				case 38: // UP
		//			 yt--;
					 break;
				case 40: // Down
		//			yt++;
					break;
				case 37: // Left
			//		xt++;
					break;
				case 39: // right
			//		xt--;
					break;
				case 107:
				case 521: // +
			//		xt=xt*2;
			//		yt=yt*2;
			//		z ++;
					break;
				case 109: // -
				case 45:
			//		xt= xt/2;
			//		yt= yt/2;
			//		z --;
					break;
				case 36: // Home
			//		xt=64222;
			//		yt=49418;
			//		z=17;
					break;
				}
				repaint();
			}
			
		});
		
		}
	
	@Override
	public void paint(Graphics g){
		System.out.println("---R-e-p-i-n-t-a---");
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
//		super.paintComponent(g);
                map.pinta(mapper, (Graphics2D)g);
    }
}
