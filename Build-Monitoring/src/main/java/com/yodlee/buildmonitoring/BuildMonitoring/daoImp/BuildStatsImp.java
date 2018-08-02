package com.yodlee.buildmonitoring.BuildMonitoring.daoImp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.yodlee.buildmonitoring.BuildMonitoring.BuildMonitoringApplication;
import com.yodlee.buildmonitoring.BuildMonitoring.dao.BuildStatsDao;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildEnvModel;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;
import com.yodlee.buildmonitoring.BuildMonitoring.model.LocaleDetails;

@Repository
public class BuildStatsImp implements BuildStatsDao{

	@Override
	public List<BuildStats> getBuildDetails(Connection connection,String statsQuery) throws SQLException, IOException {

		System.out.println("entering into cache");
		
		HashMap<String,List<BuildStats>> buildEnvSetUp=new HashMap<>();
		List<BuildStats> srList1 = new ArrayList<BuildStats>();
		Statement stmt = connection.createStatement();
		System.out.println("+++++++++++stats query="+statsQuery);
		BuildEnvModel buildEnvModel=new BuildEnvModel();
		ResultSet rs = stmt.executeQuery(statsQuery);
		while (rs.next()) {
			BuildStats sr = new BuildStats();
			sr.setClassName(rs.getString("class_name"));
			sr.setTotalRequests(rs.getInt("total_request"));
			sr.setSuccess(rs.getInt("success"));
			sr.setAgentErrors(rs.getInt("agent_errors"));
			sr.setSiteErrors(rs.getInt("site_errors"));
			sr.setUarErrors(rs.getInt("uar_errors"));
			sr.setInfraErrors(rs.getInt("infra_errors"));
			sr.setError402(rs.getInt("ERR402"));
			sr.setError406(rs.getInt("ERR406"));
			sr.setError407(rs.getInt("ERR407"));
			sr.setError414(rs.getInt("ERR414"));
			sr.setError422(rs.getInt("ERR422"));
			sr.setError427(rs.getInt("ERR427"));
			sr.setError428(rs.getInt("ERR428"));
			sr.setError429(rs.getInt("ERR429"));
			sr.setError518(rs.getInt("ERR518"));
			sr.setError519(rs.getInt("ERR519"));
			sr.setError522(rs.getInt("ERR522"));
			sr.setError523(rs.getInt("ERR523"));
			sr.setSuccessPerc(rs.getDouble("successPer"));
			sr.setAvgLatency(rs.getDouble("script_latency"));
			sr.setAvgInfraLatency(rs.getDouble("infra_script_latency"));
			sr.setNumNavigations(rs.getDouble("num_of_navigations"));
			sr.setError413(rs.getInt("ERR413"));
			sr.setDocDownloadErrors(rs.getInt("ERRDOCDOWNLOAD"));
			sr.setError402Perc(rs.getDouble("err402Per"));
			sr.setError413Perc(rs.getDouble("err413Per"));
						
			srList1.add(sr);
		}
		List<LocaleDetails> locDetails=BuildMonitoringApplication.readLocaleFile();
		for(BuildStats bs:srList1)
		{
			for(LocaleDetails bsLoc:locDetails)
			{
				if(bsLoc.getClassName()!=null)
				{
					if(bs.getClassName().toLowerCase().equals(bsLoc.getClassName().toLowerCase()))
					{
						bs.setLocale(bsLoc.getLocale());
						bs.setTag(bsLoc.getTag());
						break;
					}
				}
			}
		}
		
		return srList1;
	}

	@Override
	public HashMap<String,List<BuildStats>> getDailyStats(Connection connection, String statsQuery) throws SQLException {
		// TODO Auto-generated method stub
		HashMap<String,List<BuildStats>> ipWiseStatsMap=new HashMap<>();
		HashSet<String> uniqueIps=new HashSet<>();
		Statement stmt = connection.createStatement();
		List<BuildStats> buildStatList=new ArrayList<>();
		List<BuildStats> buildIPList=null;
		System.out.println("+++++++++++++++++executing query="+statsQuery);
		ResultSet rs = stmt.executeQuery(statsQuery);
		System.out.println("+++++++++++stats daily stats query executed");
		while (rs.next()) {
			uniqueIps.add(rs.getString("gatherer_ip"));
			BuildStats sr = new BuildStats();
			sr.setTimeStamp(rs.getString("DATE1"));
			sr.setGathererIp(rs.getString("gatherer_ip"));
			System.out.println("++++++++++++++comparing ip="+rs.getString("gatherer_ip")+"+++map="+BuildMonitoringApplication.IPBuildMapper);
			if(BuildMonitoringApplication.IPBuildMapper.get(rs.getString("gatherer_ip")))
			{
				sr.setBuildIP(true);
			}
			else
			{
				sr.setBuildIP(false);
			}
			sr.setTotalRequests(rs.getInt("total_request"));
			sr.setSuccess(rs.getInt("success"));
			buildStatList.add(sr);
		}
		
		for(String ips:uniqueIps)
		{
			buildIPList=new ArrayList<>();
			for(BuildStats bs:buildStatList)
			{
				System.out.println("+++++++++++bs ip="+bs.getGathererIp());
				System.out.println("+++++++++++bs comapre ip="+ips);
				if(ips.equals(bs.getGathererIp()))
				{
					System.out.println("+++++++++++adding into list");
					buildIPList.add(bs);
				}
			}
			System.out.println("=========================");
			System.out.println("+++++++++++adding into map");
			ipWiseStatsMap.put(ips, buildIPList);
		}
		
		
		
		return ipWiseStatsMap;
	}

	@Override
	public List<BuildStats> getAgentDailyStats(Connection connection, String agentBuildStatsQuery) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("++++++++++++++agent stats query="+agentBuildStatsQuery);
		List<BuildStats> buildStatList=new ArrayList<>();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(agentBuildStatsQuery);
		while(rs.next())
		{
			BuildStats sr=new BuildStats();
			sr.setTimeStamp(rs.getString("DATE1"));
			sr.setGathererIp(rs.getString("gatherer_ip"));
			sr.setTotalRequests(rs.getInt("total_request"));
			sr.setSuccess(rs.getInt("success"));
			sr.setAgentErrors(rs.getInt("agent_errors"));
			sr.setSiteErrors(rs.getInt("site_errors"));
			sr.setUarErrors(rs.getInt("uar_errors"));
			buildStatList.add(sr);
		}
		return buildStatList;
	}

}
