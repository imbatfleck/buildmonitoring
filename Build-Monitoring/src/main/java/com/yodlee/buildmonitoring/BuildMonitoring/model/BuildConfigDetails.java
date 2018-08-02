package com.yodlee.buildmonitoring.BuildMonitoring.model;

public class BuildConfigDetails {
	
	private String environment;
	private String buildIP;
	private String nonBUildIP;
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getBuildIP() {
		return buildIP;
	}
	public void setBuildIP(String buildIP) {
		this.buildIP = buildIP;
	}
	public String getNonBUildIP() {
		return nonBUildIP;
	}
	public void setNonBUildIP(String nonBUildIP) {
		this.nonBUildIP = nonBUildIP;
	}
	public BuildConfigDetails() {
		super();
	}
	@Override
	public String toString() {
		return "BuildConfigDetails [environment=" + environment + ", buildIP=" + buildIP + ", nonBUildIP=" + nonBUildIP
				+ "]";
	}
	
	

}
