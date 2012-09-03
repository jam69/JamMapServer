



/*	     var shpLayer= mapa.createShpLayer("/Data/cntry02/cntry02.shp","LONG_NAME");
	     shpLayer.setZoomLimits(1,10);
	     shpLayer.setEdgeColor(255,0,0);
		 shpLayer.setSurfColor(255,250,100);
		 mapa.add(shpLayer);		
*/

         var dataRepos = mapa.createShpRepos("/Data/Madrid/comunidad_de_madrid_highway.shp",true);
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(11,19);
		 
         var simb= mapa.createSymboly()
            .addLineColor(0xcccccc)
            .addNodeSymbol(1,0xaa00aa)
            .addSurfaceColor(0xffaaaa);

         var green= mapa.createSymboly()
            .addLineColor(0x00ff00);
 
         var red= mapa.createSymboly()
            .addLineColor(0xff0000);
 
         geomLayer.setDefault(simb);
         geomLayer.where("TYPE","residential",green);
         geomLayer.where("TYPE","motorway",red);
         
         /*
		 var shpLayer= mapa.createShpLayer("/Data/Madrid/comunidad_de_madrid_highway.shp",true);
		 shpLayer.setEdgeColor(255,255,255);
		 shpLayer.setSurfColor(100,100,100);
		 shpLayer.setLineColor(100,100,100);
		 shpLayer.setNodeColor(100,100,100);
		 shpLayer.setNodeSymbol(1);
		 shpLayer.setZoomLimits(11,19);
		 var painter=mapa.createAttrGeomPainter();
		 var gris =  mapa.createStyles( 0xaaaaaa,0xaaaaaa,0xaaaaaa);
		 var green = mapa.createStyles( 0x00ff00,0x00ff00,0x00ff00);
		 var red =   mapa.createStyles( 0xff0000,0xff0000,0xff0000);
		 var blue =  mapa.createStyles( 0x0000ff,0x0000ff,0x0000ff);
		 var pink =  mapa.createStyles( 0xffff00,0xffff00,0xffff00);
		 var magenta =  mapa.createStyles( 0xff00ff,0xff00ff,0xff00ff);
		 
		 var uno    =  mapa.createStyles( 0xffff00,0x003300,0xffff00);
		 var dos    =  mapa.createStyles( 0xffff00,0x006600,0xffff00);
		 var tres   =  mapa.createStyles( 0xffff00,0x00aa00,0xffff00);
		 var cuatro =  mapa.createStyles( 0xffff00,0x00ff00,0xffff00);
		 
		 painter.where("TYPE","residential",green);
		 painter.where("TYPE","unclassified",gris);
		 painter.where("TYPE","primary",red);
		 painter.where("TYPE","footway",gris);
		 painter.where("TYPE","motorway",red);
		 painter.where("TYPE","cycleway",blue);
		 painter.where("TYPE","service",gris);
		 painter.where("TYPE","motorway_link",red);
		 painter.where("TYPE","pedestrian",gris);
		 painter.where("TYPE","tertiary",pink);
		 painter.where("TYPE","track",gris);
		 painter.where("TYPE","path",gris);
		 painter.where("TYPE","construction",gris);
		 painter.where("TYPE","secondary",red);
		 painter.where("TYPE","trunk",red);
		 painter.where("TYPE","steps",gris);
		 painter.where("TYPE","primary_way",red);
		 painter.where("TYPE","bridleway",red);
		 painter.where("TYPE","living_street",pink);
		 painter.where("TYPE","primary_link",red);
		 painter.where("TYPE","trunk_link",red);
		 painter.where("TYPE","secondary_link",green);
		 painter.where("TYPE","raceway",blue);
		 painter.where("TYPE","road",green);
		 painter.where("TYPE","tertiary_link",gris);
		 painter.where("TYPE","unsurfaced",gris);
		 painter.where("TYPE","services",gris);
		 painter.where("TYPE","platform",gris);
		 painter.where("TYPE","traffic_signals",gris);
		 painter.where("TYPE","proposed",gris);
		 painter.where("TYPE","emergency_access_point",blue);
		 painter.where("TYPE","motorway_junction",red);
		 painter.where("TYPE","turning_circle",red);
		 painter.where("TYPE","residential;service",gris);
		 painter.where("TYPE","pedestrian;residential",gris);
		 
	//	 painter.where("LANES","1",uno);
	//	 painter.where("LANES","2",dos);
	//	 painter.where("LANES","3",tres);
	//	 painter.where("LANES","4",cuatro);
		 
		 shpLayer.addPaintAttr(mapa.createPaintAttr("TYPE",0,0,0xffff00,16,19));
		 shpLayer.addPaintAttr(mapa.createPaintAttr("NAME",0,10,0xffff00,16,19));
		 shpLayer.addPaintAttr(mapa.createPaintAttr("ONEWAY",0,20,0xffff00,16,19));
		 shpLayer.addPaintAttr(mapa.createPaintAttr("LANES",0,30,0xffff00,16,19));
		 
		 shpLayer.setPainter(painter);
		 mapa.add(shpLayer);		
*/
/*
		 var shpLayer= mapa.createShpLayer("/Data/Madrid/comunidad_de_madrid_water.shp","NAME");
		 shpLayer.setEdgeColor(0,0,255);
		 shpLayer.setSurfColor(100,100,255);
		 shpLayer.setLineColor(100,100,255);
		 shpLayer.setTextColor(100,100,255);
		 shpLayer.setNodeColor(100,100,255);
		 shpLayer.setNodeSymbol(2);
		 shpLayer.setZoomLimits(5,19);
		 mapa.add(shpLayer);		

		 var shpLayer= mapa.createShpLayer("/Data/Madrid/comunidad_de_madrid_natural.shp","NAME");
		 shpLayer.setEdgeColor(0,255,0);
		 shpLayer.setSurfColor(0,255,0);
		 shpLayer.setLineColor(100,255,100);
		 shpLayer.setTextColor(100,255,100);
		 shpLayer.setNodeColor(100,255,100);
		 shpLayer.setNodeSymbol(4);
		 shpLayer.setZoomLimits(11,19);
		 mapa.add(shpLayer);		
*/
		 /*
		 var shpLayer= mapa.createShpLayer("/Data/Madrid/comunidad_de_madrid_poi.shp",true);
		 shpLayer.setEdgeColor(255,255,0);
		 shpLayer.setSurfColor(255,255,100);
		 shpLayer.setLineColor(255,255,100);
		 shpLayer.setTextColor(255,255,100);
		 shpLayer.setNodeColor(255,255,100);
		 shpLayer.setNodeSymbol(3);
		 shpLayer.setZoomLimits(11,19);
		 mapa.add(shpLayer);		

		 var shpLayer= mapa.createShpLayer("/Data/Madrid/comunidad_de_madrid_location.shp",true);
		 shpLayer.setEdgeColor(255,0,255);
		 shpLayer.setSurfColor(255,100,255);
		 shpLayer.setLineColor(255,100,255);
		 shpLayer.setTextColor(255,100,255);
		 shpLayer.setNodeColor(255,100,255);
		 shpLayer.setNodeSymbol(5);
		 shpLayer.setZoomLimits(11,19);
		 mapa.add(shpLayer);		
		*/ 
		 
	/*	 var shpLayer= mapa.createShpLayer("/Data/spain/buildings.shp","");
		 shpLayer.setEdgeColor(100,255,255);
		 shpLayer.setSurfColor(100,200,200);
		 mapa.add(shpLayer);		
		*/ 
/*		 var shpLayer= mapa.createShpLayer("/Data/spain/landuse.shp","");
		 shpLayer.setEdgeColor(255,255,255);
		 shpLayer.setSurfColor(100,100,255);
		 mapa.add(shpLayer);		
	 	 
		 var shpLayer= mapa.createShpLayer("/Data/spain/natural.shp","");
		 shpLayer.setEdgeColor(0,255,0);
		 shpLayer.setSurfColor(100,255,100);
		 mapa.add(shpLayer);		
*/
		
		/* 
		 var shpLayer= mapa.createShpLayer("/Data/spain/places.shp",true);
		 shpLayer.setEdgeColor(255,0,255);
		 shpLayer.setSurfColor(255,100,255);
		 shpLayer.setLineColor(255,100,255);
		 shpLayer.setTextColor(255,100,255);
		 shpLayer.setNodeColor(255,100,255);
		 shpLayer.setNodeSymbol(5);
		 shpLayer.addPaintAttr(mapa.createPaintAttr("name",0,0,0xffffff,13,19));
		 shpLayer.addPaintAttr(mapa.createPaintAttr("type",0,10,0xff00ff,16,19));
		 shpLayer.addPaintAttr(mapa.createPaintAttr("population",0,20,0xff00ff,16,19));
		 
		 shpLayer.setZoomLimits(5,19);
		 mapa.add(shpLayer);		
		 
		 */
 /*		 
		 var shpLayer= mapa.createShpLayer("/Data/spain/points.shp",true);
		 shpLayer.setLineColor(255,255,100);
		 shpLayer.setTextColor(255,100,255);
		 shpLayer.setNodeColor(255,100,255);
		 shpLayer.setNodeSymbol(5);
		 shpLayer.addPaintAttr(mapa.createPaintAttr("name",0,0,0xffff00,16,19));
		 mapa.add(shpLayer);		
	*/	 
	/*	 var shpLayer= mapa.createShpLayer("/Data/spain/railways.shp",true);
		 shpLayer.setEdgeColor(0,255,255);
		 shpLayer.setLineColor(255,255,100);
		 shpLayer.setSurfColor(100,255,255);
		 shpLayer.setTextColor(255,100,255);
		 shpLayer.setNodeColor(255,100,255);
		 shpLayer.setNodeSymbol(5);
		 shpLayer.setZoomLimits(5,19);
		 mapa.add(shpLayer);		
		*/ 
	/*	 var shpLayer= mapa.createShpLayer("/Data/spain/roads.shp","");
		 shpLayer.setEdgeColor(0,0,0);
		 shpLayer.setSurfColor(100,100,100);
		 mapa.add(shpLayer);		
	*/	 
	/*	 var shpLayer= mapa.createShpLayer("/Data/spain/waterways.shp","");
		 shpLayer.setEdgeColor(0,0,255);
		 shpLayer.setSurfColor(100,100,255);
		 mapa.add(shpLayer);		
		*/ 
		 
/*		 var poiLayer=mapa.createPOILayer("/Data/cities.csv", "LONGITUDE", "LATITUDE","CITY_NAME");
		 poiLayer.setColor(255,0,0);//new Color(100,100,000));
		 poiLayer.setSimbolo(0);
		 poiLayer.setZoomLimits(0.003, 0.1);
		 mapa.add(poiLayer);
	*/	 
		/*
		  
		 var wayPoints=new POILayer(mapper,"/Data/waypoints-spain.txt", "Longitude", "Latitude","Ident","\t");
		 wayPoints.setColor(new Color(80,80,255));
		 wayPoints.setSimbolo(POILayer.CRUZS);	
		 wayPoints.setZoomLimits(0.003f, 0.04f);
		 mapa.add(poiLayer);
		 
		 var airports=new POILayer(mapper,"/Data/Airports-spain.txt", "Longitude", "Latitude","Name","\t");
		 airports.setColor(new Color(80,190,80));
		 airports.setSimbolo(POILayer.EQUISS);
		 airports.setZoomLimits(0.0003f, 0.03f);
		 mapa.add(poiLayer);
*/
    