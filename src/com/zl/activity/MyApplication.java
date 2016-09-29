package com.zl.activity;

import android.app.Application;

public class MyApplication extends Application{
	private int QueryType;
	/**
	 * 内网直连模式
	 */
	public final int JDTQUERY = 0x01;
	/**
	 * 外网WebService连接方式
	 */
	public final int SOAPQUERY = 0x02;
	
	@Override
	public void onCreate( ) {
		super.onCreate( );
		
		setQueryType(JDTQUERY);//默认为内网直连模式
		
	}
	
	/**
	 * 设置连接方式
	 * @param type
	 */
	public void setQueryType(int type){
		this.QueryType = type;
	}
	
	/**
	 * 获取连接方式
	 * @return
	 */
	public int getQueryType(){
		return this.QueryType;
	}

}
