package dataStructure;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


/**
 * The Abstract Class Algorithms.
 * It contains a collection of useful algorithms on graphs 
 * 
 */
public abstract class Algorithms  {
	
	
	/* 
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 * @return true if graph connected.
	 */
	
	/**
	 * Checks if is connected.
	 *
	 * @param AlgoG the graph
	 * @return true, if is connected
	 */
	public static boolean isConnected(DGraph AlgoG) {
		// Reset all tags of the nodes to 0
		ReseTags(AlgoG);
		// Take the first node (key) and mark all nodes can be reached from it
		Collection<node_data> C1=AlgoG.getV();
		if(C1.size()==0)
			return true;
		int key=C1.iterator().next().getKey();
		MarkTags(AlgoG,key);
		// If not all nodes marked return false
		for(node_data itr:C1) {
			if(itr.getTag()==0) return false;
		}
		
		// Create new graph  with all the edges reversed
		DGraph Rev=new DGraph();
		Reversed(Rev,AlgoG);
		// Mark again all nodes can be reached from the node
		ReseTags(Rev);
		MarkTags(Rev,key);
		// If not all nodes marked return false
		Collection<node_data> C2=Rev.getV();
		for(node_data itr:C2) {
			if(itr.getTag()==0) return false;
		}
		return true;
	}

	/* 
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return The shortest path in double
	 */
	
	/**
	 * Shortest path dist.
	 *
	 * @param AlgoG the graph
	 * @param src the src
	 * @param dest the dest
	 * @return the double
	 */
	public static double shortestPathDist(DGraph AlgoG,int src, int dest) {
		if(AlgoG.getNode(src)==null||AlgoG.getNode(dest)==null) {
			throw new IllegalArgumentException("one of the node you entered doesn't exist in this graph");
		}
		// Check if there is any path to src
		ReseTags(AlgoG);
		MarkTags(AlgoG,src);
		if(AlgoG.getNode(dest).getTag()!=1) {
			return -1;
		}
		SetToInf(AlgoG);
		AlgoG.getNode(src).setWeight(0);
		Collection<node_data> notVisited=new LinkedList<>(AlgoG.getV());
		node_data minWeight;
		while (!notVisited.isEmpty()) {
			// Find the node with the minimum weight from all nodes in the collection.
			minWeight = findMinNode(notVisited);
			// Go over all the neighbors of minWeight that we didn't removed from the collection.
			for(edge_data e : AlgoG.getE(minWeight.getKey())) {
				node_data neighbor = AlgoG.getNode(e.getDest());
				if(neighbor.getInfo() != "finished") {
					double distance = minWeight.getWeight() + e.getWeight();
					// If we find a shorter distance update the weight.
					// And save the node who lead to this node.
					if(distance < neighbor.getWeight()) {
						neighbor.setWeight(distance);
						neighbor.setTag(minWeight.getKey());
					}
				}
			}
			// Mark the node as "finished", and remove it from the collection
			minWeight.setInfo("finished");
			notVisited.remove(minWeight);
		}
		// Check if the distance is infinity, which means there is no shortest path. If so returns -1
		double ans = AlgoG.getNode(dest).getWeight();
		if(ans == Double.MAX_VALUE) 
			return -1;
		else
			return ans;
	}
	
	/**
	 * Shortest path dist through.
	 *
	 * @param AlgoG the algo G
	 * @param src the src
	 * @param middle the middle
	 * @param dest the dest
	 * @return the double
	 */
	public static double shortestPathDistThrough(DGraph AlgoG,int src,int middle, int dest) {
		return shortestPathDist(AlgoG, src,middle)+shortestPathDist(AlgoG,middle,dest);
		
	}

	/* 
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * see: https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return a List of the nodes of the shortest path 
	 */
	
	/**
	 * Shortest path.
	 *
	 * @param AlgoG the graph
	 * @param src the src
	 * @param dest the dest
	 * @return the list
	 */
	public static List<node_data> shortestPath(DGraph AlgoG,int src, int dest) {
		// Check if there is a path from src to dest, if not return null.
		double distance = shortestPathDist(AlgoG,src, dest);
		if(distance == -1) return null;

		// In each node the "tag" field saves the key of the node from which we reached the current node.
		// We will start from dest and go through all the nodes until we reach the source node.
		List<node_data> nodePath = new LinkedList<node_data>();
		node_data n = AlgoG.getNode(dest);
		nodePath.add(n);
		// Each node in the path inserted into the stack
		while(n != AlgoG.getNode(src)) {		
			n = AlgoG.getNode(n.getTag());
			nodePath.add(0, n);
		} 
		return nodePath;
	}
	
	
	
	/**
	 * Shortest path through.
	 *
	 * @param AlgoG the algo G
	 * @param src the src
	 * @param middle the middle
	 * @param dest the dest
	 * @return the list
	 */
	public static List<node_data> shortestPathThrough(DGraph AlgoG,int src,int middle,int dest){
		if(shortestPath(AlgoG, src, middle)!=null) {
		List<node_data> path=shortestPath(AlgoG, src,middle);
		path.add(AlgoG.getNode(dest));
		return path;
		}
		return null;
	}

	/* 
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem, 
	 * as you can visit a node more than once, and there is no need to return to source node - 
	 * just a simple path going over all nodes in the list.
	 * if even one node from targets is not in the same connected component the function returns null
	 * @param targets
	 * @return List of the shortest path of nodes to walk through @targets
	 */

	/**
	 * Tsp.
	 *
	 * @param AlgoG the algo G
	 * @param targets the targets
	 * @return the list
	 */
	public static List<node_data> TSP(DGraph AlgoG ,List<Integer> targets) {
		List<Integer> targetsCopy = new LinkedList<Integer>(targets);
		List<node_data> TSP = new LinkedList<node_data>();
		// Get the first node from targets
		TSP.add(0,AlgoG.getNode(targetsCopy.get(0)));
		targetsCopy.remove(0);
		
		while(!targetsCopy.isEmpty()) {
			// Activate shortestPathDist to calculate distance to all nodes
			int src = TSP.get((TSP.size())-1).getKey();
			int dest = targetsCopy.get(0);
			shortestPathDist(AlgoG,src, dest);
			// Find the node in targets the path to is the shortest
			node_data minWeightNode = AlgoG.getNode(dest);
			for(Integer i : targetsCopy) {
				if(AlgoG.getNode(i).getWeight() < minWeightNode.getWeight())
					minWeightNode = AlgoG.getNode(i);
			}
			// If aren't any - return null
			if(minWeightNode.getWeight() == Double.MAX_VALUE)
				return null;
			// Enter the shortest path to minWeight node to TSP
			List<node_data> SP = shortestPath(AlgoG,src, minWeightNode.getKey());
			SP.remove(0);
			TSP.addAll(SP);
			// Remove the minWeight node from targets
			int index = targetsCopy.indexOf(minWeightNode.getKey());
			targetsCopy.remove(index);
		}
		return TSP;
	}
	/**
	 * Resets all tags to be 0.
	 * @param g the g
	 */
	private static void ReseTags(DGraph g) {
		for(node_data itr:g.getV()) {
			itr.setTag(0);
		}
	}

	/**
	 * Marks all the tags that the node with the key it gets has path to.
	 *
	 * @param g the g
	 * @param key the key
	 */
	private static void MarkTags(DGraph g,int key) {
		Stack<Integer> Stack=new Stack<Integer>();
		Stack.push(key);
		while(!Stack.isEmpty()) {
			node_data n=g.getNode(Stack.peek());
			n.setTag(1);
			Stack.pop();
			for(edge_data itr:g.getE(n.getKey())) {
				boolean isExsits=g.getNode(itr.getDest())!=null;
				boolean NotPassYet=g.getNode(itr.getDest()).getTag()==0;
				if(isExsits&&NotPassYet) {
					Stack.push(itr.getDest());
					g.getNode(itr.getDest()).setTag(1);
				}
			}
		}
	}

	/**
	 * Sets all the node's weights to be infinity,set all the info to be empty, 
	 * and set the tags to 0.
	 * @param g the g
	 */
	private static void SetToInf(DGraph g) {
		for(node_data n : g.getV()) {
			n.setWeight(Double.MAX_VALUE);
			n.setInfo("");
			n.setTag(0);
		}
	}

	/**
	 * Finds a node with minimum weight in collection.
	 * @param nodes the nodes
	 * @return minWeight-node_data
	 */
	private static node_data findMinNode(Collection<node_data> nodes) {
		Iterator<node_data> itr = nodes.iterator();
		node_data minWeight = itr.next();
		for(node_data n : nodes) {
			if(n.getWeight() < minWeight.getWeight())
				minWeight = n;
		}
		return minWeight;
	}

	/**
	 * Reverse a graph.turns over all the edges to be b-->a instead of a-->b 
	 * @param rev the rev
	 * @param curr the curr
	 * @return the same graph reversed
	 */
	private static graph Reversed(graph rev,graph curr){
		for(node_data n:curr.getV()) {
			rev.addNode(new node(n.getKey()));
			rev.getNode(n.getKey()).setInfo(n.getInfo());
			rev.getNode(n.getKey()).setWeight((n.getWeight()));
			rev.getNode(n.getKey()).setLocation((n.getLocation()));
		}
		for(node_data n:curr.getV()) {
			for(edge_data e:curr.getE(n.getKey())){
				rev.connect(e.getDest(),e.getSrc(),e.getWeight());
			}
		}
		return rev;
	}
	
	
}
