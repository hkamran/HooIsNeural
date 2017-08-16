package com.hkamran.ai.tests.backprop.gates;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropNetwork;
import com.hkamran.ai.BackPropSettings;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;

public class XORGateTest {
	
	private static final double MIN_ERROR = 0.0001;
	private static final int TRAINING_LIMIT = 250000;
	static BackPropNetwork network;
	
	@BeforeClass
	public static void beforeClassSetup() throws InterruptedException {
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
			network.train();
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
	
}
