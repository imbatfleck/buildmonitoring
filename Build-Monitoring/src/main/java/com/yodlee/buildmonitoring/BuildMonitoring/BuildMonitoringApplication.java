package com.yodlee.buildmonitoring.BuildMonitoring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.loginmodel.AuthenticationDeatils;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.model.BatchDetails;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.EnvIPSetUp;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.IPBuildSetUp;
import com.yodlee.buildmonitoring.BuildMonitoring.envsetup.IPSetUP;
import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildConfigDetails;
import com.yodlee.buildmonitoring.BuildMonitoring.model.LocaleDetails;
import com.yodlee.buildmonitoring.BuildMonitoring.serviceimp.BuildStatsServiceImp;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BuildMonitoringApplication {
	
	public static Stack<AuthenticationDeatils> authStack=new Stack<>();
	public static Stack<AuthenticationDeatils> reservedAuthStack=new Stack<>();
	public static List<AuthenticationDeatils> authList=new ArrayList<>();
	public static final ArrayList<String> custRouteList=new ArrayList<>();
	

	private static final String BUILD_DETAIL_KEY = "build.detail";
	private static final String BUILD_DETAIL_PROP_FILE = "batchdetails.properties";
	
	private static final String UPDATED_BUILD_DETAIL_PROP_FILE="updatedbatdetail.properties";

	private static Properties buildProps = ReadPropertiesFile.getProperties(BUILD_DETAIL_PROP_FILE);
	private static String buildDetails = buildProps.getProperty(BUILD_DETAIL_KEY);


	public static Connection SITEPConn;
	public static HashMap<String, Boolean> IPBuildMapper = new HashMap<>();
	public static HashMap<String, List<BuildConfigDetails>> configDetails = new HashMap<>();
	private static Properties dbProps = ReadPropertiesFile.getProperties("dbdemo.properties");
	public static boolean isFilePresent = false;
	public static boolean isDisable = false;
	public static String fileName = null;

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		Class.forName(dbProps.getProperty("driver")).newInstance();

		SITEPConn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");

		
		// System.out.println("+++++++++++getting connection");

		/*
		 * String s=null; try { if(s.equals("a")) {
		 * 
		 * } } catch (Exception e) { // TODO: handle exception
		 * System.out.println(e.getMessage()); }
		 */

		
		/* SITEPConn = DriverManager.getConnection(dbProps.getProperty("url") +
		 dbProps.getProperty("dbName"), dbProps.getProperty("userName"),
		 dbProps.getProperty("password"));*/
		 

		/*
		 * String
		 * data="Backend;Build Ips;Non Build Ips;SDKEE;172.17.7.22;172.17.7.234;CHANNEL;172.17.6.108;172.17.9.193;YI_Channel;172.17.6.132;172.17.6.228;LARGE_BACKEND;172.17.8.193;172.17.9.158;YODLEE;172.17.9.40;172.17.6.174;YODLEE;172.17.8.106;172.17.6.170;"
		 * ; getConfigDetails(data);
		 */
		SpringApplication.run(BuildMonitoringApplication.class, args);

		/*
		 * String date="Thursday, 02 August, 2018 21:00 IST"; SimpleDateFormat
		 * dateFormat=new SimpleDateFormat("E, dd MMM, yyyy HH:mm z"); Date
		 * dateOne=null; try { dateOne=dateFormat.parse(date);
		 * System.out.println("++++date="+dateOne);
		 * 
		 * Calendar cal = Calendar.getInstance(); cal.setTime(dateOne);
		 * cal.add(Calendar.DATE, -7); Date dateBefore30Days = cal.getTime();
		 * System.out.println("+++oldbuild date="+dateBefore30Days);
		 * 
		 * cal.setTime(dateBefore30Days); cal.add(Calendar.HOUR, 4); Date
		 * afterdays=cal.getTime();
		 * 
		 * System.out.println("+++oldbuild date="+afterdays);
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * System.out.println("+++++=inside catch"); }
		 * 
		 * Date currentDate=new Date(); long
		 * diffDate=currentDate.getTime()-dateOne.getTime();
		 * System.out.println("+++++++++++++currentDate="+(diffDate/(1000*60*60)));
		 */
		// for()
		// getConnection();

	}

	public static HashMap<String, List<BuildConfigDetails>> getConfigDetails(String data, String buildNumber,
			String buildDate, String confirm, String isDelete) {
		if (isDelete != null && isDelete.equals("true")) {
			BuildStatsServiceImp.buildStats = null;
			isDisable = true;
			System.out.println("+++++++++triggerd removing");
			if (BuildStatsServiceImp.triggeredCompareMap.containsKey("TRIGGERED")) {
				System.out.println("++++++triggered removed");
				BuildStatsServiceImp.triggeredCompareMap.remove("TRIGGERED");
			}
			BuildStatsServiceImp.buildStats = null;
			BuildStatsServiceImp.dailyStatsMap = null;
			String bn = "BuildNumber=" + buildNumber;
			String bd = "BuildDate=" + buildDate;
			String fn = "FileName=" + "NOT_FOUND";
			String en = "IsEnable=false";
			String propFile = bn + "\n" + bd + "\n" + fn + "\n" + en;
			Utility.writeToFile("build", "properties", propFile);
			return null;
		}
		String[] spliteData = data.split(";");
		List<BuildConfigDetails> buildConfigList = new ArrayList<>();
		HashSet<String> uniqueEnv = new HashSet<>();
		for (int i = 3; i < spliteData.length; i++) {
			BuildConfigDetails buildConfigDetails = new BuildConfigDetails();
			String env = spliteData[i];
			String buildIP = spliteData[i + 1];
			String nonBuildIP = spliteData[i + 2];
			uniqueEnv.add(env);
			buildConfigDetails.setEnvironment(env);
			buildConfigDetails.setBuildIP(buildIP);
			buildConfigDetails.setNonBUildIP(nonBuildIP);
			buildConfigList.add(buildConfigDetails);
			System.out.println("++++++++++++env=" + env);
			System.out.println("++++++++++++buildIP=" + buildIP);
			System.out.println("++++++++++++nonBuildIP=" + nonBuildIP);
			i = i + 2;
		}
		HashMap<String, List<BuildConfigDetails>> configMap = new HashMap<>();
		for (String env : uniqueEnv) {
			List<BuildConfigDetails> uniqueConfigList = new ArrayList<>();
			for (BuildConfigDetails bcd : buildConfigList) {
				if (env.toLowerCase().equals(bcd.getEnvironment().toLowerCase())) {
					uniqueConfigList.add(bcd);
				}
			}
			configMap.put(env, uniqueConfigList);
		}
		String compConfing = "";
		for (String env : configMap.keySet()) {
			String nonBuildIP = "";
			String buildIP = "";
			System.out.println("+++++++++++env=" + env + "----deatails=" + configMap.get(env));
			List<BuildConfigDetails> det = configMap.get(env);
			boolean isComa = false;
			for (BuildConfigDetails bd : det) {
				if (isComa) {
					nonBuildIP = nonBuildIP + "," + "'" + bd.getNonBUildIP() + "'";
					buildIP = buildIP + "," + "'" + bd.getBuildIP() + "'";
				} else {
					nonBuildIP = nonBuildIP + "'" + bd.getNonBUildIP() + "'";
					buildIP = buildIP + "'" + bd.getBuildIP() + "'";
					isComa = true;
				}
			}
			String full = nonBuildIP + "; ;" + env + ";NonBuildIp";
			String full1 = "\n" + buildIP + "; ;" + env + ";OldBuildIp";
			String full2 = "\n" + buildIP + "; ;" + env + ";BuildIp";
			String complete = full + full1 + full2;
			System.out.println("++complete=" + complete);
			compConfing = compConfing + complete + "\n";
		}
		if (confirm != null && confirm.equals("true")) {
			System.out.println("++++++++true");
			String bn = "BuildNumber=" + buildNumber;
			String bd = "BuildDate=" + buildDate;
			String fn = "FileName=" + buildNumber + ".txt";
			String en = "IsEnable=true";

			String propFile = bn + "\n" + bd + "\n" + fn + "\n" + en;
			Utility.writeToFile("build", "properties", propFile);
			Utility.writeToFile(buildNumber, "txt", compConfing);
		}
		System.out.println("Complete configuration");
		System.out.println(compConfing);
		return configMap;
	}

	public static HashMap<String, IPBuildSetUp> getRegressionIP() throws IOException {
		List<String> buildIpList = null;
		HashMap<String, IPSetUP> envIpMap = new HashMap<>();
		IPSetUP ipSetUP = null;
		EnvIPSetUp envIPSetUp = new EnvIPSetUp();
		LinkedHashSet<String> envUniqueList = new LinkedHashSet<>();
		for (String ipDetail : getUniqueIPdetails()) {
			ipSetUP = new IPSetUP();
			System.out.println("++++++++++ip detail=" + ipDetail);

			String[] ipDetSplit = ipDetail.split(";");

			ipSetUP.setBuildIP(setBuildIps(buildIpList, getIpList(ipDetSplit)));
			String envKey = getEnv(ipDetSplit) + getBuildType(ipDetSplit);

			envIpMap.put(envKey, ipSetUP);
			envUniqueList.add(getEnv(ipDetSplit));

		}
		envIPSetUp.setEnvIPMap(envIpMap);
		HashMap<String, IPSetUP> map = envIPSetUp.getEnvIPMap();
		HashMap<String, IPBuildSetUp> buildMapping = new HashMap<>();
		for (String env : envUniqueList) {
			IPBuildSetUp ipBuildSetUp = new IPBuildSetUp();
			for (String envMap : map.keySet()) {
				System.out.println("++++++++envmap=" + envMap);
				String compEnv = null;
				if (envMap.contains("NonBuildIp")) {
					compEnv = envMap.replace("NonBuildIp", "").trim();
				} else if (envMap.contains("OldBuildIp")) {
					compEnv = envMap.replace("OldBuildIp", "").trim();
				} else {
					compEnv = envMap.replace("BuildIp", "").trim();
				}

				if (compEnv.toLowerCase().equals(env.toLowerCase())) {
					if (envMap.toLowerCase().contains("old")) {
						ipBuildSetUp.setOldBuild(map.get(envMap).getBuildIP());
					} else if (envMap.toLowerCase().contains("non")) {
						ipBuildSetUp.setNonBuild(map.get(envMap).getBuildIP());
					} else {
						ipBuildSetUp.setCurrentBuild(map.get(envMap).getBuildIP());
					}
				}
			}
			buildMapping.put(env, ipBuildSetUp);
		}

		return buildMapping;
	}

	public static String getIps(List<String> buildIps) {
		String ipString = buildIps.toString();
		ipString = ipString.replace("[", "");
		ipString = ipString.replace("]", "");
		return ipString;
	}

	private static List<String> setBuildIps(List<String> BuildIpList, List<String> ipList) {
		// TODO Auto-generated method stub
		BuildIpList = new ArrayList<>();
		for (String ips : ipList) {
			BuildIpList.add(ips);
		}
		return BuildIpList;
	}

	public static String getBuildType(String[] ipDetSplit) {
		// TODO Auto-generated method stub
		return ipDetSplit[3];

	}

	public static String getEnv(String[] ipDetSplit) {
		// TODO Auto-generated method stub
		return ipDetSplit[2];

	}

	public static CopyOnWriteArrayList<String> getUniqueIPdetails() throws IOException {
		CopyOnWriteArrayList<String> ipDetails = new CopyOnWriteArrayList<>();
		Properties buildProps = null;
		File buildFile = new File("build.properties");
		String fileName = null;
		if (buildFile.exists() && !buildFile.isDirectory()) {
			buildProps = ReadPropertiesFile.getProperties("build.properties");
			fileName = buildProps.getProperty("FileName");
		}

		System.out.println("+++++++filename=" + fileName);
		if (fileName.contains("txt")) {
			ipDetails = Utility.readFromFileList(fileName);
			for (String ipDet : ipDetails) {
				if (ipDet.contains("#") || ipDet.equals("")) {
					ipDetails.remove(ipDet);
				}
			}
		}
		return ipDetails;
	}

	public static ArrayList<String> getIpList(String[] ipDetailsList) {
		ArrayList<String> ipList = new ArrayList<>();
		String ips = ipDetailsList[0];
		String[] ipArr = ips.split(",");
		for (String ip : ipArr) {
			ipList.add(ip);
		}
		return ipList;
	}

	public static List<LocaleDetails> readLocaleFile() throws IOException {
		List<LocaleDetails> localeList = new ArrayList<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("locale.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			int j = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				LocaleDetails localeDetails = new LocaleDetails();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				int i = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_NUMERIC:
						break;
					case Cell.CELL_TYPE_STRING:
						String cellValue = cell.getStringCellValue();
						if (j != 0) {
							if (i == 0) {
								localeDetails.setClassName(cell.getStringCellValue());
							} else if (i == 1) {
								localeDetails.setLocale(cell.getStringCellValue());
							} else {
								localeDetails.setTag(cell.getStringCellValue());
							}
						}
						break;
					}
					i++;
				}

				j++;
				localeList.add(localeDetails);
			}
			fileInputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return localeList;
	}

	public static LinkedHashSet<BatchDetails> getBatchDetailsInfo(String data) {
		
		if(data!=null)
		{
			buildProps = ReadPropertiesFile.getProperties(UPDATED_BUILD_DETAIL_PROP_FILE);
			buildDetails = buildProps.getProperty(BUILD_DETAIL_KEY);
		}
		else
		{
			buildProps = ReadPropertiesFile.getProperties(BUILD_DETAIL_PROP_FILE);
			buildDetails = buildProps.getProperty(BUILD_DETAIL_KEY);
		}
		LinkedHashSet<BatchDetails> batchDeatilSet = new LinkedHashSet<>();
		String[] batchList = buildDetails.split(",");
		for (String bat : batchList) {
			BatchDetails batchDetails = new BatchDetails();
			String[] batchDet = bat.split("\\|");
			batchDetails.setBatchName(batchDet[0]);
			batchDetails.setBatchID(batchDet[1]);
			batchDetails.setBatchNickName(batchDet[2]);
			batchDeatilSet.add(batchDetails);
		}

		return batchDeatilSet;
	}
	
	public static void createAuthPool() throws IOException
	{
		
		try {
			FileInputStream fileInputStream=new FileInputStream(new File("authdetails.xlsx"));
			XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int j=0;
            while (rowIterator.hasNext())
            {
	            	AuthenticationDeatils authenticationDeatils=new AuthenticationDeatils();
	                Row row = rowIterator.next();
	                //For each row, iterate through all the columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                int i=0;
	                while (cellIterator.hasNext())
	                {
	                    Cell cell = cellIterator.next();
	                    //Check the cell type and format accordingly
	                    switch (cell.getCellType())
	                    {
	                    
	                        case Cell.CELL_TYPE_NUMERIC:
	                            break;
	                        case Cell.CELL_TYPE_STRING:
	                            String cellValue=cell.getStringCellValue();
	                            if(j!=0)
	                            {
		                            if(i==0)
		                            {
		                            	authenticationDeatils.setUsername(cell.getStringCellValue());
		                            }
		                            else
		                            {
		                            	authenticationDeatils.setPassword(cell.getStringCellValue());
		                            }
	                            }
	                            break;
	                    }
	                    i++;
	                }
	                authList.add(authenticationDeatils);
	                reservedAuthStack.add(authenticationDeatils);
            	
                j++;
            }
            fileInputStream.close();

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(AuthenticationDeatils authDet:authList)
		{
			if(authDet.getUsername()==null)
			{
				authList.remove(authDet);
				break;
			}
			
		}
		
		for(AuthenticationDeatils authDet:authList)
		{
			authStack.push(authDet);
			
		}
	}
	
	public static void createRouteList()
	{
		custRouteList.add("C");
		custRouteList.add("R");
	}

}