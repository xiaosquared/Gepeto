package gepeto;

import ddf.minim.*;
import processing.core.PApplet;


public class Gepeto extends PApplet {
	Minim m = new Minim(this);
	
	String filename = "Yang1_declaration11";
	SpeechSegment s;
	
	SemitoneGraph stg;

	public void settings() {
		size(800, 600);
	}
	
	public void setup() {
		textAlign(PApplet.CENTER, PApplet.CENTER);
		
		stg = new SemitoneGraph(0, 50, width, height-150);
		s = new SpeechSegment(filename, m, stg, this);

		
	}

	public void draw() {
		background(50);
		s.draw();
		stg.draw(this);
	}
	
	public void keyPressed() {
		s.playInterval();
	}
	
	public void mousePressed() {
		s.selectIntervalFromMouse(mouseX, mouseY);
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { gepeto.Gepeto.class.getName() });
	}
}
