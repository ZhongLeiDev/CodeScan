package com.zkc.pc700.helper;


import com.zkc.io.EmGpio;

public class ScanGpio {

	// 打开电源
	public void openPower() {
		try {
			if (true == EmGpio.gpioInit()) {
				// 电源调低
				EmGpio.setGpioOutput(111);
				EmGpio.setGpioDataLow(111);
				Thread.sleep(100);
				// 电源调高
				EmGpio.setGpioOutput(111);
				EmGpio.setGpioDataHigh(111);
				Thread.sleep(100);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void closePower() {
		try {
			if (true == EmGpio.gpioInit()) {
				// 电源调低
				EmGpio.setGpioOutput(111);
				EmGpio.setGpioDataLow(111);
				Thread.sleep(100);
				EmGpio.setGpioInput(111);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 打开扫描
	public void openScan() {
		// 开启扫描
		try {
			if (true == EmGpio.gpioInit()) {
				EmGpio.setGpioOutput(110);
				EmGpio.setGpioDataHigh(110);
				Thread.sleep(100);
				EmGpio.setGpioDataLow(110);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// 关闭扫描
	public void closeScan() {
		// 开启扫描
		try {
			if (true == EmGpio.gpioInit()) {
				EmGpio.setGpioOutput(110);
				EmGpio.setGpioDataHigh(110);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}