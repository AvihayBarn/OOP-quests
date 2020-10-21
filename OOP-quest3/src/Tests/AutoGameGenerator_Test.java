package Tests;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.fruits;
import Server.game_service;
import dataStructure.Algorithms;
import dataStructure.DGraph;
import dataStructure.edge_data;
import gameClient.AutoGameGenerator;
import gameClient.Fruit;
import gameClient.Robot;

class AutoGameGenerator_Test {

	@Test
	void testAutoMove() {
		game_service game=Game_Server.getServer(0);
		DGraph Graph=new DGraph();
		Graph.init(game.getGraph());
		List<Fruit> fruits=new ArrayList<Fruit>();
		for(String s:game.getFruits()) {
			fruits.add(new Fruit(s));
		}
		game.addRobot((int)(Math.random()*11));
		List<Robot> robots=new ArrayList<Robot>();
		for(String s:game.getRobots()) {
			robots.add(new Robot(s));
		}
		Thread Game=new Thread(new Runnable() {

			@Override
			public void run() {
				try {
				while(game.isRunning()) {
					Fruit f=fruits.get(AutoGameGenerator.MostValuableFruitIndex(fruits));
					edge_data e=AutoGameGenerator.FindFruitEdge(Graph, f);
					int src=AutoGameGenerator.NodeBeforeFruit(f,e);

					Robot r=robots.get(0);
					double d=Algorithms.shortestPathDist(Graph,r.getSrc(),src);
					AutoGameGenerator.AutoMove(game,Graph, robots, fruits);
					game.move();
					for(Robot robot:robots) {
						assertNotEquals(robot.getDest(),-1);
					}
					robots.clear();
					for(String s:game.getRobots()) {

						robots.add(new Robot(s));
					}
					Robot r1=robots.get(0);
					assertEquals(r.getId(),r1.getId());
					System.out.println(r.getSrc());
					System.out.println(r1.getSrc());
					double c=Algorithms.shortestPathDist(Graph,r1.getSrc(),src);
					System.out.println(d);
					System.out.println(c);
					boolean b=(d>=c);
					assertTrue(b);

				
				
					Thread.sleep(1000);
				}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		game.startGame();
		Game.start();










	}


}
