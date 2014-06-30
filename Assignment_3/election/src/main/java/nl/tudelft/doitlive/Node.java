package nl.tudelft.doitlive;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Node extends Remote, Serializable {
	
	public boolean sendMessage(Message m) throws java.rmi.RemoteException;
	
	//public boolean pulse(int r) throws java.rmi.RemoteException;
	
	public void pulse1(int r) throws RemoteException;
	
	public void pulse2(int r) throws RemoteException;
	
	public void pulse3(int r) throws RemoteException;

	public int getId() throws java.rmi.RemoteException; 
	
	public int getDecision() throws java.rmi.RemoteException; 
}
