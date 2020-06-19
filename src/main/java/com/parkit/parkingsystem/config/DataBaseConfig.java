package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {

private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

	public Connection getConnection() {
		LOGGER.info("Create DB connection");

		Connection con = null;
		FileInputStream fis = null;
		Properties props = new Properties();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			fis = new FileInputStream(
					"src/main/resources/jdbc.properties");
			props.load(fis);
			fis.close();

			con = DriverManager.getConnection(
					props.getProperty("databaseURL"),
					props.getProperty("username"),
					props.getProperty("password"));
		} catch (IOException | SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				LOGGER.info("Closing DB connection");
			} catch (SQLException e) {
				LOGGER.error("Error while closing connection", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				LOGGER.info("Closing Prepared Statement");
			} catch (SQLException e) {
				LOGGER.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				LOGGER.info("Closing Result Set");
			} catch (SQLException e) {
				LOGGER.error("Error while closing result set", e);
			}
		}
	}
}
