package nl.tudelft.in4150;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Hello world!
 *
 */
public class App {
	
    public static void main( String[] args ) throws Throwable {
    	
        //Security Manager setup
        if (System.getSecurityManager() == null)
        	System.setSecurityManager(new RMISecurityManager()); 
        
        //Registry creation
        try {
        	java.rmi.registry.LocateRegistry.getRegistry(Integer.parseInt(args[1])); 
        } catch (RemoteException e) {
        	java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(args[1]));
        }
        
    	int nodeId = Integer.parseInt(args[0]);

    	BSSNode node;
		node = new BSSNode(nodeId);
		
		System.out.println("Node["+ nodeId +"] Initialized..");
		
		Context namingContext = new InitialContext(); 
		namingContext.bind(args[1], node);
		
		List<String> ms = new ArrayList<String>();
		ms.add(nodeId + " -> Message 1");
		ms.add(nodeId + " -> Message 2");
		ms.add(nodeId + " -> Message 3");
		node.sendMessage(ms);


    }
    
}
