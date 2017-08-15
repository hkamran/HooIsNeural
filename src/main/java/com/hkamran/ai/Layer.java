package com.hkamran.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hkamran.ai.Activations.Activation;

public class Layer {

	List<Node> nodes = new LinkedList<Node>();
	String label;


	Map<Node, Set<Connection>> mapping = new HashMap<Node, Set<Connection>>();

	public Layer(String label) {
		this.label = label;
	}

	public Layer() {

	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addNode(Node node) {
		this.nodes.add(node);
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
			mapping.put(connection.to, new HashSet<Connection>());

		}

		if (mapping.containsKey(connection.to)) {
			Set<Connection> list = mapping.get(connection.to);
			list.add(connection);
		}
	}
	
	public void removeConnection(Connection connection) {
		Node to = connection.to;
		if (to == null) return;
		Set<Connection> cur = mapping.get(to);
		if (cur == null) return;
		cur.remove(connection);
	}
	
	public Connection getConnection(Connection connection) {
		List<Connection> connections = getConnections(connection.to);
		if (connections == null || connections.size() == 0) return null;
		
		for (int i = 0; i < connections.size(); i++) {
			Connection c = connections.get(i);
			if (c.equals(connection)) return c;
		}
		return null;
	}
	
	public List<Connection> getConnections(Node node) {
		if (mapping.containsKey(node))
			return new LinkedList<Connection>(mapping.get(node));
		return new LinkedList<Connection>();
	}
	
	public void removeNode(int id) {
		Node node = this.nodes.get(id);
		mapping.remove(node);
	}

	public void clear() {
		for (Node node : nodes) {
			node.clear();
		}
		
		for (Node node : mapping.keySet()) {
			node.clear();
		}
	}
	
	public void removeConnections() {
		mapping = new HashMap<Node, Set<Connection>>();
	}

	public void calculate() {
		for (Node node : mapping.keySet()) {
			Set<Connection> connections = mapping.get(node);
			for (Connection connection : connections) {
				if (!connection.isEnabled()) continue;
				
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
		if (index <  0 || index >= nodes.size()) return null;
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
			Set<Connection> connections = mapping.get(node);
			for (Connection connection : connections) {
				totalConnections.add(connection);
			}
		}
		return totalConnections;
	}

	public Layer clone() {
		Layer cLayer = new Layer(this.label);
		for (Node node : this.nodes) {
			Node cNode = new Node(cLayer, node.activation);
			cLayer.addNode(cNode);
		}
		return cLayer;
	}


}
