package gepeto;

import ddf.minim.*;
import processing.core.PApplet;


public class Gepeto extends PApplet {
	Minim m = new Minim(this);
	
	String filename = "Yang1_declaration11";
	SpeechSegment s;
	
	PitchTier pt;

	public void settings() {
		size(600, 400);
	}
	
	public void setup() {
		textAlign(PApplet.CENTER, PApplet.CENTER);
		
		s = new SpeechSegment(filename, m, this);

		pt = new PitchTier("data/"+filename+"_styl.PitchTier", true);	
		pt.print();
	}

	public void draw() {
		background(50);
		s.draw();
	}
	
	public void keyPressed() {
		s.playInterval();
	}
	
	public void mousePressed() {
		//println(mouseX + ", " + mouseY);
		s.selectIntervalFromMouse(mouseX, mouseY);;
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { gepeto.Gepeto.class.getName() });
	}
}
