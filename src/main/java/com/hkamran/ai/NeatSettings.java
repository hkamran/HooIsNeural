package com.hkamran.ai;

import com.hkamran.ai.Activations.Activation;

public class NeatSettings extends NetworkSettings {
	
	public double addConnectionChange = 0.5;
	public double addNodeChance = 0.5;
	public double adjustWeightChance = 0.15;
	public double addLayerChance = 0.01;
	
	int maxMutations = 10;
	double weightMutationStep = 2;
	int hiddenNodeCap = 2;
	int hiddenLayerCap = 1;
	double adjustmentChange = 0.2;
	Activation activation = Activations.sigmoid;
	long seed = 3000;
	
	public static NeatSettings create() {
		return new NeatSettings();
	}
	
	public NeatSettings setRandomSeed(long seed) {
		this.seed = seed;
		return this;
	}
	
	@Override
	protected void apply(Network network) {
		if (!(network instanceof NeatNetwork)) return;
		NeatNetwork nNetwork = (NeatNetwork) network;
		nNetwork.setRandomSeed(seed);
	}
	
}