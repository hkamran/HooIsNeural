package com.hkamran.ai;

import java.util.LinkedList;
import java.util.List;

public class NetworkBuilder {

	public static enum NetworkType {
		NEURAL, BACKPROP, EVOLUTION
	}
	
	public static class ConnectionRequest {		
		int fromLayerIndex;
		int fromNodeIndex;
		int toLayerIndex;
		int toNodeIndex;
		double weight;
		
		public ConnectionRequest(
				int fromLayerIndex, int fromNodeIndex, 
				int toLayerIndex, int toNodeIndex, double weight) {
			this.toLayerIndex = toLayerIndex;
			this.toNodeIndex = toNodeIndex;
			this.fromLayerIndex = fromLayerIndex;
			this.fromNodeIndex = fromNodeIndex;
			this.weight = weight;
		}
	}
	

	NetworkSettings settings = new NetworkSettings();
	boolean hasBias = false;
	List<Layer> hidden = new LinkedList<Layer>();
	Layer input;
	Layer output;
	
	Network network;
	boolean allConnection = false;
	NetworkType type;
	
	List<ConnectionRequest> requests = new LinkedList<ConnectionRequest>();
	private boolean hasVisualizer;
	
	private NetworkBuilder(NetworkType type) {
		this.type = type;
		if (type == NetworkType.BACKPROP) {
			this.network = new BackPropNetwork();
		} else if (type == NetworkType.NEURAL) {
			this.network = new Network();
		} else {
			throw new RuntimeException("Not implemented");
		}
	}
	
	public static NetworkBuilder create(NetworkType type) {
		return new NetworkBuilder(type);
	}
	
	public NetworkBuilder withInputLayer(Layer layer) {
		network.setInputLayer(layer);
		return this;
	}
	
	public NetworkBuilder withOutputLayer(Layer layer) {
		network.setOutputLayer(layer);
		return this;
	}
	
	public NetworkBuilder addHiddenLayer(LayerBuilder builder) {
		builder.layer.setLabel("HIDDEN");
		hidden.add(builder.layer);
		return this;
	}
	
	public NetworkBuilder setInputLayer(LayerBuilder builder) {
		builder.layer.setLabel("INPUT");
		this.input = builder.layer;
		return this;
	}
	
	public NetworkBuilder setOutputLayer(LayerBuilder builder) {
		builder.layer.setLabel("OUTPUT");
		this.output = builder.layer;
		return this;
	}
	
	public NetworkBuilder setLabel(String label) {
		network.setLabel(label);
		return this;
	}
	
	public NetworkBuilder withBiasNode() {
		hasBias = true;
		return this;
	}
	
	public NetworkBuilder withVisualizer() {
		hasVisualizer = true;
		return this;
	}
	
	public NetworkBuilder createAllConnections() {
		allConnection = true;
		return this;
	}
	
	public NetworkBuilder createConnection(int fromLayerIndex, int fromNodeIndex, int toLayerIndex, int toNodeIndex, double weight) {
		ConnectionRequest request = new ConnectionRequest(fromLayerIndex, fromNodeIndex,
				toLayerIndex, toNodeIndex, weight);
		requests.add(request);
		return this;
	}
	
	public Network build() {
		
		if (input == null || output == null) 
			throw new RuntimeException("Missing Input/Output layer!");
		if (!allConnection && requests.size() == 0) {
			throw new RuntimeException("No Connection has been made!");
		}
		
		network.setInputLayer(input);
		for (Layer layer : hidden) {
			network.addHiddenLayer(layer);
		}
		network.setOutputLayer(output);
		
		if (hasBias) {
			network.addBiasNode();
		}
		
		for (ConnectionRequest request : requests) {
			Layer fromLayer = network.getAllLayers().get(request.fromLayerIndex);
			Node fromNode = fromLayer.getNode(request.fromNodeIndex);
			
			Layer toLayer = network.getAllLayers().get(request.toLayerIndex);
			Node toNode = toLayer.getNode(request.toNodeIndex);
			
			Connection connection = new Connection(fromNode, toNode);
			connection.weight =  request.weight;
			
			toLayer.addConnection(connection);
		}
		
		this.settings.apply(network);
		
		if (allConnection) {
			network.createAllConnection();
		}
		
		if (hasVisualizer) {
			Visualizer visual = new Visualizer(network);
			network.setVisualizer(visual);
		}
		
		
		return network;
	}

	public NetworkBuilder withSettings(NetworkSettings settings) {
		this.settings = settings;
		return this;
	}
	
	
}
