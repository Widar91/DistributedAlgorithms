package nl.tudelft.in4150;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BSSNode extends UnicastRemoteObject implements BSS_RMI {
	
	
	private static final int[] networkNodes = Utils.getNetworkNodeIds();

	private int nodeId;
	private int[] localVectorClock;		//Needs to be synchronized
	private List<Message> buffer;
	
	protected BSSNode(int id) throws RemoteException {
		super();
		this.nodeId = id;
		this.localVectorClock = new int[networkNodes.length];
		this.buffer = new ArrayList<Message>();
	}

	@Override
	public void processMessage(Message msg) throws RemoteException {
		
		System.out.println(nodeId + ">> Receiving Message...");
		
		if(canBeDelivered(msg)) {
			
			//Deliver the message
			System.out.println("Received Message: " + msg.toString());
			
			//Increase message count of the sender
			localVectorClock[msg.getSenderId()] += 1;
			
			//Check for pending messages in the buffer
			for(int i = 0; i < buffer.size(); i++ ) { 
				if(canBeDelivered(buffer.get(i))) { 
					System.out.println("Received [Buffered] Message: " + buffer.get(i).toString());
					buffer.remove(i);
				}
			}
			
		}
		else {
			System.out.println(nodeId + ">> [Buffered] Message cannot be displayed" + msg.toString());
			buffer.add(msg);
		}
		
		System.out.println("\n");
	}
	
	public void sendMessage(String msgBody) {
		
		//Spawn a new Thread for sending messages
		new Thread ( () -> {
			
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//Increase message count for this node
			localVectorClock[nodeId] += 1;
			
			//Create the message
			Message msg = new Message(nodeId, msgBody, localVectorClock.clone());
			
			//Broadcast the message
			broadcastMessage(msg);
			
		}).start();;
	}

	private void broadcastMessage(Message msg) {
		
		for(int receiverId : networkNodes) {
			
			//If I'm not trying to send a message to myself
			if(receiverId != nodeId) {
				try {
					
					System.out.println(nodeId + ">> Sending Message to " + receiverId + ": " + msg.toString());
					
					//Lookup remote object
					Context namingContext = new InitialContext(); 
					BSS_RMI RMIreceiver = (BSS_RMI) namingContext.lookup("rmi:" + receiverId);
					
					//Call remote method - send the message
					RMIreceiver.processMessage(msg);
					
					System.out.println(nodeId + ">> Message Sent.\n\n");
					
					//Sleep before sending another message (create delay)
					try {
						if(nodeId == 0) Thread.sleep(10000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				} catch (Exception e) { e.printStackTrace(); }
			}
			
		}
	}
	
	private boolean canBeDelivered(Message msg) {
		int[] mvc = msg.getVectorClock();
		int[] lvc = localVectorClock.clone();
		
		//Compute V + e_j
		lvc[msg.getSenderId()] += 1;
		
		//Check delivery condition --- D_j(m) = ( V + e_j >= V_m )
		for (int i = 0; i < lvc.length; i++)
			if(!(lvc[i] >= mvc[i]))
				return false;
		
		return true;
	}
	
}
