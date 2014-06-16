package nl.tudelft.doitlive;

import java.rmi.RemoteException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

public class Main 
{
    public static void main( String[] args ) throws Throwable
    {
    	if (System.getSecurityManager() == null)
        	System.setSecurityManager(new SecurityManager() {
        	    public void checkConnect (String host, int port) {}
        	    public void checkConnect (String host, int port, Object context) {}
        	    public void checkPermission (Permission perm) {}
        	    public void checkPermission (Permission perm, Object context) {}
        	    @SuppressWarnings("unused")
				public void checkPropertyAccess () {}
        	    public void checkPropertyAccess (String key) {}
        	});
    	
        // Registry creation
    	java.rmi.registry.LocateRegistry.createRegistry(1099);  
//        try {
//        	java.rmi.registry.LocateRegistry.getRegistry(1099); 
//        	System.out.println("Registry created");
//        } catch (RemoteException e) {
//        	java.rmi.registry.LocateRegistry.createRegistry(1099);
//        	System.out.println("Registry located");
//        }
    	
    	List<Node> nodes = new ArrayList<>();
    	for (int i = 0; i < Config.numLocalNodes; i++) {
    			
    			Node n = new NodeImpl(i, i == 3);
    			System.out.println("* node " + i);
    			nodes.add(n);
    			try {
					java.rmi.Naming.bind(Config.nodes[i], n);
				} catch (Exception e) {
					e.printStackTrace();
				}
    			
    	}
    	
    	while (true) {}
    	
    }
}
