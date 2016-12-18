package com.jrsolutions.mapserver.servlet;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import com.jrsolutions.mapserver.geometry.Rect;
import com.jrsolutions.mapserver.gis.Mapa;

public class VectorOutput {

	private static Mapa mapa = new Mapa("algo");

	public VectorOutput() {

	}

	public void dumpWKB(Rect r, ServletOutputStream outputStream) throws IOException {
		mapa.dumpWKB(r, outputStream);
	}

}
