package com.yodlee.buildmonitoring.BuildMonitoring.model;

import java.util.HashMap;

public class StatsSummary {
	
	private long totalRequests;
	private double avgSuccessPerc;
	private double avgLatency;
	private double infraAvgLatency;
	private double avgError402Perc;
	private double avgInfraErrorPerc;
	private double avgError413Perc;
	private long avgAgentError;
	private long avgSiteError;
	private long avgUARError;
	
	private HashMap<String,BuildStats> classMap;
	
	
	
	
	
	public long getAvgAgentError() {
		return avgAgentError;
	}
	public void setAvgAgentError(long avgAgentError) {
		this.avgAgentError = avgAgentError;
	}
	public long getAvgSiteError() {
		return avgSiteError;
	}
	public void setAvgSiteError(long avgSiteError) {
		this.avgSiteError = avgSiteError;
	}
	public long getAvgUARError() {
		return avgUARError;
	}
	public void setAvgUARError(long avgUARError) {
		this.avgUARError = avgUARError;
	}
	public HashMap<String, BuildStats> getClassMap() {
		return classMap;
	}
	public void setClassMap(HashMap<String, BuildStats> classMap) {
		this.classMap = classMap;
	}
	public final double getAvgInfraErrorPerc() {
		return avgInfraErrorPerc;
	}
	public final void setAvgInfraErrorPerc(double avgInfraErrorPerc) {
		this.avgInfraErrorPerc = avgInfraErrorPerc;
	}
	public final double getInfraAvgLatency() {
		return infraAvgLatency;
	}
	public final void setInfraAvgLatency(double infraAvgLatency) {
		this.infraAvgLatency = infraAvgLatency;
	}
	public final long getTotalRequests() {
		return totalRequests;
	}
	public final void setTotalRequests(long totalRequests) {
		this.totalRequests = totalRequests;
	}
	public final double getAvgSuccessPerc() {
		return avgSuccessPerc;
	}
	public final void setAvgSuccessPerc(double avgSuccessPerc) {
		this.avgSuccessPerc = avgSuccessPerc;
	}
	public final double getAvgLatency() {
		return avgLatency;
	}
	public final void setAvgLatency(double avgLatency) {
		this.avgLatency = avgLatency;
	}
	public double getAvgError402Perc() {
		return avgError402Perc;
	}
	public void setAvgError402Perc(double avgError402Perc) {
		this.avgError402Perc = avgError402Perc;
	}
	public double getAvgError413Perc() {
		return avgError413Perc;
	}
	public void setAvgError413Perc(double avgError413Perc) {
		this.avgError413Perc = avgError413Perc;
	}
}

