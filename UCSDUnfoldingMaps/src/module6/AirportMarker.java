package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;

public class AirportMarker extends CommonMarker {
	
public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public AirportMarker(Location location) {
		super(location);
	}

	public AirportMarker(Feature Airport) {
		super(((PointFeature)Airport).getLocation(), Airport.getProperties());

	}
	
	
	// pg is the graphics object on which you call the graphics
	// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
	// x and y are the center of the object to draw. 
	// They will be used to calculate the coordinates to pass
	// into any shape drawing methods.  
	// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
	// whose upper left corner is at position x, y
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each Airport
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getName();
		String country = getCountry();
		String city = getCity();
		String code = getCode();
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(16);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(country)) + 20, 80);
		//pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(city), pg.textWidth(code)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(country, x+3, y - TRI_SIZE -18);
		pg.text(city, x+3, y - TRI_SIZE -3);
		pg.text(code, x+3, y - TRI_SIZE +12);
		
		pg.popStyle();
	}
	
	
	
	public String getName()
	{
		return getStringProperty("name");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private String getCity()
	{
		return getStringProperty("city");
	}
	private String getCode()
	{
		return getStringProperty("code");
	}

}
