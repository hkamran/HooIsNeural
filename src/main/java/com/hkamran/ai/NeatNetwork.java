package com.hkamran.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.TreeNode;

public class NeatNetwork extends Network {

	Network best;
	
	NeatSettings settings;
	
	class Gene {
		int fromLayerIndex;
		int fromNodeIndex;
		int toLayerIndex;
		int toNodeIndex;
		
		public Gene(int fromLayerIndex, int fromNodeIndex,
					int toLayerIndex, int toNodeIndex) {
			this.fromLayerIndex = fromLayerIndex;
			this.fromNodeIndex = fromNodeIndex;
			this.toLayerIndex = toLayerIndex;
			this.toNodeIndex = toNodeIndex;
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Gene)) return false;
			Gene gene = (Gene) obj;
			if (this.fromLayerIndex == gene.fromLayerIndex &&
				this.fromNodeIndex == gene.fromNodeIndex &&
				this.toLayerIndex == gene.toLayerIndex &&
				this.toNodeIndex == gene.toNodeIndex) return true;
			return false;
		}
	}
	
	class Genome {
		Network network;
		int fitness;
		
		Map<Gene, Connection> genes = new HashMap<Gene, Connection>();
		
		public void addGene(Gene gene) {
			if (genes.containsKey(gene)) return;

			Connection connection = createConnection(
										gene.fromLayerIndex, 
										gene.fromNodeIndex, 
										gene.toLayerIndex, 
										gene.toNodeIndex);
			genes.put(gene, connection);
		}
		
		public boolean hasGene(Gene gene) {
			return genes.containsKey(gene);
		}
		
		public void removeGene(Gene gene) {
			if (!genes.containsKey(gene)) return;
			removeConnection(gene.fromLayerIndex,
					gene.fromNodeIndex,
					gene.toLayerIndex, 
					gene.toNodeIndex);
			genes.remove(gene);
		}
		
		public void modifyGene(Gene gene, double weight) {
			Connection connection = genes.get(gene);
			connection.weight = weight;
		}
				
	}
	
	class Species {
		List<Network> species = new LinkedList<Network>();
		int avgFitness = 0;
		
		
	}
	
	public Connection createConnection(int fromLayerIndex, int fromLayerNodeIndex, 
			int toLayerIndex, int toLayerNodeIndex) {
		
		List<Layer> layers = getLayers();
		
		Layer fromLayer = layers.get(fromLayerIndex);
		Node fromNode = fromLayer.getNode(fromLayerNodeIndex);
		
		Layer toLayer = layers.get(toLayerIndex);
		Node toNode = toLayer.getNode(toLayerNodeIndex);
		
		if (fromNode == null || toNode == null) 
			throw new RuntimeException("Invalid connection");
		
		Connection connection = new Connection(fromNode, toNode);
		connection.weight =  getRandomInt(-1, 1);
		
		toLayer.addConnection(connection);
		
		return connection;
	}
	
	public void removeConnection(int fromLayerIndex, int fromLayerNodeIndex, 
			int toLayerIndex, int toLayerNodeIndex) {
		
		List<Layer> layers = getLayers();
		
		Layer fromLayer = layers.get(fromLayerIndex);
		Node fromNode = fromLayer.getNode(fromLayerNodeIndex);
		
		Layer toLayer = layers.get(toLayerIndex);
		Node toNode = toLayer.getNode(toLayerNodeIndex);	
		
		List<Connection> connections = toLayer.getConnections();
		for (Connection connection : connections) {
			if (connection.from == fromNode &&
				connection.to == toNode) {
				toLayer.removeConnection(connection);
			}
		}
	}

	
	private void createPopulation(int size) {
		
	}
	
	public void mutate(Genome genome) {
		
		
		
	}
	
}
