package com.yodlee.buildmonitoring.BuildMonitoring.model;

import java.util.HashMap;
import java.util.List;

public class BuildEnvModel {
	private HashMap<String,List<BuildStats>> envMapping;

	

	public HashMap<String, List<BuildStats>> getEnvMapping() {
		return envMapping;
	}

	public void setEnvMapping(HashMap<String, List<BuildStats>> envMapping) {
		this.envMapping = envMapping;
	}

	public BuildEnvModel() {
		super();
	}
	

}
