package gepeto;

import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;

public class OSCSender {
	OscP5 oscP5;
	NetAddress dest;
	
	boolean rhythm_button = false;
	
	public OSCSender(PApplet parent, String ip, int port) {
		oscP5 = new OscP5(parent, port);
		dest = new NetAddress(ip, port);
	}
	
	public void sendPitch(float pitch) {
		sendMsg("/pitch", pitch);
	}
	
	public void sendVocalEffort(float effort) {
		sendMsg("/effort", effort);
	}
	
	public void sendReset() {
		sendMsg("/reset", true);
	}
	
	/* 
	 * To prevent repeated keyPress triggers, keeps internal button state
	 */
	public void sendBinaryRhythm(boolean rhythm) {
		if (rhythm_button != rhythm) {
			sendMsg("/binary", rhythm);
			rhythm_button = rhythm;
		}
	}
	
	private void sendMsg(String type, float value) {
		OscMessage msg = new OscMessage(type);
		msg.add(value);
		oscP5.send(msg,dest);
	}
	
	private void sendMsg(String type, boolean value) {
		OscMessage msg = new OscMessage(type);
		msg.add(value);
		oscP5.send(msg,dest);
	}
}
