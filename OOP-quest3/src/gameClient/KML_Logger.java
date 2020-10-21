package gameClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;


/**
 * The Class KML_Logger. it gives you the option the save Your game review in a kml format
 */
public class KML_Logger {

	/** The Game review. */
	public final Kml GameReview;

	/** The Doc. */
	private Document Doc;


	/** The Graph. */
	DGraph Graph=new DGraph();

	/**
	 * Instantiates a new KM L logger.
	 *
	 * @param Graph the game graph
	 */
	public KML_Logger(DGraph Graph) {
		GameReview=new Kml();
		Doc=GameReview.createAndSetDocument();
		this.Graph=Graph;
		GraphNodesConvertToKml();
	}

	/**
	 * Graph nodes convert to kml.
	 */
	private void GraphNodesConvertToKml() {
		for(node_data n: Graph.getV()) {
			Placemark Node=Doc.createAndAddPlacemark();
			Node.withName(String.valueOf(n.getKey())) ;
			Node.withDescription("Node "+n.getKey()+"");
			Node.createAndSetPoint().addToCoordinates(n.getLocation().x(),n.getLocation().y());
			for(edge_data e:Graph.getE(n.getKey())) {
				Placemark Edge=Doc.createAndAddPlacemark();
				Edge.createAndSetLineString()
				.addToCoordinates(Graph.getNode(e.getSrc()).getLocation().x(),Graph.getNode(e.getSrc()).getLocation().y())
				.addToCoordinates(Graph.getNode(e.getDest()).getLocation().x(),Graph.getNode(e.getDest()).getLocation().y());
			}

		}
	}


	/**
	 * Save info to kml.
	 *
	 * @param robots the robots
	 * @param fruits the fruits
	 */
	public void SaveInfoToKml(List<Robot> robots,List<Fruit> fruits) {
		RobotsConvertToKML(robots);
		FruitsConvertToKML(fruits);
	}

	/**
	 * Robots convert to KML.
	 *
	 * @param robots the robots
	 */
	private void RobotsConvertToKML(List<Robot> robots){
		for(Robot robot: robots) {
			Placemark KmlRobot=Doc.createAndAddPlacemark();
			java.util.Date date=new java.util.Date();
			DateFormat BigTimes = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat LittleTimes = new SimpleDateFormat("HH:mm:ss");
			String time1 = BigTimes.format(date);
			String time2 = LittleTimes.format(date);
			String ActualDate = time1+"T"+time2+"Z";
			Icon icon=new Icon();
			icon.setHref("https://www.pngrepo.com/png/134/170/robot.png");
			KmlRobot.createAndAddStyle().createAndSetIconStyle().withScale(1).withIcon(icon);
			KmlRobot.createAndSetPoint().addToCoordinates(Graph.getNode(robot.getSrc()).getLocation().x(), Graph.getNode(robot.getSrc()).getLocation().y());
			KmlRobot.createAndSetTimeStamp().withWhen(ActualDate);
		}
	}

	/**
	 * Fruits convert to KML.
	 *
	 * @param fruits the fruits
	 */
	private void FruitsConvertToKML(List<Fruit> fruits){
		for(Fruit fruit: fruits) {
			java.util.Date date=new java.util.Date();
			DateFormat BigTimes = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat LittleTimes = new SimpleDateFormat("HH:mm:ss");
			String time1 = BigTimes.format(date);
			String time2 = LittleTimes.format(date);
			String ActualDate = time1+"T"+time2+"Z";
			Placemark KmlFruit=Doc.createAndAddPlacemark();
			Icon icon=new Icon();
			if(fruit.getType()==-1) icon.setHref("https://www.pngrepo.com/png/202560/170/banana.png");
			else icon.withHref("https://www.pngrepo.com/png/3011/170/apple.png");
			KmlFruit.createAndAddStyle().createAndSetIconStyle().withScale(1).withIcon(icon);
			KmlFruit.createAndSetTimeStamp().withWhen(ActualDate);
			KmlFruit.createAndSetPoint().addToCoordinates(fruit.getLocation().x(),fruit.getLocation().y());

		}
	}

	/**
	 * Save to kml file.
	 *
	 * @param Folder the folder
	 */
	public void SaveToKmlFile(String Folder ,String Filename) {
		GameReview.setFeature(Doc);
		try {
			String file=Folder+"\\"+Filename+".kml";
			System.out.println(file);
			GameReview.marshal(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
