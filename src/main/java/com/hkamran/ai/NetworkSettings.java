package com.hkamran.ai;

import java.util.Random;

/**
 * This class represents the basic settings that can be applied.
 * @author hkamran
 *
 */
public class NetworkSettings {

	public boolean hasBias = true;
	long seed = System.currentTimeMillis();
	Random random = new Random(seed);

	public Random getRandomizer() {
		return random;
	}
	
	public boolean hasBias() {
		return hasBias;
	}
	
	public NetworkSettings setRandomSeed(long seed) {
		this.seed = seed;
		this.random = new Random(seed);
		return this;
	}


	
}
