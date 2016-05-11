/*
 *  Copyright &copy; Indra 2016
 */
package com.jrsolutions.mapserver.gis;

import java.awt.Graphics2D;

/**
 *
 * @author jamartinm
 */
public interface Layer
{
    public void render(IMapper m,Graphics2D g);

}
