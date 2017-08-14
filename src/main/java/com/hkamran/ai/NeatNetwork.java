package com.hkamran.ai;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NeatNetwork extends Network implements Comparable<NeatNetwork> {

	
	List<Connection> added = new LinkedList<Connection>();
	List<Connection> removed = new LinkedList<Connection>();
	int fitness;
	
	public NeatNetwork() {
		super();
		this.settings = (NeatSettings) super.settings;
	}
	
	private void createPopulation(int size) {

		
		
	}
	
	public void setFitness() {
		
	}
	
	public void copulate() {
		
	}
	
	public void train() {
		
	}
	
	public void evolve() {
	}
	
	public void mutate() {

		int mutateCount = 0;
		
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
		
		return true;
	}
	
	public boolean mutateConnection() {
		if (removed.size() == 0) return false;
		int index = (int) getRandom(0, removed.size() - 1);
		Connection connection = removed.get(index);
		connection.setEnabled(true);
		removed.remove(index);
		added.add(connection);
		return true;
	}

	public boolean mutateWeight() {
		if (added.size() == 0) return false;
		int index = (int) getRandom(0, added.size() - 1);
		Connection connection = added.get(index);
		double adjustment = getRandom(-1, 1) * getSettings().adjustmentChange;
		connection.weight += adjustment;
		return true;
	}
	
	@Override
	public Network clone() {
		NeatNetwork neat = new NeatNetwork();
		return this.cloneHelper(neat);
	}
	
	private NeatSettings getSettings() {
		return (NeatSettings) settings;
	}
	

	@Override
	public int compareTo(NeatNetwork neat) {
		return neat.fitness - this.fitness;
	}
	
	
}
