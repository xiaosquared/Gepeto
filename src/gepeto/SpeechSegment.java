package gepeto;

import textgrids.*;

import java.util.ArrayList;

import ddf.minim.*;
import processing.core.*;

public class SpeechSegment {
	PApplet parent;
	Minim m;
	
	TextGrid tg;
	AudioPlayer a;
	ArrayList<SpeechInterval> intervals;
	int selected_interval_id = -1;
	
	
	double duration;
	
	int y;
	int width;
	int height = 50;
	
	public SpeechSegment(String filename, Minim m, PApplet parent) {
		this.parent = parent;
		this.m = m;
		
		tg = new TextGrid("data/"+filename +".TextGrid");
		a = m.loadFile("data/"+filename+".wav", 1024);
		
		width = parent.width;
		y = parent.height - 52;
		
		// extract data from TextGrid into an ArrayList of intervals
		intervals = new ArrayList<SpeechInterval>();
		String[] labels = tg.getLabels(1);
		double[][] times = tg.getTimes(1);
		for (int i = 0; i < labels.length; i++) {
			SpeechInterval my_interval = new SpeechInterval(labels[i], (int) (times[i][0]*1000), (int) (times[i][1]*1000),
															getIntervalX(times[i][0]), getIntervalX(times[i][1])); 
			intervals.add(my_interval);		
		}
	}
	
	public void setPlaybackInterval(int id) {
		if (id < 0 || id >= intervals.size())
			return;
		SpeechInterval my_interval = intervals.get(id);
		a.setLoopPoints(my_interval.getStartTime(), my_interval.getEndTime());
	}
	
	public void playInterval() {
		a.loop(0);
	}
	
	// Just draw tier 1 for now
	public void draw() {
		parent.stroke(0, 0, 0);
		parent.strokeWeight(1);
		for (int i = 0; i < intervals.size(); i++) {
			SpeechInterval my_interval = intervals.get(i);

			parent.fill(200, 200, 200);
			parent.rect(my_interval.getStartX(), y, my_interval.getWidth(), height);
			
			parent.fill(0, 0, 0);
			parent.text(my_interval.getLabel(), my_interval.getCenterX(), y+height/2);
		}		
		
		// special box for selected interval
		if (selected_interval_id >= 0 && selected_interval_id < intervals.size()) {
			parent.stroke(255, 255, 255);
			parent.strokeWeight(2);
			parent.noFill();
			SpeechInterval my_interval = intervals.get(selected_interval_id);
			parent.rect(my_interval.getStartX(), y, my_interval.getWidth(), height);
		}
	}
	
	public void selectIntervalFromMouse(int mouseX, int mouseY) {
		if (mouseY < y || mouseY > y+height)
			return;
		for (int i = 0; i < intervals.size(); i++) {
			SpeechInterval my_interval = intervals.get(i);
			if (mouseX >= my_interval.getStartX() && mouseX <= my_interval.getEndX()) {
				selected_interval_id = i;
				setPlaybackInterval(i);
			}
		}
	}
	
	
	
	private int getIntervalX(double time) {
		return (int) (width/tg.getDuration() * time);
	}
}
