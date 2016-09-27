package com.zl.dbmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import android.content.Context;
import android.util.Log;

public class ExcelUtils {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
			arial12font = new WritableFont(WritableFont.ARIAL, 12);
			arial12format = new WritableCellFormat(arial12font);
			arial12format.setAlignment(jxl.format.Alignment.CENTRE);
			arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		}
		catch (WriteException e) {

			e.printStackTrace();
		}
	}

	public static void initExcel(String fileName, String[] colName) {
		format();
		WritableWorkbook workbook = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
				workbook = Workbook.createWorkbook(file);
				WritableSheet sheet = workbook.createSheet("OutBoundSheet", 0);
				CellView cellView = new CellView();  cellView.setAutosize(true); //设置自动大小    
//				sheet.setColumnView(1, cellView);//根据内容自动设置列宽 
				sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
				for (int col = 0; col < colName.length; col++) {
					sheet.setColumnView(col, cellView);//根据内容自动设置列宽 
					sheet.addCell(new Label(col, 0, colName[col], arial10format));
				}
				workbook.write();
			}
//			workbook = Workbook.createWorkbook(file);
//			WritableSheet sheet = workbook.createSheet("OutBoundSheet", 0);
//			sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
//			for (int col = 0; col < colName.length; col++) {
//				sheet.addCell(new Label(col, 0, colName[col], arial10format));
//			}
//			workbook.write();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (workbook != null) {
				try {
					workbook.close();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

/**
 * 存储结果到Excel表并返回存储结果，存储成功为true，失败为false
 * @param objList 源数据，List<ArrayList<String>>类型
 * @param fileName 目标Excel文件
 * @param c 上下文Context
 * @return 是否存储成功
 */
	public static  boolean writeObjListToExcel(List<ArrayList<String>> objList, String fileName, Context c) {
		boolean result = false;
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName), workbook);
				WritableSheet sheet = writebook.getSheet(0);
				CellView cellView = new CellView();  cellView.setAutosize(true); //设置自动大小  
				Log.i("SheetColums", "记录表总列数为："+sheet.getColumns()+"列；记录表总行数为："+sheet.getRows()+"行；");
//				int sheetcolumns = sheet.getColumns();//获取总列数
				int sheetrows = sheet.getRows();//获取总行数
				for (int j = 0; j < objList.size(); j++) {	
					ArrayList<String> list=(ArrayList<String>) objList.get(j);
					for (int i = 0; i < list.size(); i++) {
						sheet.setColumnView(i, cellView);//设置自适应列宽
						sheet.addCell(new Label(i, j+sheetrows, list.get(i), arial12format));//鍦ㄥ垪琛ㄦ渶鍚庝竴琛屼箣鍚庢坊鍔?
					}
				}
				writebook.write();
//				Toast.makeText(c, "保存成功", Toast.LENGTH_SHORT).show();
				result = true;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (writebook != null) {
					try {
						writebook.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (in != null) {
					try {
						in.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return result;
	}
	
	/**
	 * 获取指定Excel表格的数据行数
	 * @param fileName 表格路径
	 * @return 读取到的表格数据行数，若读取失败则返回-10
	 */
	public static int getExcelRows(String fileName){
		int rows;
		InputStream in = null;
//		WritableWorkbook writebook = null;
		try{
//		WorkbookSettings setEncode = new WorkbookSettings();
//		setEncode.setEncoding(UTF8_ENCODING);
		in = new FileInputStream(new File(fileName));
		Workbook workbook = Workbook.getWorkbook(in);
//		writebook = Workbook.createWorkbook(new File(fileName), workbook);
//		WritableSheet sheet = writebook.getSheet(0);
//		CellView cellView = new CellView();  cellView.setAutosize(true); //设置自动大小  
//		Log.i("getSheetColums", "记录表总列数为："+sheet.getColumns()+"列；记录表总行数为："+sheet.getRows()+"行；");
		Sheet sheet = workbook.getSheet(0);
		rows = sheet.getRows();//获取总行数
		}catch(Exception e){
			e.printStackTrace();
			rows = -10;
		}finally {
//		if(writebook != null){
//			try {
//				writebook.close();
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//				rows = -10;
//			}
//		}
		if(in != null){
			try {
				in.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				rows = -10;
			}
		}
		}
		return rows;
	}
	
	
	public static boolean deleteLatestRow( String fileName){
		boolean isdeleted = false;
		
		WritableWorkbook writebook = null;
		InputStream in = null;
		try{
		WorkbookSettings setEncode = new WorkbookSettings();
		setEncode.setEncoding(UTF8_ENCODING);
		in = new FileInputStream(new File(fileName));
		Workbook workbook = Workbook.getWorkbook(in);
		writebook = Workbook.createWorkbook(new File(fileName), workbook);
		WritableSheet sheet = writebook.getSheet(0);
		int sheetrows = sheet.getRows();//获取总行数
		sheet.removeRow(sheetrows-1);//删除指定行
		writebook.write();
		if(writebook!=null){
		writebook.close();
		}
		if(in!=null){
		in.close();
		}
		isdeleted = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return isdeleted;
	}
	

	public static Object getValueByRef(Class<?> cls, String fieldName) {
		Object value = null;
		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase(Locale.CHINA));
		String getMethodName = "get" + fieldName;
		try {
			Method method = cls.getMethod(getMethodName);
			value = method.invoke(cls);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
