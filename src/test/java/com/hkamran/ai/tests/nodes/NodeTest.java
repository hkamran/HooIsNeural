package com.hkamran.ai.tests.nodes;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hkamran.ai.Activations;
import com.hkamran.ai.Node;

import org.junit.Assert;


public class NodeTest {

	@Test
	public void getNumberFromMapTest() {
		Node a = new Node(null, Activations.sigmoid);
		Node b = new Node(null, Activations.sigmoid);
		Node c = new Node(null, Activations.sigmoid);
		Node d = new Node(null, Activations.sigmoid);
		
		Map<Node, Node> mapping = new HashMap<Node, Node>();
		mapping.put(a, c);
		mapping.put(b, d);
		
		Assert.assertTrue(mapping.get(b).equals(d));
	}
	
	@Test
	public void getNodeFromMapTest() {
		Node a = new Node(null, Activations.sigmoid);
		Node b = new Node(null, Activations.sigmoid);
		Node c = new Node(null, Activations.sigmoid);
		
		Map<Node, Integer> mapping = new HashMap<Node, Integer>();
		mapping.put(a, 1);
		mapping.put(b, 2);
		
		Assert.assertTrue(mapping.get(c) == null);
	}
	
}
