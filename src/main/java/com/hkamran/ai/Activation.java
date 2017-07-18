package com.hkamran.ai;

import java.util.function.UnaryOperator;

public class Activation {

	public static UnaryOperator<Double> sigmoid = x -> 1 / (1 + (Math.pow(Math.E, x)));
	
	public static class derivatives {
		
		public static UnaryOperator<Double> sigmoid = x -> x * (1 - x);
		
	}
	
}
