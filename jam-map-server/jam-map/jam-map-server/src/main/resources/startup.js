

         var dbURL="jdbc:mysql://localhost:3306/mapas?user=root&password=root";
         var iconPath="/data/map-icons/icons/";
         
        function createSymb(a,b,c){
        	var simb= xxxx.createSymboly()
                          .addLineColor(b)
                          .addNodeSymbol(1,a)
                          .addSurfaceColor(c);
        	return simb;
        } 
   	 
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa);

         var dataRepos = xxxx.createMySQLRepos(dbURL,"naturales");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(7,19);
         geomLayer.setDefault(simb);


         var dataRepos = xxxx.createMySQLRepos(dbURL,"landuse");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(8,19);
         geomLayer.setDefault(createSymb(0xaa00aa,0xcccccc,0xccffff));

		var dataRepos = xxxx.createMySQLRepos(dbURL,"buildings");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(13,19);
         geomLayer.setDefault(createSymb(0xaa00aa,0xcccccc,0xffcc99)
        		 .addSurfNodeText(0 , 0,0xffffff,"name",15,13,"","N")
        		 .addSurfNodeText(0 , 0,0xffffff,"type",15,13,"","S")
       		   );

         var dataRepos = xxxx.createMySQLRepos(dbURL,"roads");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(11,19);
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa);
          //      .addMidPointText(0 ,0,0xccffcc,"name",17,12,"","E");
         geomLayer.setDefault(simb);

         var dataRepos = xxxx.createMySQLRepos(dbURL,"railways");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(11,19);
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa);
         geomLayer.setDefault(simb);

         
         var dataRepos = xxxx.createMySQLRepos(dbURL,"points");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(13,19);       
         var simb=createSymb(0xaa00aa,0xcccccc,0xffaaaa)
         		   .addNodeText(0 , 0,0xffccff,"name",15,10,"","N")
         		   .addNodeText(0 , 0,0xffcccc,"type",15,10,"","S");
         		   //.addNodeIcon(iconPath+"agriculture.png",17,"N");
         geomLayer.setDefault(simb);
         
         geomLayer.where("type","subway_entrance",iconPath+"subway.png",17,"N");
         geomLayer.where("type","bus_stop",iconPath+"bus.png",17,"N");
         geomLayer.where("type","taxi",iconPath+"taxi.png",17,"N");
         geomLayer.where("type","station","",17,"N");
         
         geomLayer.where("type","place_of_worship",iconPath+"church.png",17,"N"); //church2, chapel
         geomLayer.where("type","school",iconPath+"school.png",17,"N");
         geomLayer.where("type","hospital",iconPath+"hospital.png",17,"N");
         geomLayer.where("type","pharmacy",iconPath+"drugs.png",17,"N");
         geomLayer.where("type","nursing_home","",17,"N");
         geomLayer.where("type","recycling",iconPath+"recycle.png",17,"N");
         geomLayer.where("type","kindergarten",iconPath+"nursery.png",17,"N");
         geomLayer.where("type","bank",iconPath+"bank.png",17,"N");
         geomLayer.where("type","post_office",iconPath+"postal.png",17,"N");
         geomLayer.where("type","hotel",iconPath+"hotel.png",17,"N");
         geomLayer.where("type","public_building","",17,"N");
         geomLayer.where("type","university",iconPath+"university.png",17,"N");
         geomLayer.where("type","bicycle_rental",iconPath+"cycling.png",17,"N");
         geomLayer.where("type","cinema",iconPath+"cinema.png",17,"N");
         geomLayer.where("type","wastewater_plant","",17,"N");
         geomLayer.where("type","camp_site",iconPath+"camping.png",17,"N");
         
         geomLayer.where("type","drinking_water",iconPath+"drinkingwater.png",17,"N");  //fountain
         geomLayer.where("type","fountain.png",iconPath+"fountain.png",17,"N");
         geomLayer.where("type","post_box","",17,"N");      
         geomLayer.where("type","telephone",iconPath+"telephone.png",17,"N");
         geomLayer.where("type","embassy",iconPath+"embassy.png",17,"N");
         geomLayer.where("type","bench",iconPath+"bench.png",17,"N");
         geomLayer.where("type","waste_basket","",17,"N");
         geomLayer.where("type","memorial","",17,"N");
         geomLayer.where("type","monument",iconPath+"monument.png",17,"N");
         geomLayer.where("type","viewpoint","",17,"N");
         geomLayer.where("type","measurement_stat","",17,"N");
         geomLayer.where("type","level_crossing","",17,"N");
         
         geomLayer.where("type","cafe",iconPath+"coffee.png",17,"N");
         geomLayer.where("type","restaurant",iconPath+"restaurant.png",17,"N");
         geomLayer.where("type","pub","",17,"N");
         geomLayer.where("type","fast_food","",17,"N");
         
         geomLayer.where("type","crossing",iconPath+"cross.png",17,"N");
         geomLayer.where("type","mini_roundabout","",17,"N");
         geomLayer.where("type","motorway_junction","",17,"N");
         geomLayer.where("type","fuel",iconPath+"gazstation.png",17,"N");
         geomLayer.where("type","speed_camera",iconPath+"photodown.png",17,"N");
         geomLayer.where("type","parking",iconPath+"parking.png",17,"N");
         geomLayer.where("type","bicycle_parking",iconPath+"bicycleparking.png",17,"N");
         geomLayer.where("type","survey_point","",17,"N");
         geomLayer.where("type","stop",iconPath+"stop.png",17,"N");
         geomLayer.where("type","traffic_signals","",17,"N");
         geomLayer.where("type","turning_cirle","",17,"N");
         geomLayer.where("type","halt","",17,"N");
         geomLayer.where("type","give_way","",17,"N");
              
         
        var dataRepos = xxxx.createMySQLRepos(dbURL,"places");
         var geomLayer = mapa.createGeomLayer(dataRepos);
         geomLayer.setZoomLimits(13,19);
         var simb2=createSymb(0xaa00aa,0xcccccc,0xccffcc)
         			   .addNodeSymbol(2,0xccffcc)
                       .addNodeText(0 ,0,0x00ff00,"name",15,10,"","N")
                       .addNodeText(0 ,0,0xcc0000,"type",15,10,"","S");
         geomLayer.setDefault(simb2);

         
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
    