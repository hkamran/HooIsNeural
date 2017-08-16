package com.hkamran.ai;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NeatNetwork extends Network implements Comparable<NeatNetwork> {
	
	double fitness;
	int size;
	
	List<NeatNetwork> population = new LinkedList<NeatNetwork>();
	NeatFitness trainer;
	
	public void setPopulationSize(int size) {
		this.size = size;
	}
	
	private void createPopulation(NeatNetwork network) {
		population = new LinkedList<NeatNetwork>();
		for (int i = 0; i < size; i++) {
			NeatNetwork cNetwork = (NeatNetwork) network.clone();
			cNetwork.mutate();
			population.add(cNetwork);
		}
	}
	
	public void setFitness(NeatFitness fitness) {
		this.trainer = fitness;
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
	
	public void copulateConnections(NeatNetwork father, NeatNetwork mother, NeatNetwork child) {
		List<Connection> fatherConnections = new LinkedList<Connection>(father.getConnections());
		List<Connection> motherConnections = new LinkedList<Connection>(mother.getConnections());
		
		int expected = Math.max(fatherConnections.size(), motherConnections.size());
		int count = 0;

		while (count < expected) {
			int chance = (int) Math.round(getRandom(0, 1));
			if (chance > 0.5 && fatherConnections.size() > 0) {
				int index = (int) Math.round(getRandom(0, fatherConnections.size() - 1));
				Connection connection = fatherConnections.get(index);
				boolean result = copulateInsertConnection(father, connection, child);
				fatherConnections.remove(index);
				child.toString();
				if (result) count++;
			} else if (chance <= 0.5 && motherConnections.size() > 0) {
				int index = (int) Math.round(getRandom(0, motherConnections.size() - 1));
				Connection connection = motherConnections.get(index);
				boolean result = copulateInsertConnection(mother, connection, child);
				child.toString();
				motherConnections.remove(index);
				if (result) count++;
			}
			
			if (child.getConnections().size() != count) {
				throw new RuntimeException("Mismatch merge");
			}
		}
	}
	
	private boolean copulateInsertConnection(NeatNetwork parent, Connection connection, NeatNetwork child) {
		int toLayerIndex = getLayerIndex(parent, connection.to.layer);
		int fromLayerIndex = toLayerIndex - 1;
		
		if (fromLayerIndex < 0) return false;
		if (toLayerIndex < 0) return false;
		
		int fromNodeIndex = connection.from.getIndex();
		int toNodeIndex = connection.to.getIndex();
		
		if (toNodeIndex < 0) return false;
		
		Node a;
		if (fromLayerIndex >= 0 && connection.from.layer != null) {
			a = child.getNode(fromLayerIndex, fromNodeIndex);
		} else {
			a = child.bias;
		}
		Node b = child.getNode(toLayerIndex, toNodeIndex);
		if (a == null || b == null) return false;
		
		Connection cChild = new Connection(a, b, connection.weight);
		if (child.hasConnection(cChild)) return false;
		
		child.addConnection(cChild);

		return true;
	}

	public NeatNetwork train(int generation) {
		if (trainer == null) return null;
		if (size < 1) return null;
		
		if (population.size() < size) {
			createPopulation(this);
		}
		
		for (int i = 0; i < generation; i++) {
			for (NeatNetwork person : population) {
				double fitness = trainer.calculate(person);
				person.fitness = fitness;
			}
		}
		
		NeatNetwork result = evolve();
		result.trainer = this.trainer;
		result.size = this.size;
		result.visualizer = this.visualizer;
		result.label = this.label;
		result.settings = getSettings();
		result.fitness = trainer.calculate(result);
		this.population = new LinkedList<NeatNetwork>();
		
		this.become(result);
		return result;
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
			
			Random random = getSettings().getRandomizer();
			
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() <= getSettings().adjustWeightChance 
				&& mutateWeight()) {
				mutateCount++;
			} 
			
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() <= getSettings().addConnectionChange
				&& mutateConnection()) {
				mutateCount++;
			} 
			
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() <= getSettings().addNodeChance
				&& mutateNode()) {
				mutateCount++;
			}  
			
			if (mutateCount < getSettings().maxMutations
				&& random.nextDouble() <= getSettings().addLayerChance
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

		if (layer.size() >= getSettings().hiddenNodeCap) return false;
		layer.addNode(Activations.sigmoid);
		

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
		List<Connection> connections = generateAllConnections();
		if (connections.size() == 0) return false;
		int index = (int) Math.round(getRandom(0, connections.size() - 1));
		Connection connection = connections.get(index);
		addConnection(connection);
		return true;
	}

	public boolean mutateWeight() {
		List<Connection> connections = getConnections();
		if (connections.size() == 0) return false;
		int index = (int) Math.round(getRandom(0, connections.size() - 1));
		Connection connection = connections.get(index);
		double adjustment = getRandom(-1, 1) * getSettings().adjustmentChange;
		connection.weight += adjustment;
		if (connection.weight > getSettings().maxConnectionWeight) {
			connection.weight = getSettings().maxConnectionWeight;
		}
		if (connection.weight < getSettings().minConnectionWeight) {
			connection.weight = getSettings().minConnectionWeight;
		}
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
		if (this.fitness > neat.fitness) {
			return -1;
		} else if (this.fitness < neat.fitness) {
			return 1;
		} else {
			return 0;
		}
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
	public void become(Network network) {
		if (!(network instanceof NeatNetwork)) return;
		NeatNetwork neat = (NeatNetwork) network;
		this.size = neat.size;
		this.fitness = neat.fitness;
		this.trainer = neat.trainer;
		super.become(network);
		
	}
	
	public double getFitness() {
		return fitness;
	}
	
}
