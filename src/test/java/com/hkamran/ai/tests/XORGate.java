package com.hkamran.ai.tests;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropNetwork;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class XORGate {

	public static void main(String[] args) throws InterruptedException {
		BackPropNetwork neural = 
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
			.createAllConnections()
			.build();
		
		neural.setTrainingDataSet(
				new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}}, 
				new double[][] {{0}, {1}, {1}, {0}});
		

		Thread.sleep(1000);
		int time = 0;
		do {
			neural.train(1);
			Thread.sleep(10);
			System.out.println("Cycle:" + (time++) + " Error:" + neural.getTotalError());
		} while (neural.getTotalError() > 0.06);
		
		testNetwork(neural);
	}
	
	private static void testNetwork(Network network) {
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
