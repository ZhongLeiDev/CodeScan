package android.serialport.api;


public class SerialPortClass{
	//public static SerialPortHelper serialPortHelper = new SerialPortHelper();
	public static String choosed_serial = "/dev/ttyMT0";
	public static int choosed_buad = 115200;
	
	
	protected static final String TAG = "SerialPortClass";
	public static SERIALPORT serialPortName = SERIALPORT.comInit;

	public static enum SERIALPORT {
		comInit,comPrinter, comScan, comOutSide,
	};
	public static byte[] bt_printer = new byte[] { 0x1b, 0x26, 00 };
	public static byte[] bt_scan = new byte[] { 0x1b, 0x26, 01 };
	public static byte[] bt_outside = new byte[] { 0x1b, 0x26, 02 };
}
