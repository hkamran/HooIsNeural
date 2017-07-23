package com.hkamran.ai;

import java.util.LinkedList;
import java.util.List;

public class NeatNetwork extends Network {

	Network fittest;
	
	class Genome {
		Network network;
		int rank;
		int fitness;
	}
	
	class Species {
		List<Network> species = new LinkedList<Network>();
		int avgFitness = 0;
	}
	
	
}
