package com.hkamran.ai.tests.gates;

import java.util.Scanner;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropNetwork;
import com.hkamran.ai.BackPropSettings;
import com.hkamran.ai.Connection;
import com.hkamran.ai.Layer;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class XORGateTest {
	
	private static final double MIN_ERROR = 0.0001;
	private static final int TRAINING_LIMIT = 250000;
	static BackPropNetwork network;
	
	@BeforeClass
	public static void xorTest() throws InterruptedException {
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
	

	
	@Test
	public void inputOneAndOneExpectZero() {
		network.clear();
		network.setInput(new double[] {1,1});
		network.calculate();
		double[] expected = new double[] {0};
		double[] actual = network.getOutput();
		for (int i = 0; i < actual.length; i++) {
			actual[i] = Math.round(actual[i]);
		}
		
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void inputZeroAndZeroExpectZero() {
		network.clear();
		network.setInput(new double[] {0, 0});
		network.calculate();
		double[] expected = new double[] {0};
		double[] actual = network.getOutput();
		for (int i = 0; i < actual.length; i++) {
			actual[i] = Math.round(actual[i]);
		}
		
		Assert.assertArrayEquals(expected, actual, 0);
	}	
	
	@Test
	public void inputOneAndZeroExpectOne() {
		network.clear();
		network.setInput(new double[] {1, 0});
		network.calculate();
		double[] expected = new double[] {1};
		double[] actual = network.getOutput();
		for (int i = 0; i < actual.length; i++) {
			actual[i] = Math.round(actual[i]);
		}

		Assert.assertArrayEquals(expected, actual, 0);
	}		
	
	@Test
	public void inputZeroAndOneExpectOne() {
		network.clear();
		network.setInput(new double[] {0, 1});
		network.calculate();
		double[] expected = new double[] {1};
		double[] actual = network.getOutput();
		for (int i = 0; i < actual.length; i++) {
			actual[i] = Math.round(actual[i]);
		}

		Assert.assertArrayEquals(expected, actual, 0);
	}		
	
	@Test
	public void copyInputOneAndOneExpectZero() {
		Network cNetwork = network.clone();
		
		cNetwork.setInput(new double[] {1,1});
		cNetwork.calculate();
		double[] expected = new double[] {0};
		double[] actual = cNetwork.getOutput();
		for (int i = 0; i < actual.length; i++) {
			actual[i] = Math.round(actual[i]);
		}
		
		Assert.assertArrayEquals(expected, actual, 0);
	}
	public void printWeights(Network network) {
		for (Layer layer : network.getAllLayers()) {
			for (Connection connection : layer.getConnections()) {
				System.out.println(connection.weight);
			}
		}
		System.out.println("");
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		xorTest();
		train(network);
		testViaUserInput(network);
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
