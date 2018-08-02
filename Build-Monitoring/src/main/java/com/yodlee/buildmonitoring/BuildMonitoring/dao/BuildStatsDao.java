package com.yodlee.buildmonitoring.BuildMonitoring.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;

public interface BuildStatsDao {
	
	List<BuildStats> getBuildDetails(Connection connection,String statsQuery) throws SQLException, IOException;
	HashMap<String, List<BuildStats>> getDailyStats(Connection connection,String statsQuesy) throws SQLException;
	List<BuildStats> getAgentDailyStats(Connection connection, String agentBuildStatsQuery) throws SQLException;
}
