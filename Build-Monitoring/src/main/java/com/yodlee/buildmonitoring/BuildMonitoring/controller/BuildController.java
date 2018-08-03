package com.yodlee.buildmonitoring.BuildMonitoring.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yodlee.buildmonitoring.BuildMonitoring.BuildMonitoringApplication;
import com.yodlee.buildmonitoring.BuildMonitoring.ReadPropertiesFile;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.BuildStatsPOJO;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildConfigDetails;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;
import com.yodlee.buildmonitoring.BuildMonitoring.model.CompareStatsSummary;
import com.yodlee.buildmonitoring.BuildMonitoring.queries.BuildQueries;
import com.yodlee.buildmonitoring.BuildMonitoring.service.BuildStatsService;

@Controller
@RequestMapping(value="/build",method=RequestMethod.GET)
public class BuildController {
	
	@Autowired
	private BuildStatsService buildStatsService;
	//private static Properties dbProps = ReadPropertiesFile.getProperties("db.properties");
	
	@RequestMapping(value="/details",method=RequestMethod.GET)
	public String getStatsPage()
	{
		System.out.println("+++++++++++details page");
		return "regression";
	}
	
	@RequestMapping(value="/agentstats",method=RequestMethod.GET)
	public String getAgentStats()
	{
		return "agentstats";
	}
	
	@RequestMapping(value="/stats",method=RequestMethod.GET)
	public ResponseEntity<HashMap<String,BuildStatsPOJO>> getstats() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		return new ResponseEntity<HashMap<String, BuildStatsPOJO>>(buildStatsService.getBuildDetails(BuildMonitoringApplication.SITEPConn), HttpStatus.OK);
	}
	
	@RequestMapping(value="/comparestats",method=RequestMethod.GET)
	public ResponseEntity<List<CompareStatsSummary>> getCompareStats() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		Properties buildProps=null;
		File buildFile=new File("build.properties");
		String fileName=null;
		if(buildFile.exists() && !buildFile.isDirectory())
		{
			 buildProps = ReadPropertiesFile.getProperties("build.properties");
			 fileName=buildProps.getProperty("FileName");
		}
		if(fileName==null || !fileName.contains("txt"))
		{
			List<CompareStatsSummary> list=new ArrayList<>();
			CompareStatsSummary cs=new CompareStatsSummary();
			cs.setAlterEnv("FNP");
			list.add(cs);
			return new ResponseEntity<List<CompareStatsSummary>>(list, HttpStatus.OK);
		}
		//System.out.println("+++++++++++compare stats details page:"+BuildMonitoringApplication.SITEPConn);
		return new ResponseEntity<List<CompareStatsSummary>>(buildStatsService.getComaprisonStats(BuildMonitoringApplication.SITEPConn), HttpStatus.OK);
	}
	
	@RequestMapping(value="/dailystats",method=RequestMethod.GET)
	public ResponseEntity<HashMap<String,List<BuildStats>>> getDailyStats(HttpServletRequest req) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		String buildIps=req.getParameter("buildip");
		String env=req.getParameter("env");
		String nonBuildIps=req.getParameter("nonBuildIp");
		String type=req.getParameter("type");
		String className=req.getParameter("class");
		
		String[] buildIpSplit=buildIps.split(",");
		String[] nonBuildIpSplit=nonBuildIps.split(",");
		for(String bip:buildIpSplit)
		{
			String bipMan=bip.replaceAll("'", "").replaceAll(" ", "");
			System.out.println("++++++++++++build ip val="+bipMan);
			BuildMonitoringApplication.IPBuildMapper.put(bipMan,true);
		}
		for(String bip:nonBuildIpSplit)
		{
			String bipMan=bip.replaceAll("'", "").replaceAll(" ", "");
			System.out.println("++++++++++++non build ip val="+bipMan);
			BuildMonitoringApplication.IPBuildMapper.put(bipMan,false);
		}
		String combinedIps=buildIps+","+nonBuildIps;
		System.out.println("+++++++++++build ip daily="+buildIps);
		System.out.println("+++++++++++non build ip daily="+nonBuildIps);
		System.out.println("+++++++++++combined ips  daily="+combinedIps);
		System.out.println("+++++++++++build ip env="+env);
		System.out.println("+++++++++++type="+type);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(type==null)
		{
			return new ResponseEntity<HashMap<String,List<BuildStats>>>(buildStatsService.getDailyStats(BuildMonitoringApplication.SITEPConn, BuildQueries.getBuildDailyStatsQuery(combinedIps, "sysdate-700"),env), HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<HashMap<String,List<BuildStats>>>(buildStatsService.getAgentDailyStats(BuildMonitoringApplication.SITEPConn, BuildQueries.getAgentBuildStatsQuery(combinedIps, "sysdate-700",className),className), HttpStatus.OK);
			//return new ResponseEntity<HashMap<String,List<BuildStats>>>(buildStatsService.getAgentDailyStats(BuildMonitoringApplication.SITEPConn, BuildQueries.getAgentBuildStatsQuery(buildIps, "sysdate-700",className),className), HttpStatus.OK);
		}
	}
	
	/*@RequestMapping(value="/agentdailystats",method=RequestMethod.GET)
	public ResponseEntity<List<BuildStats>> getAgentDailyStats(HttpServletRequest req) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		String buildIps=req.getParameter("buildip");
		String env=req.getParameter("env");
		String className=req.getParameter("class");
		System.out.println("+++++++++++build ip daily="+buildIps);
		System.out.println("+++++++++++build ip env="+env);
		System.out.println("+++++++++++class name="+className);
		
		return new ResponseEntity<List<BuildStats>>(buildStatsService.getAgentDailyStats(BuildMonitoringApplication.SITEPConn, BuildQueries.getAgentBuildStatsQuery(buildIps, "sysdate-700",className),className), HttpStatus.OK);
	}*/
	@RequestMapping(value="/monitor",method=RequestMethod.GET)
	public String getBuildDetails()
	{
		return "monitor";
	}
	
	@RequestMapping(value="/regression",method=RequestMethod.GET)
	public String getRegressionPage()
	{
		return "regressiontest";
	}
	
	@RequestMapping(value="/getbuilddetails",method=RequestMethod.POST)
	public ResponseEntity<HashMap<String,List<BuildConfigDetails>>> getBuildIPDet(HttpServletRequest req)
	{
		
		Properties buildProps=null;
		File buildFile=new File("build.properties");
		if(buildFile.exists() && !buildFile.isDirectory())
		{
			 System.out.println("++++++++inside build props");
			 buildProps = ReadPropertiesFile.getProperties("build.properties");
		}
		String fileName=buildProps.getProperty("FileName");
		String isEnabled=buildProps.getProperty("IsEnable");
		
		System.out.println("+++++++filename="+fileName);
		System.out.println("+++++++isEnabled="+isEnabled);
		if(isEnabled.equals("true"))
		{
			File f = new File(fileName);
			if(f.exists() && !f.isDirectory()) { 
				BuildMonitoringApplication.isFilePresent=true;
			}
		}
		else
		{
			BuildMonitoringApplication.isFilePresent=false;
		}
		
		String data=req.getParameter("data");
		String buildNumber=req.getParameter("bn");
		String buildDate=req.getParameter("date");
		String confirm=req.getParameter("confirm");
		
		if(data==null
				&&buildDate==null
				&&isEnabled.equals("true"))
		{
			HashMap<String,List<BuildConfigDetails>> fnpMap=new HashMap<>();
			fnpMap.put("FNP", null);
			return new ResponseEntity<HashMap<String,List<BuildConfigDetails>>>(fnpMap, HttpStatus.OK);
		}
		if(data==null
				&&buildDate==null
				&&isEnabled.equals("false"))
		{
			
			return null;
		}
		System.out.println("++++++++++++buildnumber="+buildNumber);
		System.out.println("++++++++++++builddate="+buildDate);
		if(BuildMonitoringApplication.isFilePresent)
		{
			HashMap<String,List<BuildConfigDetails>> fnpMap=new HashMap<>();
			fnpMap.put("FNP", null);
			return new ResponseEntity<HashMap<String,List<BuildConfigDetails>>>(fnpMap, HttpStatus.OK);
		}
		return new ResponseEntity<HashMap<String,List<BuildConfigDetails>>>(BuildMonitoringApplication.getConfigDetails(data,buildNumber,buildDate,confirm,null), HttpStatus.OK);
	}
	@RequestMapping(value="/deletebuilddetails",method=RequestMethod.POST)
	public ResponseEntity<HashMap<String,List<BuildConfigDetails>>> deleteBuildIPDet(HttpServletRequest req)
	{
		String data=req.getParameter("data");
		String buildNumber=req.getParameter("bn");
		String buildDate=req.getParameter("date");
		String isDelete=req.getParameter("confirm");
		
		return new ResponseEntity<HashMap<String,List<BuildConfigDetails>>>(BuildMonitoringApplication.getConfigDetails(data,buildNumber,buildDate,null,isDelete), HttpStatus.OK);
	}

}
