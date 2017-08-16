package com.hkamran.ai.tests.neat;

import org.junit.BeforeClass;

import com.hkamran.ai.Activations;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NeatSettings;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class MutateConnectionTest {
	static NeatNetwork network;
	
	@BeforeClass
	public static void beforeClassSetup() throws InterruptedException {
		network = 
				(NeatNetwork) NetworkBuilder
				.create(NetworkType.EVOLUTION)
				.setLabel("XOR GATE")
				.setInputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid))
				.addHiddenLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid))
				.setOutputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid)
						)
				.withBiasNode()
				.withSettings(
						NeatSettings
						.create()
						.setRandomSeed(3000)
						.setHiddenLayerCap(1)
						.setHiddenNodeCap(1))
				.build();		
	}
	

	public void testConnectionBetweenLayers() throws InterruptedException {
		
		network.mutate();
		network.mutate();
		network.mutate();
		while (true) {
			
		}
	}
}
