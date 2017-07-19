package com.hkamran.ai;

public class Connection {

	public double weight;
	Node from;
	Node to;
	
	public Connection() {}
	public Connection(Node from, Node to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public String toString() {
		return from + " TO (WEIGHT:" + weight + ") " + to;
	}
	
}
