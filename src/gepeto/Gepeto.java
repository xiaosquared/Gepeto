package gepeto;

import ddf.minim.*;
import processing.core.PApplet;


public class Gepeto extends PApplet {
	Minim m = new Minim(this);
	
	String filename = "Yang1_declaration11";
	SpeechSegment s;
	
	

	public void settings() {
		size(600, 400);
	}
	
	public void setup() {
		textAlign(PApplet.CENTER, PApplet.CENTER);
		
		s = new SpeechSegment(filename, m, this);

		background(50);		
	}

	public void draw() {
		background(50);
		s.draw();
	}
	
	public void keyPressed() {
//		if (key == '1') {
//			println(key);
//			s.setPlaybackInterval(1);
//		} else if (key == '2') {
//			println(key);
//			s.setPlaybackInterval(2);
//		} else if (key == '3') {
//			println(key);
//			s.setPlaybackInterval(3); 
//		} 
		println("play");
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
