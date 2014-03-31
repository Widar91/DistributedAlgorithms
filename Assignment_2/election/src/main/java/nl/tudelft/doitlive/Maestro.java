package nl.tudelft.doitlive;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * Maestro = Master
 * 
 * @author Magister
 */
public class Maestro {

	public static List<NodeImpl> nodes = new ArrayList<>();

	public static void main(String[] args) throws Throwable {
		
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
		
		
		// create registry, no need to do it from the commnad line
    	//java.rmi.registry.LocateRegistry.createRegistry(1099);  
    	
    	
    	// create nodes and bind them
//    	for (int i = 0; i < Config.numLocalNodes; i++) {
//    			
//    			NodeImpl n = new NodeImpl(i);
//    			System.out.println("* node " + i);
//    			nodes.add(n);
//    			try {
//					java.rmi.Naming.bind(Config.localNodes[i], n);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//    			
//    	}
		
    	java.rmi.registry.LocateRegistry.getRegistry(1099);
		List<Node> nodes = new ArrayList<Node>();
		for (String node : Config.nodes) {
			nodes.add((Node) java.rmi.Naming.lookup(node));
		}
		

    	// turns
		while (true) {
			System.out.println("\n===================== NEW ROUND =====================");
			System.out.println("*** CANDIDATE ***");
			for (Node n : nodes) {
				Thread.sleep(1000);
				if (n.pulseCandidate())
					return;
			}
			System.out.println("\n*** ORDINARY ***");
			for (Node n : nodes) {
				Thread.sleep(1000);
				n.pulseOrdinary();
			}
		}
	}

}
