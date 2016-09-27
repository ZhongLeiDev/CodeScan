package com.zkc.Receiver;

import com.zkc.Service.CaptureService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ShutdownReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		CaptureService.scanGpio.closePower();
	}
}

