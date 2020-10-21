package dataStructure;

import java.io.Serializable;

import utils.Point3D;

/**
 * This interface represents the set of operations applicable on a 
 * node (vertex) in a (directional) positive weighted graph.
 * @author Dolev Brender, Avihai Bernholtz
 */
public class node implements node_data, Serializable {

	/** The key. */
	private int key;
	
	/** The weight. */
	private double weight;  // > 0
	
	/** The location. */
	private Point3D location;
	
	/** The info. */
	private String info;
	
	/** The tag. */
	private int tag;
	
	/** The id. */
	// Static variable that provide a key for each node built by the default constructor
	public static int id = 1;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public node() {
		this.key = id++;
		this.weight = 0;
		this.location = null;
		this.info = "";
		this.tag = 0;
	}

	/**
	 * Initialize an node.
	 *
	 * @param k - the key of the node
	 */
	public node(int k) {
		this.key = k;
		this.weight = 0;
		this.location = null;
		this.info = "";
		this.tag = 0;
	}

	/**
	 * Initialize an node.
	 *
	 * @param p - the location of the node
	 */
	public node(Point3D p) {
		this.key = id++;
		this.weight = 0;
		this.location = p;
		this.info = "";
		this.tag = 0;
	}


	/**
	 * Instantiates a new node.
	 *
	 * @param k the k
	 * @param p the p
	 */
	public node(int k,Point3D p) {
		this.key = k;
		this.weight = 0;
		this.location = p;
		this.info = "";
		this.tag = 0;
	}


	/**
	 * Gets the key.
	 *
	 * @return the key (id) associated with this node.
	 */
	@Override
	public int getKey() {
		return this.key;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location (of applicable) of this node, if none return null.
	 */
	@Override
	public Point3D getLocation() {
		return this.location;
	}

	/** Allows changing this node's location.
	 * @param p - new location (position) of this node.
	 */
	@Override
	public void setLocation(Point3D p) {
		this.location = p;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight associated with this node.
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	@Override
	public void setWeight(double w) {
		if(w < 0)
			throw new NumberFormatException("ERR: The weight of the node: " + this.toString() + " must be non-negative");
		this.weight = w;
	}

	/**
	 * Gets the info.
	 *
	 * @return the remark (meta data) associated with this node.
	 */
	@Override
	public String getInfo() {
		return info;
	}

	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s - the information the node will save
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	/**
	 * Gets the tag.
	 *
	 * @return @return the temporal data (aka color: e,g, white, gray, black) which can be used by algorithms.
	 */
	@Override
	public int getTag() {
		return this.tag;
	}

	/** 
	 * Allow setting the "tag" value for temporal marking an node - common practice for marking by algorithms.
	 * @param t - the new value of the tag.
	 */
	@Override
	public void setTag(int t) {
		this.tag = t;
	}

	/**
	 * Test if this node is logically equals to obj.
	 * @param obj - the object to check if equal.
	 * @return true iff this node represent the same edge as obj.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof node))
			throw new IllegalArgumentException("The argument you entered is not instance of node");
		node n = (node) obj;
		return (this.getKey() == n.getKey());
	}

	/**
	 * returns a a string that represent the node
	 * 
	 * @return string representation of the node
	 */
	@Override
	public String toString() {
		return this.getKey() + "," + this.getTag() + "," + this.getWeight() + ":" + this.getInfo();
	}

	/**
	 * To JSON.
	 *
	 * @return the string
	 */
	public String toJSON() {
		String ans = "";
		ans = String.valueOf(ans) + "{id:" + this.getKey() + ",info:" + this.getInfo() + "}";
		return ans;
	}

	/**
	 * Reset id.
	 */
	public static void resetId() {
		node.id = 1;
	}
}