package com.yodlee.buildmonitoring.BuildMonitoring.envsetup;

import java.util.HashMap;

public class EnvIPSetUp {
	private HashMap<String, IPSetUP> envIPMap=new HashMap<>();

	public HashMap<String, IPSetUP> getEnvIPMap() {
		return envIPMap;
	}

	public void setEnvIPMap(HashMap<String, IPSetUP> envIpMap2) {
		this.envIPMap = envIpMap2;
	}

	public EnvIPSetUp() {
		super();
	}

	@Override
	public String toString() {
		return "EnvIPSetUp [envIPMap=" + envIPMap + "]";
	}
	
}
