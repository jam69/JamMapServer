
/*
 *  JavaScript test to copy files from SHP/DBF files to MySQL spatial tables
 *  
 *   Files can be downloaded from http://planet.openstreetmap.org/ (22Gb)
 *  or smaller
 *   http://wiki.openstreetmap.org/wiki/Planet.osm#Downloading 
 * 
 *  For this example:
 *     http://download.geofabrik.de/osm/europe/spain.shp.zip (385Mb)
 *  and unzip in /Data directory
 *  
 *  In Mysql entry as root (administrator)
 *  and type:
 *  
 *  mysql> CREATE DATABASE mapas;
 *  
 *  
 */
var dir="/Users/joseantoniomartin/Documents/projects/JamMapServer/Data/spain/";
var dbURL="jdbc:mysql://localhost:3306/mapas?user=root&password=root";

function copyShpToMySQL(filePath, dbURL, tableName){
	xxxx.log("COPIANDO: "+tableName);
	var repos1=xxxx.createShpRepos(filePath);
	var repos2=xxxx.createMySQLRepos(dbURL,tableName);
	xxxx.copy(repos1,repos2);
}

xxxx.log("Empiezo...");

copyShpToMySQL(dir+"buildings.shp",dbURL,"buildings");
copyShpToMySQL(dir+"landuse.shp",dbURL,"landuse");
copyShpToMySQL(dir+"natural.shp",dbURL,"naturales"); // Don't use "natural" as table name
copyShpToMySQL(dir+"places.shp",dbURL,"places");
copyShpToMySQL(dir+"points.shp",dbURL,"points");
copyShpToMySQL(dir+"railways.shp",dbURL,"railways");
copyShpToMySQL(dir+"roads.shp",dbURL,"roads");
copyShpToMySQL(dir+"waterways.shp",dbURL,"waterways");
		
xxxx.log("Terminado");
