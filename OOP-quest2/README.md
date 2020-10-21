# OOP_Ex2
#### Authors: Meir Nizri and Avihay Bernholtz
This repository represents a graph with nodes and edges, on which you can run basic graph algorithms and draw in GUI.
Most of operations in the graph are performed with the complexity of maximum O(n), where n is the number of nodes in the graph.



## **data structures**
This project contains three types of data structures:

### **Node**
Represents a node in the graph, the data structure fields are:
<br> **Key** - Each node has a unique numeric key that cannot be changed.
<br> **Location** - The location of the node in the graph. The location is represented using the Point3D class.
However, in the graph GUI we will only use two dimensions to represent the node.
<br> **Weight** (double), **Tag** (int), and **Info** (String) - auxiliary fields used for graph algorithms.     
<br> The methods defined on the node are:
<br> **get** and **set** for each of the node's fields.

### **Edge**
Represents an Edge in the graph. The data structure fields are:
<br> **src** - The source node key from which the edge starts.
<br> **dest** - The destination node key in which the edge is ending.
<br> **Weight** - The cost of moving in the edge. this value is non-negative double
<br> **Tag** (int), and **Info** (String) - auxiliary fields used for graph algorithms.   
<br> The methods defined on the edge are:
<br> **get** and **set** for each of the edge's fields.

### **Graph**
Represents a Graph. The data structure fields are:
<br> **nodes** - HashMap of Nodes. The key is the key of each node and the value is the node containing that key.
<br> **adjacencyMap** - HashMap of HashMaps. The key is the key of each node and the value is a HashMap containing all the
edged exiting out of that node. Within this HashMap, the key is the destination key of each edge and the value is the edge itself.
<br> **numEdges** - Contains the number of edged in any given situation in the graph
<br> **mc** - mode count. Only when something changes in the graph this fiels updates. Its purpose is to track changes.   
<br> The methods defined on the graph are:
<br> **get Node** - Gets a key and returns the corresponding node. If node does not exist returns null.
<br> **get Edge** - Gets a source key and a destination key and returns the edge between them. If the edge does not exist null returns.
<br> **Add Node** - Gets a node and adds it to the graph. If the node already exists in the graph, a runtime exception will be returned.
<br> **Connection** - Receives a destination key, source key, and weight. Connects edge between the two nodes with the weight it received. If the edge already exists in the graph, or the source and destination key do not exist in the graph, or are the same - an appropriate runtime exception will be returned.
<br> **Remove Node** - Removes the node and all edges that exit or reach it. If the node does not exist returns null.
<br> **Remove Edge** - Removes the edge. If the edge does not exist returns null.
<br> **getV** - Returns a collection of all nodes in the graph. If the graph is empty returns null.
<br> **getE** - Gets a get a key of source node, and returns a collection of all edges coming out of it. if aren't any return null.
<br> **Node Size** - Returns the number of nodes in the entire graph.
<br> **Edge Size** - Returns the number of edges in the entire graph.



## **Graph Algorithms**
Represents algorithm operated on graph. This calss fields are:
<br> **graph**. We chose to allow this graph to be public, So anyone who runs algorithms on the graph can also access this graph.
<br> The methods defined on the graph algorithms are:    
<br> **init** - Gets a graph and initializes the algorithm graph to contain this graph.
<br> **Save** - saves the graph to a text file. Performs through serialization.
<br> **Load** - Initializes the graph from a text file. Performs through serialization.
<br> **Is Connect** - Checks whether the graph is strongly connected,i.e. there is a valid path from EVREY node to eachother node.
NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).  If connected returns truth, else returns false.
<br> **Shortest path distance** - Gets two keys of two nodes, and returns the shortest path length between them. Is done using the Dijkstra's algorithm. If no valid path returns -1.
<br> **Shortest path** - Gets two keys of two nodes and returns the shortest path between them as an ordered List of nodes:. If no such path - returns null.
<br> **TSP** - Gets a target List of nodes, and returns a  relatively short path which visit each node in the targets List. Note: this is NOT the classical traveling salesman problem, as you can visit a node more than once, and there is no need to return to source node. just a simple path going over all nodes in the list. If no such path returns null.
<br> **copy** - Returns a deep copy of the graph.



## **Graph GUI**
Represents a graphical interface where you can see the graph, and do the following operations on it:
<br> **Save to Image** - Creates a new file with type png or jpeg, containing the drawing of the graph.
<br> **Save** - Saves the graph to text file.
<br> **Load** - Draws a new graph from text file the user chose.
<br> **Is Connected** - Checks whether the graph is connected or not.
<br> **Shortest Path** - Draws in green the shortest Path between two nodes the user chooses.
<br> **TSP** - Draws a relatively short green path between all nodes the user has chooses.
<br> **Add node** - Adds a node to the graph where the user has selected.
<br> **Add Edge** - Adds an edge between two nodes that the user has selected. will ask the user for weight to the edge.
<br> **Delete node** - Deletes a node the user has chosen to delete.
<br> **Delete Edge** - Delete an edge between two nodes that the user has selected.
<br> *Some of the operation has a shortcut keys as written in the menu bar.



## **Utilities**
In the project we used three well-known classes:
<br> **Point3D** - to indicate the location of each node
<br> **Range** - to calculate the GUI screen range.
<br> **StdDraw** - to draw the graph



<br> <br> This project is our second assignment in Object Oriented Course at Ariel University. The course is led by Professor Boaz Ben Moshe.
