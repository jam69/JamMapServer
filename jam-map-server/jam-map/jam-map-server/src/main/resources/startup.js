

         var dbURL="jdbc:mysql://localhost:3306/mapas?user=root&password=root";

        function createSymb(a,b,c){
        	var simb= xxxx.createSymboly()
                          .addLineColor(b)
                          .addNodeSymbol(1,a)
                          .addSurfaceColor(c);
        	return simb;
        } 
   	 
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa);
/*
         var dataRepos = xxxx.createMySQLRepos(dbURL,"naturales");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(7,19);
         geomLayer.setDefault(simb);
*/
/*
         var dataRepos = xxxx.createMySQLRepos(dbURL,"landuse");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(8,19);
         geomLayer.setDefault(createSymb(0xaa00aa,0xcccccc,0xccffff));
*/
/*         var dataRepos = xxxx.createMySQLRepos(dbURL,"buildings");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(9,19);
         geomLayer.setDefault(createSymb(0xaa00aa,0xcccccc,0xccffcc));
*/
         var dataRepos = xxxx.createMySQLRepos(dbURL,"roads");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(11,19);
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa);
          //      .addMidPointText(0 ,0,0xccffcc,"name",17,12,"","E");
         geomLayer.setDefault(simb);

  /*       var dataRepos = xxxx.createMySQLRepos(dbURL,"railways");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(11,19);
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa);
         geomLayer.setDefault(simb);
*/
         
         var dataRepos = xxxx.createMySQLRepos(dbURL,"points");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(13,19);       
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa)
         		   .addNodeText(0 , 0,0xffccff,"name",15,10,"","N")
         		   .addNodeText(0 , 0,0xffcccc,"type",15,10,"","S");
         geomLayer.setDefault(simb);

/*         var dataRepos = xxxx.createMySQLRepos(dbURL,"places");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(13,19);
         var simb2=createSymb(0xaa00aa,0xcccccc,0xccffcc)
         			   .addNodeSymbol(2,0xccffcc)
                       .addNodeText(0 ,0,0xffffff,"name",17,10,"","N")
            //    .addNodeText(0 ,10,0xcccccc,"type",17,10,"","S");
         geomLayer.setDefault(simb2);
*/
         
/*		 
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
		 
*/
    