package gepeto;

public class SpeechInterval {
	String label;
	
	int start_x;
	int end_x;
	int width;
	
	int start_time;
	int end_time;
	
	public SpeechInterval(String label, int start_time, int end_time, int start_x, int end_x) {
		this.label = label;
		this.start_time = start_time;
		this.end_time = end_time;
		this.start_x = start_x;
		this.end_x = end_x;
		this.width = end_x - start_x;
	}
	
	public String getLabel() { return label; }
	public int getStartTime() { return start_time; }
	public int getEndTime() { return end_time; }
	public int getStartX() { return start_x; }
	public int getWidth() { return width; }
	public int getEndX() { return end_x; }
	public int getCenterX() { return start_x + (width)/2; }
}
