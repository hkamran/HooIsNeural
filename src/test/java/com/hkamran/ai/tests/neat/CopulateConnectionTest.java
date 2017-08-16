package com.hkamran.ai.tests.neat;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.Connection;
import com.hkamran.ai.LayerBuilder;
import com.hkamran.ai.NeatNetwork;
import com.hkamran.ai.NeatSettings;
import com.hkamran.ai.NetworkBuilder;
import com.hkamran.ai.NetworkBuilder.NetworkType;
import com.hkamran.ai.Node;

public class CopulateConnectionTest {
	static NeatNetwork network;
	static NeatNetwork a;
	static NeatNetwork b;
	
	Node first;
	Node second;
	
	@BeforeClass
	public static void beforeClassSetup() throws InterruptedException {
		network = 
				(NeatNetwork) NetworkBuilder
				.create(NetworkType.EVOLUTION)
				.setInputLayer(
						LayerBuilder
						.create()
						.addNodes(1, Activations.sigmoid))
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
				.withSettings(
						NeatSettings
						.create()
						.setRandomSeed(3000))
				.build();		
	}
	
	
	
	@Test
	public void testTranferOfConnectionBetweenLayers() throws InterruptedException {
		
		a = (NeatNetwork) network.clone();
		first = a.getNode(0, 0);
		second = a.getNode(1, 0);
		a.addConnection(first, second, 1.0);

		b = (NeatNetwork) network.clone();
		first = b.getNode(0, 1);
		second = b.getNode(1, 1);
		b.addConnection(first, second, -1.0);

		NeatNetwork c = (NeatNetwork) network.clone();		
		c.copulateConnections(a, b, c);
		
		//Assertion
		
		List<Connection> connections = c.getConnections();
		Assert.assertTrue(connections.size() == 1);	
	}
	
	@Test
	public void testTranferOfConnectionBetweenBias() {
		a = (NeatNetwork) network.clone();
		first = a.getNode(-1, 0);
		second = a.getNode(1, 0);
		a.addConnection(first, second, 1.0);
		
		b = (NeatNetwork) network.clone();
		first = b.getNode(-1, 0);
		second = b.getNode(1, 0);
		b.addConnection(first, second, -1.0);
		
		
		NeatNetwork c = (NeatNetwork) network.clone();		
		c.copulateConnections(a, b, c);
		
		//Assert
		
		List<Connection> connections = c.getConnections();
		Assert.assertTrue(connections.size() == 1);
		
		Connection connection = connections.get(0);
		
		Assert.assertTrue(connection.getFrom() == c.getNode(-1, 0));
	
	}	
	
	@Test
	public void testTransferOfAllConnection() {
		a = (NeatNetwork) network.clone();
		a.createAllConnections();
		
		b = (NeatNetwork) network.clone();
		b.createAllConnections();
		
		NeatNetwork c = (NeatNetwork) network.clone();
		c.copulateConnections(a, b, c);
		
		//Assert
		
		List<Connection> connections = c.getConnections();
		Assert.assertTrue(connections.size() == a.getConnections().size());
	
	}
}
