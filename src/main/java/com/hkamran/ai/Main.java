package com.hkamran.ai;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Network network = new Network(2, 1, false);
		
		Layer layer1 = new Layer();
		layer1.addNode(Activations.sigmoid);
		layer1.addNode(Activations.sigmoid);
		
		Layer layer2 = new Layer();
		layer2.addNode(Activations.sigmoid);
		
		network.addHiddenLayer(layer1);
		network.addHiddenLayer(layer2);
		
//		network.createConnection(0, 0, 1, 0);
//		network.createConnection(0, 1, 1, 0);
		network.createAllConnection();
		
		Visualizer visual = new Visualizer(network);
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Enter: ");
			int size = network.getInputLayer().size();
			double[] input = new double[size];
			for (int i = 0; i < size; i++) {
				input[i] = in.nextDouble();
			}
			network.setInput(input);
			network.calculate();
			visual.repaint();
		}
		
	}
	
}
