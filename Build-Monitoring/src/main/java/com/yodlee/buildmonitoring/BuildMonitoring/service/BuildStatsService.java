package com.yodlee.buildmonitoring.BuildMonitoring.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.yodlee.buildmonitoring.BuildMonitoring.dao.BuildStatsDao;
import com.yodlee.buildmonitoring.BuildMonitoring.daoImp.BuildStatsImp;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.BuildStatsPOJO;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;
import com.yodlee.buildmonitoring.BuildMonitoring.model.CompareStatsSummary;
import com.yodlee.buildmonitoring.BuildMonitoring.model.StatsSummary;

public interface BuildStatsService {
	HashMap<String, BuildStatsPOJO> getBuildDetails(Connection connection) throws SQLException, IOException;

	List<CompareStatsSummary> getComaprisonStats(Connection connection) throws SQLException, IOException;
	HashMap<String, List<BuildStats>> getDailyStats(Connection connection,String statsQuery, String env) throws SQLException;
	HashMap<String, List<BuildStats>> getAgentDailyStats(Connection connection, String agentBuildStatsQuery,String className) throws SQLException;
}
