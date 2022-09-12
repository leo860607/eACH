package tw.org.twntch.db.dataaccess;


import java.sql.*;
/**
 * 
 * @author Hugo
 *
 */
public class DBConnect {
	/**
	 * To get connection of database
	 * @param sJndi
	 * @param bCommit
	 * @return Connection
	 */
	public Connection getConnect(String sJndi,boolean bCommit){
		try{
//			Context ctx = new InitialContext();
//			DataSource ds = (DataSource)ctx.lookup("java:comp/env/"+sJndi+"");
//			Connection dbconn = ds.getConnection();
			Connection dbconn = ConnectionManager.getConnection(sJndi);
			dbconn.setAutoCommit(bCommit);
			return dbconn;			
		}catch (Exception e){
			System.out.println("#getConnect#:"+e.getMessage());
			return null;
		}
	}
	/**
	 * Close connection
	 * @param conn
	 */
	public static void closeConnection(Connection conn){
		if (conn!=null){
			try{
				conn.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				throw new RuntimeException(se);
			}
		}
	}
	/**
	 * Close statement
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt){
		if (stmt!=null){
			try{
				stmt.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				throw new RuntimeException(se);
			}
		}
	}
}
