package nl.tudelft.doitlive;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
	private List<Message> ms1;
	private List<Message> ms2;
	private List<Message> ms3;
	private boolean isBetrayer;

	public NodeImpl(int id, boolean isBetrayer) throws Throwable {
		this.id = id;
		this.random = new Random();
		this.value = random.nextInt(2);
		this.ms1 = new ArrayList<>();
		this.ms2 = new ArrayList<>();
		this.ms3 = new ArrayList<>();
		this.isBetrayer = isBetrayer;
		System.out.println("Instantiating node, id: " + id + " value: " + value);
	}

	@Override
	public boolean pulse(int r) throws RemoteException {
		
		this.ms1 = new ArrayList<>();
		this.ms2 = new ArrayList<>();
		this.ms3 = new ArrayList<>();
		
		System.out.println("----- NOTIFICATION -----");
		broadcastMessage(new Message(Message.N, r, value));
		
		while(ms1.size() < Config.nodes.length - Config.faultyNodes) {/*System.out.println("[id] " + id + " [messages size] " + messages.size());*/}
		
		List<Message> ms = new ArrayList<>();
		synchronized(ms1) {
			for(Message m : ms1) {
				ms.add(m);
			}
		}
		
		System.out.println(id + "> [messages] " + ms);
//		int num0 = (int) messages.stream().filter(m -> m.value == 0).count();
//		int num1 = (int) messages.stream().filter(m -> m.value == 1).count();
		
		int num0 = 0;
		int num1 = 0;
		for(Message m : ms) {
			if (m.value == 0) {
				num0++;
			} else if (m.value == 1) {
				num1++;
			}
		}
		
		int c = Math.max(num0, num1);
		int w = c == num0 ? 0 : 1;
		
		System.out.println("----- PROPOSAL -----");
		if(c > (Config.nodes.length + Config.faultyNodes )/2) {
			broadcastMessage(new Message(Message.P, r, w));
		} else {
			broadcastMessage(new Message(Message.P, r, -13));
		}
		
		if(decided)
			return true;
		
		while(ms2.size() < Config.nodes.length - Config.faultyNodes) {System.out.println("[id] " + id + " [messages size] " + ms2.size());}
		
		List<Message> msp = new ArrayList<>();
		synchronized(ms2) {
			for(Message m : ms2) {
				msp.add(m);
			}
		}
		
//		num0 = (int) messages.stream().filter(m -> m.value == 0).count();
//		num1 = (int) messages.stream().filter(m -> m.value == 1).count();
		
		num0 = 0;
		num1 = 0;
		for(Message m : msp) {
			if (m.value == 0) {
				num0++;
			} else if (m.value == 1) {
				num1++;
			}
		}
		
		c = Math.max(num0, num1);
		w = c == num0 ? 0 : 1;

		System.out.println("----- DECISION -----");
		
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
		if(m.type == Message.N) {
			ms1.add(m);
		}
		else if (m.type == Message.P) {
			ms2.add(m);
		}
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
		if (isBetrayer) {
			System.out.println("I AM A BETRAYERRRR: " + id);
			msg.value = 0;
			//return;
		}
		
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
