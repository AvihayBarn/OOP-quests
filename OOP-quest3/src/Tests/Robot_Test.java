package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import gameClient.Robot;

class Robot_Test {
	int RandomSenario;
	game_service game;
	List<String> list;
	String robot;
	
	
	@BeforeEach public void initialize() {
    	int RandomSenario=(int)Math.random()*24;
    	game_service game=Game_Server.getServer(RandomSenario);
    	game.addRobot(3);
    	List<String> list=game.getRobots();
    	Iterator<String> itr=list.iterator();
    	robot=itr.next();
    	
     }
	@Test
	void testRobotFromJson() {
		System.out.println(robot);
		Robot r=new Robot(robot);
		String s=r.toString();
		System.out.println(s);
		boolean eaual=(s.equals(robot));
		assertTrue(eaual);
	}

}
