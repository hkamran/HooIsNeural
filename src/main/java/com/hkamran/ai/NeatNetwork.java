package com.hkamran.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class NeatNetwork extends Network {

	List<Genome> genomes = new LinkedList<Genome>();
	NeatSettings settings;
	
	public NeatNetwork() {
		super();
		this.settings = (NeatSettings) super.settings;
	}
	
	class Genome implements Comparable<Genome> {
		Network network;
		List<Connection> added = new LinkedList<Connection>();
		List<Connection> removed = new LinkedList<Connection>();
		int fitness;
		
		public Genome(Network network) {
			this.network = network;
			for (Connection connection : network.connections) {
				if (connection.isEnabled()) {
					added.add(connection);
				} else {
					removed.add(connection);
				}
			}
		}
		
		public Genome clone() {
			Network cNetwork = network.clone();
			Genome cGenome = new Genome(cNetwork);
			return cGenome;
		}

		@Override
		public int compareTo(Genome genome) {
			return genome.fitness - this.fitness;
		}
		
	}
	
	private void createPopulation(int size) {
		Collections.sort(genomes);
		
		Genome elite = genomes.size() == 0 ? 
				new Genome(this) : genomes.get(0);
		
		for (int i = 0; i < size; i++) {
			Genome genome = elite.clone();
			mutate(genome);
			genomes.add(genome);
		}
		
		
	}
	
	public void setFitness() {
		
	}
	
	public void copulate() {
		
	}
	
	public void train() {
		
	}
	
	public void evolve() {
		
	}
	
	private void mutate(Genome genome) {
		
	}
	
}
