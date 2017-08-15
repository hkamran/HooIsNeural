package com.hkamran.ai.tests.neat;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NeatSettings;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;
import com.hkamran.ai.Visualizer;

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

				.withSettings(
						NeatSettings
						.create())
				.createAllConnections()
				.build();	

		NeatNetwork a = (NeatNetwork) network.clone();
		a.mutate();
		a.mutate();
		Visualizer aV = new Visualizer(a);
		a.setVisualizer(aV);
		
		NeatNetwork b = (NeatNetwork) network.clone();
		b.mutate();
		b.mutate();
		Visualizer bV = new Visualizer(b);
		b.setVisualizer(bV);
		
		NeatNetwork c = a.copulate(b);
		Visualizer cV = new Visualizer(c);
		c.setVisualizer(cV);
		
		testViaUserInput(network);
	}
	
	private static void testViaUserInput(NeatNetwork network) throws InterruptedException {
		@SuppressWarnings({ "resource", "unused" })
		Scanner in = new Scanner(System.in);
		int i = 0;
		while (true) {
			System.out.println("MUTATE " + (i++));
			//network.mutate();
			Thread.sleep(10050);
		}
	}
}
