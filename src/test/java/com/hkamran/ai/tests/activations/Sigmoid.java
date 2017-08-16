package com.hkamran.ai.tests.activations;

import org.junit.Assert;
import org.junit.Test;

import com.hkamran.ai.Activations;


public class Sigmoid {

	@Test
	public void calculateSigmoidTest() {
		double output = Activations.sigmoid.calculate((double) 2);
		Assert.assertTrue(output == 0.8807970779778823);
	}
	
	@Test
	public void calculateDerivativeSigmoidTest() {
		double output = Activations.sigmoid.derivative(0.5);
		Assert.assertTrue(output == 0.25);		
	}
	
	
}
