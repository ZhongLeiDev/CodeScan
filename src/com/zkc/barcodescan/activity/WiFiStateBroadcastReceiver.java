package com.zkc.barcodescan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WiFiStateBroadcastReceiver extends BroadcastReceiver{
	private Handler myhandler;
	
	public WiFiStateBroadcastReceiver(Handler handler){
		this.myhandler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){  
            //signal strength changed  
        }  
        else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi连接上与否  
            System.out.println("网络状态改变");  
            Log.i("WiFiSTATE", "STATE_CHANGED");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);  
            if(info.getState().equals(NetworkInfo.State.DISCONNECTED)){  
            	Log.i("WiFiSTATE", "DISCONNECTED"); 
            	Message msg = Message.obtain(myhandler);
            	msg.what = 0x10;
            	msg.obj = "网络已断开";
            	msg.sendToTarget();
            }  
            else if(info.getState().equals(NetworkInfo.State.CONNECTED)){  
                  
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();                  
                  
                //获取当前wifi名称  
                Log.i("WiFiSTATE", "CONNECTED"+wifiInfo.getSSID()); 
                Message msg = Message.obtain(myhandler);
            	msg.what = 0x11;
            	msg.obj = "已连接到"+wifiInfo.getSSID();
            	msg.sendToTarget();
                  
            }  
              
        }  
        else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//wifi打开与否  
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);  
              
            if(wifistate == WifiManager.WIFI_STATE_DISABLED){  
//                System.out.println("系统关闭wifi");  
                Log.i("WiFiSTATE", "DISABLED"); 
//                Message msg = Message.obtain(myhandler);
//            	msg.what = 0x10;
//            	msg.obj = "系统WiFi已关闭";
//            	msg.sendToTarget();
            }  
            else if(wifistate == WifiManager.WIFI_STATE_ENABLED){  
//                System.out.println("系统开启wifi");  
                Log.i("WiFiSTATE", "ENABLED"); 
//                Message msg = Message.obtain(myhandler);
//            	msg.what = 0x11;
//            	msg.obj = "系统WiFi已开启";
//            	msg.sendToTarget();
            }  
        }  

}
}
