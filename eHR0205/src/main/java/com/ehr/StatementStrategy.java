package com.ehr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public interface StatementStrategy {

	Logger LOG = Logger.getLogger(StatementStrategy.class);
	public PreparedStatement makePrepardStatement(Connection c)
		throws SQLException;
}
