package com.zkc.Receiver;


import com.zkc.Service.CaptureService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.serialport.api.SerialPort;
import android.serialport.api.SerialPortHelper;
import android.util.Log;

/**
 * 按键消息广播接收
 * 
 * @author zkc-soft2
 * 
 */
public class RemoteControlReceiver extends BroadcastReceiver {

	private static final String TAG = "RemoteControlReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// 获取按键消息
		String action = intent.getAction();
		Log.i(TAG, "System message " + action);
		if (action.equals("com.zkc.keycode")) {
			if (StartReceiver.times++ > 0) {
				StartReceiver.times = 0;
				int keyValue = intent.getIntExtra("keyvalue", 0);
				Log.i(TAG, "KEY VALUE:"+keyValue);
				if (keyValue == 136 || keyValue == 135 || keyValue == 131) {
					Log.i(TAG, "Scan key down.........");
					SerialPort.CleanBuffer();
					CaptureService.scanGpio.openScan();
				}
			}
		} else if (action.equals("android.intent.action.SCREEN_ON")) {
			Log.i(TAG, "Power off,Close scan modules power.........");
			CaptureService.serialPort = new SerialPortHelper(CaptureService.handler);//9600
			CaptureService.serialPort.OpenSerialPort(CaptureService.choosed_serial,CaptureService.choosed_buad);
			CaptureService.scanGpio.openPower();
			CaptureService.serialPort.Write(CaptureService.defaultSetting2D);
		} else if (action.equals("android.intent.action.SCREEN_OFF")) {
			Log.i(TAG, "ACTION_SCREEN_OFF,Close scan modules power.........");
			CaptureService.serialPort.CloseSerialPort();
			CaptureService.serialPort = new SerialPortHelper(CaptureService.handler);//115200
			CaptureService.serialPort.OpenSerialPort(CaptureService.choosed_serial,115200);
			CaptureService.serialPort.CloseSerialPort();
			CaptureService.scanGpio.closePower();
			CaptureService.scanGpio.closeScan();
		} else if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
			Log.i(TAG, "ACTION_SCREEN_ON,Open scan modules power.........");
			CaptureService.serialPort.CloseSerialPort();
			CaptureService.serialPort = new SerialPortHelper(CaptureService.handler);//115200
			CaptureService.serialPort.OpenSerialPort(CaptureService.choosed_serial,115200);
			CaptureService.serialPort.CloseSerialPort();
			CaptureService.scanGpio.closePower();
			CaptureService.scanGpio.closeScan();
		}
	}
}