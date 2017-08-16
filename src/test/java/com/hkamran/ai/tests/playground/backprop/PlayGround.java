package com.hkamran.ai.tests.playground.backprop;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropNetwork;
import com.hkamran.ai.BackPropSettings;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class PlayGround {

	private static final double MIN_ERROR = 0.01;
	private static final int TRAINING_LIMIT = 350000;
	
	public static void main(String[] args) {
		BackPropNetwork network;
		network = 
				(BackPropNetwork) NetworkBuilder
				.create(NetworkType.BACKPROP)
				.setLabel("XOR GATE")
				.setInputLayer(
						LayerBuilder
						.create()
						.addNodes(2, Activations.sigmoid))
				.addHiddenLayer(
						LayerBuilder
						.create()
						.addNodes(4, Activations.sigmoid))
				.setOutputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid)
						)
				.withBiasNode()
				.withVisualizer()
				.withSettings(
						BackPropSettings
						.create()
						.setLearningRate(0.04)
						.setRandomSeed(3000))
				.createAllConnections()
				.build();		
		
		network.setTrainingDataSet(
				new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}}, 
				new double[][] {{0}, {1}, {1}, {0}});
		
		train(network);
		testViaUserInput(network);
	}
	
	private static void train(BackPropNetwork network) {
		int cycle = 0;
		while (network.getTotalError() > MIN_ERROR && cycle < TRAINING_LIMIT) {
			network.clear();
			network.train(1);
			if (cycle % 10000 == 0) System.out.println(cycle);
			cycle++;
		};
		if (cycle >= TRAINING_LIMIT) {
			throw new RuntimeException("Unable to train network! " + network.getTotalError());
		}
	}	
	
	private static void testViaUserInput(Network network) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		while (true) {
			System.out.println("Enter: ");
			int size = network.getInputLayer().size();
			double[] input = new double[size];
			for (int i = 0; i < size; i++) {
				input[i] = in.nextDouble();
			}

			network.clear();
			network.setInput(input);
			network.calculate();
		
			for (int i = 0; i < network.getOutput().length; i++) {
				System.out.println("Output: " + Math.round(network.getOutput()[i]) + " [" + network.getOutput()[i] + "]");
			}
		}
	}
}
