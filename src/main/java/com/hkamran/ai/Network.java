package com.hkamran.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Network {

	public Node bias  = new Node(null, Activations.sigmoid);
	
	List<Layer> hidden = new LinkedList<Layer>();
	Layer input;
	Layer output;
	Visualizer visualizer;
	Set<Connection> connections = new HashSet<Connection>();
	
	boolean hasBias;
	String label;
	long seed = System.currentTimeMillis();
	Random random = new Random(seed);
	
	public Network() {
		
	}
	
	public void createAllConnection(boolean enable) {
		List<Layer> layers = getLayers();
		
		int i = 1;
		do {
			Layer first = layers.get(i - 1);
			Layer second = layers.get(i);
			
			for (Node firstNode : first.nodes) {
				for (Node secondNode : second.nodes) {
					Connection connection = new Connection(firstNode, secondNode);
					connection.weight = getRandomInt(-1, 1);
					addConnection(connection);
				}
			}
			i++;
		} while ( i < layers.size());
		
		if (!hasBias) return;
		
		//bias setup
		for (i = 1; i < layers.size() - 1; i++) {
			Layer layer = layers.get(i);
			for (Node node : layer.nodes) {
				Connection connection = new Connection(bias, node);
				connection.weight = getRandomInt(-2, 1);
				addConnection(connection);
			}
		}
	}

	protected double getRandomInt(int min, int max) {
		double result = (random.nextDouble() * (max - min)) + min;
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

	public void addConnection(Connection connection) {
		if (connection.from == null || connection.to == null) return;
		connections.add(connection);
		Layer toLayer = connection.to.layer;
		toLayer.addConnection(connection);
	}
	
	public void removeConnection(Connection connection) {
		if (connection.from == null || connection.to == null) return;
		connections.remove(connection);
		Layer toLayer = connection.to.layer;
		if (toLayer == null) return;
		toLayer.removeConnection(connection);
	}
	
	public boolean hasConnection(Connection connection) {
		return connections.contains(connection);
	}
	
	public Node getNode(int layerIndex, int nodeIndex) {
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
	
	public void addBiasNode() {
		this.hasBias = true;
		this.bias.setLabel("BIAS");
		this.bias.setInput(1);
	}

	public Network clone() {
		Network cNetwork = new Network();
		
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
					
					if (cFrom == null || cTo == null) {
						throw new RuntimeException("Error copy failure");
					}
					
					Connection cConnection = new Connection(cFrom, cTo);
					cConnection.setEnabled(connection.enabled);
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

		cNetwork.hasBias = this.hasBias;
		cNetwork.label = this.label;
		
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
	
	public void setRandomSeed(long seed) {
		this.seed = seed;
		random = new Random(seed);
	}
}
