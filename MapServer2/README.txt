Servidor Geometrias...

      Servlet -> render -> geometry

Servlets:
     openMap -> Tiles  (x/y/z)
     WCS  -> OGIS-Consortium
     Export -> raster(jpeg,png,..)
     Export -> vector(vrml, svg, kml,...)

Geometry : 
       Topología:   node, line , surface, multigeom, rect
       Operations:  lenght, distance, surface, paralel, intersect, contains,...

Datos:
       Iterator<GeomEntity>iterator();
       Iterator<GeomEntity>iterator(rect);
       
       class GeomEntity{
             Geometry getGeom();
             Object getAttr(String name);
             }
       class EditableGeomEntity extends GeomEntity{
	     void setGeom(Geometry g);
             void setAttr(String name, Object value);
	     }
 
    Collection       : todo en memoria
    File             : Todo en fichero
    BigDataCollection: pasa data desde fichero a ndx + data 
    BigDataSpatialCollection: merIndex + data
    BigDataSpatialCollection: geomIndex + data
    BigDataSpatialCollection: geoHashIndex  + data
    BigDataSpatialCollection(Tree):     geo-Tree + data
                           Quad-Tree
                           R-Tree
    DataBase (MySQL-Graph)
    DataBase (Spatial-Ora)
    DataBase (mer-indexed)
    DataBase (geoHash-indexed)

  

Simbology:

    ImagePath, SVGSymbolPath, Font-repos, simbol-repos

    void paint(Mapper m, Graphics2D g, ent);
    
    List<NodeSimbology> nodePrims;
    List<LineSimbology> linePrims;
    List<SurfaceSimbology> surfacePrims;
   
    Nodesimbology:
              Simbol:  color tamaño off-x off-y justif      (cte,Attrib)  
              Text:    color tamaño off-x off-y justif Font Effect Decoration[underlined,joined,boxed,fillbox  (cte,Attrib) 
              Icon:    color tamaño off-x off-y justif      (cte,Attrib) 
              SVG:     color tamaño off-x off-y justif      (cte,Attrib)   
    LineSimbology:     
              line      : color tamaño joins ends
	      dash      : color grosor pattern 
              paralel   : LineSimbology 
              StartNode : NodeSimbology oriented
              EndNode   : NodeSimbology oriented
              InterNode : NodeSimbology oriented(no,before,med,after) 
              MiddleSeg : NodeSimbology oriented
              RepeaterNode: NodeSymbology oriented offset interval oriented                            
    Surface:  
	      fill        : color
              gradientFill:  p1 color p2 color
	      image       : path fix repeat offset-x offset-y
              border      : LineSimbology
              centroid    : NodeSimbology 
	      pattern     :
    
   DefaultSimbology extends Simbology   
	       
Render:
    Mapa : 
	List<Layer> layers;

    Layer:  IteradorDatos

        private Datos  datos;
        private List<PaintFiltro> filtros;

	void render(Mapper m,Graphics2D g){
		for(Entity ent:datos.iterator(m.getMer()){
	      	   for(Filter f:filtros){
			f.paint(m,g,ent);
			}
                }
        }

    Abstract Filter: 
        private final Condition cond
        
        boolean checkCond(GeomEntity ent);

    PaintFilter extends Filter:
        private final Symbology simbology;

        paint(Mapper m, Graphics2D g,Entity ent){
	    if(checkCond(ent)) paint(m, g, ent);
        }
     
Config.js
=========   
    DatosCollection data=new DatosCollection(DatosFileShp("fichero",loadAttrs));
    Layer layer=new Layer(data);
    Simbology simbology=new DefaultSimbology();
    simbology.add(new PrimSymbol(3,0xccc,0,0,1,NW);
    layer.addPaintFilter(new AttrCond("TYPE","road"),simbology);
    map.addLayer(layer);