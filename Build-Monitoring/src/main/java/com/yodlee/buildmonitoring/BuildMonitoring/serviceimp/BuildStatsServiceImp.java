package com.yodlee.buildmonitoring.BuildMonitoring.serviceimp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yodlee.buildmonitoring.BuildMonitoring.BuildMonitoringApplication;
import com.yodlee.buildmonitoring.BuildMonitoring.ReadPropertiesFile;
import com.yodlee.buildmonitoring.BuildMonitoring.Utility;
import com.yodlee.buildmonitoring.BuildMonitoring.dao.BuildStatsDao;
import com.yodlee.buildmonitoring.BuildMonitoring.daoImp.BuildStatsImp;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.BuildStatsPOJO;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.IPBuildSetUp;
import com.yodlee.buildmonitoring.BuildMonitoring.model.AgentStats;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;
import com.yodlee.buildmonitoring.BuildMonitoring.model.CompareStatsSummary;
import com.yodlee.buildmonitoring.BuildMonitoring.model.LocaleDetails;
import com.yodlee.buildmonitoring.BuildMonitoring.model.StatsSummary;
import com.yodlee.buildmonitoring.BuildMonitoring.queries.BuildQueries;
import com.yodlee.buildmonitoring.BuildMonitoring.service.BuildStatsService;
import com.yodlee.buildmonitoring.BuildMonitoring.serviceutility.ServiceUtility;

@Service
public class BuildStatsServiceImp implements BuildStatsService {

	private static BuildStatsDao buildStatsDao=new BuildStatsImp();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");
	
	private static HashMap<String,IPBuildSetUp> buildIpMap=null;
	private static List<CompareStatsSummary> buildStats=null;
	private static HashMap<String,BuildStatsPOJO> buildDetails=null;
	private static HashMap<String, HashMap<String,List<BuildStats>>> agentStatsMap=new HashMap<>();
	
	private static List<BuildStats> agentBuildStats=null;
	
	private static HashMap<String,HashMap<String,List<BuildStats>>> dailyStatsMap=new HashMap<>();
	private static HashMap<String, Boolean> triggeredMap=new HashMap<>();
	private static HashMap<String, Boolean> triggeredAgentMap=new HashMap<>();
	
	@Override
	public HashMap<String,BuildStatsPOJO> getBuildDetails(Connection connection) throws SQLException, IOException {
		if(buildStats!=null)
		{
			System.out.println("++++++++inside cacheacble");
			return buildDetails;
		}
		
		HashMap<String,BuildStatsPOJO> buildMap=new HashMap<>(); 
		buildIpMap=BuildMonitoringApplication.getRegressionIP();
		
		List<BuildStats> oldBuilduserDetails=null;
		List<BuildStats> nonBuilduserDetails=null;
		List<BuildStats> currentBuilduserDetails=null;
		
		for(String env:buildIpMap.keySet())
		{
			triggeredMap.put(env, false);
			IPBuildSetUp ips=buildIpMap.get(env);
			BuildStatsPOJO buildStatsPOJO=new BuildStatsPOJO();
			System.out.println("ENV:"+env);
			List<String> oldBuildIp=ips.getOldBuild();
			List<String> nonBuildIp=ips.getNonBuild();
			List<String> currentBuildIp=ips.getCurrentBuild();
			
			String oldBuildstatsQuery=BuildQueries.getBuildStatsQuery(BuildMonitoringApplication.getIps(oldBuildIp),"sysdate-700");
			String nonBuildstatsQuery=BuildQueries.getBuildStatsQuery(BuildMonitoringApplication.getIps(nonBuildIp),"sysdate-700");
			String currentBuildstatsQuery=BuildQueries.getBuildStatsQuery(BuildMonitoringApplication.getIps(currentBuildIp),"sysdate-700");
			oldBuilduserDetails = buildStatsDao.getBuildDetails(connection, oldBuildstatsQuery);
			nonBuilduserDetails = buildStatsDao.getBuildDetails(connection, nonBuildstatsQuery);
			currentBuilduserDetails = buildStatsDao.getBuildDetails(connection, currentBuildstatsQuery);
			
			buildStatsPOJO.setOldBuildStats(oldBuilduserDetails);
			buildStatsPOJO.setNonBuildStats(nonBuilduserDetails);
			buildStatsPOJO.setCurrentBuildStats(currentBuilduserDetails);
			
			buildMap.put(env, buildStatsPOJO);
		}
		buildDetails=buildMap;
		return buildMap;
	}
	
	@Override
	public  List<CompareStatsSummary> getComaprisonStats(Connection connection) throws SQLException, IOException
	{
		Map<String,CompareStatsSummary> summaryMap = new LinkedHashMap<>();
		List<CompareStatsSummary> compStatSumm=new ArrayList<>();
		
		BuildStatsService buildStatsService=new BuildStatsServiceImp();
		HashMap<String, BuildStatsPOJO> map=buildStatsService.getBuildDetails(connection);
		
		for(String env:map.keySet())
		{
			IPBuildSetUp ips=buildIpMap.get(env);
			String buildIP=BuildMonitoringApplication.getIps(ips.getCurrentBuild());
			String nonBuildIP=BuildMonitoringApplication.getIps(ips.getNonBuild());
			List<AgentStats> agentStatsList=new ArrayList<>();
			CompareStatsSummary compareStatsSummary=new CompareStatsSummary();
			BuildStatsPOJO buildStatsPOJO=map.get(env);
			List<BuildStats> nonBuildStats=buildStatsPOJO.getNonBuildStats();
			List<BuildStats> buildStats=buildStatsPOJO.getCurrentBuildStats();
			List<BuildStats> oldStats=buildStatsPOJO.getOldBuildStats();
			StatsSummary nonBuildStat=ServiceUtility.getStatsSummary(nonBuildStats);
			StatsSummary buildStat=ServiceUtility.getStatsSummary(buildStats);
			StatsSummary oldStat=ServiceUtility.getStatsSummary(oldStats);
			HashMap<String,BuildStats> buildClassMap=buildStat.getClassMap();
			HashMap<String,BuildStats> nonBuildClassMap=nonBuildStat.getClassMap();
			HashMap<String,BuildStats> oldBuildClassMap=oldStat.getClassMap();
			System.out.println("++++++++++++++++build classmap="+buildClassMap);
			System.out.println("++++++++++++++++non build classmap="+nonBuildClassMap);
			System.out.println("++++++++++++++++old build classmap="+oldBuildClassMap);
			
			for(String className:buildClassMap.keySet())
			{
				
				System.out.println("++++++++++comparing class="+className);
				BuildStats bs=buildClassMap.get(className);
				BuildStats nonbs=nonBuildClassMap.get(className);
				BuildStats oldbs=oldBuildClassMap.get(className);
				if(bs!=null && nonbs!=null && oldbs!=null)
				{
					AgentStats agentStats=new AgentStats();
					agentStats.setClassName(className);
					agentStats.setLocale(bs.getLocale());
					agentStats.setTag(bs.getTag());
					agentStats.setBuildTotalRequest(bs.getTotalRequests());
					agentStats.setNonBuildTotalRequest(nonbs.getTotalRequests());
					agentStats.setOldBuildTotalRequest(oldbs.getTotalRequests());
					agentStats.setBuildSuccessPer(bs.getSuccessPerc());
					agentStats.setNonBuildSuccessPer(nonbs.getSuccessPerc());
					agentStats.setOldBuildSuccessPer(oldbs.getSuccessPerc());
					agentStats.setSuccessDiff(bs.getSuccessPerc()-nonbs.getSuccessPerc());
					agentStats.setBuildLatency(bs.getAvgLatency());
					agentStats.setNonBuildLatency(nonbs.getAvgLatency());
					agentStats.setOldBuildLatency(oldbs.getAvgLatency());
					agentStats.setLatencyDiff(bs.getAvgLatency()-nonbs.getAvgLatency());
					agentStats.setBuildInfraLatency(bs.getAvgInfraLatency());
					agentStats.setNonBuildInfraLatency(nonbs.getAvgInfraLatency());
					agentStats.setOldBuildInfraLatency(oldbs.getAvgInfraLatency());
					agentStats.setInfraLatencyDiff(bs.getAvgInfraLatency()-nonbs.getAvgInfraLatency());
					agentStats.setBuildErr402(bs.getError402Perc());
					agentStats.setNonBuildErr402(nonbs.getError402Perc());
					agentStats.setOldBuildErr402(oldbs.getError402Perc());
					agentStats.setErr402Diff(bs.getError402Perc()-nonbs.getError402Perc());
					agentStats.setBuildErr413(bs.getError413Perc());
					agentStats.setNonBuildErr413(nonbs.getError413Perc());
					agentStats.setOldBuildErr413(oldbs.getError413Perc());
					agentStats.setErr413Diff(bs.getError413Perc()-nonbs.getError413Perc());
					agentStats.setBuildInfraError(bs.getAvgInfraLatency());
					agentStats.setNonBuildInfraError(nonbs.getAvgInfraLatency());
					agentStats.setOldBuildInfraError(oldbs.getAvgInfraLatency());
					agentStats.setInfraDiff(bs.getAvgInfraLatency()-nonbs.getAvgInfraLatency());
					agentStatsList.add(agentStats);
				}
			}
			
			compareStatsSummary.setEnvironament(env);
			compareStatsSummary.setAlterEnv(env.replaceAll(" ", ""));
			compareStatsSummary.setBuildTotalRequest(buildStat.getTotalRequests());
			compareStatsSummary.setNonBuildTotalRequest(nonBuildStat.getTotalRequests());
			compareStatsSummary.setBuildSuccessPer(buildStat.getAvgSuccessPerc());
			compareStatsSummary.setNonBuildSuccessPer(nonBuildStat.getAvgSuccessPerc());
			compareStatsSummary.setSuccessDiff(buildStat.getAvgSuccessPerc()-nonBuildStat.getAvgSuccessPerc());
			compareStatsSummary.setBuildLatency(buildStat.getAvgLatency());
			compareStatsSummary.setNonBuildLatency(nonBuildStat.getAvgLatency());
			compareStatsSummary.setLatencyDiff(buildStat.getAvgLatency()-nonBuildStat.getAvgLatency());
			compareStatsSummary.setBuildInfraLatency(buildStat.getInfraAvgLatency());
			compareStatsSummary.setNonBuildInfraLatency(nonBuildStat.getInfraAvgLatency());
			compareStatsSummary.setInfraLatencyDiff(buildStat.getInfraAvgLatency()-nonBuildStat.getInfraAvgLatency());
			compareStatsSummary.setBuildErr402(buildStat.getAvgError402Perc());
			compareStatsSummary.setNonBuildErr402(nonBuildStat.getAvgError402Perc());
			compareStatsSummary.setErr402Diff(buildStat.getAvgError402Perc()-nonBuildStat.getAvgError402Perc());
			compareStatsSummary.setBuildErr413(buildStat.getAvgError413Perc());
			compareStatsSummary.setNonBuildErr413(nonBuildStat.getAvgError413Perc());
			compareStatsSummary.setErr413Diff(buildStat.getAvgError413Perc()-nonBuildStat.getAvgError413Perc());
			compareStatsSummary.setBuildInfraError(buildStat.getAvgInfraErrorPerc());
			compareStatsSummary.setNonBuildInfraError(nonBuildStat.getAvgInfraErrorPerc());
			compareStatsSummary.setInfraDiff(buildStat.getAvgInfraErrorPerc()-nonBuildStat.getAvgInfraErrorPerc());
			compareStatsSummary.setAgentStatList(agentStatsList);
			compareStatsSummary.setBuildIps(buildIP);
			compareStatsSummary.setNonBuildIps(nonBuildIP);
			compStatSumm.add(compareStatsSummary);
			//summaryMap.put(env, compareStatsSummary);
		}
		buildStats=compStatSumm;
		return compStatSumm;
	}
	
	@Override
	public HashMap<String,List<BuildStats>> getDailyStats(Connection connection,String statsQuery,String env) throws SQLException {
		// TODO Auto-generated method stub
		HashMap<String,List<BuildStats>> dailyStats=null;
		if(dailyStatsMap==null)
		{
			System.out.println("+++++++++++++++inside map null");
			dailyStatsMap=new HashMap<>();
		}
		if(dailyStatsMap!=null && dailyStatsMap.get(env)!=null)
		{
			System.out.println("+++++++inside daily stats cache");
			return dailyStatsMap.get(env);
		}
		System.out.println("+++++++++++triggeredMap="+triggeredMap);
		if(!triggeredMap.get(env))
		{   
			System.out.println("+++++++inside triggered map true");
			triggeredMap.put(env, true);
			dailyStats=buildStatsDao.getDailyStats(connection, statsQuery);
		}
		else
		{
			System.out.println("not triggering query");
		}
		
		System.out.println("+++++++++++++++++dailyStats="+dailyStats);
		dailyStatsMap.put(env, dailyStats);
		return dailyStatsMap.get(env);
	}
	
	@Scheduled(cron = "0 0 */2 * * *")
	public  List<CompareStatsSummary> scheduleTaskUsingCronExpression() throws SQLException, IOException {
		
		
	    long now = System.currentTimeMillis() / 1000;
	    System.out.println("Regular task performed using Cron at "
				+ dateFormat.format(new Date()));
	    buildStats=null;
	    dailyStatsMap=null;
	    return getComaprisonStats(BuildMonitoringApplication.SITEPConn);
	}
	
	public  void scheduleTaskUsingCronExpressionReadLocale() throws SQLException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		
		Connection RepaldaSITEPConn;
		Properties repaldadbProps = ReadPropertiesFile.getProperties("repaldadb.properties");
		String[] columns = { "CLASS", "LOCALE", "TAG" };		
		Class.forName(repaldadbProps.getProperty("driver")).newInstance();
	    RepaldaSITEPConn = DriverManager.getConnection(
				repaldadbProps.getProperty("url") + repaldadbProps.getProperty("dbName"),
				repaldadbProps.getProperty("userName"), repaldadbProps.getProperty("password"));

		System.out.println("+++++++connected");
		List<LocaleDetails> localeList = new ArrayList<>();
		Statement statement = RepaldaSITEPConn.createStatement();
		ResultSet rs = statement.executeQuery(Utility.readFromFile("localequery.txt"));
		int i = 0;
		while (rs.next()) {
			LocaleDetails localeDetails = new LocaleDetails();
			localeDetails.setClassName(rs.getString("class_name"));
			localeDetails.setLocale(rs.getString("country_name"));
			localeDetails.setTag(rs.getString("tag"));
			localeList.add(localeDetails);
			System.out.println("+++classname=" + i + " " + rs.getString("class_name"));
			i++;
		}

		Workbook workbook = new XSSFWorkbook();
		CreationHelper creationHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("locale");

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);

		for (int j = 0; j < columns.length; j++) {
			Cell cell = headerRow.createCell(j);
			cell.setCellValue(columns[j]);
			cell.setCellStyle(headerCellStyle);
		}
		int rowNum = 1;
		for (LocaleDetails ld : localeList) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(ld.getClassName());

			row.createCell(1).setCellValue(ld.getLocale());

			row.createCell(2).setCellValue(ld.getTag());
		}

		for (int j = 0; j < columns.length; j++) {
			sheet.autoSizeColumn(j);
		}

		FileOutputStream fileOut = new FileOutputStream("locale.xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	@Override
	public HashMap<String, List<BuildStats>> getAgentDailyStats(Connection connection, String agentBuildStatsQuery,String className) throws SQLException {
		HashMap<String,List<BuildStats>> agentStats=null;
		
		if(agentStatsMap.size()!=0 && agentStatsMap.containsKey(className))
		{
			System.out.println("++++++++++cached data retrieving");
			return agentStatsMap.get(className);
		}
		if(!triggeredAgentMap.containsKey(className))
		{
			System.out.println("++++++++++triggering stats for class="+className);
			triggeredAgentMap.put(className, true);
			agentStats=buildStatsDao.getDailyStats(connection, agentBuildStatsQuery);
			agentStatsMap.put(className, agentStats);
		}
		else
		{
			System.out.println("++++++++++triggerred stats for "+className+" processing");
		}
		return agentStats;
	}
	
	
	

}
