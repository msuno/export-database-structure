package com.msw.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OracleUtils {

	private static String url = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnnection(String url,String user,String password){
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("ORA-01017: invalid username/password; logon denied");
			return null;
		}
	}
	
	public static void closeConnection(Connection conn) throws SQLException{
		if(conn!=null){
			conn.close();			
		}
	}
	
	public static ResultSet getResultSet(Connection conn ,String sql){
		try {
			Statement stat = conn.createStatement();
			return stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
