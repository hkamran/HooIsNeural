package com.hkamran.ai;

import com.hkamran.ai.Activations.Activation;

public class LayerBuilder {

	Layer layer;

	private LayerBuilder() {
		layer = new Layer();
	}
	
	public static LayerBuilder create() {
		return new LayerBuilder();
	}
	
	public LayerBuilder addNode(Activation activation) {
		this.layer.addNode(activation);
		return this;
	}
	
	public LayerBuilder addNodes(int num, Activation activation) {
		this.layer.addNodes(num, activation);
		return this;
	}
	

	
	
}
