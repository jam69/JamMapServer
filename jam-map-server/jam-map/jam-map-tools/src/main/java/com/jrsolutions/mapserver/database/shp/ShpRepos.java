package com.jrsolutions.mapserver.database.shp;

import java.io.IOException;
import java.util.Iterator;

import com.jrsolutions.mapserver.database.DataRepos;
import com.jrsolutions.mapserver.database.Entity;
import com.jrsolutions.mapserver.database.datadefinition.FieldDescriptor;
import com.jrsolutions.mapserver.database.datadefinition.TableDescriptor;
import com.jrsolutions.mapserver.database.shp.javadbf.DBFReader;
import com.jrsolutions.mapserver.database.shp.javadbf.JDBFException;
import com.jrsolutions.mapserver.database.shp.javadbf.JDBField;
import com.jrsolutions.mapserver.geometry.Geometry;
import com.jrsolutions.mapserver.geometry.Rect;

public class ShpRepos implements DataRepos {

	private String path;
//	private boolean loadAttr;
	private Rect env;
	private final TableDescriptor descriptor;
//	protected final ArrayList<Entity> world=new ArrayList<Entity>();	

	
	@Override
	public Rect getEnvelope() {
		return env;
	}

	@Override
	public Iterator<Entity> getIterator() {
		return new SHPIterator(path);
	}

	@Override
	public Iterator<Entity> getIterator(Rect env) {
		return new SHPIterator(path);
	}
	
	@Override
	public TableDescriptor getDescription(){
		return descriptor;
	}

	class SHPIterator implements Iterator<Entity>{

		ShapefileReader shf;
		Iterator<Geometry> geomIt;
		DBFReader dbf;
		
		public SHPIterator(String path){
			try {
				shf=new ShapefileReader(path);
				String path3=path.substring(0,path.length()-4)+".dbf";
				dbf=new DBFReader(path3);
				geomIt=shf.iterator();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDBFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		@Override
		public boolean hasNext() {
			boolean b= geomIt.hasNext();
			if(!b){
				try {
					dbf.close();
					shf.closeShapefile();
				} catch (JDBFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return b;
		}

		@Override
		public Entity next() {
			Entity ent;
			try {
				Geometry g=geomIt.next();
				ent = new Entity();
				ent.setGeom(g);
				Object[] rec =(Object[])dbf.nextRecord();
				for(int i=0;i<dbf.getFieldCount();i++){
					ent.setAttr(dbf.getField(i).getName(),rec[i]);
				}
			} catch (JDBFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ent=null;
			}
			return ent;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
	public ShpRepos(String path2){
		System.out.println("Loading SHP:"+path2);
		descriptor=new TableDescriptor();
		path=path2;
		env=new Rect();
		String aux=path.substring(path.lastIndexOf("/")+1);
		descriptor.setName(aux.substring(0,aux.indexOf(".")));
		descriptor.addField(new FieldDescriptor("geom",FieldDescriptor.Type.Geometry));

//		try {
//			ShapefileReader shf=new ShapefileReader(path);
//			Iterator<Geometry> it=shf.iterator();
//			int cont=0;
//			while(it.hasNext()){
//				cont++;
//				if(cont%10000==0)System.out.println("Leidas:"+cont);
//				Geometry g=(Geometry)it.next();
//				Entity ent=new Entity();
//				ent.setGeom(g);
//				world.add(ent);	
//				env.extend(g.getEnvelope());
//			}
//			shf.closeShapefile();
//			System.out.println("Coords:"+env+" NRec:"+ shf.numRecords());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		if(loadAttr){
		try {
			String path3=path.substring(0,path.length()-4)+".dbf";
			System.out.println("Loading DBF:"+path3);
			DBFReader dbf=new DBFReader(path3);
			
			FieldDescriptor.Type t;
			for(int i=0;i<dbf.getFieldCount();i++){
				JDBField field=dbf.getField(i);
				switch(field.getType()){
				case 'C' : t=FieldDescriptor.Type.Varchar; break;
				case 'N' : t=FieldDescriptor.Type.Decimal; break;
				case 'D' : t=FieldDescriptor.Type.Real; break;
				case 'B' : t=FieldDescriptor.Type.Logical;break;
				default  : t=FieldDescriptor.Type.Varchar;
				}
				
				descriptor.addField(new FieldDescriptor(field.getName(), t,field.getLength(),field.getDecimalCount() ));
				System.out.println("   Campo:["+dbf.getField(i).getType()+"]:"+dbf.getField(i).getName());
			}
//			int cont=0;
//			while(dbf.hasNextRecord()){
//				Object[] rec =(Object[])dbf.nextRecord();
//				Map<String,Object> ent=new HashMap<String,Object>(dbf.getFieldCount());
//				for(int i=0;i<dbf.getFieldCount();i++){
//					ent.put(dbf.getField(i).getName(),rec[i]);
//				}
//				world.get(cont++).setAllAttrs(ent);
//			}
//			dbf.close();
			System.out.println("   Leidos datos Alfa");
		} catch (JDBFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

//	}
		
	@Override
	public void create(TableDescriptor description){
    	//TODO
    }
	
	@Override
	public void open(){
		// TODO
	}
	@Override
	public void close(){
		// TODO
	}
	@Override
	public boolean save(Entity entity){
		return false;
	}

}
