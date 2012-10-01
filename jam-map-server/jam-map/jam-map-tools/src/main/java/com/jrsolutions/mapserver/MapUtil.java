package com.jrsolutions.mapserver;



import com.jrsolutions.mapserver.database.CopyRepos;
import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.database.MySqlRepos;
import com.jrsolutions.mapserver.database.shp.ShpRepos;
import com.jrsolutions.mapserver.render.Simbology;


public class MapUtil {

	
	public void log(String s){
		System.out.println("log:"+s);
	}

	public ShpRepos createShpRepos(String file){
		return new ShpRepos(file);
	}
	
	public DataRepos createMySQLRepos(String url,String name){
		return new MySqlRepos(url,name);
	}
	
	public Simbology createSymbology(){
		return new Simbology();
	}

	public void copy(DataRepos d1,DataRepos d2){
		CopyRepos.copy(d1,d2);
	}
}
