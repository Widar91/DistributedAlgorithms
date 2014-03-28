package nl.tudelft.doitlive;

import java.util.ArrayList;
import java.util.List;

public class Main 
{
    public static void main( String[] args ) throws Throwable
    {
//    	if (System.getSecurityManager() == null)
//        	System.setSecurityManager(new SecurityManager() {
//        	    public void checkConnect (String host, int port) {}
//        	    public void checkConnect (String host, int port, Object context) {}
//        	    public void checkPermission (Permission perm) {}
//        	    public void checkPermission (Permission perm, Object context) {}
//        	    @SuppressWarnings("unused")
//				public void checkPropertyAccess () {}
//        	    public void checkPropertyAccess (String key) {}
//        	});
    	
    	List<Node> nodes = new ArrayList<>();
    	
    	for (int i = 0; i < Config.numNodes; i++) {
    		Node n = new NodeImpl(i);
    		System.out.println("* node " + i);
    		nodes.add(n);
    		java.rmi.Naming.bind(Config.nodes[i], n);
    	}
    	
    	while (true) {}
    	
    }
}
