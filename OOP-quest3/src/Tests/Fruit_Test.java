package Tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import gameClient.Fruit;
class Fruit_Test {
	
	int RandomSenario;
	game_service game;
	List<String> list;
	String fruit;
	
	
	@BeforeEach public void initialize() {
    	int RandomSenario=(int)Math.random()*24;
    	game_service game=Game_Server.getServer(RandomSenario);
    	List<String> list=game.getFruits();
    	Iterator<String> itr=list.iterator();
    	fruit=itr.next();
    	
     }
	@Test
	void testFruitJsonParsing() {
		System.out.println(fruit);
		Fruit f=new Fruit(fruit);
		String s=f.toString();
		System.out.println(s);
		boolean eaual=(s.equals(fruit));
		assertTrue(eaual);
		
	}
}
