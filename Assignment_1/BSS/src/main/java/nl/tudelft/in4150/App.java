package nl.tudelft.in4150;

import java.security.Permission;
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
        	System.setSecurityManager(new SecurityManager() {
        	    public void checkConnect (String host, int port) {}
        	    public void checkConnect (String host, int port, Object context) {}
        	    public void checkPermission (Permission perm) {}
        	    public void checkPermission (Permission perm, Object context) {}
        	    public void checkPropertyAccess () {}
        	    public void checkPropertyAccess (String key) {}
        	 }); 
        
        //Registry creation
//        try {
//        	java.rmi.registry.LocateRegistry.getRegistry(Integer.parseInt(args[1])); 
//        } catch (RemoteException e) {
//        	java.rmi.registry.LocateRegistry.createRegistry(Integer.parseInt(args[1]));
//        }
        
    	int nodeId = Integer.parseInt(args[0]);

    	List<BSSNode> nodes = new ArrayList<BSSNode>();
		nodes.add(new BSSNode(0));
		nodes.add(new BSSNode(1));
		nodes.add(new BSSNode(2));

		Context namingContext = new InitialContext(); 
		
		nodes.parallelStream()
			 .forEach( n -> {
				 
				 try {
					namingContext.bind("rmi:" + n.getNodeId(), n);
				 } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				
				 List<String> ms = new ArrayList<String>();
				 ms.add(n.getNodeId() + " -> Message 1");
				 ms.add(n.getNodeId() + " -> Message 2");
				 ms.add(n.getNodeId() + " -> Message 3");
				 n.sendMessage(ms);
					 
			 });
		
    }
    
}
