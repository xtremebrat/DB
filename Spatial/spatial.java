import java.io.*;
import java.sql.*;

public class spatial {
	private static final int BUILDING = 0;
	private static final int FIRE_HYDRANT = 1;
	private static final int FIRE_BUILDING = 2;
	private static final String URL="jdbc:oracle:thin:localhost:1521:orcl";
	private static final String ID="scott";
	private static final String PASSWORD="tiger";

	public spatial() {

	}
	
	public static Connection connectDB() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			System.out.print("JDBC driver Loaded");
			System.out.println("Connecting to the DB");
			Connection conn = DriverManager
					.getConnection(URL, ID, PASSWORD);
			System.out.println("success!!!");
			return conn;
		} catch (Exception e) {
			System.out.println("Error occurred while connecting to DB: " + e.toString());
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	public static void main(String[] args) {
		try {
			
			Connection conn = connectDB();
			if (args[0].equalsIgnoreCase("window")) {
				window_proc(args, conn);
			} else if (args[0].equalsIgnoreCase("within")) {
				winthin_procedure(args, conn);
			} else if (args[0].equals("nn")) {
				nn_procedure(args, conn);
			} else if (args[0].equals("demo")) {
				demo_procedure(args, conn);
			} else {
				System.out.println("Error: invalid command.");
				return;
			}
			return;
		} catch (Exception e) {
		}
	}

	public static void window_proc(String[] args, Connection conn)
			throws NumberFormatException, Exception {
		int type = -1;
		if (args.length != 6) {
			System.out.println("ERROR: window command line argument number error.");
		}
		if (args[1].equalsIgnoreCase("building")) {
			type = BUILDING;
		} else if (args[1].equalsIgnoreCase("FIREHYDRANT")) {
			type = FIRE_HYDRANT;
		} else if (args[1].equalsIgnoreCase("FIREBUILDING")) {
			type = FIRE_BUILDING;
		} else {
			System.out.println("ERROR: window command type error.");
			return;
		}
		win_query(type, Float.parseFloat(args[2]),
				Float.parseFloat(args[3]), Float.parseFloat(args[4]),
				Float.parseFloat(args[5]), conn);
	}

	public static void winthin_procedure(String[] args, Connection conn)
			throws NumberFormatException, Exception {
		int type = -1;
		if (args.length != 4) {
			System.out.println("ERROR: within command line argument number error.");
		}
		if (args[1].equalsIgnoreCase("building")) {
			type = BUILDING;
		} else if (args[1].equalsIgnoreCase("FIREHYDRANT")) {
			type = FIRE_HYDRANT;
		} else if (args[1].equalsIgnoreCase("FIREBUILDING")) {
			type = FIRE_BUILDING;
		} else {
			System.out.println("ERROR: within command type error.");
			return;
		}
		within_query(type, args[2].toString(), Integer.parseInt(args[3]), conn);
	}

	public static void nn_procedure(String[] args, Connection conn)
			throws NumberFormatException, Exception {
		int type = -1;
		if (args.length != 4) {
			System.out.println("ERROR: within command line argument number error.");
		}
		if (args[1].equalsIgnoreCase("building")) {
			type = BUILDING;
		} else if (args[1].equalsIgnoreCase("FIREHYDRANT")) {
			type = FIRE_HYDRANT;
		} else if (args[1].equalsIgnoreCase("FIREBUILDING")) {
			type = FIRE_BUILDING;
		} else {
			System.out.println("Error: nn command type error");
			return;
		}
		n_query(type, args[2].toString(), Integer.parseInt(args[3]), conn);
	}

	public static void demo_procedure(String[] args, Connection conn)
			throws NumberFormatException, Exception {
		int type = Integer.parseInt(args[1]);
		if (type == 1) {
			demo_1(conn);
		} else if (type == 2) {
			demo_2(conn);
		} else if (type == 3) {
			demo_3(conn);
		} else if (type == 4) {
			demo_4(conn);
		} else if (type == 5) {
			demo_5(conn);
		} else {
			System.out.println("Error: demo command type error.");
			return;
		}
	}
	
	public static void win_query(int type, float x1, float y1, float x2,
			float y2, Connection conn) throws Exception {
		String query1 = "";
		if (type == BUILDING) {
			query1="SELECT B.BID AS Q1 FROM BUILDING B WHERE B.BUILD_NAME NOT IN (SELECT FNAME FROM FIRE_BUILDING) AND SDO_INSIDE(B.BUILD_SHAPE, SDO_GEOMETRY(2003,NULL,NULL, SDO_ELEM_INFO_ARRAY(1,1003,3), SDO_ORDINATE_ARRAY("+x1+","+y1+","+x2+","+y2+")))='TRUE';";
			
		} else if (type == FIRE_HYDRANT) {
			query1 = "SELECT F.FIREID FROM FIRE_HYDRANT F WHERE SDO_INSIDE(F.FIRE_SHAPE,"
					+ "SDO_GEOMETRY(2003,NULL,NULL, SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY("
					+ x1 + "," + y1 + "," + x2 + "," + y2 + ")))='TRUE'";
		} else {
			query1 = "SELECT B.BID FROM BUILDING B, FIRE_BUILDING F WHERE B.BUILD_NAME=F.FNAME "
					+ "AND SDO_INSIDE(B.BUILD_SHAPE, SDO_GEOMETRY(2003,NULL,NULL, SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY("
					+ x1 + "," + y1 + "," + x2 + "," + y2 + ")))='TRUE'";
		}
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query1);

		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + "\n");
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void within_query(int type, String BUILD_NAME, float dist,
			Connection conn) throws Exception {
		String query2 = "";
		if (type == BUILDING) {
			query2="SELECT B1.BID AS ANSWER1 FROM BUILDING B1,BUILDING B2 WHERE B2.BUILD_NAME='"+BUILD_NAME+"' AND B1.BID!=B2.BID AND " +
					"B1.BUILD_NAME NOT IN (SELECT FNAME FROM FIRE_BUILDING) AND SDO_WITHIN_DISTANCE(B1.BUILD_SHAPE,B2.BUILD_SHAPE," +
					"'DISTANCE="+dist+"')='TRUE'";
		} else if (type == FIRE_HYDRANT) {
			query2 = "SELECT F.FIREID  FROM FIRE_HYDRANT F,BUILDING B WHERE B.BUILD_NAME='"
					+ BUILD_NAME
					+ "' AND  SDO_WITHIN_DISTANCE(F.FIRE_SHAPE, B.BUILD_SHAPE,'DISTANCE="
					+ dist + "')='TRUE'";
		} else if (type == FIRE_BUILDING) {
			query2 = "SELECT B1.BID FROM BUILDING B1,BUILDING B2, FIRE_BUILDING F WHERE B1.BUILD_NAME=F.FNAME AND B2.BUILD_NAME='"
					+ BUILD_NAME
					+ "' AND SDO_WITHIN_DISTANCE(B2.BUILD_SHAPE, B1.BUILD_SHAPE,'DISTANCE="
					+ dist + "')='TRUE' AND B1.BID!=B2.BID";

		}
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query2);
		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + "\n");
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void n_query(int type, String bid, int num, Connection conn)
			throws Exception {
		String query3 = "";
		if (type == BUILDING) {
			query3 = "SELECT B1.BID FROM BUILDING B1,BUILDING B2 WHERE B2.BID='"
					+bid +"' AND B1.BUILD_NAME NOT IN (SELECT FNAME FROM FIRE_BUILDING) AND B1.BID!=B2.BID " +
							"AND SDO_NN(B1.BUILD_SHAPE,B2.BUILD_SHAPE,'SDO_NUM_RES="+(num+1)+"')='TRUE'";
			
		} else if (type == FIRE_HYDRANT) {
			query3 = "SELECT F.FIREID FROM BUILDING B,FIRE_HYDRANT F WHERE B.BID='"
					+ bid + "' AND SDO_NN(F.FIRE_SHAPE, B.BUILD_SHAPE,'SDO_NUM_RES="
					+ num + "')='TRUE'";
			
		} else if (type == FIRE_BUILDING) {
			query3="SELECT TEMP.BID FROM (SELECT B1.BID, SDO_NN_DISTANCE(1) DIST FROM BUILDING B2," +
					"BUILDING B1  WHERE B2.BID='"+bid+"' AND B1.BID!=B2.BID AND B1.BUILD_NAME IN (SELECT FNAME FROM FIRE_BUILDING) " +
					"AND SDO_NN(B1.BUILD_SHAPE, B2.BUILD_SHAPE,1)='TRUE' ORDER BY DIST ) TEMP WHERE ROWNUM<="+num;
		}
		//System.out.println(query);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query3);
		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + "\n");
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void demo_1(Connection conn) throws SQLException {
		String queryd1 = "";
		
		queryd1="select DISTINCT  b.BUILD_NAME from building b where b.BUILD_NAME like 'S%' and b.BUILD_NAME not in (select f.FNAME from FIRE_BUILDING f)";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(queryd1);
		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + "\n");
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void demo_2(Connection conn) throws SQLException {
		String queryd2 = "";
		queryd2 = "SELECT B.BUILD_NAME, F.FIREID FROM BUILDING B, FIRE_HYDRANT F, FIRE_BUILDING FB WHERE B.BUILD_NAME=FB.FNAME AND SDO_NN(F.FIRE_SHAPE, B.BUILD_SHAPE,'SDO_NUM_RES=5')='TRUE'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(queryd2);
		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + " " + rs.getObject(2) + "\n");
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void demo_3(Connection conn) throws SQLException {
		String queryd3 = "";
		queryd3="SELECT F.FIREID FROM FIRE_HYDRANT F,BUILDING B WHERE SDO_WITHIN_DISTANCE(F.FIRE_SHAPE, B.BUILD_SHAPE,'DISTANCE=120')='TRUE' group by F.FIREID having count(*)= (SELECT max(count(*)) FROM FIRE_HYDRANT F,BUILDING B  WHERE SDO_WITHIN_DISTANCE(F.FIRE_SHAPE, B.BUILD_SHAPE,'DISTANCE=120')='TRUE' group by F.FIREID)";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(queryd3);
		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + "\n" );
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void demo_4(Connection conn) throws SQLException {
		String queryd4 = "";
		queryd4 = "select * from (SELECT  f.FIREID, count(*) FROM BUILDING B, FIRE_HYDRANT F WHERE SDO_NN( F.FIRE_SHAPE, b.build_shape, 'SDO_NUM_RES=1')='TRUE'group by f.FIREID order by count(*) desc) where rownum<=5";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(queryd4);
		String result = "";
		while (rs.next()) {
			result += (rs.getObject(1) + " " + rs.getObject(2) + "\n");
		}
		System.out.println("Output of query: ");
		if (result.equals("")) {
			System.out.println("No row selected.");
		} else {
			System.out.println(result);
		}
	}

	public static void demo_5(Connection conn) throws SQLException {
		String queryd5 = "";
		
		queryd5="SELECT SDO_AGGR_MBR(b.build_shape).get_wkt() AS clobCol FROM building b WHERE b.BUILD_NAME like '%HE'";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(queryd5);
		
		  while(rs.next()){   
			   
			 StringBuffer strOut = new StringBuffer();
			   String aux;
			   try {
			    BufferedReader br = new BufferedReader(rs.getClob("clobCol").
			      getCharacterStream());
			    while ((aux=br.readLine())!=null) {
			     strOut.append(aux);
			     strOut.append(System.getProperty("line.separator"));
			    }
			   }catch (Exception e) {
			    e.printStackTrace();
			   }
			   String clobStr = strOut.toString();
			  
			   String[] parts = clobStr.split(",");
			   String temp=parts[0];
			   
			   System.out.println("Lower Left: "+temp.substring(10,temp.length()));
			   System.out.print("Upper Right: "+parts[2]);
			  }
	}
}
