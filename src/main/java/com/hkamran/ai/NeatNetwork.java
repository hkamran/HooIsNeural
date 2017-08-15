package com.hkamran.ai;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NeatNetwork extends Network implements Comparable<NeatNetwork> {

	
	List<Connection> added = new LinkedList<Connection>();
	List<Connection> removed = new LinkedList<Connection>();
	int fitness;
	
	List<NeatNetwork> population = new LinkedList<NeatNetwork>();

	
	public NeatNetwork() {
		super();
		this.seed = 3000;
		this.settings = (NeatSettings) super.settings;
	}
	
	public void createPopulation(int size) {
		for (int i = 0; i < size; i++) {
			NeatNetwork network = (NeatNetwork) this.clone();
			network.mutate();
			population.add(network);
		}
	}
	
	public void setFitness() {
		
	}
	
	public NeatNetwork copulate(NeatNetwork network) {
		NeatNetwork child = new NeatNetwork();
		
		Layer input = new Layer("INPUT");
		input.addNodes(network.input.size(), getSettings().activation);
		Layer output = new Layer("OUTPUT");
		output.addNodes(network.output.size(), getSettings().activation);
		
		child.setInputLayer(input);		
		child.setOutputLayer(output);
		
		copulateLayers(this, network, child);
		copulateNodes(this, network, child);
		copulateConnections(this, network, child);
		
		child.setSettings(this.settings);
		child.createAllConnection(false);
		
		if (child.visualizer != null) visualizer.repaint();
		
		return child;
	}

	private void copulateLayers(NeatNetwork father, NeatNetwork mother, NeatNetwork child) {
		
		int fatherSize = father.getHiddenLayers().size();
		int motherSize = mother.getHiddenLayers().size();
		
		int chance = (int) Math.round(getRandom(0, 1));
		int hiddenLayers = chance > 0.5 ? fatherSize : motherSize;
		
		for (int i = 0; i < hiddenLayers; i++) {
			child.addHiddenLayer(new Layer());
		}
	}
	
	private void copulateNodes(NeatNetwork father, NeatNetwork mother, NeatNetwork child) {

		List<Layer> fatherLayer = father.getHiddenLayers();
		List<Layer> motherLayer = mother.getHiddenLayers();
		
		for (int i = 0; i < child.getHiddenLayers().size(); i++) {
			int fatherCount = i < fatherLayer.size() ? fatherLayer.get(i).size() : 0;
			int motherCount = i < motherLayer.size() ? motherLayer.get(i).size() : 0;
			
			Layer layer = child.getLayer(i + 1);
			
			int chance = (int) Math.round(getRandom(0, 1));
			if (chance > 0.5) {
				layer.addNodes(fatherCount, getSettings().activation);
			} else {
				layer.addNodes(motherCount, getSettings().activation);
			}
			
		}
	}
	
	private void copulateConnections(NeatNetwork father, NeatNetwork mother, NeatNetwork child) {
		List<Connection> fatherConnections = new LinkedList<Connection>(father.added);
		List<Connection> motherConnections = new LinkedList<Connection>(mother.added);
		
		int expected = (fatherConnections.size() + motherConnections.size()) / 2;
		int count = 0;
		
		while (count < expected && fatherConnections.size() + motherConnections.size() >= expected) {
			int chance = (int) Math.round(getRandom(0, 1));
			if (chance > 0.5 && fatherConnections.size() > 0) {
				int index = (int) Math.round(getRandom(0, fatherConnections.size() - 1));
				Connection connection = father.added.get(index);
				boolean result = copulateInsertConnection(father, connection, child);
				fatherConnections.remove(index);
				if (result) count++;
			} else if (chance <= 0.5 && motherConnections.size() > 0) {
				int index = (int) Math.round(getRandom(0, motherConnections.size() - 1));
				Connection connection = mother.added.get(index);
				boolean result = copulateInsertConnection(mother, connection, child);
				motherConnections.remove(index);
				if (result) count++;
			}
		}
	}
	
	private boolean copulateInsertConnection(NeatNetwork parent, Connection connection, NeatNetwork child) {
		int toLayerIndex = getLayerIndex(parent, connection.to.layer);
		int fromLayerIndex = toLayerIndex - 1;
		
		if (fromLayerIndex == -1) return false;
		if (toLayerIndex < 0) return false;
		
		int fromNodeIndex = connection.from.getIndex();
		int toNodeIndex = connection.to.getIndex();
		
		if (fromNodeIndex < 0 || toNodeIndex < 0) return false;
		
		Node a = child.getNode(fromLayerIndex, fromNodeIndex);
		Node b = child.getNode(toLayerIndex, toNodeIndex);
		if (a == null || b == null) return false;
		
		Connection cChild = child.addConnection(a, b, connection.weight);
		cChild.enabled = true;
		return cChild != null;
	}

	public void train() {
		
	}
	
	public NeatNetwork evolve() {
		Collections.sort(population);
		NeatNetwork mother = population.get(0);
		NeatNetwork father = population.get(1);
		
		NeatNetwork child = mother.copulate(father);
		child.mutate();
		
		return child;
	}
	
	public void mutate() {

		int mutateCount = 0;
		while (mutateCount < getSettings().maxMutations) {
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() >= getSettings().adjustWeightChance 
				&& mutateWeight()) {
				mutateCount++;
			}
			
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() >= getSettings().addConnectionChange
				&& mutateConnection()) {
				mutateCount++;
			}
	
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() >= getSettings().addNodeChance
				&& mutateNode()) {
				mutateCount++;
			}
			
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() >= getSettings().addLayerChance
				&& mutateLayer()) {
				mutateCount++;
			}
		}
		if (visualizer != null) visualizer.repaint();
	}
	
	public boolean mutateNode() {
		List<Layer> layers = getHiddenLayers();
		if (layers.size() == 0) {
			mutateLayer();
		}
		
		int index = (int) Math.round(getRandom(0, layers.size() - 1));
		Layer layer = layers.get(index);

		if (layer.size() == getSettings().hiddenNodeCap) return false;
		layer.addNode(Activations.sigmoid);
		
		//createConnections
		List<Connection> newConnections = new LinkedList<Connection>();
		
		newConnections.addAll(createConnections(index + 1, false));
		newConnections.addAll(createConnections(index + 2, false));
	
		for (Connection newConnection : newConnections) {
			removed.add(newConnection);
		}		

		return true;
	}

	public boolean mutateLayer() {
		if (getHiddenLayers().size() >= getSettings().hiddenLayerCap) return false;
		Layer layer = new Layer();
		addHiddenLayer(layer);

		//remove existing 
		Layer last = getOutputLayer();
		last.removeConnections();
		
		mutateNode();
		mutateConnection();
		
		return true;
	}
	
	public boolean mutateConnection() {
		if (removed.size() == 0) return false;
		int index = (int) Math.round(getRandom(0, removed.size() - 1));
		Connection connection = removed.get(index);
		connection.setEnabled(true);
		removed.remove(index);
		added.add(connection);
		return true;
	}

	public boolean mutateWeight() {
		if (added.size() == 0) return false;
		int index = (int) Math.round(getRandom(0, added.size() - 1));
		Connection connection = added.get(index);
		double adjustment = getRandom(-1, 1) * getSettings().adjustmentChange;
		connection.weight += adjustment;
		return true;
	}
	
	@Override
	public Network clone() {
		NeatNetwork neat = new NeatNetwork();
		neat.setSettings(settings);
		return this.cloneHelper(neat);
	}
	
	private NeatSettings getSettings() {
		return (NeatSettings) settings;
	}
	

	@Override
	public int compareTo(NeatNetwork neat) {
		return neat.fitness - this.fitness;
	}
	
	private int getLayerIndex(NeatNetwork network, Layer layer) {
		List<Layer> layers = network.getLayers();
		for (int i = 0; i < layers.size(); i++) {
			Layer curr = layers.get(i);
			if (curr == layer) return i;
		}
		return -1;
	}
	
	@Override
	public Connection addConnection(Connection connection) {
		if (connection.enabled) {
			this.added.add(connection);
		} else {
			this.removed.add(connection);
		}
		super.addConnection(connection);
		return connection;
	}
	
}
