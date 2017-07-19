package com.hkamran.ai;

public class NetworkBuilder {

	public static enum NetworkType {
		NEURAL, BACKPROP, EVOLUTION
	}
	
	Network network;
	
	private NetworkBuilder(NetworkType type) {
		if (type == NetworkType.BACKPROP) {
			this.network = new BackPropogate();
		} else if (type == NetworkType.NEURAL) {
			this.network = new Network();
		} else {
			throw new RuntimeException("Not implemented");
		}
	}
	
	public NetworkBuilder create(NetworkType type) {
		return new NetworkBuilder(type);
	}
	
	public void addLayer(Layer layer) {
		
	}
	
	public Network build() {
		return network;
	}
	
	
}
