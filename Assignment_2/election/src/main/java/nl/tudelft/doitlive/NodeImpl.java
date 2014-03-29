package nl.tudelft.doitlive;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class NodeImpl implements Node {
	private static final long serialVersionUID = -7172794904772647591L;
	
	private int id;
	
	private List<Message> messages;
	private List<Integer> acks;
	
	private Candidate candidate;
	private Ordinary ordinary;
	
	public NodeImpl(int id) {
		this.id = id;

		messages = new ArrayList<>();
		acks = new ArrayList<>();

		List<Integer> E = new ArrayList<>();
		for (int n = 0; n < Config.numNodes; n++)
			if(n != id)
				E.add(n);

		candidate = new Candidate(id, E);
		ordinary = new Ordinary(id);
		
	}

	@Override
	public synchronized void sendMessage(Message m) throws RemoteException {
		messages.add(m);
		System.out.println(id + " >> [NodeImpl]  sendMessage: " + m);
		System.out.println(id + " >> [NodeImpl]  msg list:   " + messages + "["+ messages.hashCode() +"]");
	}

	@Override
	public synchronized void sendAck(int fromNode) throws RemoteException {
		acks.add(fromNode);
	}

	@Override
	public synchronized boolean pulseOrdinary() throws RemoteException {
		try {
			System.out.println(id + " >> [pulseOrd]  messages: " + messages);
			ordinary.run(messages);
			System.out.println(id + " >> [pulseOrd] finished - clearing msg list " + messages);
			messages.clear();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	@Override
	public synchronized boolean pulseCandidate() throws RemoteException {
		try {
			if (candidate.canLee() && candidate.run(acks))
				return true;
			System.out.println(id + " >> [pulseCand] finished - clearing ack list: " + acks);
			acks.clear();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

}
