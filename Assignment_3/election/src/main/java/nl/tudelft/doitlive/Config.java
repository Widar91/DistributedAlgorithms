package nl.tudelft.doitlive;

public class Config {

	public static final String[] nodes = {
		"rmi://127.0.0.1:1099/0",
		"rmi://127.0.0.1:1099/1",
		"rmi://127.0.0.1:1099/2",
		"rmi://127.0.0.1:1099/3",
		"rmi://127.0.0.1:1099/4",
		"rmi://127.0.0.1:1099/5",
		"rmi://127.0.0.1:1099/6"
	};
	
//	public static final String[] localNodes = {
//		"rmi://145.94.47.44:1099/0",
//		"rmi://145.94.47.44:1099/1",
//		"rmi://145.94.47.44:1099/2"
//	};
	
//	public static final String[] nodes = {
//		"rmi://145.94.47.44:1099/0",
//		"rmi://145.94.47.44:1099/1",
//		"rmi://145.94.47.44:1099/2",
//		"rmi://145.94.191.124:1099/3",
//		"rmi://145.94.191.124:1099/4",
//		"rmi://145.94.191.124:1099/5"
//	};
	
	public static final int faultyNodes = 0;
	
	public static final int numLocalNodes = 7;
}
