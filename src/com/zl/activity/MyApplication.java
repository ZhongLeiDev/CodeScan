package com.zl.activity;

import android.app.Application;

public class MyApplication extends Application{
	private int QueryType;
	/**
	 * ����ֱ��ģʽ
	 */
	public final int JDTQUERY = 0x01;
	/**
	 * ����WebService���ӷ�ʽ
	 */
	public final int SOAPQUERY = 0x02;
	
	@Override
	public void onCreate( ) {
		super.onCreate( );
		
		setQueryType(JDTQUERY);//Ĭ��Ϊ����ֱ��ģʽ
		
	}
	
	/**
	 * �������ӷ�ʽ
	 * @param type
	 */
	public void setQueryType(int type){
		this.QueryType = type;
	}
	
	/**
	 * ��ȡ���ӷ�ʽ
	 * @return
	 */
	public int getQueryType(){
		return this.QueryType;
	}

}
