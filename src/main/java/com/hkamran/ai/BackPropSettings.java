package com.hkamran.ai;

public class BackPropSettings extends Settings {

	double learningRate = 0.2;
	
	public Settings setLearningRate(double rate) {
		this.learningRate = rate;
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
	}
	
}
