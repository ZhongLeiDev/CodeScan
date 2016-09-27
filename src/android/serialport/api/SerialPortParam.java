package android.serialport.api;

public class SerialPortParam {
	public static String Name="";
	public static String Path="";
	public static int Baudrate=115200;
	public static int DataBits=8;
	public static int StopBits=1;
	public static int Parity='n';
	public static int SpaceTime=0;
	/**
	 * Flowcontrol
	 * 0 无流控
	 * 1硬件流控
	 * 2软流控
	 */
	public static int Flowcontrol = 0;
	public static enum ParityEnum{
		n,
		o,
		e,
		s,
	}
	
}
