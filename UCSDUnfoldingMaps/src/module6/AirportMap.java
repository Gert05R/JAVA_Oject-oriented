package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	List<Marker> routeListX;
	public static int TRI_SIZE = 5;
	protected boolean clicked = false;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	private HashMap<Integer, Location> airports;
	
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		AbstractMapProvider provider = new Microsoft.RoadProvider();
		//map = new UnfoldingMap(this, 50, 50, 750, 550, provider);
		map = new UnfoldingMap(this, 200, 50, 650, 600, provider);
		//map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			SimplePointMarker m = new AirportMarker(feature);
	
			//m.setRadius(5);
			airportList.add(m);
			
			//System.out.println("getid " +Integer.parseInt(feature.getId()));
			//System.out.println("getLoc " +feature.getLocation());
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(route.getLocations());
			//System.out.println(sl.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
		}
		
		/*for(Marker route : routeList) {
			
			SimpleLinesMarker sl = (SimpleLinesMarker) route;
			
		System.out.println(sl.getLocations());
			
		}*/
		
		/*for(Marker airport : airportList) 
		{
			
			System.out.println( "test" + airport.getLocation());
			
		}*/
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		//map.addMarkers(routeList);
		//System.out.println("Test1" + airports.keySet());
		map.addMarkers(airportList);
		
		
	}
	
	public void draw() {
		background(0);
		map.draw();
	}
		
		
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			hideRoutes(); 
			lastClicked = null;
			
		}
		else if (lastClicked == null) 
		{
			checkAirportsForClick();
			
			
		}
	}
	
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
			
	}
	
	private void hideRoutes() 
	{
		if (lastClicked == null) return;
		if (lastClicked != null) 
		{
		for (Marker route: routeList) 
		{
			route.setHidden(true);
		}
		
		}
	}
	
	private void checkAirportsForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		for (Marker m : airportList) {
			AirportMarker marker = (AirportMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				for (Marker route: routeList) 
				{
					SimpleLinesMarker sl = (SimpleLinesMarker) route;
					if (sl.getLocations().contains(lastClicked.getLocation())) 
					{
						map.addMarkers(route);
						route.setHidden(false);
						System.out.println("Test1");
					}
					
				}
				// Hide all the other earthquakes and hide
				for (Marker mhide : airportList) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
						//testing();
						
					}
				}
				return;
			}
		}
	}
}