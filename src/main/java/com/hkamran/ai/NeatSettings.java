package com.hkamran.ai;

/**
 * A class that holds the setings of a NEAT network.
 * @author hkamran
 *
 */
public class NeatSettings extends NetworkSettings {
	
	public double addConnectionChange = 0.3;
	public double addNodeChance = 0.1;
	public double adjustWeightChance = 0.5;
	public double addLayerChance = 0.1;
	
	int maxMutations = 1;
	int hiddenNodeCap = 2;
	int hiddenLayerCap = 1;
	int popSize = 10;
	
	double adjustmentChange = 0.08;
	double minConnectionWeight = Integer.MIN_VALUE;
	double maxConnectionWeight = Integer.MAX_VALUE;
	
	Activation activation = Activations.sigmoid;
	public long seed = 3000;
	
	public static NeatSettings create() {
		return new NeatSettings();
	}
	
	public NeatSettings setRandomSeed(long seed) {
		this.seed = seed;
		super.setRandomSeed(seed);
		return this;
	}
	
	public NeatSettings setHiddenNodeCap(int num) {
		this.hiddenNodeCap = num;
		return this;
	}
	
	public NeatSettings setHiddenLayerCap(int num) {
		this.hiddenLayerCap = num;
		return this;
	}
	
	public NeatSettings setMaxMutations(int num) {
		this.maxMutations = num;
		return this;
	}
	
	public NeatSettings setWeightAdjustment(double num) {
		this.adjustmentChange = num;
		return this;
	}
	

	public NeatSettings setMinWeight(double num) {
		this.minConnectionWeight = num;
		return this;
	}
	

	public NeatSettings setMaxWeight(double num) {
		this.maxConnectionWeight = num;
		return this;
	}
	
	public NeatSettings setPopulationSize(int num) {
		this.popSize = num;
		return this;
	}

	public int getPopulationSize() {
		return popSize;
	}
	
	public NeatSettings setActivation(Activation activation) {
		this.activation = activation;
		return this;
	}
	

	
}