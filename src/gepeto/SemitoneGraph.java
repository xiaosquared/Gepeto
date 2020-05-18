package gepeto;

import processing.core.PApplet;

public class SemitoneGraph {
	float x;
	float y;
	float width;
	float height;

	// based on MIDI numbers
	float min_value = 36; 
	float max_value = 76;
	float range = max_value-min_value;
	
	public SemitoneGraph(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public float getX() { return x; }
	public float getY() { return y; }
	public float getMaxX() { return x + width; }
	public float getMaxY() { return y + height; }
	public float getwidth() { return width; }
	public float getHeight() { return height; }
	
	public void draw(PApplet parent) {
		
		// Lines
		parent.strokeWeight(1);
		parent.stroke(150);
		parent.fill(255);
		for (float i = 0; i <= range; i++) {
			float my_y = y + i/range * height;
			parent.line(x, my_y, x + width, my_y);
		}
		
		// Some labels
		parent.text("MIDI:" + (int)max_value, x+30, y);
		parent.text("MIDI:" + (int)min_value, x+30, y+height);
	}
	
	public float yToMidi(float my_y) {
		if (my_y < y || my_y > y+height)
			return -1;
		
		float f = 1 - (my_y - y)/height;
		return min_value + f * range;
	}
	
	public float midiToY(float midi) {
		if (midi < min_value || midi > max_value)
			return -1;
		
		float f = 1 - (midi-min_value)/range;
		return y + f * height; 
		
	}
}
