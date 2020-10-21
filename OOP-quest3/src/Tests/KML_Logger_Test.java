package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import gameClient.KML_Logger;

class KML_Logger_Test {

	@Test
	void KmlGraphConverttest() {
	
		game_service game=Game_Server.getServer(0);
		DGraph Graph=new DGraph();
		Graph.init(game.getGraph());
		KML_Logger KML=new KML_Logger(Graph);
		KML.SaveToKmlFile("data","kmltest");
		Kml k=Kml.unmarshal("data/Compare.kml");
		Kml l=Kml.unmarshal("data/kmltest.kml");
		boolean b=k==l;
		assertTrue(b);
			


	}

}
