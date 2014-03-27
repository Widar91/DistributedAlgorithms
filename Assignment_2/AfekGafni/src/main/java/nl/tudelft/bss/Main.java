package nl.tudelft.bss;

import java.rmi.RMISecurityManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main 
{
    public static void main( String[] args ) throws NamingException
    {
        System.out.println( "Hello World!" );
        //Security Manager setup
        if (System.getSecurityManager() == null)
        	System.setSecurityManager(new RMISecurityManager()); 
        
//        Context namingContext = new InitialContext();
//        namingContext.bind(Config.NodeID, node);
        
    }
}
