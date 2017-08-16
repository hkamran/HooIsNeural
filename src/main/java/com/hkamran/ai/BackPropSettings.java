package com.hkamran.ai;

/**
 * A class that holds the settings used for Back-Propagating settings. 
 * @author hkamran
 *
 */
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
