package gepeto;

import java.util.*;

import controlP5.*;
import ddf.minim.*;
import processing.core.PApplet;

public class Gepeto extends PApplet {
	Minim m = new Minim(this);
	
	String[] filenames;
	SpeechSegment s;
	
	SemitoneGraph stg;
	OSCSender osc;
	
	ControlP5 cp5;
	int selector_width = 150;
	
	String data_path = "/Users/xx/Github/Gepeto/Gepeto/data/";
	
	boolean bScrub = true;
	
	public void settings() {
		size(600, 500);
	}
	
	public void setup() {
		textAlign(PApplet.CENTER, PApplet.CENTER);
		
		filenames = loadStrings("recordings.txt");
		
		stg = new SemitoneGraph(0, 50, width, height-150);
		s = new SpeechSegment(filenames[0], m, stg, this);

		osc = new OSCSender(this, "127.0.0.1", 7400);
		osc.sendAnalysis(data_path + "analysis/" + s.getFilename() + "_analysis.jxf.jit");
		osc.sendLabeling(data_path + s.getFilename() + ".txt");
		
		osc.sendRestart();
		osc.sendScrubMode();
		
		initFileSelector();
	}

	private void initFileSelector() {
		cp5 = new ControlP5(this);
		List<String> items = Arrays.asList(filenames);		
		cp5.addScrollableList("select")
			.setPosition(width - selector_width, 0)
			.setSize(selector_width, 100)
			.setBarHeight(20)
			.setItemHeight(20)
			.addItems(items)
			.setOpen(false);
	}
	
	public void draw() {
		background(50);
		s.draw();
		stg.draw(this);
	}
	

	public void select(int n) {
		String filename = cp5.get(ScrollableList.class, "select").getItem(n).get("name").toString();
		s = new SpeechSegment(filename, m, stg, this);
		osc.sendAnalysis(data_path + "analysis/" + s.getFilename() + "_analysis.jxf.jit");
		osc.sendLabeling(data_path + s.getFilename() + ".txt");
	}
	
	public void keyPressed() {
		if (key == ' ') {
			if (!bScrub)
				osc.sendBinaryRhythm(false);
		} 
		else if (key == 'i') {
			s.playInterval();
		}
		else if (key == 'r' ) {
			osc.sendRestart();
		} else if (key == 'b') {
			println("binary mode");
			bScrub = false;
			osc.sendBinaryMode();
		}
		else if (key == 'p') {
			println("parabolic mode");
			bScrub = false;
			osc.sendParabolicMode();
		}
		else if (key == 's') {
			println("scrub mode");
			bScrub = true;
			osc.sendRestart();
			osc.sendScrubMode();
		}
			
	}
	
	public void keyReleased() {
		if (bScrub)
		 osc.sendVocalEffort(0);
		else {
			if (key == ' ') {
				//s.playInterval();
				osc.sendBinaryRhythm(true);
			}
		}
	}
	
	public void mouseReleased() {
		osc.sendVocalEffort(0);
	}
	
	public void mouseMoved() {
		if (bScrub) { 
			if (keyPressed) {
				osc.sendVocalEffort(1);
				osc.sendScrub(s.getScrubPosition(mouseX));
				osc.sendPitch(stg.yToMidi(mouseY));
			}	
		} else {
			osc.sendVocalEffort(1);
			osc.sendPitch(stg.yToMidi(mouseY));
		}
			
	}
	
	public void mousePressed() {
		s.selectIntervalFromMouse(mouseX, mouseY);
		osc.sendVocalEffort(1);
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { gepeto.Gepeto.class.getName() });
	}
}
