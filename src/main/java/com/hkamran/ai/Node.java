package com.hkamran.ai;

/**
 * This class represents a single node in a neural network.
 * @author hkamran
 *
 */
public class Node {

	private static int IdCounter = 0;
	private static int unknownCounter = 0;
	
	private double input = 0;
	public double error = 0;
	private final int id;
	private final int index;
	public Layer layer;
	public String label;
	public Activation activation;
	
	public Node(Layer layer, Activation activation) {
		this.layer = layer;
		this.activation = activation;
		this.id = IdCounter++;
		this.index = (layer != null) ? this.layer.size() : -1;
		this.label = "Node " + (layer == null ? unknownCounter++ : this.layer.size());
	}
	
	
	public void clear() {
		this.input = 0;
		this.error = 0;
	}
	
	public void setInput(double val) {
		this.input = val;
	}
	
	public double getInput() {
		return this.input;
	}
	
	public void addToInput(double val) {
		this.input += val;
	}
	
	public double getOutput() {
		return activation.calculate(this.input);
	}
	
	public double getDeriOutput() {
		return activation.derivative(getOutput());
	}

	@Override
	public int hashCode() {
		return ("NODE: " + this.id).hashCode();
	}
	
	@Override
	public boolean equals(Object a) {
		if (!(a instanceof Node)) return false;
		Node node = (Node) a;
		if (node.hashCode() != this.hashCode()) return false;
		if (node.id != this.id) return false;
		return true;
	}
	
	public String toString() {
		String result = String.format("[%s %s (output: %f, input: %f, error: %f)]", 
				this.layer == null ? "NONE" : this.layer.label, this.label, this.getOutput(), this.getInput(), this.error);

		return result;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	protected Integer getId() {
		return id;
	}
	
}
