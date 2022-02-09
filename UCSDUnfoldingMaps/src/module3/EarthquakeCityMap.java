package module3;

//Java utilities libraries
import java.util.ArrayList;

//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//import com.jogamp.opengl.math.geom.Frustum.Location;
import de.fhpotsdam.unfolding.geo.Location; 

//Processing library
import processing.core.PApplet;
import processing.core.PGraphics;
//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.GeoMapApp;
import de.fhpotsdam.unfolding.providers.GeoMapApp.GeoMapAppProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.Microsoft.MicrosoftProvider;
//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	//private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/atom.php";


	PGraphics pg;
	
	public void setup() {
		size(950, 600, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			//AbstractMapProvider provider = new Google.GoogleMapProvider();
			AbstractMapProvider provider = new Microsoft.RoadProvider();
			map = new UnfoldingMap(this, 200, 50, 700, 500, provider);//http://unfoldingmaps.org/javadoc/de/fhpotsdam/unfolding/UnfoldingMap.html + http://unfoldingmaps.org/javadoc/de/fhpotsdam/unfolding/providers/Google.GoogleMapProvider.html
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2); //http://unfoldingmaps.org/javadoc/de/fhpotsdam/unfolding/UnfoldingMap.html#zoomToLevel(int)
	    MapUtils.createDefaultEventDispatcher(this, map);	//http://unfoldingmaps.org/javadoc/
	   
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    //TODO (Step 3): Add a loop here that calls createMarker (see below) 
	    // to create a new SimplePointMarker for each PointFeature in 
	    // earthquakes.  Then add each new SimplePointMarker to the 
	    // List markers (so that it will be added to the map in the line below)
	    for (PointFeature eq: earthquakes) {
	    	SimplePointMarker SPM1 = createMarker (eq);
	    	markers.add(SPM1);	    	
	    }
	    
	    
	    // Add the markers to the map so that they are displayed
	    
	    map.addMarkers(markers);
	    
	    pg = createGraphics(20, 100, P2D);

	}
		
	/* createMarker: A suggested helper method that takes in an earthquake 
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is.  Call it from a loop in the 
	 * setp method.  
	 * 
	 * TODO (Step 4): Add code to this method so that it adds the proper 
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		
		// Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int blue = color(0, 0, 255);
	    int red = color(255, 0, 0);
	    float smallRadius = 5.22f;
	    float mediumRadius = 7.55f;
	    float largeRadius = 15.22f;
		
		// TODO (Step 4): Add code below to style the marker's size and color 
	    // according to the magnitude of the earthquake.  
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")
	    if (mag < THRESHOLD_LIGHT) {
	    	marker.setColor(blue);
	    	marker.setRadius(smallRadius);
	    	
	    	
	    }
	    
	    if (mag > THRESHOLD_LIGHT && mag < THRESHOLD_MODERATE) {
	    	marker.setColor(yellow);
	    	marker.setRadius(mediumRadius);
	    	
	    }
	    
	    if  (mag > THRESHOLD_MODERATE){
	    	marker.setColor(red);
	    	marker.setRadius(largeRadius);
	    	
	    }
	    
	    // Finally return the marker
	    return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
	    int grey = color(255, 247, 247);
	    int yellow = color(255, 255, 0);
	    int blue = color(0, 0, 255);
	    int red = color(255, 0, 0);
	    float smallRadius = 5.22f;
	    float mediumRadius = 7.55f;
	    float largeRadius = 15.22f;

		  
		  pg.beginDraw();
		  pg.background(grey);
		  fill(grey);
		  rect(20, 50, 150, 220);
		  
		  fill(0);
		  textAlign(CENTER);
		  text("Earthquake Key", 50, 80, 120, 100);
		  
		  fill(red);
		  ellipse(50, 120,largeRadius, largeRadius);
		  fill(0);
		  textAlign(LEFT);
		  text("5.0+ Magnitude", 70, 115, 120, 100);
		  
		  fill(yellow);
		  ellipse(50, 160,7.55f, 7.55f);
		  fill(0);
		  textAlign(LEFT);
		  text("4.0+ Magnitude", 70, 155, 120, 100);
		  
		  fill(blue);
		  ellipse(50, 190,smallRadius, smallRadius);
		  fill(0);
		  textAlign(LEFT);
		  text("Below 4.0", 70, 185, 120, 100);
		  
		  pg.endDraw();
	
	}
}
