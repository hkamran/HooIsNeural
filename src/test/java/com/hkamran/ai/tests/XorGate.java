package com.hkamran.ai.tests;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropogateNetwork;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class XorGate {

	public static void main(String[] args) throws InterruptedException {
		BackPropogateNetwork neural = 
			(BackPropogateNetwork) NetworkBuilder
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
			.addBiasNode()
			.withVisualizer()
			.createAllConnections()
			.build();
		
		neural.setTrainingDataSet(
				new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}}, 
				new double[][] {{0}, {1}, {1}, {0}});
		

		Thread.sleep(1000);
		int time = 300;
		for (int i = 0; i < time; i++) {
			neural.train(500);
			Thread.sleep(10);
			System.out.println(time - i);
		}
		
		testNetwork(neural);
	}
	
	private static void testNetwork(Network network) {
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
