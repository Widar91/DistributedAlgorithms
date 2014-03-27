package nl.tudelft.bss;

import java.io.Serializable;
import java.rmi.Remote;

public interface Node extends Remote, Serializable {
	
	public void sendMessage(Message m) throws java.rmi.RemoteException;
	
	public void sendAck(String fromNode) throws java.rmi.RemoteException;
	
}
