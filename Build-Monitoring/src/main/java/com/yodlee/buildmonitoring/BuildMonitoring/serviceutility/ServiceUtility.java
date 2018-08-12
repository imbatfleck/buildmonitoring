package com.yodlee.buildmonitoring.BuildMonitoring.serviceutility;

import java.util.HashMap;
import java.util.List;

import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;
import com.yodlee.buildmonitoring.BuildMonitoring.model.StatsSummary;

public class ServiceUtility {
	
	public static StatsSummary getStatsSummary(List<BuildStats> buildStats)
	{
		HashMap<String,BuildStats> classMap=new HashMap<>();
		long buildTotalRequest = 0;
		double buildTotalSuccess = 0.00;
		long buildAgentErrors=0;
		long buildSiteErrors=0;
		long buildUARErrors=0;
		
		double buildTotalWeightedLatency = 0.00;
		double buildTotalInfraWeightedLatency=0.00;
		double buildTotalError402 = 0.00;
		double buildTotalError413 = 0.00;
		double buildTotalInfraError=0.00;
		for(BuildStats bs:buildStats)
		{
				classMap.put(bs.getClassName(), bs);
				buildTotalRequest+=bs.getTotalRequests();
				buildTotalSuccess+=bs.getSuccess();
				buildAgentErrors+=bs.getAgentErrors();
				buildSiteErrors+=bs.getSiteErrors();
				buildUARErrors+=bs.getUarErrors();
				
				buildTotalWeightedLatency+=bs.getTotalRequests()*bs.getAvgLatency();
				buildTotalInfraWeightedLatency+=bs.getTotalRequests()*bs.getAvgInfraLatency();
				buildTotalError402+=bs.getError402();
				buildTotalError413+=bs.getError413();
				buildTotalInfraError+=bs.getInfraErrors();
		}
			double avgSuccessPerc = (double)((float)buildTotalSuccess/buildTotalRequest*100);
			double avgWeightedLatency = buildTotalWeightedLatency/buildTotalRequest;
			double avgInfraWeightedLatency=buildTotalInfraWeightedLatency/buildTotalRequest;
			double avgError402Perc = (double)((float)buildTotalError402/buildTotalRequest*100);
			double avgError413Perc = (double)((float)buildTotalError413/buildTotalRequest*100);
			double avgInfraPerc=(double)((float)buildTotalInfraError/buildTotalRequest*100);
			
			avgSuccessPerc = Math.round(avgSuccessPerc*100)/100.0;
			avgWeightedLatency = Math.round(avgWeightedLatency*100)/100.0;
			avgInfraWeightedLatency=Math.round(avgInfraWeightedLatency*100)/100.0;
			avgError402Perc = Math.round(avgError402Perc*100)/100.0;
			avgError413Perc = Math.round(avgError413Perc*100)/100.0;
			avgInfraPerc=Math.round(avgInfraPerc*100)/100.0;
			
			System.out.println("avgSuccessPerc: "+avgSuccessPerc);
			System.out.println("avgWeightedLatency: "+avgWeightedLatency);
			System.out.println("avgInfraWeightedLatency: "+avgInfraWeightedLatency);
			System.out.println("avgError402Perc: "+avgError402Perc);
			System.out.println("avgError413Perc: "+avgError413Perc);
			System.out.println("avgInfraPerc: "+avgInfraPerc);
			System.out.println("=================================");
			StatsSummary statsSummary = new StatsSummary();
			
			
			statsSummary.setTotalRequests(buildTotalRequest);
			statsSummary.setAvgAgentError(buildAgentErrors);
			statsSummary.setAvgSiteError(buildSiteErrors);
			statsSummary.setAvgUARError(buildUARErrors);
			statsSummary.setAvgSuccessPerc(avgSuccessPerc);
			statsSummary.setAvgLatency(avgWeightedLatency);
			statsSummary.setInfraAvgLatency(avgInfraWeightedLatency);
			statsSummary.setAvgError402Perc(avgError402Perc);
			statsSummary.setAvgError413Perc(avgError413Perc);
			statsSummary.setAvgInfraErrorPerc(avgInfraPerc);
			statsSummary.setClassMap(classMap);
			
			System.out.println("=================================");
			
			
			return statsSummary;

	}

}
