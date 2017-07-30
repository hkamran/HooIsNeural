package com.hkamran.ai;

public class BackPropSettings extends NetworkSettings {

	double learningRate = 0.2;
	long seed = System.currentTimeMillis();
	
	public BackPropSettings setLearningRate(double rate) {
		this.learningRate = rate;
		return this;
	}
	
	public BackPropSettings setRandomSeed(long seed) {
		this.seed = seed;
		return this;
	}

	public static BackPropSettings create() {
		return new BackPropSettings();
	}
	
	@Override
	protected void apply(Network network) {
		if (!(network instanceof BackPropNetwork)) return;
		BackPropNetwork bNetwork = (BackPropNetwork) network;
		bNetwork.setLearningRate(learningRate);
		bNetwork.setRandomSeed(seed);
	}
	
}
