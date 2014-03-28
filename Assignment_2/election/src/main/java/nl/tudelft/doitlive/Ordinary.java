package nl.tudelft.doitlive;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Ordinary implements Serializable {
	private static final long serialVersionUID = 6631492478470169633L;

	private int id;
	private int level;
	
	public Ordinary(int id) {
		this.id = id;
		this.level = -1;
	}
	
	public void run(List<Message> messages) throws Throwable {
		level++;
		if (!messages.isEmpty()) {
			Message max = Collections.max(messages);
			if (max.compareTo(new Message(level, id)) == 1) {
				level = max.level;
				id = max.id;
				Node RMIreceiver = (Node) java.rmi.Naming.lookup(Config.nodes[id]);
				RMIreceiver.sendAck(id);
	            System.out.println("Sent ACK to " + Config.nodes[id] + " (" + id + ")");
			}
		}
	}
}
