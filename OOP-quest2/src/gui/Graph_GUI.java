package gui;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import algorithms.*;
import dataStructure.*;
import utils.*;

public class Graph_GUI implements ActionListener, MouseListener {
	// The graph to draw
	public graph Graph = new DGraph();
	private Graph_Algo ga = new Graph_Algo();
	// Range for x and y scale
	private Range rx, ry;
	// Radius of circle to draw node
	private double circle, EdgeWeight;
	// Variables to distinguish which algorithems was activated
	private boolean shortestsPathActivated, clickedOnce, TSPActivated;
	private boolean addNodeActivated, addEdgeActivated, removeNodeActivated, removeEdgeActivated;
	private node_data src, dest;
	private List<Integer> TSPKeys = new LinkedList<Integer>();
	private List<node_data> GreenPath = null;

	/**
	 * Default constructor
	 */
	public Graph_GUI() {
		Graph = new DGraph();
	}
	
	/**
	 * Initializes a GUI with graph g
	 * @param g - the graph to draw
	 */
	public Graph_GUI(graph g) {
		Graph = g;
		initGUI();
	}

	/**
	 * Enter graph to GUI
	 * @param g - the graph to draw
	 */
	public void init(graph g) {
		Graph = g;
		initGUI();
	}
	
	/**
	 * Initializes a GUI with all the preliminary preparations
	 * We used StdDraw library to draw the graph
	 */
	public void initGUI() {
		// Set thread listen to changes in the graph
		GraphChangeListener changeListener = new GraphChangeListener(this);
		Thread t = new Thread(changeListener);
		t.start();
		// Preparation of the frame
		StdDraw.setCanvasSize(1200,600);
		StdDraw.setMenuBar(createMenuBar());
		StdDraw.addMouseListener(this);
		// Draw the graph
		this.drawGraph();
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
		circle = rx.get_length()/200;
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
				StdDraw.text((src.x()+dest.x())/2, (src.y()+dest.y())/2, ""+ e.getWeight());
				
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
		
		// If Shortest path is activated and found the path - draw the shortest path in green
		if(GreenPath != null) {
			StdDraw.setPenColor(Color.GREEN);
			// Draw line between any two adjacent nodes in the list that returned from the shortestPath function
			Iterator<node_data> itr = GreenPath.iterator();
			Point3D p1 = itr.next().getLocation();
			while(itr.hasNext()) {
				Point3D p2 = itr.next().getLocation();
				StdDraw.line(p1.x(), p1.y(), p2.x(), p2.y());
				p1 = p2;
			}			
		}
	}      

	/**
	 * This function identifies when an action performed.
	 * We will use this function to identify when the user clicked an option from the menu bar
	 * @param e - The object that is sent when an action performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Init all algorithms flags to false
		shortestsPathActivated = false;
		clickedOnce = false;
		TSPActivated = false;
		addNodeActivated = false;
		addEdgeActivated = false;
		removeNodeActivated = false;
		removeEdgeActivated = false;
		GreenPath = null;
		
		String ActivatedItem = e.getActionCommand();
		
		// Save the draw to png or jpeg file.
		// The user can choose file to overwrite or make new file in the directory he chooses.
		if(ActivatedItem.equals("Save To Image")) {
			FileDialog chooser = new FileDialog(StdDraw.frame, "Use a .png or .jpg extension", FileDialog.SAVE);
			chooser.setVisible(true);
			String filename = chooser.getFile();
			if (filename != null) {
				StdDraw.save(chooser.getDirectory() + File.separator + chooser.getFile());
			}
		}
		
		// Save to graph text file.
		if(ActivatedItem.equals("Save")) {
			ga.init(Graph);
			
			FileDialog chooser = new FileDialog(StdDraw.frame, "Save As..", FileDialog.SAVE);
			chooser.setVisible(true);
			String filename = chooser.getFile();
			if(filename != null) {
				String path = chooser.getDirectory() + File.separator + chooser.getFile();
				try {
					ga.save(path);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(StdDraw.frame, e1.getMessage(),
							"Error", JOptionPane.PLAIN_MESSAGE);					
				}
			}
		}
		
		// Load graph from text file.
		if(ActivatedItem.equals("Load")) {
			FileDialog chooser = new FileDialog(StdDraw.frame, "Load From..", FileDialog.LOAD);
			chooser.setVisible(true);
			String filename = chooser.getFile();
			if(filename != null) {
				String path = chooser.getDirectory() + File.separator + chooser.getFile();
				try {
					ga.init(path);
					Graph = ga.AlgoG;
					this.drawGraph();
					JOptionPane.showMessageDialog(StdDraw.frame, "Load successfully completed",
							"Load", JOptionPane.PLAIN_MESSAGE);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(StdDraw.frame, e1.getMessage(),
							"Error", JOptionPane.PLAIN_MESSAGE);					
				}
			}
		}

		// Show message to user if the graph connected or not
		if(ActivatedItem.equals("Is Connected")) {
			String result = "";
			ga.init(Graph);
			if(ga.isConnected())
				result = "The Graph is Connected.";
			else
				result = "The Graph is not Connected.";
			JOptionPane.showMessageDialog(StdDraw.frame, result, "Is Connected", JOptionPane.PLAIN_MESSAGE);
		}
		
		// Changes the flag of the shortestPath function. So now can select nodes to see the shortest path.
		if(ActivatedItem.equals("Shortest Path")) {
			JOptionPane.showMessageDialog(StdDraw.frame, "Choose 2 nodes you want on the graph"
													+ " to calculate the shortest Path between them",
													"Shortests Path", JOptionPane.PLAIN_MESSAGE);
			shortestsPathActivated = true;
			GreenPath = null;
		}
		// Changes the flag of the TSP function. So now can select nodes to see the path to all of them.
		if(ActivatedItem.equals("TSP")) {
			JOptionPane.showMessageDialog(StdDraw.frame, "Choose how many nodes you want on the graph"
													+ " to calculate the shortest Path between them",
													"TSP", JOptionPane.PLAIN_MESSAGE);
			TSPActivated = true;
			ga.init(Graph);
			TSPKeys = new LinkedList<Integer>();
			GreenPath = null;
		}
		
		// Changes the flag of edit operations. So now can select node/edge to add/remove.
		if(ActivatedItem.equals("Add Node")) 
			addNodeActivated = true;	
		if(ActivatedItem.equals("Add Edge")) 
			addEdgeActivated = true;
		if(ActivatedItem.equals("Remove Node")) 
			removeNodeActivated = true;	
		if(ActivatedItem.equals("Remove Edge")) 
			removeEdgeActivated = true;
	}

	/**
	 * This function identifies when the mouse is clicked.
	 * We will use this function To find location of the point the user clicked on gui
	 * @param e - The object that is sent when the mouse is clicked.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// Find location of the point the user clicked on gui
		double x_location = StdDraw.userX(e.getX()); 
		double y_location = StdDraw.userY(e.getY()); 	

		if(shortestsPathActivated) {
			findNodes(x_location, y_location);
			// If found 2 nodes calculate the shortest path and draw the graph with this path
			if(src!=null && dest!=null) {
				ga.init(Graph);
				List<node_data> path = ga.shortestPath(src.getKey(), dest.getKey());
				if(path!=null) {
					GreenPath = path;
					this.drawGraph();
				}
				// if the path doesn't exsist inform the user.
				else 
					JOptionPane.showMessageDialog(StdDraw.frame, "There is no path between the two nodes you selected",
												   "Shortests Path", JOptionPane.PLAIN_MESSAGE);
				// Init all shortest path variables for the next operation
				src = null; dest = null;
				shortestsPathActivated = false;
			}			
		}
		
		if(TSPActivated) {
			node_data pressed = getNodeOnPoint(x_location, y_location);
			if(pressed != null) {
				if(!TSPKeys.contains(pressed.getKey()))
					TSPKeys.add(pressed.getKey());
				List<node_data> TSPNodes = ga.TSP(TSPKeys);					
				if(TSPNodes != null) {
					GreenPath = TSPNodes;
					this.drawGraph();	
				}
				else {
					JOptionPane.showMessageDialog(StdDraw.frame, "There is no path between the nodes you selected",
												 "TSP", JOptionPane.PLAIN_MESSAGE);
					TSPActivated = false;
					TSPKeys = null;
				}
			}
		}
		
		if(addNodeActivated) {
			// Make new node in this location, and draw the graph with this node
			Point3D p = new Point3D(x_location, y_location);
			node_data n = new node(p);
			Graph.addNode(n);
			this.drawGraph();
		}

		if(addEdgeActivated) {
			findNodes(x_location, y_location);
			// Make new edge between the 2 nodes the user chooses, and draw the graph with this edge.
			if(src!=null && dest!=null) {
				// Get weight for the from the user 
				String weightReceived = JOptionPane.showInputDialog(StdDraw.frame,"Enter non-negative weight for the new edge");
				try {
					EdgeWeight = Double.parseDouble(weightReceived);
					Graph.connect(src.getKey(), dest.getKey(), EdgeWeight);				
				}
				// IF the edge non-negative double
				catch (NumberFormatException e1){
					JOptionPane.showMessageDialog(StdDraw.frame, "The weight must be non-negative double",
												  "Add Edge", JOptionPane.PLAIN_MESSAGE);
				}
				// If the edge already exists inform the user.
				catch (RuntimeException e1) {
					JOptionPane.showMessageDialog(StdDraw.frame, e1.getMessage(),
													"Add Edge", JOptionPane.PLAIN_MESSAGE);
				}
				src = null; dest = null;
				this.drawGraph();
			}
		}
		
		if(removeNodeActivated) {
			// Remove the node the user pressed, and draw the graph without this node
			node_data pressed = getNodeOnPoint(x_location, y_location);
			if(pressed != null) {
				Graph.removeNode(pressed.getKey());
				this.drawGraph();
			}
		}
		
		if(removeEdgeActivated) {
			findNodes(x_location, y_location);
			// Remove the edge between the 2 nodes the user chooses, and draw the graph without this edge
			if(src!=null && dest!=null) {
				edge_data removedEdge = Graph.removeEdge(src.getKey(), dest.getKey());
				// If the edge doesn't exists inform the user.
				if(removedEdge == null)
					JOptionPane.showMessageDialog(StdDraw.frame, "There is no such edge in this graph",
												"Remove Edge", JOptionPane.PLAIN_MESSAGE);
				src = null; dest = null;
				this.drawGraph();
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	/**
	 * This function creates the menu bar in the top of the window. Some of the items has a shortcut keys.
	 * The operations are:
	 * Save to Image - Creates a new file with type png or jpeg, containing the drawing of the graph
	 * Save - Saves the graph to a text file
	 * Load - Draws a new graph from text file the user chose
	 * Is Connected - Checks whether the graph is connected or not.
	 * Shortest Path - Draws  in green the shortest Path between two nodes the user chooses.
	 * TSP - Draws a relatively short green path between all nodes the user has chooses.
	 * Add node - Adds a node to the graph where the user has selected.
	 * Add Edge - Adds an edge between two nodes that the user has selected.
	 * Delete node - Deletes a node the user has chosen to delete.
	 * Insert Edge - Delete an edge between two nodes that the user has selected.
	 * @return JMenuBar Object to enter the JFrame
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Creates all the File options
		JMenu File = new JMenu("File");
		menuBar.add(File);
		JMenuItem SaveToImage = new JMenuItem("Save To Image");
		SaveToImage.addActionListener(this);
		SaveToImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
			    KeyEvent.VK_SHIFT | KeyEvent.VK_CONTROL));
		File.add(SaveToImage);
		JMenuItem Save = new JMenuItem("Save");
		Save.addActionListener(this);
		Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		File.add(Save);
		JMenuItem Load = new JMenuItem("Load");
		Load.addActionListener(this);
		Load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		File.add(Load);	

		// Creates all the Algorithms options
		JMenu Algorithms = new JMenu("Algorithms");
		menuBar.add(Algorithms);
		JMenuItem IsConnected = new JMenuItem("Is Connected");
		IsConnected.addActionListener(this);
		Algorithms.add(IsConnected);
		JMenuItem ShortestPath = new JMenuItem("Shortest Path");
		ShortestPath.addActionListener(this);
		Algorithms.add(ShortestPath);
		JMenuItem TSP = new JMenuItem("TSP");
		TSP.addActionListener(this);
		Algorithms.add(TSP);		

		// Creates all the Edit options
		JMenu Edit = new JMenu("Edit");
		menuBar.add(Edit);
		JMenuItem AddNode = new JMenuItem("Add Node");
		AddNode.addActionListener(this);
		Edit.add(AddNode);
		JMenuItem AddEdge = new JMenuItem("Add Edge");
		AddEdge.addActionListener(this);
		Edit.add(AddEdge);
		JMenuItem RemoveNode = new JMenuItem("Remove Node");
		RemoveNode.addActionListener(this);
		Edit.add(RemoveNode);
		JMenuItem RemoveEdge = new JMenuItem("Remove Edge");
		RemoveEdge.addActionListener(this);
		Edit.add(RemoveEdge);

		return menuBar;
	}
	
	/**
	 * Find the min and max x location in this graph
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
	 * Find the min and max y location in this graph
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
	 * Find the node the user pressed on in the gui
	 * @param x - the x location of the mouse
	 * @param y - the y location of the mouse
	 * @return the node the user pressed on
	 */
	private node_data getNodeOnPoint(double x, double y) {
		Point3D p = new Point3D(x, y);
		for(node_data n : Graph.getV()) {
			if(p.distance2D(n.getLocation()) < circle) {
				return n;
			}
		}
		return null;
	}
	
	private void findNodes(double x, double y) {
		// Find the node pressed on and enter to src and dest node
		node_data pressed = null;
		pressed = getNodeOnPoint(x, y);
		if(!clickedOnce && pressed!=null) {
			src = pressed;
			dest = null;
			clickedOnce = true;
		}
		else if(clickedOnce && pressed!=null)	{
			dest = pressed;
			clickedOnce = false;
		}
	}
}