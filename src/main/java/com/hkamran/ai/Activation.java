package com.hkamran.ai;

/**
 * Interface for activation methods used in Nodes
 * @author hkamran
 *
 */
public interface Activation {
	public double calculate(double x);
	public double derivative(double x);
}
