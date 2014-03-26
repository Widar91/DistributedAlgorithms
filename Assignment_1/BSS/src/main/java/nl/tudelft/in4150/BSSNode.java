package nl.tudelft.in4150;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

public class BSSNode extends UnicastRemoteObject implements BSS_RMI {
	
	private static final long serialVersionUID = -8797608050445201096L;

	private int nodeId;
	private int[] localVectorClock;	
	private List<Message> buffer;
	private List<String> networkNodes;
	
	protected BSSNode(int id) throws RemoteException {
		super();
		this.nodeId = id;
		this.networkNodes = Utils.getConfiguration(nodeId, "nodes");
		this.localVectorClock = new int[networkNodes.size()];
		this.buffer = new ArrayList<Message>();
	}


	@Override 
	public synchronized void processMessage(Message msg) throws RemoteException {
		
		System.out.println(nodeId + ">> Receiving Message...");
		
		if(canBeDelivered(msg)) {
			
			//Deliver the message
			System.out.println("Received Message: " + msg);
			
			//Increase message count of the sender
			updateVectorClock(msg.getSenderId());
			
			
			//Check for pending messages in the buffer
			for(int i = 0; i < buffer.size(); i++ ) {
				
				Message m = buffer.get(i);
				
				if(canBeDelivered(m)) { 
					
					System.out.println("Delivering [Buffered] Message: " + m);
					
					updateVectorClock(m.getSenderId());
					buffer.remove(i);
					i = -1;
					
				}
			}
				
		}
		else {
			System.out.println(nodeId + ">> [Buffered] Message cannot be displayed");
			buffer.add(msg);
		}
		
		System.out.println("buffer" + buffer);
	}


	public synchronized void sendMessage(List<String> msgBodies) {
		
		//Spawn a new Thread for sending messages
		new Thread ( () -> {
			
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
 	 		}
			
			msgBodies.stream()
					 .forEach(msgBody -> {
			
						Message msg = null;
						
						//no modifications must be conducted between the update of the Vector Clock and the creation of the message
						synchronized(localVectorClock) {
							
							//Increase message count for this node
							updateVectorClock(nodeId);
							
							//Create the message
							msg = new Message(nodeId, msgBody, localVectorClock.clone());
						
						}
						
						//Broadcast the message
						broadcastMessage(msg);
			
					 });
			
		}).start();
	}

	
	private void updateVectorClock(int index) {
		synchronized(localVectorClock){ 
			localVectorClock[index] += 1; 
		}
	}
	
	
	private void broadcastMessage(Message msg) {
		
		networkNodes.parallelStream()
					.forEach( receiverURL -> {
						int receiverId = Integer.parseInt("" + receiverURL.charAt(receiverURL.length() - 1)); 
						if(receiverId != nodeId) {
							try {
								
								//Thread.sleep(Utils.getDelay(nodeId, receiverId));
								
								System.out.println(nodeId + ">> Sending Message to " + receiverURL /*+ ": " + msg.toString()*/);
								
								Context namingContext = new InitialContext(); 
								BSS_RMI RMIreceiver = (BSS_RMI) namingContext.lookup(receiverURL);
								RMIreceiver.processMessage(msg);
								
								//System.out.println(nodeId + ">> Message Sent.");
								
							} catch (Exception e) { 
								e.printStackTrace(); 
							}
						}
					});

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
