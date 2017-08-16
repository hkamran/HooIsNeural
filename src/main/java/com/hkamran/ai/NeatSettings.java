package com.hkamran.ai;

import com.hkamran.ai.Activations.Activation;

public class NeatSettings extends NetworkSettings {
	
	public double addConnectionChange = 1;
	public double addNodeChance = 0.9;
	public double adjustWeightChance = 0.9;
	public double addLayerChance = 0.9;
	
	int maxMutations = 1;
	int hiddenNodeCap = 2;
	int hiddenLayerCap = 1;
	int popSize = 10;
	
	double adjustmentChange = 0.08;
	double minConnectionWeight = Integer.MIN_VALUE;
	double maxConnectionWeight = Integer.MAX_VALUE;
	
	Activation activation = Activations.sigmoid;
	long seed = 3000;
	
	public static NeatSettings create() {
		return new NeatSettings();
	}
	
	public NeatSettings setRandomSeed(long seed) {
		this.seed = seed;
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
	

	
}