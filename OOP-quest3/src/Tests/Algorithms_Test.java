package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataStructure.Algorithms;
import dataStructure.DGraph;
import dataStructure.edge;
import dataStructure.edge_data;
import dataStructure.node;
import dataStructure.node_data;
import utils.Point3D;

class Algorithms_Test {
	Collection<node_data> nodes = new LinkedList<node_data>();
	Collection<edge_data> edges = new LinkedList<edge_data>();
	DGraph Graph;

	@BeforeEach public void initialize() {
		node.id = 1;
		nodes.add(new node(new Point3D(-100, 400)));
		nodes.add(new node(new Point3D(800, 500)));
		nodes.add(new node(new Point3D(500, 400)));
		nodes.add(new node(new Point3D(900, -200)));
		nodes.add(new node(new Point3D(405, 350)));
		nodes.add(new node(new Point3D(200, -100)));
		nodes.add(new node(new Point3D(300, 600)));
		nodes.add(new node(new Point3D(-700, 300)));
		nodes.add(new node(new Point3D(110, 450)));
		nodes.add(new node(new Point3D(950, -600)));
		nodes.add(new node(new Point3D(800, -300)));
		nodes.add(new node(new Point3D(400, 200)));
		nodes.add(new node(new Point3D(-750, 300)));
		nodes.add(new node(new Point3D(200, 400)));
		nodes.add(new node(new Point3D(100, -200)));
		edges.add(new edge(1,2,7));
		edges.add(new edge(2,3,2));
		edges.add(new edge(3,4,1));
		edges.add(new edge(4,5,3));
		edges.add(new edge(5,6,1));
		edges.add(new edge(6,7,5));
		edges.add(new edge(7,8,7));
		edges.add(new edge(8,9,2));
		edges.add(new edge(9,10,1));
		edges.add(new edge(10,11,3));
		edges.add(new edge(11,12,1));
		edges.add(new edge(12,13,5));
		edges.add(new edge(13,14,3));
		edges.add(new edge(14,15,1));
		edges.add(new edge(15,1,5));
		Graph=new DGraph(nodes, edges);
	}

	@Test
	void testIsConnected() {
		boolean isTrue = Algorithms.isConnected(Graph);
		assertTrue(isTrue);
		Graph.removeNode(1);
		boolean isFalse = Algorithms.isConnected(Graph);
		assertFalse(isFalse);
	}

	@Test
	void testShortestPathDist() {
		double distance1 = Algorithms.shortestPathDist(Graph,2, 15);
		assertEquals(distance1, 35);
		double distance2 =  Algorithms.shortestPathDist(Graph,5, 4);
		assertEquals(distance2, 44);
	}

	@Test
	void testShortestPathDistThrough() {
		double distance1 = Algorithms.shortestPathDistThrough(Graph,2, 15,1);
		assertEquals(distance1, 40);
		double distance2 =  Algorithms.shortestPathDistThrough(Graph,5, 4,5);
		assertEquals(distance2, 47);
	}

	@Test
	void testShortestPath() {
		List<node_data> list1 = Algorithms.shortestPath(Graph,2, 15);
		int[] arr1= {2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		for(int i=0;i<arr1.length;i++) {
			assertEquals(arr1[i],list1.get(i).getKey());

		}
		list1 = Algorithms.shortestPath(Graph,5, 4);
		int[] arr2= {5,6,7,8,9,10,11,12,13,14,15,1,2,3,4};
		for(int i=0;i<arr2.length;i++) {
			assertEquals(arr2[i],list1.get(i).getKey());
		}
	}

	@Test
	void testShortestPathThrough() {
		List<node_data> list1 = Algorithms.shortestPathThrough(Graph,2, 15,1);
		int[] arr1= {2,3,4,5,6,7,8,9,10,11,12,13,14,15,1};
		for(int i=0;i<arr1.length;i++) {
			assertEquals(arr1[i],list1.get(i).getKey());

		}
		list1 = Algorithms.shortestPathThrough(Graph,5, 4,5);
		int[] arr2= {5,6,7,8,9,10,11,12,13,14,15,1,2,3,4,5};
		for(int i=0;i<arr2.length;i++) {
			assertEquals(arr2[i],list1.get(i).getKey());
			
		}
	}

	@Test
	void testTSP() {
		List<Integer> list=new LinkedList<>();
		list.add(7);
		list.add(9);
		list.add(4);
		list.add(6);
		List<node_data> listcheck=Algorithms.TSP(Graph,list);
		list.clear();
		for(int i=7;i<16;i++) {
			list.add(i);
		}
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		Iterator<Integer> itr1=list.iterator();
		Iterator<node_data> itr2=listcheck.iterator();
		while(itr1.hasNext()&&itr2.hasNext()) {
			int a=itr1.next();
			node_data n=itr2.next();
			int b=n.getKey();
			System.out.println(a +"  "+n.toString());
			boolean bool=(a==b);
			assertTrue(bool);
		}
	}

}
