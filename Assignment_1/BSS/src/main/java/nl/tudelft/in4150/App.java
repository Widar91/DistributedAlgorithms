package nl.tudelft.in4150;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Hello world!
 *
 */
public class App {
	
    public static void main( String[] args ) throws Throwable {
    	
        //Security Manager setup
        //if (System.getSecurityManager() == null)
        //	System.setSecurityManager(new RMISecurityManager()); 
        
//        //Registry creation
//        try {
//        	java.rmi.registry.LocateRegistry.getRegistry(Integer.parseInt(args[1])); 
//        } catch (RemoteException e) {
//        	java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(args[1]));
//        }
        
    	int nodeId = Integer.parseInt(args[0]) - 1;

    	BSSNode node;
		node = new BSSNode(nodeId);
		
		System.out.println("Node["+ nodeId +"] Initialized..");
		
		Context namingContext = new InitialContext(); 
		namingContext.bind("rmi:" + nodeId, node);
		
		
		node.sendMessage("Porco di dio");
		node.sendMessage("Tortellini");
		node.sendMessage("The Anszver is Guwac");


    }
    
}
