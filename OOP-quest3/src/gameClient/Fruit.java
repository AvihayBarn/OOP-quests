package gameClient;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;


/**
 * The Class Fruit.
 */
public class Fruit {
	
	/** The value. */
	private double value;
	
	/** The type. */
	private int type;
	
	/** The pos. */
	private Point3D pos;
	
	
	
	/**
	 * Instantiates a new fruit.
	 *
	 * @param fromJSON the from JSON
	 */
	public Fruit(String fromJSON) {
		try {
			JSONObject fruit=new JSONObject(fromJSON).getJSONObject("Fruit");
			this.value=fruit.getDouble("value");
			this.type=fruit.getInt("type");
			this.pos=new Point3D(fruit.getString("pos"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Inits the fruits from Json String
	 *
	 * @param fromJSON the from JSON
	 */
	public void Init(String fromJSON) {
		JSONObject obj;
		try {
			obj = new JSONObject(fromJSON);
			JSONObject fruit=obj.getJSONObject("Fruit");
			this.value=fruit.getDouble("value");
			this.type=fruit.getInt("type");
			this.pos=new Point3D(fruit.getString("pos"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Point3D getLocation() {
		return pos;
	}

	
	/**
	 * to Json String
	 * @return Json String
	 */
	public String toString() {
		return "{\"Fruit\":{\"value\":"+this.value+",\"type\":"+this.type+",\"pos\":\""+this.pos.toString()+"\"}}";
	}


}
