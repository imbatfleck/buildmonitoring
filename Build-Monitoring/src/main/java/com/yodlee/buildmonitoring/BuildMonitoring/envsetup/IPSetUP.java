package com.yodlee.buildmonitoring.BuildMonitoring.envsetup;

import java.util.List;

public class IPSetUP {
	
	private List<String> buildIP;
	
	
	public List<String> getBuildIP() {
		return buildIP;
	}


	public void setBuildIP(List<String> buildIP) {
		this.buildIP = buildIP;
	}


		public IPSetUP() {
		super();
	}


		@Override
		public String toString() {
			return "IPSetUP [buildIP=" + buildIP + "]";
		}


	
	

}
