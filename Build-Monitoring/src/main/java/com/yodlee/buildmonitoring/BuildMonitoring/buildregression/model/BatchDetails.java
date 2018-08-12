package com.yodlee.buildmonitoring.BuildMonitoring.buildregression.model;

public class BatchDetails {
	
	private String batchName;
	private String batchID;
	private String batchNickName;
	
	
	public String getBatchNickName() {
		return batchNickName;
	}
	public void setBatchNickName(String batchNickName) {
		this.batchNickName = batchNickName;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	public BatchDetails() {
		super();
	}
}
