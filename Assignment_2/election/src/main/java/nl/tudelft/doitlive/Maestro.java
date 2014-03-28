package nl.tudelft.doitlive;

import java.util.ArrayList;
import java.util.List;

/**
 * Maestro = Master
 * 
 * @author Magister
 */
public class Maestro {

	public static void main(String[] args) throws Throwable {
		List<Node> nodes = new ArrayList<Node>();
		for (String node : Config.nodes) {
			nodes.add((Node) java.rmi.Naming.lookup(node));
		}

		while (true) {
			for (Node n : nodes) {
				Thread.sleep(1000);
				if (n.pulseCandidate())
					return;
			}

			for (Node n : nodes) {
				Thread.sleep(1000);
				n.pulseOrdinary();
			}
		}
	}

}
