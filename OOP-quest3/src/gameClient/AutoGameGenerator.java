package gameClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Server.game_service;
import dataStructure.Algorithms;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;


/**
 * The Abstract Class AutoGameGenerator.this is the tool that manages the automatic game in MyGameGui.
 * it moves the the robots systematically by processing the information about the robot's speed, the fruit's value 
 * and the distance of the robot from the fruit-and deciding which robot should reach each fruit.
 * in addition , this class adds the robots to the best node in the graph to start Which will give a good yield of points for the beginning 
 * of the game.
 */
public abstract class AutoGameGenerator {

	/** The Constant EPS. */
	private static final double EPS= Math.pow(1.0E-4, 2.0);

	/**
	 * Auto move of the game. the first step of the auto move is to copy the losts of the robots and the fruits to new lists.
	 * after this, a loop go over those lists and finds which fruit is the most valuable,
	 * which edge this fruit on and from which node should a robot come and get it from.
	 * the function search for the closest robot to reach this node and after this updates this robot's dest in the game server
	 * the loop ends when there are no more fruits or robots in lists 
	 *
	 * @param game the game
	 * @param graph the graph
	 * @param robots the robots
	 * @param fruits the fruits
	 */
	public static int AutoMove(game_service game,DGraph graph,List<Robot> robots,List<Fruit> fruits) {
		int Sleep=Integer.MAX_VALUE;
		List<Fruit> Fruits=new LinkedList<>(fruits); 
		List<Robot> Robots=new LinkedList<>(robots);
		/*Iterator<Robot> robot=Robots.iterator();
		while(!Fruits.isEmpty()&&robot.hasNext()) {
			Robot r=robot.next();
			if(!r.getPath().isEmpty()) {
				game.chooseNextEdge(r.getId(),r.getPath().get(0).getKey());
				r.getPath().remove(0);
				Robots.remove(r);
				Fruits.remove(r.getFruit());
			}
			else {
				Fruit f=Fruits.get(MostValuableFruitIndex(Fruits));
				edge_data edge=FindFruitEdge(graph,f);
				int src=NodeBeforeFruit(f, edge);
				int dest=NodeAfterFruit(f, edge);
				Robot r1=ClosestRobot(graph,Robots,edge,src,dest);
				if(r1!=null&&Algorithms.shortestPathThrough(graph,r.getSrc(),src,dest)!=null&&(Algorithms.shortestPathThrough(graph,r.getSrc(),src,dest)).size()>=1) {
					r1.setPath(Algorithms.shortestPathThrough(graph,r.getSrc(),src,dest));
					game.chooseNextEdge(r1.getId(),r1.getPath().indexOf(0));
					r1.getPath().remove(0);
					r1.setFruit(f);

				}
				Robots.remove(r1);
				Fruits.remove(f);			
				}
		}*/
		while(!Fruits.isEmpty()&&!Robots.isEmpty()){
			Fruit f=Fruits.get(MostValuableFruitIndex(Fruits));
			edge_data edge=FindFruitEdge(graph,f);
			int src=NodeBeforeFruit(f, edge);
			int dest=NodeAfterFruit(f, edge);
			System.out.println(graph.getNode(dest));
			Robot r=ClosestRobot(graph,Robots,edge,src,dest);
			if(r!=null&&Algorithms.shortestPathThrough(graph,r.getSrc(),src,dest)!=null&&(Algorithms.shortestPathThrough(graph,r.getSrc(),src,dest)).size()>=1) {
				List<node_data> path=Algorithms.shortestPathThrough(graph,r.getSrc(),src,dest);
				path.remove(0);
				r.setDest(path.get(0).getKey());
				game.chooseNextEdge(r.getId(),r.getDest());
				int sleep=SleepCauculator(graph,r,src,dest,f);
				Fruits.remove(f);
				Robots.remove(r);	
				if(sleep<Sleep) {
					Sleep=sleep;
				}

			}
			else {
				Fruits.remove(f);
			}

		}
		game.move();
		if(Sleep==Integer.MAX_VALUE) return 100;
		else return Sleep;
	}

	/**
	 * Adds the robots to the best place.
	 * The robots add to the nodes that before the fruits, 
	 * so that when the game starts they will eat those fruits.
	 *
	 * @param game the game
	 * @param Graph the graph
	 * @param RobotsNum the robots num
	 * @param fruits the fruits
	 */
	public static void AddRobots(game_service game,DGraph Graph,int RobotsNum,List<Fruit> fruits) {
		int i=0;
		List<Fruit> Fruits=new ArrayList<Fruit>(fruits);
		while(i<RobotsNum&&!Fruits.isEmpty()) {
			int index=MostValuableFruitIndex(Fruits);
			int robotid =NodeBeforeFruit(Fruits.get(index),FindFruitEdge(Graph, fruits.get(index)));
			Fruits.remove(index);
			game.addRobot(robotid);
		}

	}


	/**
	 * Find the edge that the fruit is on by checking all the edges if they match.
	 * 
	 *
	 * @param graph the graph
	 * @param fruit the fruit
	 * @return the edge that the fruit is on.
	 */
	public static edge_data FindFruitEdge(DGraph graph,Fruit fruit) {
		for(node_data n :graph.getV()) {
			for(edge_data e :graph.getE(n.getKey())) {
				if(OnThisEdge(graph,fruit,e)) return e;
			}
		}
		return null;
	}


	/**
	 *  Checks if the fruit is on this specific edge 
	 *  by measuring if the distance between the src and dest of the edge
	 *  equals to the distance between the fruit location to the src +  distance between the fruit location to the dest.
	 *
	 * @param graph the graph
	 * @param fruit the fruit
	 * @param edge the edge
	 * @return true, if the fruit on this edge
	 */
	public static boolean OnThisEdge(DGraph graph,Fruit fruit,edge_data edge) {
		Point3D p1=fruit.getLocation();
		Point3D p2=graph.getNode(edge.getSrc()).getLocation();
		Point3D p3=graph.getNode(edge.getDest()).getLocation();
		double a,b,distance1,distance2;
		a=p1.distance2D(p2);
		b=p1.distance2D(p3);
		distance1=a+b;
		distance2=p2.distance2D(p3);
		double gap=Math.abs(distance2-distance1);
		if(gap<=EPS) {
			return true;
		}
		return false;
	}


	/**
	 * The fruit has a type variable that can be 1 or -1.
	 * if the type is 1, the fruit is an apple and can be eaten only if you walk through a edge that- src< dest
	 * if the type is -1, the fruit is a banana and can be eaten only if you walk through a edge that- dest< src
	 * because of that,we need to check from which node to come eat this fruit.
	 * this function finds the node before the fruit
	 *
	 * @param fruit the fruit
	 * @param edge the edge
	 * @return  the int that represent the key of the node before the fruit
	 */
	public static int NodeBeforeFruit(Fruit fruit,edge_data edge) {
		if(fruit.getType()==1) {
			if(edge.getDest()>=edge.getSrc()) return edge.getSrc();
			else return edge.getDest();
		}
		else {
			if(edge.getDest()>=edge.getSrc()) return edge.getDest();
			else return edge.getSrc();		
		}
	}


	/*
	 * Search in NodeBeforeFruit for information
	 *
	 * @param fruit the fruit
	 * @param edge the edge
	 * @return the int that represent the key of the node after the fruit
	 */
	public static int NodeAfterFruit(Fruit fruit,edge_data edge) {
		if(fruit.getType()==1) {
			if(edge.getDest()>=edge.getSrc()) return edge.getDest();
			else return edge.getSrc();
		}
		else {
			if(edge.getDest()>=edge.getSrc()) return edge.getSrc();
			else return edge.getDest();		
		}
	}


	/**
	 * Closest robot to a the node with the key src.
	 * src is the key of the node which a fruit is on it.
	 * the function checks which robot has the shortest path to reach to this node and returns this robot.
	 *
	 * @param graph the graph
	 * @param robots the robots
	 * @param edge the edge
	 * @param src the src
	 * @param dest the dest
	 * @return the closest robot to src
	 */
	public static Robot ClosestRobot(DGraph graph,List<Robot> robots,edge_data edge,int src,int dest) {
		Iterator<Robot> robot=robots.iterator();
		Robot ans=robot.next();
		double shortestpath=(Algorithms.shortestPathDistThrough(graph,ans.getSrc(),src,dest))*ans.getSpeed();
		while(robot.hasNext()) {
			Robot r=robot.next();
			double checkpath=(Algorithms.shortestPathDistThrough(graph,r.getSrc(),src,dest))*r.getSpeed();
			if(checkpath>=0) {
				if(checkpath<shortestpath) {
					shortestpath=checkpath;
					ans=r;
				}
			}
		}
		if(shortestpath<0) {
			return null;
		}
		return ans;
	}


	/**
	 *Search for the Most valuable fruit index.
	 *
	 * @param Fruits the fruits
	 * @return the int
	 */
	public static int MostValuableFruitIndex(List<Fruit> Fruits) {
		Iterator<Fruit> itr=Fruits.iterator();
		double max=Double.MIN_VALUE;
		int Max=Integer.MIN_VALUE;
		while(itr.hasNext()) {
			Fruit fruit=itr.next();
			if(fruit.getValue()>max) {
				max=fruit.getValue();
				Max=Fruits.indexOf(fruit);
			}
		}
		return Max;
	}
	/**
	 * 
	 * @return
	 */
	public static int SleepCauculator(DGraph Graph,Robot robot,int src,int dest,Fruit f) {
		if(robot.getSrc()==src&&robot.getDest()==dest) {
			double Time = Graph.getEdge(src, dest).getWeight();
			double sleep = (Time/robot.getSpeed())*1000;
			double distance1=Graph.getNode(src).getLocation().distance2D(Graph.getNode(dest).getLocation());
			double distance2=Graph.getNode(src).getLocation().distance2D(f.getLocation());
			double distance=distance2/distance1;
			sleep=sleep*distance;
			return (int)sleep;	
		}
		else return Integer.MAX_VALUE;
	}



}
