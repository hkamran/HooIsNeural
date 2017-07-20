package com.hkamran.ai;

import java.util.LinkedList;
import java.util.List;

public class Network {

	List<Layer> layers = new LinkedList<Layer>();
	Node bias  = new Node(null, Activations.sigmoid);
	
	Layer input;
	Layer output;
	boolean hasBias;
	Visualizer visualizer;
	String label;
	
	public Network() {
		
	}
	
	public void setInputLayer(Layer layer) {
		this.input = layer;
		layer.setLabel("INPUT");
		if (layers.size() == 0) {
			layers.add(layer);
			return;
		}
		layers.set(0, layer);
	}
	
	public void setOutputLayer(Layer layer) {
		if (this.output != null) {
			this.output = null;
			layers.remove(layers.size() - 1);
		}
		this.output = layer;
		this.output.setLabel("OUTPUT");
		layers.add(this.output);
	}
	
	protected Network(Network network) {
		this.layers = network.layers;
		this.bias = network.bias;
		this.input = network.input;
		this.output = network.output;
		this.hasBias = network.hasBias;
		this.visualizer = network.visualizer;
	}
	
	public void createAllConnection() {
		int i = 1;
		do {
			Layer first = layers.get(i - 1);
			Layer second = layers.get(i);
			
			for (Node firstNode : first.nodes) {
				for (Node secondNode : second.nodes) {
					Connection connection = new Connection();
					connection.from = firstNode;
					connection.to = secondNode;
					connection.weight = getRandomInt(-1, 1);
					second.addConnection(connection);
				}
			}
			i++;
		} while ( i < layers.size());
		
		if (!hasBias) return;
		
		for (i = 1; i < layers.size() - 1; i++) {
			Layer layer = layers.get(i);
			for (Node node : layer.nodes) {
				Connection connection  = new Connection();
				connection.from = bias;
				connection.to = node;
				connection.weight = getRandomInt(-2, 1);
				layer.addConnection(connection);
				
			}
		}
	}

	private double getRandomInt(int min, int max) {
		double result = (Math.random() * (max - min)) + min;
		return result;
	}
	
	public void createConnection(int fromLayerIndex, int fromLayerNodeIndex, 
			int toLayerIndex, int toLayerNodeIndex) {
		
		Layer fromLayer = layers.get(fromLayerIndex);
		Node fromNode = fromLayer.getNode(fromLayerNodeIndex);
		
		Layer toLayer = layers.get(toLayerIndex);
		Node toNode = toLayer.getNode(toLayerNodeIndex);
		
		if (fromNode == null || toNode == null) 
			throw new RuntimeException("Invalid connection");
		
		Connection connection = new Connection();
		connection.from = fromNode;
		connection.to = toNode;
		connection.weight =  getRandomInt(-1, 1);
		
		
		toLayer.addConnection(connection);
	}
	
	public void addHiddenLayer(Layer layer) {
		this.layers.add(layer);
	}
	
	public void setInput(double[] vals) {
		if (input.nodes.size() != vals.length) 
			throw new RuntimeException("Input size does not match network input size!");
		
		for (int i = 0; i < vals.length; i++) {
			Node node = input.nodes.get(i);
			node.setInput(vals[i]);
		}
	}
	
	public void calculate() {
		for (int i = 1; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.calculate();
			if (visualizer != null) visualizer.repaint();
		}
	}

	public void clear() {
		for (int i = 1; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.clear();
		}
		if (visualizer != null) visualizer.repaint();
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
	
	public List<Layer> getHiddenLayer() {
		return layers.subList(1, layers.size() - 2);
	}
	
	public Layer getInputLayer() {
		return input;
	}
	
	public List<Layer> getLayers() {
		return new LinkedList<Layer>(layers);
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
		List<Layer> cLayers = new LinkedList<Layer>();
		
		for (Layer layer : layers) {
			Layer cLayer = new Layer();
			cLayer.label = layer.label;
			cLayers.add(cLayer);
		}
		
		Network cNetwork = new Network();
		cNetwork.hasBias = this.hasBias;
		cNetwork.label = this.label;
		cNetwork.setLayers(cLayers);
		
		return cNetwork;
	}

	private void setLayers(List<Layer> cLayers) {
		this.input = cLayers.get(0);
		this.layers = cLayers;
		this.output = cLayers.get(cLayers.size() - 1);
		
	}
}
