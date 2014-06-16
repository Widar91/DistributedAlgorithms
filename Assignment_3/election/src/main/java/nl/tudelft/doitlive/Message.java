package nl.tudelft.doitlive;

import java.io.Serializable;

class Message implements Serializable {
	private static final long serialVersionUID = 3220457928591303522L;
	
	public static final int N = 0;
	public static final int P = 1;

	public int type;
	public int round;
	public int value;
	
	public Message(int type, int round, int value) {
		this.type = type;
		this.round = round;
		this.value = value;
	}

	@Override
	public String toString() {
		return "< type " + type + ", value " + value + ">";
	}
}
