package com.zl.showlist;

public class QueryResult {
	private String outboundtime = "";
	private String serialnumber = "";
	
	public QueryResult(String outboundtime,String serialnumber){
		this.outboundtime = outboundtime;
		this.serialnumber = serialnumber;
	}
	
	public void setOutboundTime(String outboundtime){
		this.outboundtime = outboundtime;
	}
	
	public String getOutboundTime(){
		return this.outboundtime;
	}
	
	public void setSerielNumber(String serialnumber){
		this.serialnumber = serialnumber;
	}
	
	public String getSerialNumber(){
		return this.serialnumber;
	}

}
