package com.yodlee.buildmonitoring.BuildMonitoring.serviceimp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yodlee.buildmonitoring.BuildMonitoring.BuildMonitoringApplication;
import com.yodlee.buildmonitoring.BuildMonitoring.Utility;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.batchreqid.BatchReqDetailListID;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.batchstatuschecking.BatchReqDetailList;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.comparebatchdetails.AfterCompareResponse;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.controller.BuildOperation;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.loginmodel.AuthenticationDeatils;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.loginmodel.LoginResponseEntity;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.serverurl.URLFactory;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.statusmodel.BuildRegressionStatus;
import com.yodlee.buildmonitoring.BuildMonitoring.buildregression.triggerbatchdetails.TriggerBatchResponse;

public class RegressionService {
	
	public static List<BuildRegressionStatus> buildRegStList=new ArrayList<>();
	public static ConcurrentHashMap<String,String> requestMapper=new ConcurrentHashMap();
	public static HashMap<String, String> requestInitTimeMapper=new HashMap<>();
	private static final String REQ_STATUS_COMPLETED="completed";
	private static final String REQ_STATUS_PENDING="pending";
	private static Stack<AuthenticationDeatils> authStack=new Stack<>();
	private static Stack<AuthenticationDeatils> reservedAuthStack=new Stack<>();
	private static List<AuthenticationDeatils> authList=new ArrayList<>();
	private static final ArrayList<String> custRouteList=new ArrayList<>();
	private static final HashMap<String,String> requestStatusMap=new HashMap<>();
	private static CopyOnWriteArrayList<String> requestList=new CopyOnWriteArrayList<>();
	private static long latestRequestStartTime=0;
	private static HashSet<String> processMap=new HashSet<>();
	public static HashMap<String, String> statusMap=new HashMap<>();
	public static int batchProcessCount=0;
	
	public static void startRegression(String batchName, String batchID) throws InterruptedException, ParseException, IOException {

		////logger.info("Build regression application has started");
		BuildOperation buildOperation=new BuildOperation();
		TriggerBatchResponse triggerBatchResponse=null;
		BatchReqDetailListID batchReqDetailListID=null;
		String userName=null;
		
	
		for(String custRoute: custRouteList)
		{
			////logger.info("Batch Name - {}",batchName);
			////logger.info("Environment - {}",custRoute);
			boolean isBatchTriggered=false;
			while(!isBatchTriggered)
			{
				boolean isPicked=false;
				if(authStack.isEmpty())
				{
					////logger.info("Auth pool is empty",authStack);
					long startTime=latestRequestStartTime;
					long endTime=startTime+(60000*30);
					int i=0;
					while(!(startTime>=endTime)){
						////logger.info("Polling auth pool for credentials - {}",i);
						int poolSize=checkPoolForCreds(buildOperation);
						////logger.info("Pool Size - {}",poolSize);
						if(poolSize>0)
						{
							isPicked=true;
							////logger.info("Succeful polling");
							////logger.info("Pool Size - {}",poolSize);
							////logger.info("Username picked - {}",authStack.peek().getUsername());
							break;
						}
						////logger.info("Auth pool is busy, sleeping for 1 minute");
						startTime=startTime+60000;
						Thread.sleep(60000);
						i++;
					}
					if(!isPicked)
					{
						////logger.info("Auth is busy so batch could not picked- Hence checking time initiated");
						for(String req:requestList)
						{
							long initTime=Long.valueOf(requestInitTimeMapper.get(req));
							long targetTime=Utility.getCurrentTImeStamp();
							long checkTime=targetTime-initTime;
							double minCheckTime=((checkTime/1000)/60.00);
							////logger.info("Minute time - {}",minCheckTime);
							
							if(checkTime>30)
							{
								try
								{
									BatchReqDetailList batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,req);
									userName=batchReqDetail.getUserName();
								}
								catch(HttpClientErrorException he)
								{
									if(he.getMessage().contains("408"))
									{
										buildOperation.getToken(URLFactory.LOGIN_URL, reservedAuthStack.peek());
										BatchReqDetailList batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,req);
										userName=batchReqDetail.getUserName();
									}
								}
								for(AuthenticationDeatils authenticationDeatils: authList)
								{
									if(authenticationDeatils.getUsername().equals(userName))
									{
											authStack.push(authenticationDeatils);
											requestList.remove(req);
											break;
									}
								}
							}
						}
					}
				}
				else
				{
					
					AuthenticationDeatils authenticationDeatils=authStack.peek();
					////logger.info("picked up credential from pool - {}",authenticationDeatils.getUsername());
					LoginResponseEntity loginResponseEntity=buildOperation.getToken(URLFactory.LOGIN_URL,authenticationDeatils);
					////logger.info("Token Created - {}",loginResponseEntity.getToken());
					
					try
					{
						 triggerBatchResponse=buildOperation.triggerBatch(URLFactory.TRIGGER_BATCH_URL, batchID,custRoute);
					}
					catch(HttpClientErrorException he)
					{
						if(he.getMessage().contains("408"))
						{
							buildOperation.getToken(URLFactory.LOGIN_URL, reservedAuthStack.peek());
							triggerBatchResponse=buildOperation.triggerBatch(URLFactory.TRIGGER_BATCH_URL, batchID,custRoute);
						}
					}
					////logger.info("User is triggering batch - {}",batchName);
					////logger.info("Batch trigger status message",triggerBatchResponse.getStatusMsg());
					String appReqID=triggerBatchResponse.getAppRequestId();
					////logger.info("Batch status id - {}",appReqID);
					if(appReqID!=null && !appReqID.equals("REJECTED"))
					{
						////logger.info("Batch successfully triggered");
						try
						{
							 batchReqDetailListID=buildOperation.getBatchRequestId(URLFactory.BATCH_GETREQID_URL,batchName,appReqID);
						}
						catch(HttpClientErrorException he)
						{
							if(he.getMessage().contains("408"))
							{
								buildOperation.getToken(URLFactory.LOGIN_URL, reservedAuthStack.peek());
								batchReqDetailListID=buildOperation.getBatchRequestId(URLFactory.BATCH_GETREQID_URL,batchName,appReqID);
							}
						}
						if(custRoute.equals("C"))
						{	
							////logger.info("Batch Trigger Environment - {}","Developer");
							requestMapper.put(batchReqDetailListID.getBatchReqDetailsId(), batchName+"_dev");
							processMap.add(batchName);
						}
						else if(custRoute.equals("R"))
						{
							////logger.info("Batch Trigger Environment - {}","Regression");
							requestMapper.put(batchReqDetailListID.getBatchReqDetailsId(), batchName+"_reg");
							processMap.add(batchName);
						}
						////logger.info("Batch Request ID - {}",batchReqDetailListID.getBatchReqDetailsId());
						requestList.add(batchReqDetailListID.getBatchReqDetailsId());
						requestInitTimeMapper.put(batchReqDetailListID.getBatchReqDetailsId(), batchReqDetailListID.getReqInitiated());
						////logger.info("Batch Request List - {}",requestList);
						isBatchTriggered=true;
						latestRequestStartTime=Long.valueOf(batchReqDetailListID.getReqInitiated());
						AuthenticationDeatils authenticationDeatils2=authStack.pop();
						////logger.info("popped one credential set from auth stack - {}",authenticationDeatils2.getUsername());
					}
					else if(appReqID.equals("REJECTED"))
					{
						////logger.info("Batch request has been rejected for the user- {}",authenticationDeatils.getUsername());
						long startTime=latestRequestStartTime;
						long endTime=startTime+(60000*5);
						while(!(startTime>=endTime)){
							////logger.info("Polling pool for independent user");
							int poolSize=checkPoolForCreds(buildOperation);
							if(poolSize>0)
							{
								////logger.info("User got picked with username - {}",authStack.peek().getUsername());
								////logger.info("Pool Size - {}",poolSize);
								break;
							}
							////logger.info("Pool is busy. Sleeping for 1 minute");
							startTime=startTime+60000;
							Thread.sleep(60000);
						}
					}
				}
			}
		}
		for(String reqID1:requestMapper.keySet())
		{
			String statusID=statusMap.get(reqID1);
			if(statusID==null)
			{
				try
				{
					BatchReqDetailList batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,reqID1);
				}
				catch(HttpClientErrorException he)
				{
					if(he.getMessage().contains("408"))
					{
						buildOperation.getToken(URLFactory.LOGIN_URL, reservedAuthStack.peek());
						BatchReqDetailList batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,reqID1);
					}
				}
			}
		}
		////logger.info("Status Map - {}",statusMap);
		////logger.info("Total Batch Request Size - {}",processMap.size());
		////logger.info("Total Batch Size - {}",batchProcessCount);
		
		if(processMap.size()==batchProcessCount)
		{
			long startTime=1530540164000l;
			//long endTime=startTime+(60000*25);
			long endTime=startTime+(60000*25);
			while(!(startTime>=endTime)){
				if(requestMapper.size()==0)
				{
					break;
				}
				for(String reqID:requestMapper.keySet())
				{
					////logger.info("Request Nav - {}",reqID);
					try
					{
						BatchReqDetailList batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,reqID);
					}
					catch(HttpClientErrorException he)
					{
						if(he.getMessage().contains("408"))
						{
							if(authStack.size()==0)
							{
								BuildMonitoringApplication.createAuthPool();
							}
							buildOperation.getToken(URLFactory.LOGIN_URL, authStack.peek());
							BatchReqDetailList batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,reqID);
						}
					}
				}
				if(!compareBatches(buildOperation,true))
				{
					////logger.info("waiting for batch request completion");
					startTime=startTime+60000;
					Thread.sleep(60000);
				}
				else
				{
					////logger.info("Batch comparision is done for the batch");
				}
			}
		}
		if(processMap.size()==batchProcessCount 
				&& requestMapper.size()!=0)
		{
			for(String request:requestMapper.keySet())
			{
				////logger.info("Pending Request - {}",request);
				compareBatches(buildOperation,false);
			}
		}
		
	
	}
	
	private static int checkPoolForCreds(BuildOperation buildOperation) throws JsonProcessingException {
		BatchReqDetailList batchReqDetail=null;
		for(String reqID:requestList)
		{
			try
			{
				 batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,reqID);
			}
			catch(HttpClientErrorException he)
			{
				if(he.getMessage().contains("408"))
				{
					buildOperation.getToken(URLFactory.LOGIN_URL, reservedAuthStack.peek());
					batchReqDetail=buildOperation.getStatusBatch(URLFactory.BATCH_STATUS_URL,reqID);
				}
			}
			if(batchReqDetail.getBatchStatusId().equals("5"))
			{
				for(AuthenticationDeatils authenticationDeatils: authList)
				{
					if(authenticationDeatils.getUsername().equals(batchReqDetail.getUserName()))
					{
							authStack.push(authenticationDeatils);
							requestList.remove(reqID);
							break;
					}
				}
			}
		}
		
		return authStack.size();
	}
	
	private static boolean compareBatches(BuildOperation buildOperation,boolean checkStatus) throws IOException
	{
		boolean isCompared=false;
		AfterCompareResponse afterCompareResponse=null;
		for(String reqID1:requestMapper.keySet())
		{
			//logger.info("Request Mapper - {}",requestMapper);
			//logger.info("Request ID - {}",reqID1);
			String statID=statusMap.get(reqID1);
			
			if(checkStatus && !statID.equals("5"))
			{
				continue;
			}
			boolean isBatDev=false;
			boolean isBatReg=false;
			String batchReqDev=null;
			String batchReqReg=null;
			String batchDev=null;
			String batchReg=null;
			String batch=requestMapper.get(reqID1);
			if(batch.contains("_dev"))
			{
				batchDev=batch;
				batchDev=batchDev.replace("_dev", "").trim();
				batchReqDev=reqID1;
				isBatDev=true;
			}
			else if(batch.contains("_reg"))
			{
				batchReg=batch;
				batchReg=batchReg.replace("_reg", "").trim();
				batchReqReg=reqID1;
				isBatReg=true;
			}
			for(String reqID2:requestMapper.keySet())
			{
				String statID2=statusMap.get(reqID2);
				if(checkStatus && !statID2.equals("5"))
				{
					continue;
				}
				String bat=requestMapper.get(reqID2);
				if(isBatDev)
				{
					if(bat.contains("_reg"))
					{
						bat=bat.replace("_reg", "");
						if(batchDev.equals(bat))
						{
							batchReg=bat;
							batchReqReg=reqID2;
							break;
						}
					}
				}
				else if(isBatReg)
				{
					if(bat.contains("_dev"))
					{
						bat=bat.replace("_dev", "");
						if(batchReg.equals(bat))
						{
							batchDev=bat;
							batchReqDev=reqID2;
							break;
						}
					}
				}
			}
			if(batchReqDev!=null && batchReqReg!=null)
			{
				try
				{
					 	afterCompareResponse=buildOperation.doCompareBatches(URLFactory.COMPARE_BATCH_URL, batch, batchReqDev, batchReqReg);
				}
				catch(HttpClientErrorException he)
				{
					if(he.getMessage().contains("408"))
					{
						buildOperation.getToken(URLFactory.LOGIN_URL, reservedAuthStack.peek());
						afterCompareResponse=buildOperation.doCompareBatches(URLFactory.COMPARE_BATCH_URL, batch, batchReqDev, batchReqReg);
					}
				}
				//logger.info("Request processed");
				//sendMail(afterCompareResponse.getFinalHTML(),batch,afterCompareResponse.isWrote());
				//logger.info("Mail Sent");
				//logger.info("Removing requests from requestmapper");
				requestMapper.remove(batchReqDev);
				//logger.info("Removed Request ID - {}",batchReqDev);
				requestMapper.remove(batchReqReg);
				//logger.info("Removed Request ID - {}",batchReqReg);
				requestStatusMap.put(batchReqDev, REQ_STATUS_COMPLETED);
				requestStatusMap.put(batchReqReg, REQ_STATUS_COMPLETED);
				isCompared=true;
				break;
			}
		}
		return isCompared;
	}
	

}
