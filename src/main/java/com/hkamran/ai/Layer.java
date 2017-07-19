package com.hkamran.ai;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hkamran.ai.Activations.Activation;

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
	
	public void addNode(Activation activation) {
		this.nodes.add(new Node(this, activation));
	}
	
	public void addNodes(int num, Activation activation) {
		for (int i = 0; i < num; i++) {
			this.nodes.add(new Node(this, activation));
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
	
}
