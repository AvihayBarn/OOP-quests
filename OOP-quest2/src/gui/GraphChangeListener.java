package gui;

public class GraphChangeListener implements Runnable{
	Graph_GUI GUI;
	int MC;
	
	public GraphChangeListener(Graph_GUI GUI) {
		this.GUI = GUI;
		MC = GUI.Graph.getMC();
	}

	@Override
	public void run() {
		// Infinite loop
		while (true) {
			// If mode count change redraw the graph
			if(GUI.Graph.getMC() != MC) {
				GUI.drawGraph();
				MC = GUI.Graph.getMC();
			}
			try {
				Thread.sleep(1000);
			}
	    	catch (InterruptedException e) {}			
		}
	}
}