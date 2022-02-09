package GUImodule;

import processing.core.PApplet;

public class MyDisplay extends PApplet{

	PImage img;
	
	public void setup ()
	
	{
		
		size(400, 400);
		background(255);
		stroke(0);
		//img = loadImage("http://cseweb.ucsd.edu/", )
		
	}
	
	public void draw() 
	{
		fill(255, 255, 0);
		ellipse(200, 200, 390, 390);
		fill(0,0,0);
		ellipse(100, 100, 50, 75);
		fill(0,0,0);
		ellipse(300, 100, 50, 75);
		//fill(255,0,0);
		//ellipse(200, 300, 150, 50);
		noFill();
		arc(200, 280, 75, 75, 0, PI, CHORD);
		
	}
}
