package gepeto;

import textgrids.*;

import java.util.ArrayList;

import ddf.minim.*;
import processing.core.*;

public class SpeechSegment {
	PApplet parent;
	Minim m;
	
	String filename;
	TextGrid tg;
	AudioPlayer a;
	ArrayList<SpeechInterval> intervals;
	int selected_interval_id = -1;
	
	PitchTier pt;
	SemitoneGraph graph;
	
	int y;
	int width;
	int height = 50;
	
	public SpeechSegment(String filename, Minim m, SemitoneGraph graph, PApplet parent) {
		this.parent = parent;
		this.m = m;
		
		this.filename = filename;
		
		tg = new TextGrid("data/"+filename +".TextGrid");
		a = m.loadFile("data/"+filename+".wav", 1024);
		pt = new PitchTier("data/"+filename +"_styl.PitchTier");
		
		this.graph = graph;
		width = parent.width;
		y = parent.height - 52;
		
		// extract data from TextGrid into an ArrayList of intervals
		intervals = new ArrayList<SpeechInterval>();
		String[] labels = tg.getLabels(1);
		double[][] times = tg.getTimes(1);
		for (int i = 0; i < labels.length; i++) {
			double start_time = times[i][0]*1000;
			double end_time = times[i][1]*1000;
			SpeechInterval my_interval = new SpeechInterval(labels[i], (int) start_time, (int) end_time,
															timeToX(start_time), timeToX(end_time)); 
			intervals.add(my_interval);		
		}
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setPlaybackInterval(int id) {
		if (id < 0 || id >= intervals.size())
			return;
		SpeechInterval my_interval = intervals.get(id);
		a.setLoopPoints(my_interval.getStartTime(), my_interval.getEndTime());
		a.cue(my_interval.getStartTime());
	}
	
	public void playInterval() {
		a.loop(0);
	}
	
	// Just draw tier 1 for now
	public void draw() {
		drawTextGrid();
		drawPitchTier();
		
		
		parent.stroke(255);
		parent.strokeWeight(1);
		float cursor_x = timeToX(a.position()) + graph.getX();
		parent.line(cursor_x, graph.getY(), cursor_x, graph.getMaxY());
	}
	
	private void drawPitchTier() {
		parent.fill(255, 100, 100);
		parent.stroke(255, 100, 100);
		
		PVector[] points = pt.getPoints();
		PVector last_xy = null;
		for (int i = 0; i < points.length; i++) {
			PVector xy = new PVector(timeToX(points[i].x), graph.midiToY(points[i].y));
			
			parent.strokeWeight(4);
			parent.ellipse(xy.x, xy.y, 5, 5);
			
			if (last_xy != null) {
				parent.strokeWeight(3);
				parent.line(last_xy.x, last_xy.y, xy.x, xy.y);
			}
			
			last_xy = xy;
		}
	}
	
	private void drawTextGrid() {
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
		if (mouseY < y || mouseY > y+height) {
			selected_interval_id = -1;
			return;
		}
		for (int i = 0; i < intervals.size(); i++) {
			SpeechInterval my_interval = intervals.get(i);
			if (mouseX >= my_interval.getStartX() && mouseX <= my_interval.getEndX()) {
				selected_interval_id = i;
				setPlaybackInterval(i);
			}
		}
	}
	
	public float getScrubPosition(float mouseX) {
		PVector[] points = pt.getPoints();
		float start_x = timeToX(points[0].x);
		float end_x = timeToX(points[points.length-1].x);
		//return (mouseX-start_x)/(width);
		return (mouseX-start_x)/(end_x - start_x);
	}
	
	
	private int timeToX(double time) {
		return (int) (width/(double)a.length() * time);
	}
}
