package com.hkamran.ai.tests.connection;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.Connection;
import com.hkamran.ai.Node;

public class ConnectionTest {

	@Test
	public void connectionMapGetTest() {
		Node a = new Node(null, Activations.sigmoid);
		Node b = new Node(null, Activations.sigmoid);
		
		Connection c1 = new Connection(a, b, 0.0);
		
		Set<Connection> set = new HashSet<Connection>();
		set.add(c1);
		
		Assert.assertTrue(set.contains(c1));
	}
	
	@Test
	public void connectionMapGetTest2() {
		Node a = new Node(null, Activations.sigmoid);
		Node b = new Node(null, Activations.sigmoid);
		
		Connection c1 = new Connection(a, b, 0.0);
		Connection c2 = new Connection(b, a, 0.0);
		
		
		Set<Connection> set = new HashSet<Connection>();
		set.add(c1);
		
		Assert.assertTrue(!set.contains(c2));
	}	
}
