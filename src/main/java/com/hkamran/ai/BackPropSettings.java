package com.hkamran.ai;

public class BackPropSettings extends NetworkSettings {

	double learningRate = 0.2;
	
	public BackPropSettings setLearningRate(double rate) {
		this.learningRate = rate;
		return this;
	}
	
	@Override
	public BackPropSettings setRandomSeed(long seed) {
		this.seed = seed;
		super.setRandomSeed(seed);
		return this;
	}

	public static BackPropSettings create() {
		return new BackPropSettings();
	}
	

	
}
