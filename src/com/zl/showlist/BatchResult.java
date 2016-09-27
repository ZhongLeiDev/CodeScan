package com.zl.showlist;

public class BatchResult {
	
	private String batchname = "";
	private String batchsize = "";
	private String batchflag = "";
	
	public BatchResult(String batchname,String batchsize,String batchflag){
		this.batchname = batchname;
		this.batchsize = batchsize;
		this.batchflag = batchflag;
	}
	
	public void setBatchName(String batchname){
		this.batchname = batchname;
	}
	
	public String getBatchName(){
		return this.batchname;
	}
	
	public void setBatchSize(String batchsize){
		this.batchsize = batchsize;
	}
	
	public String getBatchSize(){
		return this.batchsize;
	}
	
	public void setBatchFlag(String batchflag){
		this.batchflag = batchflag;
	}
	
	public String getBatchFlag(){
		return this.batchflag;
	}

}
