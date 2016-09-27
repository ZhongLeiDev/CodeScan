package android.serialport.api;

import android.os.Handler;
import android.serialport.api.SerialPort;
import android.util.Log;

public class SerialPortHelper {

	protected static final String TAG = "SerialPortHelper";
	protected SerialPort mSerialPort;
	SerialPortDataReceived serialportDataReceived = null;

	public SerialPortHelper(Handler _msgHandler) {
		mSerialPort = new SerialPort(_msgHandler);
	}

	public void setOnserialportDataReceived(
			SerialPortDataReceived _serialportDataReceived) {
		this.serialportDataReceived = _serialportDataReceived;
	}

	public Boolean OpenSerialPort(String device, int baudrate) {

		try {
			mSerialPort.open(device, baudrate);
		} catch (SecurityException e) {
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	public Boolean Write(String str) {
		byte[] buffer = str.getBytes();
		return Write(buffer);
	}

	public Boolean Write(byte[] buffer) {

		int sendSize = 500;
		if (buffer.length <= sendSize) {
			mSerialPort.Write(buffer);
			return true;
		}
		for (int j = 0; j < buffer.length; j += sendSize) {

			byte[] btPackage = new byte[sendSize];
			if (buffer.length - j < sendSize) {
				btPackage = new byte[buffer.length - j];
			}
			System.arraycopy(buffer, j, btPackage, 0, btPackage.length);
			mSerialPort.Write(btPackage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}

	public Boolean CloseSerialPort() {
		if (mSerialPort == null) {
			return true;
		}
		return mSerialPort.closePort();
	}

	public static String byte2HexStr(byte[] b, int lenth) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < lenth; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}

}
