package nl.tudelft.in4150;

import java.io.Serializable;
import java.rmi.Remote;

public interface BSS_RMI extends Remote, Serializable {
	
	public void processMessage(Message m) throws java.rmi.RemoteException;
	
}
