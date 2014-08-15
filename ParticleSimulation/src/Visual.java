import peasy.PeasyCam;
import processing.core.PApplet;

public class Visual extends PApplet{
	final int fieldX = 900;
	final int fieldY = 600;
	final int fieldZ = 500;
	final int numParticles = 10;
	boolean clicked = true;

	Field field;
	PeasyCam camera;

	public void setup(){		
		size(900, 600, P3D);
		camera = new PeasyCam(this, 500, height/2, 0, width/2.65); //initialize the peasycam
		//		camera();
		field = new Field(this, new Vector(fieldX, fieldY, fieldZ), numParticles);
	}

	public void keyPressed(){
		clicked = !clicked;
	}

	public void draw(){
		background(0);
		lights();
		spotLight(255, 255, 255, 250, 0, 400, 0, 0, -1, PI/4, 2);
		noStroke();
		drawBox();
		if(clicked){
			field.populateForces();
			field.handleWallCollisions();
			field.handleInterCollisions();
			field.accelerateAllParticles();
			field.moveAllParticles();
		}
		field.drawAllParticles();
	}

	public void drawBox(){
		noFill();
		strokeWeight(5);
		stroke(255, 255, 255);
		translate(fieldX/2, fieldY/2, -fieldZ/2);
		box(fieldX, fieldY, fieldZ);
		fill(100, 100, 100);
		translate(0, 0, -fieldZ/2);
		box(fieldX, fieldY, 0);
		translate(fieldX/2, 0, fieldZ/2);
		box(0, fieldY, fieldZ);
		translate(-fieldX, 0, 0);
		box(0, fieldY, fieldZ);
		translate(fieldX/2, fieldY/2, 0);
		box(fieldX, 0, fieldZ);
		translate(0, -fieldY, 0);
		box(fieldX, 0, fieldZ);
		translate(0, fieldY/2, 0);
		noStroke();
	}
}