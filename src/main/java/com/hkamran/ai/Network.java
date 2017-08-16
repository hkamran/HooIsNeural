package com.hkamran.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Network {

	int hiddenIndex = 0;
	public Node bias  = new Node(null, Activations.sigmoid);
	
	List<Layer> hidden = new LinkedList<Layer>();
	Layer input;
	Layer output;
	Visualizer visualizer;
	Set<Connection> connections = new HashSet<Connection>();
	
	String label;
	NetworkSettings settings = new NetworkSettings();
	
	public void createAllConnections() {
		List<Connection> connections = generateAllConnections();
		for (Connection connection : connections) {
			addConnection(connection);
		}
	}
	
	protected List<Connection> generateAllConnections() {
		List<Layer> layers = getLayers();
		List<Connection> connections = new LinkedList<Connection>();
		
		int i = 1;
		do {
			Layer first = layers.get(i - 1);
			Layer second = layers.get(i);
			
			for (Node firstNode : first.nodes) {
				for (Node secondNode : second.nodes) {
					Connection connection = new Connection(firstNode, secondNode);
					if (hasConnection(connection)) continue;
					connection.weight = getRandom(-1, 1);
					connections.add(connection);
				}
			}
			i++;
		} while ( i < layers.size());
		
		if (!settings.hasBias()) return connections;
		
		//bias setup
		for (i = 1; i < layers.size() - 1; i++) {
			Layer layer = layers.get(i);
			for (Node node : layer.nodes) {
				Connection connection = new Connection(bias, node);
				connection.weight = getRandom(-1, 1);
				connections.add(connection);
			}
		}
		
		return connections;
	}

	protected double getRandom(int min, int max) {
		double result = (settings.getRandomizer().nextDouble() * (max - min)) + min;
		return result;
	}
	

	
	public void calculate() {
		List<Layer> layers = getLayers();
		for (int i = 1; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.calculate();
			if (visualizer != null) visualizer.repaint();
		}
	}

	public void clear() {
		List<Layer> layers = getLayers();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.clear();
		}
		this.bias.clear();
		
		if (visualizer != null) visualizer.repaint();
	}	
	
	public Connection getConnection(Node a, Node b) {
		Connection aTob = new Connection(a, b);
		if (!connections.contains(aTob)) return null;
		Layer from = b.layer;
		if (from == null) return null;
		return from.getConnection(aTob);
	}
	
	public List<Connection> getConnections() {
		return new LinkedList<Connection>(connections);
	}

	public Connection addConnection(Connection connection) {
		if (connection.from == null || connection.to == null) return null;
		if (connections.contains(connection)) return null;
		connections.add(connection);
		Layer toLayer = connection.to.layer;
		toLayer.addConnection(connection);
		
		if (visualizer != null) visualizer.repaint();
		return connection;
	}
	
	public Connection addConnection(Node a, Node b) {
		Connection connection = new Connection(a, b, getRandom(-1, 1));
		this.addConnection(connection);
		return connection;
	}
	
	public Connection addConnection(Node a, Node b, double weight) {
		Connection connection = new Connection(a, b, weight);
		this.addConnection(connection);
		return connection;
	}
	
	public void removeConnection(Connection connection) {
		if (connection.from == null || connection.to == null) return;
		if (!connections.contains(connection)) return;
		connections.remove(connection);
		Layer toLayer = connection.to.layer;
		if (toLayer == null) return;
		toLayer.removeConnection(connection);
	}
	
	public boolean hasConnection(Connection connection) {
		return connections.contains(connection);
	}
	
	public Node getNode(int layerIndex, int nodeIndex) {
		if (layerIndex == -1 && settings.hasBias()) return bias; 
		Layer layer = getLayer(layerIndex);
		if (layer == null) return null;
		return layer.getNode(nodeIndex);
	}
	
	public Layer getLayer(int layerIndex) {
		List<Layer> layers = getLayers();
		if (layerIndex >= layers.size()) return null;
		
		Layer layer = layers.get(layerIndex);
		return layer;
	}
	
	
	public double[] getOutput() {
		double[] result = new double[output.size()];
		for (int i = 0; i < output.size(); i++) {
			result[i] = output.getNode(i).getOutput();
		}
		return result;
	}

	public Layer getOutputLayer() {
		return output;
	}
	
	public void setOutputLayer(Layer layer) {
		this.output = layer;
		this.output.setLabel("OUTPUT");
	}	
	
	public List<Layer> getHiddenLayers() {
		return hidden;
	}
	
	public void setHiddenLayers(List<Layer> layers) {
		this.hidden = layers;
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.setLabel("HIDDEN " + i);
		}
	}
	
	public void addHiddenLayer(Layer layer) {
		layer.setLabel("HIDDEN " + ++hiddenIndex);
		this.hidden.add(layer);
	}	
	
	public void setInput(double[] vals) {
		if (input.nodes.size() != vals.length) 
			throw new RuntimeException("Input size does not match network input size!");
		
		for (int i = 0; i < vals.length; i++) {
			Node node = input.nodes.get(i);
			node.setInput(vals[i]);
		}
	}
	
	public void setInputLayer(Layer layer) {
		this.input = layer;
		layer.setLabel("INPUT");
	}
	
	public Layer getInputLayer() {
		return input;
	}	
	
	public List<Layer> getLayers() {
		List<Layer> layers = new LinkedList<Layer>();
		layers.add(input);
		layers.addAll(hidden);
		layers.add(output);
		return layers;
	}
		
	public void setVisualizer(Visualizer visualizer) {
		this.visualizer = visualizer;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	protected void setSettings(NetworkSettings settings) {
		this.settings = settings;
	}
	
	public void addBiasNode() {
		settings.hasBias = true;
		this.bias.setLabel("BIAS");
		this.bias.setInput(1);
	}

	@Override
	public Network clone() {
		Network cNetwork = new Network();
		return cloneHelper(cNetwork);
	}
	
	protected Network cloneHelper(Network cNetwork) {
		
		//Copy node, and layers
		List<Layer> cLayers = new LinkedList<Layer>();
		Map<Node, Node> mapping = new HashMap<Node, Node>();
		for (Layer layer : this.getLayers()) {
			Layer cLayer = new Layer();
			for (Node node : layer.getNodes()) {
				Node cNode = new Node(cLayer, node.activation);
				mapping.put(node, cNode);
				cLayer.addNode(cNode);
			}
			cLayers.add(cLayer);
		}
		
		mapping.put(this.bias, cNetwork.bias);
		
		//Copy connections
		List<Layer> layers = getLayers();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			
			for (Node node : layer.getNodes()) {
				for (Connection connection : layer.getConnections(node)) {
					Node from = connection.from;
					Node to = connection.to;
					double weight = connection.weight;
					
					Node cFrom = mapping.get(from);
					Node cTo = mapping.get(to);
					double cWeight = weight;
					
					
					if (cTo == null) {
						throw new RuntimeException("Error copy failure");
					}
					
					if (cFrom == null) {
						cFrom = cNetwork.bias;
					}
					
					Connection cConnection = new Connection(cFrom, cTo);
					cConnection.weight = cWeight;
					
					cNetwork.addConnection(cConnection);
				}
			}
		}
		
		
		int inputIndex = 0;
		int outputIndex = cLayers.size() - 1;
		
		Layer cInput = cLayers.get(inputIndex);
		Layer cOutput = cLayers.get(outputIndex);
		List<Layer> cHidden = cLayers.subList(inputIndex + 1, outputIndex);
		
		cNetwork.setInputLayer(cInput);
		cNetwork.setOutputLayer(cOutput);
		cNetwork.setHiddenLayers(cHidden);

		cNetwork.label = this.label;
		cNetwork.setSettings(this.settings);
		
		return cNetwork;
	}

	public String toString() {
		StringBuffer content = new StringBuffer();
		for (Layer layer : getLayers()) {
			for (Connection connection : layer.getConnections()) {
				content.append(connection.toString() + System.lineSeparator());
			}
		}
		return content.toString();
	}
	
	public void become(Network network) {
		this.hidden = network.hidden;
		this.input = network.input;
		this.output = network.output;
		this.visualizer = network.visualizer;
		this.connections = network.connections;
		

		this.label = network.label;
		this.settings = network.settings;
		this.hiddenIndex = network.hiddenIndex;
		
		if (visualizer != null) {
			visualizer.network = this;
			visualizer.repaint();
		}
	}
	
	public void withVisualizer() {
		Visualizer visualizer = new Visualizer(this);
		this.visualizer = visualizer;
		this.visualizer.repaint();
	}
	
	public NetworkSettings getSettings() {
		return settings;
	}
}
