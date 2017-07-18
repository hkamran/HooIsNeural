package com.hkamran.ai;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Layer {

	List<Node> nodes = new LinkedList<Node>();
	String label;
	
	Map<Node, List<Connection>> mapping = new HashMap<Node, List<Connection>>();
	
	public Layer(String label) {
		this.label  = label;
	}
	
	public Layer() {
		
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void addNode() {
		this.nodes.add(new Node(this));
	}
	
	public void addNodes(int num) {
		for (int i = 0; i < num; i++) {
			this.nodes.add(new Node(this));
		}
	}
	
	public void addConnection(Connection connection) {
		if (!mapping.containsKey(connection.to)) {
			mapping.put(connection.to, new LinkedList<Connection>());
			
		} 
		
		if (mapping.containsKey(connection.to)) {
			List<Connection> list = mapping.get(connection.to);
			list.add(connection);
		}
	}
	
	public List<Connection> getConnections(Node node) {
		if (mapping.containsKey(node)) return mapping.get(node);
		return new LinkedList<Connection>();
	}
	
	public void removeNode(int id) {
		Node node = this.nodes.get(id);
		mapping.remove(node);
	}
	
	public void clear() {
		for (Node node : mapping.keySet()) {
			node.clear();
		}
	}
	
	public void calculate() {
		for (Node node : mapping.keySet()) {
			List<Connection> connections = mapping.get(node);
			for (Connection connection : connections) {
				Node from = connection.from;
				Node to = connection.to;
				
				to.addToInput(from.getOutput() * connection.weight);
			}		
		}

	}
	
	public int size() {
		return nodes.size();
	}
	

	
	public Node getNode(int index) {
		return nodes.get(index);
	}
	
	
	
	public String toString() {
		return label + " nodes: " + nodes.size();
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

	public void createNode() {
		Node node = new Node(this);
		nodes.add(node);
		
	}
	
	public List<Connection> getConnections() {
		List<Connection> totalConnections = new LinkedList<Connection>();
		for (Node node : mapping.keySet()) {
			List<Connection> connections = mapping.get(node);
			for (Connection connection : connections) {
				totalConnections.add(connection);
			}
		}
		return totalConnections;
	}
	

	public void createNodes(int num) {
		for (int i = 0; i < num; i++) {
			createNode();
		}
	}
	
}
