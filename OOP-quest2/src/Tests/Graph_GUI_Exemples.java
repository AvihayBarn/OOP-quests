package Tests;

import java.util.Collection;
import java.util.LinkedList;
import dataStructure.*;
import gui.Graph_GUI;
import utils.Point3D;

public class Graph_GUI_Exemples{
	public static void main(String[] args) {
		Collection<node_data> nodes = new LinkedList<node_data>();
		Collection<edge_data> edges = new LinkedList<edge_data>();
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
		DGraph dg = new DGraph(nodes, edges);
		Graph_GUI GUI = new Graph_GUI(dg);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		dg.addNode(new node(new Point3D(350, 3900)));
		dg.addNode(new node(new Point3D(1500, -300)));
	}
}
