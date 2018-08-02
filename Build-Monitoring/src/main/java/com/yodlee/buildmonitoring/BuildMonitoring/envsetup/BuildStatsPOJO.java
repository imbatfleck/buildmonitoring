package com.yodlee.buildmonitoring.BuildMonitoring.envsetup;

import java.util.List;

import com.yodlee.buildmonitoring.BuildMonitoring.model.BuildStats;

public class BuildStatsPOJO {
	
	private List<BuildStats> oldBuildStats;
	private List<BuildStats> nonBuildStats;
	private List<BuildStats> currentBuildStats;
	public List<BuildStats> getOldBuildStats() {
		return oldBuildStats;
	}
	public void setOldBuildStats(List<BuildStats> oldBuildStats) {
		this.oldBuildStats = oldBuildStats;
	}
	public List<BuildStats> getNonBuildStats() {
		return nonBuildStats;
	}
	public void setNonBuildStats(List<BuildStats> nonBuildStats) {
		this.nonBuildStats = nonBuildStats;
	}
	public List<BuildStats> getCurrentBuildStats() {
		return currentBuildStats;
	}
	public void setCurrentBuildStats(List<BuildStats> currentBuildStats) {
		this.currentBuildStats = currentBuildStats;
	}
	public BuildStatsPOJO() {
		super();
	}
	
	

}
