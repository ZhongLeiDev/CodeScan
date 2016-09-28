package com.zl.soap;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class SoapActivity extends Activity{
//	private Button btntest,btnnumber,btnupdate;
	private TextView tvtest;
	private DBUtils dbUtil;
	private Map<String,String> resultmap;
	private int number = 0;
	private boolean donestate = false;
	private Handler myhandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x01){
				if(resultmap.isEmpty()){
					Toast.makeText(SoapActivity.this, "Result is empty!", Toast.LENGTH_SHORT).show();
				}else{
					tvtest.append("\r\n"+"SERIAL_NUMBER："+resultmap.get("SERIAL_NUMBER"));
					tvtest.append("\r\n"+"MO_NUMBER："+resultmap.get("MO_NUMBER"));
					tvtest.append("\r\n"+"MODEL_NAME："+resultmap.get("MODEL_NAME"));
					tvtest.append("\r\n"+"LAN_MAC："+resultmap.get("LAN_MAC"));
					tvtest.append("\r\n"+"BT_MAC："+resultmap.get("BT_MAC"));
					tvtest.append("\r\n"+"BRAODCAST_CN："+resultmap.get("BRAODCAST_CN"));
					tvtest.append("\r\n"+"WIFI_MAC："+resultmap.get("WIFI_MAC"));
					tvtest.append("\r\n"+"TEST_TIME："+resultmap.get("TEST_TIME"));
					tvtest.append("\r\n"+"HDCP_KEY14："+resultmap.get("HDCP_KEY14"));
					tvtest.append("\r\n"+"HDCP_KEY20："+resultmap.get("HDCP_KEY20"));
				}
			}else if(msg.what == 0x02){
				tvtest.append("\r\n本批次已扫描"+number+"台！");
			}else if(msg.what == 0x03){
				tvtest.append("\r\n更新操作成功！");
			}else if(msg.what == 0x04){
				tvtest.append("\r\n更新操作失败！");
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
//		setContentView(R.layout.test);
		
//		btntest = (Button)findViewById(R.id.btntest);
//		btnnumber = (Button)findViewById(R.id.btnnumber);
//		btnupdate = (Button)findViewById(R.id.btnupdate);
//		tvtest = (TextView)findViewById(R.id.tvtest);
//		dbUtil = new DBUtils();
//		
//		btntest.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						resultmap = dbUtil.getMessageWithSN("KV1630D1000019Q005");
//						Message msg = Message.obtain(myhandler);
//						msg.what = 0x01;
//						msg.sendToTarget();
//					}
//					
//				}).start();
//			}
//		});
//		
//		btnnumber.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						number = dbUtil.getSumWithOutbondState("VJ2016090800003");
//						Message msg = Message.obtain(myhandler);
//						msg.what = 0x02;
//						msg.sendToTarget();
//					}
//					
//				}).start();
//			}
//		});
//		
//		btnupdate.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						donestate = dbUtil.updateOutbondState("KM1624D1000151G09F", "NO");
//						Message msg = Message.obtain(myhandler);
//						if(donestate){
//						msg.what = 0x03;
//						}else{
//							msg.what = 0x04;
//						}
//						msg.sendToTarget();
//					}
//					
//				}).start();
//			}
//		});
		
	}
	
	private Runnable getMessageWithSNRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			resultmap = dbUtil.getMessageWithSN("KV1630D1000019Q005");
			Message msg = Message.obtain(myhandler);
			msg.what = 0x01;
			msg.sendToTarget();
		}
		
	};
	
	private Runnable getSumWithOutbondState = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			number = dbUtil.getSumWithOutbondState("VJ2016090800003");
			Message msg = Message.obtain(myhandler);
			msg.what = 0x02;
			msg.sendToTarget();
		}
		
	};
	
	private Runnable updateOutbondStateRunnable = new Runnable(){
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			donestate = dbUtil.updateOutbondState("KM1624D1000151G09F", "NO");
			Message msg = Message.obtain(myhandler);
			if(donestate){
			msg.what = 0x03;
			}else{
				msg.what = 0x04;
			}
			msg.sendToTarget();
		}
		
	};

}
