package com.yodlee.buildmonitoring.BuildMonitoring.buildregression.statusmodel;

public class BuildRegressionStatus {
	
	private String batchID;
	private boolean authStatus;
	private boolean devBatchStatus;
	private boolean regBatchStatus;
	private boolean batchCompStatus;
	
	
	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	public boolean getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(boolean authStatus) {
		this.authStatus = authStatus;
	}
	public boolean getDevBatchStatus() {
		return devBatchStatus;
	}
	public void setDevBatchStatus(boolean devBatchStatus) {
		this.devBatchStatus = devBatchStatus;
	}
	public boolean getRegBatchStatus() {
		return regBatchStatus;
	}
	public void setRegBatchStatus(boolean regBatchStatus) {
		this.regBatchStatus = regBatchStatus;
	}
	public boolean getBatchCompStatus() {
		return batchCompStatus;
	}
	public void setBatchCompStatus(boolean batchCompStatus) {
		this.batchCompStatus = batchCompStatus;
	}
	public BuildRegressionStatus() {
		super();
	}
	
	

}
