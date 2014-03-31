package nl.tudelft.doitlive;

import java.io.Serializable;
import java.util.List;

public class Candidate implements Serializable {
	private static final long serialVersionUID = -8849649310762035742L;

	private int id;
	private int level;
	private List<Integer> E;
	private int K;
	private boolean canLee;
	
	public Candidate(int id, List<Integer> E) {
		this.id = id;
		this.E = E;
		this.level = -1;
		this.canLee = true;
	}
	
	public boolean run(List<Integer> acks) throws Throwable {
		level++;
		K = Math.min((int)Math.pow(2, level/2), Math.abs(E.size()));
		if (level % 2 == 0) {
			if (E.isEmpty()) {
				System.out.println(id + " >> I HAVE BEEN ELECTED!!");
				return true;
			} else {
				for (int i = 0; i < K; i++) {
					Integer n = E.get(0);
					System.out.println(id + " >> [Candidate] sending msg to " + Config.localNodes[n]);
					
					Node RMIreceiver = (Node) java.rmi.Naming.lookup(Config.localNodes[n]);
					RMIreceiver.sendMessage(new Message(level, id));
					//Maestro.nodes.get(n).sendMessage(new Message(level, id));
					
					E.remove(0);
				}
			}
		} else if (acks.size() < K) {
				System.out.println(id + " >> I CAN LEE ANYMOOOO'");
				canLee = false;
		}
		return false;
	}
	
	public boolean canLee() {
		return canLee;
	}
}
