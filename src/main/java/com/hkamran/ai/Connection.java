package com.hkamran.ai;

public class Connection {

	public double weight;
	Node from;
	Node to;
	boolean enabled = true;
	
	public Connection(Node from, Node to) {
		this.from = from;
		this.to = to;
		this.weight = 0;
	}
	
	public Connection(Node from, Node to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}	
	
	public Connection(Node from, Node to, Double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public String toString() {
		return from + " TO (WEIGHT:" + weight + ") " + to;
	}
	
	@Override
	public int hashCode() {
		String str = from.getId() + " " + to.getId();
		return str.hashCode();
	}
	
	@Override 
	public boolean equals(Object obj) {
		if ((obj instanceof Connection)) return false;
		Connection connection = (Connection) obj;
		if (connection.from == this.from 
				&& connection.to == this.to
				&& connection.weight == this.weight) {
			return true;
		}
		return false;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean val) {
		this.enabled = val;
	}
	
}
