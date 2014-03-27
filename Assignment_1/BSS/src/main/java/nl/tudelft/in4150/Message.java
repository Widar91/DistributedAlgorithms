package nl.tudelft.in4150;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 683752848649275872L;
	
	private String body;
	private int senderId;
	private int[] vectorClock;
	
	
	public Message(int senderId,  String body, int[] vectorClock) {
		this.senderId = senderId;
		this.body = body;
		this.vectorClock = vectorClock;
	}
	
	
	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public int getSenderId() {
		return senderId;
	}


	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}


	public int[] getVectorClock() {
		return vectorClock;
	}


	public void setVectorClock(int[] vectorClock) {
		this.vectorClock = vectorClock;
	}


	public String toString() {
		String vc = "";
		for (int v : vectorClock) { vc += v + " "; } 
		
//		return 	  "\n\t ***********************"
//				+ "\n\t Message: [" + body + "]" 
//				+ "\n\t Sender: Node[" + senderId + "]"
//				+ "\n\t Timestamp: <" + vc.trim() + ">"
//				+ "\n\t ***********************";
		
		return body;
		
	}

	
}
