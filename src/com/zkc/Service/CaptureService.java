package com.zkc.Service;

import java.io.DataOutputStream;
import com.zkc.Receiver.RemoteControlReceiver;
import com.zkc.beep.ServiceBeepManager;
import com.zkc.pc700.helper.ScanGpio;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.serialport.api.SerialPort;
import android.serialport.api.SerialPortHelper;
import android.util.Log;

public class CaptureService extends Service {
	public static SharedPreferences sharedPreferences = null;
	public static String str_encoding = "UTF-8";

	static Context context;
	private static final String TAG = "CaptureService";
	public static ServiceBeepManager beepManager;// 扫锟斤拷晒锟斤拷锟绞撅拷锟�

	RemoteControlReceiver screenStatusReceiver;

	/*
	 * 鏄庡痉涓�缁存潯鐮佹仮澶嶅嚭鍘傝缃�
	 */
	public static byte[] defaultSetting1D = new byte[] { 0x04, (byte) 0xC8,
			0x04, 0x00, (byte) 0xFF, 0x30 };// 锟斤拷锟斤拷锟斤拷

	/*
	 * 鏄庡痉涓�缁存潯鐮佽缃暟鎹牸寮忥紝鍥炶溅鎹㈣
	 */
	public static byte[] dataTypeFor1D = new byte[] { 0x07, (byte) 0xC6, 0x04,
			0x08, 0x00, (byte) 0xEB, 0x07, (byte) 0xFE, 0x35 };// 锟斤拷取锟斤拷莞锟绞�

	/*
	 * 鏄庡痉浜岀淮鏉＄爜鎭㈠鍑哄巶璁剧疆
	 */
	public static byte[] defaultSetting2D = new byte[] { 0x16, 0x4D, 0x0D,
			0x25, 0x25, 0x25, 0x44, 0x45, 0x46, 0x2E };

	/*
	 * 鏄庡痉浜岀淮鏉＄爜璁剧疆鏁版嵁鏍煎紡锛屽洖杞︽崲琛�
	 */
	public static byte[] dataTypeFor2D = new byte[] { 0x16, 0x4D, 0x0D, 0x38,
			0x32, 0x30, 0x32, 0x44, 0x30, 0x31, 0x2E };

	public static SerialPortHelper serialPort = null;

	public static String choosed_serial = "/dev/ttyMT0";// 涓插彛鍚嶇О
	public static int choosed_buad = 9600;// 娉㈢壒鐜�
	public static ScanGpio scanGpio = new ScanGpio();

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "CaptureService onBind");
		return null;
	}

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String barCodeStr = msg.obj.toString();
			////barCodeStr=barCodeStr.trim();
			if (beepManager != null) {
//				beepManager.playBeepSoundAndVibrate();//暂时扫描条码不播放声音
			}
			CaptureService.scanGpio.closeScan();
			Intent intentBroadcast = new Intent();
			intentBroadcast.setAction("com.zkc.scancode");
			intentBroadcast.putExtra("code", barCodeStr+"");
			context.sendBroadcast(intentBroadcast);
			barCodeStr = "";
		}

	};
 
	public static StringBuffer bytesToString(byte[] bytes, int size) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < size; i++) {
			String s = Integer.toHexString(bytes[i] & 0xff);
			if (s.length() < 2)
				sBuffer.append('0');
			sBuffer.append(s + " ");
		}
		return sBuffer;
	}

	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			// process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				Thread.sleep(10);
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "CaptureService onCreate");

	}

	@Override
	public int onStartCommand(Intent intent, int flag ,int startId) {
//		super.onStartCommand(intent, flag, startId);
		context = this;
		Log.v(TAG, "CaptureService onStart");

		beepManager = new ServiceBeepManager(this);
		beepManager.updatePrefs();

		CaptureService.serialPort = new SerialPortHelper(handler);
		serialPort.OpenSerialPort(choosed_serial, choosed_buad);

		scanGpio.openPower();

		screenStatusReceiver = new RemoteControlReceiver();
		IntentFilter screenStatusIF = new IntentFilter();
		screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
		screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
		screenStatusIF.addAction(Intent.ACTION_SHUTDOWN);
		// screenStatusIF.addAction("com.zkc.Receiver.RemoteControlReceiver");
		registerReceiver(screenStatusReceiver, screenStatusIF);

		sharedPreferences = getSharedPreferences("com.zkc.barcodescan",
				Activity.MODE_PRIVATE);
		if (sharedPreferences != null) {
			String str = CaptureService.sharedPreferences.getString("encoding",
					"");
			if (str != null && str != "") {
				CaptureService.str_encoding = str;
			}
		}
		SerialPort.CleanBuffer();
		
//		return startId;
		return super.onStartCommand(intent, flag, startId);
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "CaptureService onDestroy");
		scanGpio.closePower();
		if (serialPort != null) {
			serialPort.CloseSerialPort();
			serialPort = null;
		}
		unregisterReceiver(screenStatusReceiver);
		super.onDestroy();
	}

}
