package com.yodlee.buildmonitoring.BuildMonitoring.model;

import java.util.List;

public class CompareStatsSummary {
	
	private String environament;
	private String alterEnv;
	private String buildIps;
	private String nonBuildIps;
	private long buildTotalRequest;
	private long nonBuildTotalRequest;
	private double buildSuccessPer;
	private double nonBuildSuccessPer;
	private double successDiff;
	private double buildLatency;
	private double nonBuildLatency;
	private double latencyDiff;
	private double buildErr402;
	private double nonBuildErr402;
	private double err402Diff;
	private double buildErr413;
	private double nonBuildErr413;
	private double err413Diff;
	private double buildInfraLatency;
	private double nonBuildInfraLatency;
	private double infraLatencyDiff;
	private double buildInfraError;
	private double nonBuildInfraError;
	private double infraDiff;
	private List<AgentStats> agentStatList;
	
	
	
	
	
	public String getNonBuildIps() {
		return nonBuildIps;
	}
	public void setNonBuildIps(String nonBuildIps) {
		this.nonBuildIps = nonBuildIps;
	}
	public String getAlterEnv() {
		return alterEnv;
	}
	public void setAlterEnv(String alterEnv) {
		this.alterEnv = alterEnv;
	}
	public String getBuildIps() {
		return buildIps;
	}
	public void setBuildIps(String buildIps) {
		this.buildIps = buildIps;
	}
	public String getEnvironament() {
		return environament;
	}
	public void setEnvironament(String environament) {
		this.environament = environament;
	}
	public List<AgentStats> getAgentStatList() {
		return agentStatList;
	}
	public void setAgentStatList(List<AgentStats> agentStatList) {
		this.agentStatList = agentStatList;
	}
	public double getInfraLatencyDiff() {
		return infraLatencyDiff;
	}
	public void setInfraLatencyDiff(double infraLatencyDiff) {
		this.infraLatencyDiff = infraLatencyDiff;
	}
	public long getBuildTotalRequest() {
		return buildTotalRequest;
	}
	public void setBuildTotalRequest(long buildTotalRequest) {
		this.buildTotalRequest = buildTotalRequest;
	}
	public long getNonBuildTotalRequest() {
		return nonBuildTotalRequest;
	}
	public void setNonBuildTotalRequest(long nonBuildTotalRequest) {
		this.nonBuildTotalRequest = nonBuildTotalRequest;
	}
	public double getBuildSuccessPer() {
		return buildSuccessPer;
	}
	public void setBuildSuccessPer(double buildSuccessPer) {
		this.buildSuccessPer = buildSuccessPer;
	}
	public double getNonBuildSuccessPer() {
		return nonBuildSuccessPer;
	}
	public void setNonBuildSuccessPer(double nonBuildSuccessPer) {
		this.nonBuildSuccessPer = nonBuildSuccessPer;
	}
	public double getSuccessDiff() {
		return successDiff;
	}
	public void setSuccessDiff(double successDiff) {
		this.successDiff = successDiff;
	}
	public double getBuildLatency() {
		return buildLatency;
	}
	public void setBuildLatency(double buildLatency) {
		this.buildLatency = buildLatency;
	}
	public double getNonBuildLatency() {
		return nonBuildLatency;
	}
	public void setNonBuildLatency(double nonBuildLatency) {
		this.nonBuildLatency = nonBuildLatency;
	}
	public double getLatencyDiff() {
		return latencyDiff;
	}
	public void setLatencyDiff(double latencyDiff) {
		this.latencyDiff = latencyDiff;
	}
	public double getBuildErr402() {
		return buildErr402;
	}
	public void setBuildErr402(double buildErr402) {
		this.buildErr402 = buildErr402;
	}
	public double getNonBuildErr402() {
		return nonBuildErr402;
	}
	public void setNonBuildErr402(double nonBuildErr402) {
		this.nonBuildErr402 = nonBuildErr402;
	}
	public double getErr402Diff() {
		return err402Diff;
	}
	public void setErr402Diff(double err402Diff) {
		this.err402Diff = err402Diff;
	}
	public double getBuildErr413() {
		return buildErr413;
	}
	public void setBuildErr413(double buildErr413) {
		this.buildErr413 = buildErr413;
	}
	public double getNonBuildErr413() {
		return nonBuildErr413;
	}
	public void setNonBuildErr413(double nonBuildErr413) {
		this.nonBuildErr413 = nonBuildErr413;
	}
	public double getErr413Diff() {
		return err413Diff;
	}
	public void setErr413Diff(double err413Diff) {
		this.err413Diff = err413Diff;
	}
	public double getBuildInfraLatency() {
		return buildInfraLatency;
	}
	public void setBuildInfraLatency(double buildInfraLatency) {
		this.buildInfraLatency = buildInfraLatency;
	}
	public double getNonBuildInfraLatency() {
		return nonBuildInfraLatency;
	}
	public void setNonBuildInfraLatency(double nonBuildInfraLatency) {
		this.nonBuildInfraLatency = nonBuildInfraLatency;
	}
	public double getBuildInfraError() {
		return buildInfraError;
	}
	public void setBuildInfraError(double buildInfraError) {
		this.buildInfraError = buildInfraError;
	}
	public double getNonBuildInfraError() {
		return nonBuildInfraError;
	}
	public void setNonBuildInfraError(double nonBuildInfraError) {
		this.nonBuildInfraError = nonBuildInfraError;
	}
	public double getInfraDiff() {
		return infraDiff;
	}
	public void setInfraDiff(double infraDiff) {
		this.infraDiff = infraDiff;
	}
	public CompareStatsSummary() {
		super();
	}
	
	

}
