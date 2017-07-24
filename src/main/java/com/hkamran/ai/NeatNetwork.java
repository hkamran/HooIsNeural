package com.hkamran.ai;

import java.util.LinkedList;
import java.util.List;

public class NeatNetwork extends Network {

	Network best;
	
	class Gene {
		int fromLayerIndex;
		int fromNodeIndex;
		int toLayerIndex;
		int toNodeIndex;
	}
	
	class Genome {
		Network network;
		int rank;
		int fitness;
	}
	
	class Species {
		List<Network> species = new LinkedList<Network>();
		int avgFitness = 0;
		
		
	}
	
	int hiddenNodeCap;
	double createConnection = 0.05;
	double removeConnection = 0.1;
	double createNode = 0.02;
	double modifyWeight = 0.15;
	double weightMutationStep = 2;

	
	private void createPopulation(Network network) {
		
	}
	
}
