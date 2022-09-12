package tw.org.twntch.db.dataaccess;

/*
 * Created on 2004/11/4
 *
 */
import java.sql.*;
import java.util.*;

import org.apache.commons.dbutils.BasicRowProcessor;
/**
 * 執行SQL指令
 * @author Kevin
 */
public class ExecuteSQL {
	private String jndiName = null;
	private boolean setAutoCommit = false;
	
	public ExecuteSQL(String jndi,boolean setAutoCommit){
		jndiName = jndi;
		this.setAutoCommit = setAutoCommit;
	}
	
	public ExecuteSQL(String jndi){
		jndiName = jndi;
		this.setAutoCommit = true;
	}
	/**
	 * To get connection
	 * @param jndi JNDI name
	 * @param setAutoCommit true for auto commit
	 * <pre>                false for manual commit</pre>
	 * @return java.sql.Connection
	 */	
	private Connection getConnection(String jndi,boolean setAutoCommit){
		return new DBConnect().getConnect(jndi,setAutoCommit);
	}
	/**
	 * To insert table without transaction
	 * @param sql
	 * @throws DataAccessException
	 */
	public void doInsert(String sql) throws DataAccessException{
		Statement stmt = null;
		Connection conn = null;
		try{
			conn = this.getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
		}
	}
	/**
	 * To insert table with transaction
	 * @param sql
	 * @throws DataAccessException
	 */
	public void doInsert(String[] sql) throws DataAccessException{
		Statement stmt = null;
		Connection conn = null;
		try{
			conn = getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			for(int i=0;i<sql.length;i++){
				stmt.addBatch(sql[i]);
			}
			stmt.executeBatch();
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			if (!setAutoCommit){
				try {
					conn.rollback();
				} catch (SQLException e) {
					throw new DataAccessException("Rollback failure.");
				}
			}
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
		}
	}
	/**
	 * To update table without transaction
	 * @param sql SQL statement
	 * @throws DataAccessException
	 */		
	public void doUpdate(String sql) throws DataAccessException{
	
		Statement stmt = null;
		Connection conn = null;
		try{
			conn = getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
		}
	}
	/**
	 * To update table without transaction
	 * @param sql SQL statement
	 * @throws DataAccessException
	 */		
	
	public int doUpdate_R(String sql) throws DataAccessException{
		
		Statement stmt = null;
		Connection conn = null;
		int result = 0;
		try{
			conn = getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
			return result;
		}
		
	}
	
	/**
	 * To update table with transaction
	 * @param sql Array of sql statement
	 * @throws DataAccessException
	 */
	public void doUpdate(String[] sql) throws DataAccessException{
		Statement stmt = null;
		Connection conn = null;
		try{
			conn = getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			for(int i=0;i<sql.length;i++){
				stmt.addBatch(sql[i]);
			}
			stmt.executeBatch();
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			if (!setAutoCommit){
				try {
					conn.rollback();
				} catch (SQLException e) {
					throw new DataAccessException("Rollback failure.");
				}
			}
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
		}
	}
	/**
	 * To delete data without transaction
	 * @param sql
	 * @throws DataAccessException
	 */
	public void doDelete(String sql) throws DataAccessException{
		
		Statement stmt = null;
		Connection conn = null;
		try{
			conn = getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
		}
	}
	/**
	 * To delete data with transaction
	 * @param sql
	 * @throws DataAccessException
	 */
	public void doDelete(String[] sql) throws DataAccessException{
		Statement stmt = null;
		Connection conn = null;
		try{
			conn = getConnection(jndiName,setAutoCommit);
			stmt = conn.createStatement();
			for(int i=0;i<sql.length;i++){
				stmt.addBatch(sql[i]);
			}
			stmt.executeBatch();
			if (!setAutoCommit){
				conn.commit();
			}
		}catch(SQLException se){
			if (!setAutoCommit){
				try {
					conn.rollback();
				} catch (SQLException e) {
					throw new DataAccessException("Rollback failure.");
				}
			}
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
		}
	}
	/**
	 * 對DB做query的動作
	 * @param sql SQL statement
	 * @return ArrayList
	 * @throws DataAccessException
	 * @throws SQLException 
	 */
	public ArrayList doQuery(String sql) throws DataAccessException, SQLException{
		Statement stmt = null;
		Connection conn = null;
		ArrayList al = new ArrayList();
		try{
			conn = getConnection(jndiName,setAutoCommit);
			//For SQL Server
			//stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(sql);
			BasicRowProcessor bp = new BasicRowProcessor();
			while(rs.next()){
				al.add(bp.toMap(rs));
			}
		}catch(SQLException se){
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(stmt);
			DBConnect.closeConnection(conn);
			System.out.println("conn.isClosed()02>>"+conn.isClosed());
		}
		return al;
	}
	public int doSP(String sql) throws DataAccessException, SQLException{
		Connection conn = null;
		CallableStatement callableStatement = null;
		int result = 0;
		ArrayList al = new ArrayList();
		try{
			conn = getConnection(jndiName,setAutoCommit);
			callableStatement = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			result = callableStatement.executeUpdate();
			System.out.println("result>>"+result);
		}catch(SQLException se){
			throw new DataAccessException("SQLException: " + se.getMessage());
		}finally{
			DBConnect.closeStatement(callableStatement);
			DBConnect.closeConnection(conn);
			System.out.println("conn.isClosed()02>>"+conn.isClosed());
		}
		return result;
	}
}
