package com.hkamran.ai;

public class Connection {

	public double weight;
	Node from;
	Node to;

	
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
		if (!(obj instanceof Connection)) return false;
		Connection connection = (Connection) obj;
		if (connection.from == this.from 
				&& connection.to == this.to) {
			return true;
		}
		return false;
	}
	
	public Node getFrom() {
		return from;
	}
	
	public Node getTo() {
		return to;
	}
	
}
