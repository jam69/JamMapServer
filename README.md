============
JamMapServer
============

This is a brand new Map and Geometry Server.

* Works on Maven and eclipse

* Download and unzip de sources

### Maven
Execute:

     cd jam-map-server
     mvn package
     mvn jetty:run

###Eclipse
* Install (or check) the **M2e** (Maven Integration for eclipse) plugin.
* Pick over the explorer window and select "Import..."
* Select General-> Import existing projects into workspace
* Select jamp-map-server
* Select **src/main/webapp/GoogleMapOSM.html** and "Run as..." >"Run on Server"

To Test it
Open a web browser on  **http://localhost:8080/jam-map-server/GoogleMapOSM.html**


See the wiki for more information about the project