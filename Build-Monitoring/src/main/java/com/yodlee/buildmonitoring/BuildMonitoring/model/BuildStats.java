package com.yodlee.buildmonitoring.BuildMonitoring.model;


public class BuildStats
{
	
		private String sumInfoID = null;
		private String timeStamp = null;
		private String serverType = null;
		private String ErrorRate = null;
		private String locale;
		private String tag;
		private int totalFailures = 0;
		private boolean isBuildIP;
		
		private String className = null;
		private String gathererIp=null;
		private String date = null;
		private int totalRequests = 0;
		private int success = 0;
		private int agentErrors = 0;
		private int siteErrors = 0;
		private int uarErrors = 0;
		private int infraErrors = 0;
		private int docDownloadErrors = 0;
		
		private int error402=0;
		private int error406=0;
		private int error407=0;
		private int error413=0;
		private int error414=0;
		private int error422=0;
		private int error427=0;
		private int error428=0;
		private int error429=0;
		private int error518=0;
		private int error519=0;
		private int error522=0;
		private int error523=0;
		private int errorInfra=0;
		
		private double avgLatency= 0.00;
		
		
		
		

		
		public boolean isBuildIP() {
			return isBuildIP;
		}

		public void setBuildIP(boolean isBuildIP) {
			this.isBuildIP = isBuildIP;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public int getErrorInfra() {
			return errorInfra;
		}

		public void setErrorInfra(int errorInfra) {
			this.errorInfra = errorInfra;
		}

		public double getAvgInfraLatency() {
			return avgInfraLatency;
		}

		public void setAvgInfraLatency(double avgInfraLatency) {
			this.avgInfraLatency = avgInfraLatency;
		}

		private double avgInfraLatency=0.00;
		private double numNavigations = 0.00;
		private double successPerc = 0.00;
		private double error402Perc = 0.00;
		private double errorInfraPrec=0.00;
		private double error413Perc = 0.00;
		private String interface_type=null;
		
		// For mail
		private double nonBuildSuccessPerc = 0.00;
		private double nonBuildError402Perc = 0.00;
		private double nonBuildError413Perc = 0.00;
		private double nonBuildInfraErrorPerc=0.00;
		private double successPercDiff = 0.00;
		private double error402PercDiff = 0.00;
		private double error413PercDiff = 0.00;
		private double errorInfraPercDiff=0.00;
		private int nonBuildTotReqs = 0;
		private double nonBuildLatency = 0.00;
		private double nonBuildInfraLatency=0.00;
		
		private double oldBuildSuccessPerc = 0.00;
		private double oldBuildError402Perc = 0.00;
		private double oldBuildError413Perc = 0.00;
		private double oldBuildInfraErrorPerc=0.00;
		private int oldBuildTotReqs = 0;
		private double oldBuildLatency = 0.00;
		private double oldBuildInfraLatency=0.00;
		private double latencyDiff = 0.00;
		private double InfraLatencyDiff=0.00;
		
		
		
		
		public final double getOldBuildInfraErrorPerc() {
			return oldBuildInfraErrorPerc;
		}

		public final void setOldBuildInfraErrorPerc(double oldBuildInfraErrorPerc) {
			this.oldBuildInfraErrorPerc = oldBuildInfraErrorPerc;
		}

		public final double getErrorInfraPrec() {
			return errorInfraPrec;
		}

		public final void setErrorInfraPrec(double errorInfraPrec) {
			this.errorInfraPrec = errorInfraPrec;
		}

		public final double getNonBuildInfraErrorPerc() {
			return nonBuildInfraErrorPerc;
		}

		public final void setNonBuildInfraErrorPerc(double nonBuildInfraErrorPerc) {
			this.nonBuildInfraErrorPerc = nonBuildInfraErrorPerc;
		}

		public double getSuccessPerc() {
			return successPerc;
		}

		public void setSuccessPerc(double successPerc) {
			this.successPerc = successPerc;
		}
		
		public String getdate() {
			return date;
		}

		public void setdate(String date) {
			this.date = date;
		}

		public int getAgentErrors() {
			return agentErrors;
		}

		public void setAgentErrors(int agentErrors) {
			this.agentErrors = agentErrors;
		}
		
		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		/**
		 * @return the gathererIp
		 */
		public final String getGathererIp() {
			return gathererIp;
		}

		/**
		 * @param gathererIp the gathererIp to set
		 */
		public final void setGathererIp(String gathererIp) {
			this.gathererIp = gathererIp;
		}

		public int getInfraErrors() {
			return infraErrors;
		}

		public void setInfraErrors(int infraErrors) {
			this.infraErrors = infraErrors;
		}

		public String getServerType() {
			return serverType;
		}

		public void setServerType(String serverType) {
			this.serverType = serverType;
		}

		public int getSiteErrors() {
			return siteErrors;
		}

		public void setSiteErrors(int siteErrors) {
			this.siteErrors = siteErrors;
		}

		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		public String getSumInfoID() {
			return sumInfoID;
		}

		public void setSumInfoID(String sumInfoID) {
			this.sumInfoID = sumInfoID;
		}

		public String getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public double getAvgLatency() {
			return avgLatency;
		}

		public void setAvgLatency(double AvgLatency) {
			this.avgLatency = AvgLatency;
		}

		public int getTotalRequests() {
			return totalRequests;
		}

		public void setTotalRequests(int totalRequests) {
			this.totalRequests = totalRequests;
		}

		public int getUarErrors() {
			return uarErrors;
		}

		public void setUarErrors(int uarErrors) {
			this.uarErrors = uarErrors;
		}

		public void setErrorRate(String ErrorRate) {
			this.ErrorRate = ErrorRate;
		}
		public int getTotalFailures() {
			return totalFailures;
		}

		public void setTotalFailures(int totalFailures) {
			this.totalFailures = totalFailures;
		}

		/*public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
*/
		public double getNumNavigations() {
			return numNavigations;
		}

		public void setNumNavigations(double numNavigations) {
			this.numNavigations = numNavigations;
		}

		public double getNonBuildSuccessPerc() {
			return nonBuildSuccessPerc;
		}

		public void setNonBuildSuccessPerc(double nonBuildSuccessPerc) {
			this.nonBuildSuccessPerc = nonBuildSuccessPerc;
		}

		public double getSuccessPercDiff() {
			return successPercDiff;
		}

		public void setSuccessPercDiff(double successPercDiff) {
			this.successPercDiff = successPercDiff;
		}
		public String  getinterfaceType() {
			return interface_type;
		}

		public void setinterfaceType(String interface_Type) {
			this.interface_type = interface_Type;
		}
		public int getNonBuildTotReqs() {
			return nonBuildTotReqs;
		}

		public void setNonBuildTotReqs(int nonBuildTotReqs) {
			this.nonBuildTotReqs = nonBuildTotReqs;
		}

		public double getNonBuildLatency() {
			return nonBuildLatency;
		}

		public void setNonBuildLatency(double nonBuildLatency) {
			this.nonBuildLatency = nonBuildLatency;
		}

		 public double getOldBuildSuccessPerc() {
			return oldBuildSuccessPerc;
		}

		public void setOldBuildSuccessPerc(double oldBuildSuccessPerc) {
			this.oldBuildSuccessPerc = oldBuildSuccessPerc;
		}

		public int getOldBuildTotReqs() {
			return oldBuildTotReqs;
		}

		public void setOldBuildTotReqs(int oldBuildTotReqs) {
			this.oldBuildTotReqs = oldBuildTotReqs;
		}

		public double getOldBuildLatency() {
			return oldBuildLatency;
		}

		public void setOldBuildLatency(double oldBuildLatency) {
			this.oldBuildLatency = oldBuildLatency;
		}

		public double getLatencyDiff() {
			return latencyDiff;
		}

		public void setLatencyDiff(double latencyDiff) {
			this.latencyDiff = latencyDiff;
		}

		/**
		 * @return the error402
		 */
		public final int getError402() {
			return error402;
		}

		/**
		 * @param error402 the error402 to set
		 */
		public final void setError402(int error402) {
			this.error402 = error402;
		}

		/**
		 * @return the error406
		 */
		public final int getError406() {
			return error406;
		}

		/**
		 * @param error406 the error406 to set
		 */
		public final void setError406(int error406) {
			this.error406 = error406;
		}

		/**
		 * @return the error407
		 */
		public final int getError407() {
			return error407;
		}

		/**
		 * @param error407 the error407 to set
		 */
		public final void setError407(int error407) {
			this.error407 = error407;
		}

		/**
		 * @return the error414
		 */
		public final int getError414() {
			return error414;
		}

		/**
		 * @param error414 the error414 to set
		 */
		public final void setError414(int error414) {
			this.error414 = error414;
		}

		/**
		 * @return the error422
		 */
		public final int getError422() {
			return error422;
		}

		/**
		 * @param error422 the error422 to set
		 */
		public final void setError422(int error422) {
			this.error422 = error422;
		}

		/**
		 * @return the error427
		 */
		public final int getError427() {
			return error427;
		}

		/**
		 * @param error427 the error427 to set
		 */
		public final void setError427(int error427) {
			this.error427 = error427;
		}

		/**
		 * @return the error428
		 */
		public final int getError428() {
			return error428;
		}

		/**
		 * @param error428 the error428 to set
		 */
		public final void setError428(int error428) {
			this.error428 = error428;
		}

		/**
		 * @return the error429
		 */
		public final int getError429() {
			return error429;
		}

		/**
		 * @param error429 the error429 to set
		 */
		public final void setError429(int error429) {
			this.error429 = error429;
		}

		/**
		 * @return the error518
		 */
		public final int getError518() {
			return error518;
		}

		/**
		 * @param error518 the error518 to set
		 */
		public final void setError518(int error518) {
			this.error518 = error518;
		}

		/**
		 * @return the error519
		 */
		public final int getError519() {
			return error519;
		}

		/**
		 * @param error519 the error519 to set
		 */
		public final void setError519(int error519) {
			this.error519 = error519;
		}

		/**
		 * @return the error522
		 */
		public final int getError522() {
			return error522;
		}

		/**
		 * @param error522 the error522 to set
		 */
		public final void setError522(int error522) {
			this.error522 = error522;
		}

		/**
		 * @return the error523
		 */
		public final int getError523() {
			return error523;
		}

		/**
		 * @param error523 the error523 to set
		 */
		public final void setError523(int error523) {
			this.error523 = error523;
		}

		public int getDocDownloadErrors() {
			return docDownloadErrors;
		}

		public void setDocDownloadErrors(int docDownloadErrors) {
			this.docDownloadErrors = docDownloadErrors;
		}

		public int getError413() {
			return error413;
		}

		public void setError413(int error413) {
			this.error413 = error413;
		}

		public double getError402Perc() {
			return error402Perc;
		}

		public void setError402Perc(double error402Perc) {
			this.error402Perc = error402Perc;
		}

		public double getError413Perc() {
			return error413Perc;
		}

		public void setError413Perc(double error413Perc) {
			this.error413Perc = error413Perc;
		}

		public double getError402PercDiff() {
			return error402PercDiff;
		}

		public void setError402PercDiff(double error402PercDiff) {
			this.error402PercDiff = error402PercDiff;
		}

		public double getError413PercDiff() {
			return error413PercDiff;
		}
		

		public final double getErrorInfraPercDiff() {
			return errorInfraPercDiff;
		}

		public final void setErrorInfraPercDiff(double errorInfraPercDiff) {
			this.errorInfraPercDiff = errorInfraPercDiff;
		}

		public void setError413PercDiff(double error413PercDiff) {
			this.error413PercDiff = error413PercDiff;
		}

		public double getNonBuildError402Perc() {
			return nonBuildError402Perc;
		}

		public void setNonBuildError402Perc(double nonBuildError402Perc) {
			this.nonBuildError402Perc = nonBuildError402Perc;
		}

		public double getNonBuildError413Perc() {
			return nonBuildError413Perc;
		}

		public void setNonBuildError413Perc(double nonBuildError413Perc) {
			this.nonBuildError413Perc = nonBuildError413Perc;
		}

		public double getOldBuildError402Perc() {
			return oldBuildError402Perc;
		}

		public void setOldBuildError402Perc(double oldBuildError402Perc) {
			this.oldBuildError402Perc = oldBuildError402Perc;
		}

		public double getOldBuildError413Perc() {
			return oldBuildError413Perc;
		}

		public void setOldBuildError413Perc(double oldBuildError413Perc) {
			this.oldBuildError413Perc = oldBuildError413Perc;
		}
		

		public double getNonBuildInfraLatency() {
			return nonBuildInfraLatency;
		}

		public void setNonBuildInfraLatency(double nonBuildInfraLatency) {
			this.nonBuildInfraLatency = nonBuildInfraLatency;
		}

		public double getOldBuildInfraLatency() {
			return oldBuildInfraLatency;
		}

		public void setOldBuildInfraLatency(double oldBuildInfraLatency) {
			this.oldBuildInfraLatency = oldBuildInfraLatency;
		}

		public double getInfraLatencyDiff() {
			return InfraLatencyDiff;
		}

		public void setInfraLatencyDiff(double infraLatencyDiff) {
			InfraLatencyDiff = infraLatencyDiff;
		}
		
	}


