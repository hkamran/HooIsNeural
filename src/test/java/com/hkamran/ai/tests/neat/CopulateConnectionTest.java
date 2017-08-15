package com.hkamran.ai.tests.neat;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.BackPropSettings;
import com.hkamran.ai.Connection;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;
import com.hkamran.ai.Node;
import com.hkamran.ai.Visualizer;

public class CopulateConnectionTest {
	static NeatNetwork network;
	static NeatNetwork a;
	static NeatNetwork b;
	
	@BeforeClass
	public static void xorTest() throws InterruptedException {
		network = 
				(NeatNetwork) NetworkBuilder
				.create(NetworkType.EVOLUTION)
				.setLabel("XOR GATE")
				.setInputLayer(
						LayerBuilder
						.create()
						.addNodes(2, Activations.sigmoid))
				.addHiddenLayer(
						LayerBuilder
						.create()
						.addNodes(4, Activations.sigmoid))
				.setOutputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid)
						)
				.withBiasNode()
				.withVisualizer()
				.withSettings(
						BackPropSettings
						.create()
						.setLearningRate(0.04)
						.setRandomSeed(3000))
				.build();		

		Node first;
		Node second;
		
		a = (NeatNetwork) network.clone();
		first = a.getNode(0, 0);
		second = a.getNode(1, 0);
		a.addConnection(first, second, 1.0);
		
		Visualizer aV = new Visualizer(a);
		a.setVisualizer(aV);
		
		b = (NeatNetwork) network.clone();
		first = b.getNode(0, 1);
		second = b.getNode(1, 1);
		b.addConnection(first, second, -1.0);
		
		Visualizer bV = new Visualizer(b);
		b.setVisualizer(bV);
		
		
	}
	
	
	@Test
	public void testConnection() throws InterruptedException {
		
		NeatNetwork c = (NeatNetwork) network.clone();		
		Visualizer cV = new Visualizer(c);
		c.setVisualizer(cV);
		c.copulateConnections(a, b, c);
		
		List<Connection> connections = c.getConnections();
		
		Connection connection = connections.get(0);
		
		Assert.assertTrue(connections.size() == 1);
		Assert.assertTrue(connection.getFrom() == c.getNode(0, 1));
		Assert.assertTrue(connection.getTo() == c.getNode(1, 1));
	}
}
