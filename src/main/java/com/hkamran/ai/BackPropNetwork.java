package com.hkamran.ai;

import java.util.Collections;
import java.util.List;

public class BackPropNetwork extends Network {

	double[][] inputDataSet;
	double[][] targetDataSet;
	double totalError = Double.MAX_VALUE;

	public void train() {
		if (inputDataSet.length != targetDataSet.length)
			throw new RuntimeException("input data size does not match target data size!");
		
		this.totalError = 0;
		for (int j = 0; j < targetDataSet.length; j++) {
			double[] input = inputDataSet[j];
			double[] target = targetDataSet[j];
			
			this.forwardPropogate(input);
			this.backPropogate(input, target);
		}
		
	}
	
	private void backPropogate(double[] input, double[] target) {
		if (input.length != this.input.size() || target.length != this.output.size())
			throw new RuntimeException("Input size does not match target size!");
		
		Layer output = this.output;
		List<Layer> layers = this.getLayers();
		Collections.reverse(layers);
		
		for (int i = 0; i < output.getNodes().size(); i++) {
			Node node = output.getNode(i);
			double delta = target[i] - node.getOutput();
			node.error = delta;
			this.totalError += Math.pow(delta, 2);
		}
		
		for (Layer layer : layers) {
			for (Node node : layer.getNodes()) {
				List<Connection> connections = layer.getConnections(node);
				for (Connection connection : connections) {
					
					connection.from.error += connection.weight * connection.to.error;
				}
			}
		}

		for (Layer layer : layers) {
			for (Node node : layer.getNodes()) {
				for (Connection connection : layer.getConnections(node)) {
					
					double error = connection.to.error;
					double derivative = connection.to.getDeriOutput();
					double out = connection.from.getOutput();
					
					double adjustment = -1 * error * derivative * out;
					double newWeight = connection.weight - adjustment;
					connection.weight = newWeight;
				}
				node.clear();
			}
		}
		
		if (visualizer != null) {
			visualizer.repaint();
		}
	}

	private void forwardPropogate(double[] input) {
		setInput(input);
		calculate();
	}

	
	public double[][] getInputDataSet() {
		return inputDataSet;
	}
	
	public double[][] getTargetDataSet() {
		return targetDataSet;
	}
	
	public double getTotalError() {		
		return totalError;
	}

	public void setTrainingDataSet(double[][] inputs, double[][] targets) {
		if (targets.length != inputs.length)
			throw new RuntimeException("Input data size does not match training data size!");
				
		
		this.inputDataSet = inputs;
		this.targetDataSet = targets;
	}
	

	public BackPropSettings getSettings() {
		return (BackPropSettings) settings;
	}


	
}
