package com.hkamran.ai;

/**
 * A class that would hold a collection of activation types.
 * @author hkamran
 */
public class Activations {

	public static Activation sigmoid = new Activation() {
		public double calculate(double x) {
			return 1 / (1 + Math.pow(Math.E, -x));
		}
		
		public double derivative(double x) {
			return x * (1 - x);
		}
	};
}
