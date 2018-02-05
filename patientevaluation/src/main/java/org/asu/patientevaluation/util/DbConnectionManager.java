package org.asu.patientevaluation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Description : Util class that creates connection object for listener
 * 
 * @author ripudamansingh
 *
 */
public class DbConnectionManager {

	private Connection con;
	
	public DbConnectionManager(String dbURL, String user, String pwd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.con = DriverManager.getConnection(dbURL,user,pwd);
	}

	public Connection getCon() {
		return this.con;
	}
	
}
