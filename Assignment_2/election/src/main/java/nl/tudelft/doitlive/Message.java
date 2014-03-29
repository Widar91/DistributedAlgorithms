package nl.tudelft.doitlive;

import java.io.Serializable;

class Message implements Comparable<Message>, Serializable {
	private static final long serialVersionUID = 3220457928591303522L;

	public int level;
	public int id;
	
	public Message(int level, int id) {
		this.level = level;
		this.id = id;
	}

	@Override
	public int compareTo(Message m) {
		int c1 = new Integer(level).compareTo(new Integer(m.level));
		if (c1 != 0)
			return c1;
		return new Integer(id).compareTo(new Integer(m.id));
	}	
	
	@Override
	public String toString() {
		return "< lvl " + level + ", from " + id + ">";
	}
}
