package nl.tudelft.bss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

public class NodeImpl implements Node {
	private static final long serialVersionUID = 7624641394444380163L;
	
	private List<Message> messages = new ArrayList<>();
	private List<String> acks = new ArrayList<>();
	
	public void candidate() throws Throwable {
		List<String> E = Arrays.asList(Config.nodes);
		int level = -1;
		while (true) {
			level++;
			int K = Math.min((int) Math.pow(2, level/2), E.size());
			if (level % 2 == 0) {
				if (E.isEmpty()) {
					System.out.println("I HAVE BEEN ELECTED!!");
					return;
				} else {
					for (int i = 0; i < K; i++) {
						String receiver = E.get(i);
						doSendMessage(receiver, new Message(level, Config.NodeID));
						E.remove(i);
					}
				}
			} else if (acks.size() < K) {
					return;
			}
		}
	}
	
	public void ordinary() throws Throwable {
		String linkId = null;
		int level = -1;
		while (true) {
			if (linkId != null) {
				doSendAck(linkId);
			}
			level++;
			Message maxMessage = maximum();
			if (maxMessage.compareTo(new Message(level, linkId)) == 1) {
				level = maxMessage.level;
				linkId = maxMessage.id;
			} else {
				linkId = null;
			}
		}
	}
	
	private Message maximum() {
		return Collections.max(messages);
	}
	
	private void doSendMessage(String receiver, Message m) throws Throwable {
		Context namingContext = new InitialContext(); 
		Node RMIreceiver = (Node) namingContext.lookup(receiver);
		RMIreceiver.sendMessage(m);
	}
	
	private void doSendAck(String receiver) throws Throwable {
		Context namingContext = new InitialContext(); 
		Node RMIreceiver = (Node) namingContext.lookup(receiver);
		RMIreceiver.sendAck(Config.NodeID);
	}
	
	// REMOTE METHODS
	
	public void sendMessage(Message m) throws java.rmi.RemoteException {
		messages.add(m);
	}
	
	public void sendAck(String fromNode) throws java.rmi.RemoteException {
		acks.add(fromNode);
	}
}

// TODO: ID SHOULD BE STRING!!!

class Message implements Comparable<Message> {
	public int level;
	public String id;
	
	public Message(int level, String id) {
		this.level = level;
		this.id = id;
	}

	@Override
	public int compareTo(Message m) {
		int c1 = new Integer(level).compareTo(new Integer(m.level));
		if (c1 != 0)
			return c1;
		return new Integer(id).compareTo(new Integer(m.id));
	}
}
