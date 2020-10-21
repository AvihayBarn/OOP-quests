package gameClient;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;




/**
 * The Class MyGameGUI. This class gives you the option to play manually or automatically in "Robots and Fruits" game.
 * The game is an instance of the interface "game_service" and this class is a graphical representation of this interface.
 * When you decelerate on MyGameGui object and run the program,white screen shows and menu bar in the left side up.
 * In the menu bar you can choose to play automatically by clicking "Automatic Game" and manually by clicking "Manual Game"
 * after you choose your game style,you choose a "scenario",which is the difficulty of the game level. there are scenarios from 0 to 23 
 * and you have to just just from this range of numbers.
 * after you choose the scenario the game is starts.
 * If you play manually,you make the robots move by click on a node they can reach and the first robot from robot list that has no destination
 * yet goes to this node.
 * during the game the time left,your current score,your current move and the scenario of the game are printed in the left up corner of the screen.
 * In the end of the manually game nothing happen and the game wait for new commands,but in the automatic game a panel with an offer to save the game info
 * in KML format is shown.
 * 
 * 
 * @author Avihay Barnholtz and Dolev brender
 */
public class MyGameGUI implements ActionListener, MouseListener {

	/** The game. */
	private game_service game;

	/** The scenario. */
	private int scenario;

	/** The Graph. */
	private  DGraph Graph;

	/** The Ranges of the Graph*/
	private Range rx, ry;

	/** Maximum number of robots to play in chosen scenario */
	private int RobotNum;
	/** The Current robots number on the game,and  */
	private int CurrentRobotsNum;

	/** The radius of each Node to print . */
	private double circle;

	/** Boolean variables to know which style game is activate */
	private boolean ManualGameActivated,AutomaticGameActivated;

	/** The robots of the game */
	private List<Robot> robots;

	/** The fruits of the game*/
	private List<Fruit> fruits; 

	/** The Game KML_Logger*/
	private KML_Logger GameKml;
	/** png of banana */
	private static final String Banana="data/banana.png";

	/** png of apple. */
	private static final String Apple="data/apple.png";
	/** png of robot. */
	private static final String Robot="data/taz.png";

	/** png of opposite robot. */
	private static final String Robot2="data/tazop.png";

	/**
	 *  Constructor. Initializing of the screen and the menu bar
	 */
	public MyGameGUI() {
		StdDraw.setCanvasSize(1200,600);
		StdDraw.setMenuBar(createMenuBar());
		StdDraw.addMouseListener(this);
		StdDraw.enableDoubleBuffering();
		
	}
	/**
	 * Creates the menu bar.
	 *
	 * @return the j menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Creates all the File options
		JMenu Menu = new JMenu("Start Game");
		JMenuItem Login = new JMenuItem("Login to Server");
		Login.addActionListener(this);
		Menu.add(Login);
		menuBar.add(Menu);
		JMenuItem ManualGame = new JMenuItem("Manual Game");
		ManualGame.addActionListener(this);
		Menu.add(ManualGame);
		JMenuItem AutomaticGame = new JMenuItem("Automatic Game");
		AutomaticGame.addActionListener(this);
		Menu.add(AutomaticGame);
		JMenuItem GameStatistics = new JMenuItem("Game Statistics");
		GameStatistics.addActionListener(this);
		Menu.add(GameStatistics);

		return menuBar;
	}

	/**
	 * when you click on one of the options in the menubar,
	 * this function is running and the option you choose is execute.
	 * if you choose manual game your game depends on your mouse clicks.
	 * if you choose automatic game ,two threads starts to working in  parallel
	 * to the game that starts.
	 * one thread updates the game drawing matching to the game process and the other thread
	 * updates the game information about the robots and fruits from the game server.
	 * on the end the GameOver function is activated and the game ends
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ManualGameActivated=false;
		AutomaticGameActivated=false;
		String Clicked=e.getActionCommand();
		if(Clicked=="Login to Server") {
			Game_Server.login(Integer.parseInt(JOptionPane.showInputDialog(StdDraw.frame,"please enter you ID to login")));
		}
		if(Clicked=="Game Statistics") {
				// Ask the user to insert ID and stage.
				String id_str = "", stage_str = "";
				JTextField input1 = new JTextField();
				JTextField input2 = new JTextField();
				Object[] message = {"Enter your ID:", input1, "Enter stage to check:", input2};
				int option = JOptionPane.showConfirmDialog(StdDraw.frame, message, "Check Position", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    id_str = input1.getText();
				    stage_str = input2.getText();
				}

				try {
					// Gets the user position
					int id = Integer.parseInt(id_str);
					int stage = Integer.parseInt(stage_str);
				    int position = SimpleDB.compare(id, stage);
					// Prints the position
					JOptionPane.showMessageDialog(StdDraw.frame, "in stage: " + stage + " your position is: " + position,
												"Position", JOptionPane.PLAIN_MESSAGE);
				// If catch exception inform user.	
				} catch (Exception e1){
					JOptionPane.showMessageDialog(StdDraw.frame, "Error! please try again with valid input", 
												"Error", JOptionPane.PLAIN_MESSAGE);
				}
			
		}
		if(Clicked=="Manual Game") {
			SetGameConfig();
			ManualGameActivated=true;
		}
		if(Clicked=="Automatic Game") {
			AutomaticGameActivated=true;
			SetGameConfig();
			GameKml=new KML_Logger(Graph);
			Thread MoveAutomatic=new Thread(new Runnable() {

				@Override
				public synchronized void run() {
					int sleep=0,Sleep=108;
					while (game.isRunning()) {

						try {
							GameKml.SaveInfoToKml(robots, fruits);
							GameFruitsUpdate();
							GameRobotsUpdate();
							sleep=AutoGameGenerator.AutoMove(game,Graph, robots, fruits);
							if(sleep!=Integer.MAX_VALUE) Sleep=80;else Sleep=77;
							Thread.sleep(Sleep);
						}
						catch (InterruptedException e) {

						}
					}

					System.out.println(game.timeToEnd());
					GameOver();


				}

			});
			Thread DrawGame=new Thread(new Runnable() {

				@Override
				public synchronized void run() {
					while (game.isRunning()) {
						try {
							StdDraw.clear();
							drawGraph();
							DrawFruits();
							DrawRobots();
							DrawInfo(game.timeToEnd());
							StdDraw.show();

							Thread.sleep(100);
						}
						catch (InterruptedException e) {

						}

					}



				}

			});
			game.startGame();
			MoveAutomatic.start();
			DrawGame.start();
		}
		


	}
	/**
	 * Sets the game configuration .
	 * clears the screen for a new graph to draw.
	 * gives the user to choose scenario and updates all the information
	 * needed to the game.
	 */
	public void SetGameConfig() {
		try {
			StdDraw.clear();
			StdDraw.show();
			String senario1=JOptionPane.showInputDialog(StdDraw.frame,"Please enter a senario Number between 0 to 23");
			this.scenario=Integer.parseInt(senario1);
			while(scenario<0||scenario>23) {
				senario1=JOptionPane.showInputDialog(StdDraw.frame,"The senario number you enterd is not exist,or th argument you enterd is not an Integer.please enter a number betweeen 0 to 23","Unknown Senario");
				this.scenario=Integer.parseInt(senario1);
			}
			game=Game_Server.getServer(this.scenario);
			if(game.isRunning()) game.stopGame();
			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			RobotNum = gameServer.getInt("robots");
			Graph=new DGraph();
			Graph.init(game.getGraph());
			GameFruitsUpdate();
			ManualGameActivated=false;
			CurrentRobotsNum=0;
			if(AutomaticGameActivated) {
				AutoGameGenerator.AddRobots(game, Graph,RobotNum, fruits);
			}

		}catch(Exception e){
			e.printStackTrace();

		}
		GameRobotsUpdate();
		drawGraph();
		DrawFruits();
		StdDraw.show();


	}
	/* 
	 * manage the functionality of the manual game.
	 * if you click with the mouse on specific node, a robot appears there.
	 * when you reach the maximum robots number of your scenario,the game starts and by clicking on node
	 * the first robot in the robots list that has no destination will move to this node(of course just if the node 
	 * is reachable by moving just on one edge .
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// Find location of the point the user clicked on gui
		double x_location = StdDraw.userX(e.getX()); 
		double y_location = StdDraw.userY(e.getY()); 	

		if(ManualGameActivated) {
			// Get the node the user clicked on
			node_data pressed = getNodeOnPoint(x_location, y_location);
			if(pressed != null) {
				System.out.println(pressed.toString());
				System.out.println(pressed.getKey());
				if(CurrentRobotsNum<RobotNum) {
					game.addRobot(pressed.getKey());
					GameRobotsUpdate();
					DrawRobots();
					StdDraw.show();
					CurrentRobotsNum++;
					if(CurrentRobotsNum==RobotNum) {
						Thread MoveByClick=new Thread(new Runnable() {

							@Override
							public synchronized void run() {
								while (game.isRunning()) {
									game.move();
									try {
										GameFruitsUpdate();
										GameRobotsUpdate();
										Thread.sleep(50);
									}
									catch (InterruptedException e) {
										// TODO: handle exception
									}


								}
								GameOver();
							}});
						Thread DrawGame=new Thread(new Runnable() {

							@Override
							public synchronized void run() {
								while (game.isRunning()) {
									try {
										StdDraw.clear();
										drawGraph();
										DrawFruits();
										DrawRobots();
										DrawInfo(game.timeToEnd());
										StdDraw.show();



										Thread.sleep(50);
									}
									catch (InterruptedException e) {

									}
								}
							}});

						game.startGame();
						MoveByClick.start();
						DrawGame.start();
					}
				}
				else {
					GameRobotsUpdate();
					for(Robot robot: robots) {

						if(robot.getDest()==-1&&Graph.getEdge(robot.getSrc(),pressed.getKey())!=null){
							game.chooseNextEdge(robot.getId(),pressed.getKey());
							break;
						}
					}

				}
			}			
		}
	}
	/**
	 * Game over. a panel with your final score appears and an option to save the game info into kml file
	 */
	public void GameOver() {
		if(game.timeToEnd()==-1) {
			try {
				JSONObject Game=new JSONObject(game.toString()).getJSONObject("GameServer");
				int saveornot=JOptionPane.showConfirmDialog(StdDraw.frame,"The game is over! your final score is:  "+Game.getInt("grade")+". Do you want to save your game view? ","GameOver", JOptionPane.YES_NO_OPTION);
				if(saveornot==JOptionPane.YES_OPTION) {
					FileDialog chooser = new FileDialog(StdDraw.frame, "kml format save", FileDialog.SAVE);
					chooser.setVisible(true);
					String filename = chooser.getFile();
					if (filename != null) {
						GameKml.SaveToKmlFile(chooser.getDirectory(),chooser.getFile());
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ManualGameActivated=false;
		AutomaticGameActivated=false;
	}

	/**
	 * Updates the information about the robots of the game.
	 */
	public synchronized void GameRobotsUpdate() {
		robots=new ArrayList<Robot>();
		for(String robot : game.getRobots()) {
			robots.add(new Robot(robot));
		}
	}

	/**
	 * Updates the information about the robots of the game.
	 */
	public synchronized void GameFruitsUpdate() {
		fruits=new ArrayList<Fruit>(); 
		for(String fruit : game.getFruits()) {
			fruits.add(new Fruit(fruit));
		}
	}



	/**
	 * Draw the all the nodes and edges in the graph
	 * In addition, draw a yellow circle at the end of the edge near the node.
	 * Indicates that the edge direction is entering this node.
	 */
	public void drawGraph() {
		// Calculate boundaries and fringes of the frame
		rx = find_rx(Graph);
		ry = find_ry(Graph);
		double Xfringe = rx.get_length()/15;
		double Yfringe = ry.get_length()/10;
		StdDraw.setXscale(rx.get_min()-Xfringe, rx.get_max()+Xfringe);
		StdDraw.setYscale(ry.get_min()-Yfringe, ry.get_max()+Yfringe);
		circle = rx.get_length()/250;
		//clear the current screen
		StdDraw.clear();

		// Draw all the nodes
		for(node_data n : Graph.getV()) {
			StdDraw.setPenColor(Color.BLUE);
			Point3D src = n.getLocation();
			StdDraw.filledCircle(src.x(), src.y(), circle);
			StdDraw.text(src.x(), src.y()+circle*3, ""+n.getKey());
			// Draw all edges came out of the node
			for(edge_data e : Graph.getE(n.getKey())) {
				StdDraw.setPenColor(Color.RED);
				Point3D dest = Graph.getNode(e.getDest()).getLocation();
				StdDraw.line(src.x(), src.y(), dest.x(), dest.y());


				// Calculate where to draw the yellow circle to indicate the direction of the edge						
				double dist = dest.distance2D(src);
				double percent = (circle*5)/dist;				
				Point3D start, end;
				double z;
				if(src.x() < dest.x()) {
					start = src;
					end = dest;
					z = -(dest.x()-src.x())*percent;
				}
				else {
					start = dest;
					end = src;
					z = (src.x()-dest.x())*percent;;
				}	
				double m = (end.y()-start.y()) / (end.x()-start.x());
				double nn = (m * (-start.x())) + start.y();
				double y = (m * (dest.x()+z)) + nn;
				double x = dest.x() + z;
				StdDraw.setPenColor(Color.YELLOW);	
				StdDraw.filledCircle(x, y, circle);

			}
		}


	}


	/**
	 * Draw the fruits the currently  exist in the game
	 */
	public synchronized void DrawFruits() {
		for(Fruit f: fruits) {
			if(f.getType()==-1) StdDraw.picture(f.getLocation().x(),f.getLocation().y(),Banana,0.0005,0.0005);
			else StdDraw.picture(f.getLocation().x(),f.getLocation().y(),Apple,0.0005,0.0005);

		}
	}

	/**
	 * Draw the robots the currently  exist in the game
	 */
	public synchronized void DrawRobots() {
		for(Robot r: robots) {
			System.out.println(r.getDest());
			if(r.getDest()!=-1) {
				if(Graph.getNode(r.getSrc()).getLocation().x()<=Graph.getNode(r.getDest()).getLocation().x()) {
					StdDraw.picture(r.getPos().x(),r.getPos().y(),Robot2,0.0025,0.0015);
				}
				else StdDraw.picture(r.getPos().x(),r.getPos().y(),Robot,0.0025,0.0015);
			}
			else StdDraw.picture(r.getPos().x(),r.getPos().y(),Robot,0.0025,0.0015);
		}

	}


	/**
	 * Draw the information of your current statusin the game
	 *
	 * @param Time- the time to end of the game
	 */
	public synchronized void DrawInfo(long Time) {
		long Seconds=0;
		if(Time >0) {

			Seconds = Time/ 1000;

		}
		String TimeToEnd = String.format("%2d",Seconds);

		try {

			JSONObject gameServer = new JSONObject(game.toString()).getJSONObject("GameServer");
			int CurrentMove = gameServer.getInt("moves");
			int CurrentScore = gameServer.getInt("grade");
			StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/9, "Time:" + TimeToEnd);
			StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/12, "Score: " + CurrentScore);
			StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/19, "Moves: " + CurrentMove);
			StdDraw.text(rx.get_max() - rx.get_length()/7, ry.get_max() - ry.get_length()/43, "Scenario: " + scenario);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Find the min and max x location in this graph.
	 *
	 * @param g - the graph
	 * @return The range of x in the graph
	 */
	private Range find_rx(graph g) {
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		for(node_data n : g.getV()) {
			if(maxX < n.getLocation().x())
				maxX = n.getLocation().x();
			if(minX > n.getLocation().x())
				minX = n.getLocation().x();
		}
		return (new Range(minX, maxX));
	}

	/**
	 * Find the min and max y location in this graph.
	 *
	 * @param g - the graph
	 * @return The range of y in the graph
	 */
	private Range find_ry(graph g) {
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		for(node_data n : g.getV()) {
			if(maxY < n.getLocation().y())
				maxY = n.getLocation().y();
			if(minY > n.getLocation().y())
				minY = n.getLocation().y();
		}
		return (new Range(minY, maxY));
	}

	/**
	 * Find the node the user pressed on in the gui.
	 *
	 * @param x - the x location of the mouse
	 * @param y - the y location of the mouse
	 * @return the node the user pressed on
	 */
	private node_data getNodeOnPoint(double x, double y) {
		Point3D p = new Point3D(x, y);
		System.out.println(p.toString());
		for(node_data n : Graph.getV()) {
			System.out.println(circle);
			System.out.println(p.distance2D(n.getLocation()));
			if(p.distance2D(n.getLocation()) < circle) {
				System.out.println(n.toString());
				return n;
			}
		}
		return null;
	}

	private void ShowStatistics() {
		List<String> DBstatistics=new ArrayList<>(SimpleDB.GetInfo());
		JOptionPane stats=new JOptionPane();
	}


	@Override
	public void mousePressed(MouseEvent e) {}


	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}


	@Override
	public void mouseExited(MouseEvent e) {}



}



