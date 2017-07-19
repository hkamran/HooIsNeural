package com.hkamran.ai;

public class Activations {

	public static interface Activation {
		public double calculate(double x);
		public double derivative(double x);
	}
	
	public static Activation sigmoid = new Activation() {
		public double calculate(double x) {
			return 1 / (1 + Math.pow(Math.E, -x));
		}
		
		public double derivative(double x) {
			return x * (1 - x);
		}
	};
}
