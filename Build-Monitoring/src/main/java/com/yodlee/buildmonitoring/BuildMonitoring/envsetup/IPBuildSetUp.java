package com.yodlee.buildmonitoring.BuildMonitoring.envsetup;

import java.util.List;

public class IPBuildSetUp {
	
	private List<String> nonBuild;
	private List<String> currentBuild;
	private List<String> oldBuild;
	

	public List<String> getNonBuild() {
		return nonBuild;
	}


	public void setNonBuild(List<String> nonBuild) {
		this.nonBuild = nonBuild;
	}


	public List<String> getCurrentBuild() {
		return currentBuild;
	}


	public void setCurrentBuild(List<String> currentBuild) {
		this.currentBuild = currentBuild;
	}


	public List<String> getOldBuild() {
		return oldBuild;
	}


	public void setOldBuild(List<String> oldBuild) {
		this.oldBuild = oldBuild;
	}


	public IPBuildSetUp() {
		super();
	}
	
	

}
