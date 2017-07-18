package com.hkamran.ai.tests.activations;

import org.junit.Assert;
import org.junit.Test;

import com.hkamran.ai.Activation;


public class Sigmoid {

	@Test
	public void test() {
		double output = Activation.sigmoid.apply((double) 2);
		Assert.assertTrue(Activation.sigmoid.apply((double) 2) == 0.8807970779778823);
	}
	
}
