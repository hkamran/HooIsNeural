package com.hkamran.ai;

public class Connection {

	public double weight;
	Node from;
	Node to;
	
	public String toString() {
		return from + " TO (WEIGHT:" + weight + ") " + to;
	}
	
}
