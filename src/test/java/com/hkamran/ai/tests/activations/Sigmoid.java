package com.hkamran.ai.tests.activations;

import org.junit.Assert;
import org.junit.Test;

import com.hkamran.ai.Activations;


public class Sigmoid {

	@Test
	public void test() {
		double output = Activations.sigmoid.calculate((double) 2);
		Assert.assertTrue(Activations.sigmoid.calculate((double) 2) == 0.8807970779778823);
	}
	
}
