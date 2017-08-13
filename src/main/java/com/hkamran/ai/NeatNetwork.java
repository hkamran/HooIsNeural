package com.hkamran.ai;

import java.util.LinkedList;
import java.util.List;

public class NeatNetwork extends Network {

	Network best;
	
	NeatSettings settings;
	
	
	class Genome {
		Network network;
		int fitness;
		
	}
	
	class Species {
		List<Network> species = new LinkedList<Network>();
		int avgFitness = 0;
		
		
	}
	
	
	private void createPopulation(int size) {
		
	}
	
	public void mutate(Genome genome) {
		
		
		
	}
	
}
