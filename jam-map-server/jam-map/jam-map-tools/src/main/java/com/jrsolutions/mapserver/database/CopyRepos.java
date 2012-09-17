package com.jrsolutions.mapserver.database;

import java.util.Iterator;

import com.jrsolutions.mapserver.database.datadefinition.TableDescriptor;

public class CopyRepos {

	public static void copy(DataRepos ori,DataRepos des){
		long t=System.currentTimeMillis();
		TableDescriptor dt=ori.getDescription();
		des.create(dt);
		
////		des.open();
		int cont=0;
		int errores=0;
		Iterator<Entity> it=ori.getIterator();
		while(it.hasNext()){
			Entity ent=it.next();
			if(des.save(ent)){
				cont++;
			}else{
				errores++;
				System.out.println("Error:"+errores);
			}
			if(cont%10000 == 0) System.out.println("Insertadas:"+cont+" Errores="+errores);
			}
//		des.close();
	
		System.out.println("Copidas "+cont+" entidades Errores:"+errores+((System.currentTimeMillis()-t)/1000)+" segundos");
	}
	
//	public void copyShpToMySQL(String filePath, String dbURL,String tableName){
//		System.out.println("COPIANDO: "+tableName);
//		DataRepos repos1=new ShpRepos(filePath);
//		DataRepos repos2=new MySqlRepos(dbURL,tableName);
//		copy(repos1,repos2);
//	}
//	public static void main(String[] args){
//		long t=System.currentTimeMillis();
//		ShpRepos shp=new ShpRepos("/Data/Madrid/comunidad_de_madrid_highway.shp");
//		MySqlFacade sql=new MySqlFacade(
//				"jdbc:mysql://localhost:3306/mapas?user=root&password=root",
//				"comunidad_de_madrid_highway");
//		
//		CopyRepos cp=new CopyRepos();
//		cp.copy(shp, sql);
//	
//		ShpRepos shp=new ShpRepos("/Data/spain/roads.shp");
//		MySqlFacade sql=new MySqlFacade(
//				"jdbc:mysql://localhost:3306/mapas?user=root&password=root",
//				"roads");
//		
//		CopyRepos cp=new CopyRepos();
//		
//		String dir="/Data/Madrid/";
//		String dbURL="jdbc:mysql://localhost:3306/mapas?user=root&password=root";
//		
//		cp.copyShpToMySQL(dir+"comunidad_de_madrid_highway.shp",dbURL,"highway");
//	
		
//		CopyRepos cp=new CopyRepos();
//		
//		String dir="/Data/spain/";
//		String dbURL="jdbc:mysql://localhost:3306/mapas?user=root&password=root";
//		
//		cp.copyShpToMySQL(dir+"buildings.shp",dbURL,"buildings");
//		cp.copyShpToMySQL(dir+"landuse.shp",dbURL,"landuse");
//		cp.copyShpToMySQL(dir+"natural.shp",dbURL,"naturales");
//		cp.copyShpToMySQL(dir+"places.shp",dbURL,"places");
//		cp.copyShpToMySQL(dir+"points.shp",dbURL,"points");
//		cp.copyShpToMySQL(dir+"railways.shp",dbURL,"railways");
//		cp.copyShpToMySQL(dir+"roads.shp",dbURL,"roads");
//		cp.copyShpToMySQL(dir+"waterways.shp",dbURL,"waterways");
//		
//		System.out.println("Terminado: total:" +((System.currentTimeMillis()-t)/1000) + "Segundos");
//	}
}
