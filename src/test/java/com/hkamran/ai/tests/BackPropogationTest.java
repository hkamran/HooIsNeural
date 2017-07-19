package com.hkamran.ai.tests;

import java.util.Scanner;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropogate;
import com.hkamran.ai.Layer;
import com.hkamran.ai.Network;
import com.hkamran.ai.Visualizer;

public class BackPropogationTest {
	
	public static void main(String[] args) throws InterruptedException {
		Visualizer visual;
		Network network = new Network(2, 1, true);
		network.setLabel("XOR GATE");
		
		Layer layer1 = new Layer("HIDDEN");
		layer1.addNodes(4, Activations.sigmoid);

		network.addHiddenLayer(layer1);
		network.createAllConnection();
		
		BackPropogate back = new BackPropogate(network);

		visual = new Visualizer(back);
		back.setVisualizer(visual);
		back.setTrainingDataSet(new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}}, 
							 new double[][] {{0}, {1}, {1}, {0}});
		

		Thread.sleep(1000);
		int time = 300;
		for (int i = 0; i < time; i++) {
			back.train(500);
			Thread.sleep(10);
			System.out.println(time - i);
		}
		
		System.out.println("Finished!");
		
		testNetwork(visual, back);		
	}

	private static void testNetwork(Visualizer visual, Network network) {
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
			visual.repaint();
		
			for (int i = 0; i < network.getOutput().length; i++) {
				System.out.println("Output: " + Math.round(network.getOutput()[i]) + " [" + network.getOutput()[i] + "]");
			}
		}
	}

}
