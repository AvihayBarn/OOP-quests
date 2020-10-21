package gameClient;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.node_data;

import utils.Point3D;


/**
 * The Class Robot.
 */
public class Robot {

	/** The id. */
	private int id;

	/** The value. */
	private double value;

	/** The src. */
	private int src;

	/** The dest. */
	private int dest;

	/** The speed. */
	private double speed;

	/** The pos. */
	private Point3D pos;
	
	private List<node_data> path;
	
	private Fruit Goingto;


	/**
	 * Instantiates a new robot from json string
	 *
	 * @param fromJSON the from JSON
	 */
	public Robot(String fromJSON) {
		JSONObject obj;
		try {
			obj = new JSONObject(fromJSON);
			JSONObject robot=obj.getJSONObject("Robot");
			this.id=robot.getInt("id");
			this.value=robot.getDouble("value");
			this.src=robot.getInt("src");
			this.dest=robot.getInt("dest");
			this.speed=robot.getDouble("speed");
			this.pos=new Point3D(robot.getString("pos"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inits the.
	 *
	 * @param fromJSON the from JSON
	 */
	public void Init(String fromJSON) {
		JSONObject obj;
		try {
			obj = new JSONObject(fromJSON);
			JSONObject robot=obj.getJSONObject("Robot");
			this.id=robot.getInt("id");
			this.value=robot.getDouble("value");
			this.src=robot.getInt("src");
			this.dest=robot.getInt("dest");
			this.speed=robot.getDouble("speed");
			this.pos=new Point3D(robot.getString("pos"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id=id;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Gets the src.
	 *
	 * @return the src
	 */
	public int getSrc() {
		return src;
	}

	/**
	 * Gets the dest.
	 *
	 * @return the dest
	 */
	public int getDest() {
		return dest;
	}

	/**
	 * Sets the dest.
	 *
	 * @param dest the new dest
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Gets the pos.
	 *
	 * @return the pos
	 */
	public Point3D getPos() {
		return pos;
	}
	public void setPath(List<node_data> list) {
		path=list;
	}
	public List<node_data> getPath(){
		return this.path;
	}
	public void setFruit(Fruit f) {
		Goingto=f;
	}
	public Fruit getFruit() {
		return Goingto;
	}


	/**
	 * to Json String.
	 * @return Json string
	 */
	public String toString() {
		return "{\"Robot\":{\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":" + this.src + "," + "\"dest\":" +this.dest+ "," + "\"speed\":" + this.speed + "," + "\"pos\":\"" + this.pos.toString() + "\"" + "}" + "}";
	}


}
