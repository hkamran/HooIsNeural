package com.hkamran.ai.tests.neat;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatFitness;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NeatSettings;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;
import com.hkamran.ai.Node;
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
				.addHiddenLayer(						
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
						.create()
						.setRandomSeed(3000))
				.build();	
		
		testViaUserInput(network);
	}
	
	public static class XORFitness implements NeatFitness {

		@Override
		public long calculate(Network network) {
			double[][] inputs = new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
			double[][] outputs = new double[][] {{0}, {1}, {1}, {0}};
			
			int score = 0;
			for (int i = 0; i < inputs.length; i++) {
				double[] input = inputs[i];
				double[] output = outputs[i];
				network.clear();
				network.setInput(input);
				network.calculate();
				
				double[] actual = network.getOutput();
				if (Math.round(actual[0]) == output[0]) {
					score++;
				}
			}
			System.out.println("  SCORE " + score);
			return score;
		}
		
	}
	
	private static void testViaUserInput(NeatNetwork network) throws InterruptedException {
		@SuppressWarnings({ "resource", "unused" })
		Scanner in = new Scanner(System.in);
		int i = 0;
		
		network.setFitness(new XORFitness());
		network.setPopulationSize(10);
		Visualizer visualizer = new Visualizer(network);
		network.setVisualizer(visualizer);
		
		while (true) {
			System.out.println("MUTATE " + (i++));
			network.train(1);
			System.out.println("FITNESS: " + network.getFitness());
			Thread.sleep(500);
		}
	}
}
