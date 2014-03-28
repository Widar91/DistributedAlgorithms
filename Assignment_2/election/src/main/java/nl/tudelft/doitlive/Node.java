package nl.tudelft.doitlive;

import java.io.Serializable;
import java.rmi.Remote;

public interface Node extends Remote, Serializable {
	
	public void sendMessage(Message m) throws java.rmi.RemoteException;
	
	public void sendAck(int fromNode) throws java.rmi.RemoteException;
	
	public boolean pulseCandidate() throws java.rmi.RemoteException;

	public boolean pulseOrdinary() throws java.rmi.RemoteException;
	
}
