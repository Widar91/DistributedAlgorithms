package nl.tudelft.doitlive;

import java.io.Serializable;
import java.rmi.Remote;

public interface Node extends Remote, Serializable {
	
	public boolean sendMessage(Message m) throws java.rmi.RemoteException;
	
	public boolean pulse(int r) throws java.rmi.RemoteException;

	public int getId() throws java.rmi.RemoteException; 
	
	public int getDecision() throws java.rmi.RemoteException; 
}
