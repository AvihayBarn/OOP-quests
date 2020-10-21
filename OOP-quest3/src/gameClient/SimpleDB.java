package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	private static final int id=205487283;
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
		int id = 205487283;  // "real" existing ID & KML

		int level = 0;//1,2,3
		//allUsers();	
		//String kml1 = getKML(id1,level);
		System.out.println("***** KML1 file example: ******");
		List<String> stats=SimpleDB.GetInfo();
		for(String s:stats) {
			System.out.println(s);
		}
		//System.out.println(kml1);
	}
	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static List<String> GetInfo() {
		List<String> statistics=new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs where userID = "+id;

			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int ind =0;
			while(resultSet.next())
			{
				statistics.add(ind+") Id: " + resultSet.getInt("UserID")+", level: "+resultSet.getInt("levelID")+", score: "+resultSet.getInt("score")+", moves: "+resultSet.getInt("moves")+", time: "+resultSet.getDate("time"));
				ind++;
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();

		}
		return statistics;
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID")+", max_level:"+resultSet.getInt("levelNum"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static int compare(int id, int stage) {
		int myPosition = 1;
		int myMaxScore = 0;
		// Maximum moves allowed at certain steps.
		int[] movesLimit = {290,580,-1,580,-1,500,-1,-1,-1,580,-1,580,-1,580,-1,-1,290,-1,-1,580,290,-1,-1,1140};

		try {
			// Connect to DB
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);				
			Statement statement = connection.createStatement();

			// Calculates te best result of the user.
			String selectScore = "";
			// Set the query request according to if this level are limtited with number of moves.
			if(movesLimit[stage] == -1) selectScore = "SELECT score FROM Logs where (userID="+id+" AND levelID="+stage+");";
			else selectScore = "SELECT score FROM Logs where (userID="+id+ " AND levelID="+stage+" AND moves<="+movesLimit[stage]+");";
			ResultSet resultSet = statement.executeQuery(selectScore);	
			while(resultSet.next()) {
				int myScore = resultSet.getInt("score");
				if(myScore > myMaxScore) 
					myMaxScore = myScore;
			}

			// Gets all the user played in stage.
			String selectUsers = "SELECT * FROM Users where levelNum="+stage+";";
			resultSet = statement.executeQuery(selectUsers);	

			// Check if there is a player got greater result from the user.
			while(resultSet.next()) {
				int userId = resultSet.getInt("UserID");
				// Set the query request according to if this level are limtited with number of moves.
				if(movesLimit[stage] == -1) selectScore = "SELECT score FROM Logs where (userID="+userId+" AND levelID="+stage+");";
				else selectScore = "SELECT score FROM Logs where (userID="+userId+ " AND levelID="+stage+" AND moves<="+movesLimit[stage]+");";
				ResultSet resultSet2 = statement.executeQuery(selectScore);	
				while(resultSet2.next()) {
					// If found better result push position up.
					if(resultSet2.getInt("score") > myMaxScore) {
						myPosition++;
						break;
					}
				}
			}		
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return myPosition;
	}

}


