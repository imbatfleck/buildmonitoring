package com.yodlee.buildmonitoring.BuildMonitoring.envsetup;

public class BuildDetailSetUp {
	
	private String buildips;
	private String buildType;
	private String buildEnvironment;
	public String getBuildips() {
		return buildips;
	}
	public void setBuildips(String buildips) {
		this.buildips = buildips;
	}
	public String getBuildType() {
		return buildType;
	}
	public void setBuildType(String buildType) {
		this.buildType = buildType;
	}
	public String getBuildEnvironment() {
		return buildEnvironment;
	}
	public void setBuildEnvironment(String buildEnvironment) {
		this.buildEnvironment = buildEnvironment;
	}
	public BuildDetailSetUp() {
		super();
	}
	@Override
	public String toString() {
		return "BuildDetailSetUp [buildips=" + buildips + ", buildType=" + buildType + ", buildEnvironment="
				+ buildEnvironment + "]";
	}
	
	

}
