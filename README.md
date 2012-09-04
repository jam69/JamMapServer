============
JamMapServer
============

This is a brand new Map and Geometry Server.

Main Project Blocks
===================
- Servlet (Output)
- Data
- Symbology
- Render
- Configuration


Http-Servlet
------------

- OpenMap:   /X/Y/Zoom
- WCS (OpenGis Consortium)
- export Raster Image:  png, jpeg, ...
- export Vector Data:  VRML, SVG, ...


Data
----

Reads Geometry and data from:
- ShapeFile + .dbf
- CSV: for Points Of Interest
- Others...


Data: 
- In Memory
- In File
- Geometry (RTree) Indexed File
- Geometry (QTree) Indexed File
- Geometry (MBR) Indexed File
- Geometry (GeoHash) Indexed File
- SQL (MBR-indexed)
- SQL (GeoHash-indexed)
- MySQL Geometry Indexed Table
- Oracle Spatial Index Table
- Remote Servers ...


Simbology
---------

config-data:ImagePath, SVGSymbolPath, Font-repos, simbol-repos

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

SurfaceSymbology:  

    fill        : color
    gradientFill:  p1 color p2 color
    image       : path fix repeat offset-x offset-y
    border      : LineSimbology
    centroid    : NodeSimbology 
    pattern     :
    
DefaultSimbology extends Simbology   

Render Package
--------------

Mapa:  

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


Configuration
-------------

With JavaScript or other language

-- Config.js --

    DatosCollection data=new DatosCollection(DatosFileShp("fichero",loadAttrs));
    Layer layer=new Layer(data);
    Simbology simbology=new DefaultSimbology();
    simbology.add(new PrimSymbol(3,0xccc,0,0,1,NW);
    layer.addPaintFilter(new AttrCond("TYPE","road"),simbology);
    map.addLayer(layer);


