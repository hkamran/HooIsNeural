package com.hkamran.ai.tests.neat;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.Layer;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NeatSettings;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class PlayGround {

	static NeatNetwork network;
	public static void main(String[] args) throws InterruptedException {
		network = 
				(NeatNetwork) NetworkBuilder
				.create(NetworkType.EVOLUTION)
				.setLabel("XOR GATE")
				.setInputLayer(
						LayerBuilder
						.create()
						.addNodes(2, Activations.sigmoid))
				.setOutputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid)
						)
				.withBiasNode()
				.withVisualizer()
				.withSettings(
						NeatSettings
						.create())
				.createAllConnections()
				.build();	


		testViaUserInput(network);
	}
	
	private static void testViaUserInput(NeatNetwork network) throws InterruptedException {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		int i = 0;
		while (true) {
			System.out.println("MUTATE " + (i++));
			//network.mutate();
			network.mutate();	
			Thread.sleep(50);
		}
	}
}
