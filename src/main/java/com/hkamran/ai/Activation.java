package com.hkamran.ai;

import java.util.function.Function;

public class Activation {

	public static Function<Double, Double> sigmoid = x -> {
		return 1 / (1 + Math.pow(Math.E, -x));
	};

	public static class derivatives {

		public static Function<Double, Double> sigmoid = x -> {
			return x * (1 - x);
		};

	}

}
