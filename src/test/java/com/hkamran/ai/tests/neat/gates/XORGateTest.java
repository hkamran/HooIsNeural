package com.hkamran.ai.tests.neat.gates;

import java.util.Scanner;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatFitness;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NeatSettings;
import com.hkamran.ai.Network;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;
import com.hkamran.ai.Visualizer;

public class XORGateTest {
	private static final int TRAINING_LIMIT = 10000;
	static NeatNetwork network;
	static Visualizer visualizer;
	
	@BeforeClass
	public static void beforeClassSetup()  {
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
						.addNodes(0, Activations.sigmoid))
				.setOutputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid)
						)
				.withBiasNode()
				.withSettings(
						NeatSettings
						.create()
						.setRandomSeed(3000)
						.setHiddenNodeCap(4)
						.setHiddenLayerCap(1)
						.setMaxMutations(5)
						.setWeightAdjustment(1)
						.setMaxWeight(10.0)
						.setMinWeight(-10.0)
						.setPopulationSize(500))
				.build();	
		
		visualizer = new Visualizer(network);
		network.setVisualizer(visualizer);
		train(network);		
	}
	
	public static void main(String[] args) {
		beforeClassSetup();
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
	
	private static void train(NeatNetwork network)  {
		network.setFitness(new XORFitness());
		System.out.println("Evolving....");
		
		int generation = 0;
		while (network.getFitness() < 3.5) {
			network = network.train();
			if (visualizer != null) visualizer.network = network;
			System.out.println("FITNESS" + network.getFitness() + " " + generation);
			generation++;
			if (generation > TRAINING_LIMIT) {
				throw new RuntimeException("Unable to evolve!");
			}
		}
		XORGateTest.network = network;
		System.out.println("Finished at Generation: " + generation);
		System.out.println("Fitness: " + network.getFitness());
	}
	
	public static class XORFitness implements NeatFitness {

		@Override
		public double calculate(Network network) {
			double[][] inputs = new double[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
			double[][] outputs = new double[][] {{0}, {1}, {1}, {0}};
			
			double score = 0;
			for (int i = 0; i < inputs.length; i++) {
				double[] input = inputs[i];
				double[] output = outputs[i];
				network.clear();
				network.setInput(input);
				network.calculate();
				
				double[] actual = network.getOutput();
				double err = Math.abs(output[0] - actual[0]);
				score += output[0] == 0 ? 1.0 - err : 1.0 - (err * 2);
			}
			
			return score;
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
	
}
