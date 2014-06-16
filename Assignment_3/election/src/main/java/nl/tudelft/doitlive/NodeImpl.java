package nl.tudelft.doitlive;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;

public class NodeImpl extends UnicastRemoteObject implements Node {
	private static final long serialVersionUID = -7172794904772647591L;

	public int decision = -13;
	
	private int id;
	private int value;
	private boolean decided = false;
	private Random random; 
	private List<Message> messages;

	public NodeImpl(int id) throws Throwable {
		this.id = id;
		this.random = new Random();
		this.value = random.nextInt(2);
		this.messages = new ArrayList<>();
		System.out.println("Instantiating node, id: " + id + " value: " + value);
	}

	@Override
	public boolean pulse(int r) throws RemoteException {
		
		System.out.println("NOTIFICATION PHASE");
		broadcastMessage(new Message(Message.N, r, value));
		System.out.println("NOTIFICATION PHASE ENDED");
		
		while(messages.size() < Config.nodes.length - Config.faultyNodes) {/*System.out.println("[id] " + id + " [messages size] " + messages.size());*/}
		
		System.out.println(id + "> [messages] " + messages);
		int num0 = (int) messages.stream().filter(m -> m.value == 0).count();
		int num1 = (int) messages.stream().filter(m -> m.value == 1).count();
		int c = Math.max(num0, num1);
		int w = c == num0 ? 0 : 1;
		messages = new ArrayList<>();
		
		if(c > (Config.nodes.length + Config.faultyNodes)/2) {
			broadcastMessage(new Message(Message.P, r, w));
		} else {
			broadcastMessage(new Message(Message.P, r, -13));
		}
		
		if(decided)
			return true;
		
		while(messages.size() < Config.nodes.length - Config.faultyNodes) {}
		
		num0 = (int) messages.stream().filter(m -> m.value == 0).count();
		num1 = (int) messages.stream().filter(m -> m.value == 1).count();
		c = Math.max(num0, num1);
		w = c == num0 ? 0 : 1;
		messages = new ArrayList<>();
		
		if(c > Config.faultyNodes) {
			value = w;
			if(c > Config.faultyNodes * 3) {
				decision = w;
				decided = true;
				System.out.println("I HAVE DECIDED FELLAS: " + decision);
			}
		} 
		else if (!decided) {
			value = random.nextInt(2);
		}
		
		return decided;
	}
	
	@Override
	public synchronized boolean sendMessage(Message m) throws RemoteException {
		messages.add(m);
		System.out.println("message: " + m);
		return m != null;
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}
	
	@Override
	public int getDecision() throws RemoteException {
		return decision;
	}

	private void broadcastMessage(Message msg) {

		int numSent = 0;
		
		for (String receiverURL : Config.nodes) {
			//String[] parts = receiverURL.split("/");
			//int receiverId = Integer.parseInt(parts[parts.length-1]); 
			//if(receiverId != id) {
				try {
					System.out.println(Thread.currentThread().getId()+ "-" + id + ">> Sending Message to " + receiverURL + ": " + msg.toString());

					Context namingContext = new InitialContext(); 
					Node RMIreceiver = (Node) namingContext.lookup(receiverURL);
					while (!RMIreceiver.sendMessage(msg)) {}
					numSent++;
					//System.out.println("Sending ended");
					
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
			//}
		}
		
		System.out.println("**** NUM SENT:" + numSent);


	}
}
