package com.zl.dbmail;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

public class DBSheetBuilder {
	private Context contex;
	private File file;
	private String[] title = {"Outbound_time", "SERIAL_NUMBER", "MO_NUMBER", "MODEL_NAME", "LAN_MAC", "BT_MAC", "Broadcast_CN", "WIFI_MAC", "Test_Time", "HDCP_Key14", "HDCP_Key20"};
	private DBHelper mDbHelper;
	private ArrayList<ArrayList<String>> mylist = new ArrayList<ArrayList<String>>();
	
	public DBSheetBuilder(Context ctx){
		this.contex = ctx;
//		mDbHelper = new DBHelper(contex);
		mDbHelper = DBHelper.getInstance(contex);//����ģʽ
		mDbHelper.open();//�����ݿ�
	}
	
	public void closeDB(){//�ر����ݿ�
		mDbHelper.close();
	}
	
	/**
	 * ����Զ�̷������ϻ�ȡ�Ľ���ȴ洢�ڱ��ط��������ٴ洢�ڱ���Excel�ļ�
	 * @param rs ��Զ�̷�������ȡ�Ĳ�ѯ���
	 * @param filename �洢��Ŀ��Excel�ļ����ļ���
	 * @return ���ش洢������洢�ɹ��򷵻�true��ʧ���򷵻�false
	 */
	public boolean saveData(ResultSet rs,String filename){
		boolean result = false;
		try{
		ContentValues values = new ContentValues();
		values.put("Outbound_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(new Date()));
		values.put("SERIAL_NUMBER", rs.getString("SERIAL_NUMBER"));
		values.put("MO_NUMBER", rs.getString("MO_NUMBER"));
		values.put("MODEL_NAME", rs.getString("MODEL_NAME"));
		values.put("LAN_MAC", rs.getString("LAN_MAC"));
		values.put("BT_MAC",rs.getString("BT_MAC"));
		values.put("Broadcast_CN", rs.getString("Braodcast_CN"));
		values.put("WIFI_MAC", rs.getString("WIFI_MAC"));
		values.put("Test_Time", rs.getString("Test_Time"));
		values.put("HDCP_Key14", rs.getString("HDCP_Key14"));
		values.put("HDCP_Key20", rs.getString("HDCP_Key20"));
		Cursor mCrusor = mDbHelper.exeSql("select * from family_bill where SERIAL_NUMBER = '"+rs.getString("SERIAL_NUMBER")+"'");
		if(mCrusor.getCount()==0){//�����ѯ���Ϊ�գ������ݿ��ڻ�δ���������Ϣʱ���룬�����ظ�����
		long insert = mDbHelper.insert("family_bill", values);
		if(insert>0){
			result = saveDataToExcelFroomDB(rs,filename);
		}
		}else{
			Log.i("INSERTDATA", "���ݿ����Ѵ��ڴ�����¼��");
			result = true;
		}
		mCrusor.close();
		}catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	/**
	 * �������ݿ�洢����������Ҫ���б���Excel����
	 * @param rs ֱ�����ݿ��ѯ���Ľ��
	 * @param batch ������Ϣ
	 * @return �Ƿ�洢�ɹ�
	 */
	public boolean saveDataWithBatch(ResultSet rs,String batch){
		boolean result = false;
		try{
		ContentValues values = new ContentValues();
		values.put("Outbound_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(new Date()));
		values.put("SERIAL_NUMBER", rs.getString("SERIAL_NUMBER"));
		values.put("MO_NUMBER", rs.getString("MO_NUMBER"));
		values.put("MODEL_NAME", rs.getString("MODEL_NAME"));
		values.put("LAN_MAC", rs.getString("LAN_MAC"));
		if(rs.getString("BT_MAC")==null){
			values.put("BT_MAC","0");
		}else{
		values.put("BT_MAC",rs.getString("BT_MAC"));
		}
		values.put("Broadcast_CN", rs.getString("Braodcast_CN"));
		values.put("WIFI_MAC", rs.getString("WIFI_MAC"));
		values.put("Test_Time", rs.getString("Test_Time"));
		values.put("HDCP_Key14", rs.getString("HDCP_Key14"));
		values.put("HDCP_Key20", rs.getString("HDCP_Key20"));
		values.put("batch", batch);//д�����κ�
		Cursor mCrusor = mDbHelper.exeSql("select * from family_bill where SERIAL_NUMBER = '"+rs.getString("SERIAL_NUMBER")+"'");
		if(mCrusor.getCount()==0){//�����ѯ���Ϊ�գ������ݿ��ڻ�δ���������Ϣʱ���룬�����ظ�����
		long insert = mDbHelper.insert("family_bill", values);
		if(insert>0){
//			result = saveDataToExcelFroomDB(rs,filename);
			result = true;//�������ݿ����ʱ����Ҫ����Excel���ش洢
		}
		}else{
			Log.i("INSERTDATA", "���ݿ����Ѵ��ڴ�����¼��");
			result = true;
		}
		mCrusor.close();
		}catch(SQLException e){
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	/**
	 * ������ͨ��WebService��ѯ���Ľ���洢���������ݿ�
	 * @param map ��ѯ���Ľ��
	 * @param batch ���κ�
	 * @return
	 */
	public boolean saveDataWithBatch_Map(Map<String,String> map,String batch){
		boolean result = false;
		ContentValues values = new ContentValues();
		values.put("Outbound_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(new Date()));
		values.put("SERIAL_NUMBER", map.get("SERIAL_NUMBER").replace(" ", ""));//����ո�;
		values.put("MO_NUMBER", map.get("MO_NUMBER").replace(" ", ""));
		values.put("MODEL_NAME", map.get("MODEL_NAME").replace(" ", ""));
		values.put("LAN_MAC", map.get("LAN_MAC").replace(" ", ""));
		if(map.get("BT_MAC")==null){
			values.put("BT_MAC","0");
		}else{
		values.put("BT_MAC",map.get("BT_MAC").replace(" ", ""));
		}
		values.put("Broadcast_CN", map.get("BRAODCAST_CN").replace(" ", ""));
		values.put("WIFI_MAC", map.get("WIFI_MAC").replace(" ", ""));
		values.put("Test_Time", map.get("TEST_TIME").replace(" ", ""));
		values.put("HDCP_Key14", map.get("HDCP_KEY14").replace(" ", ""));
		values.put("HDCP_Key20", map.get("HDCP_KEY20").replace(" ", ""));
		values.put("batch", batch);//д�����κ�
		Cursor mCrusor = mDbHelper.exeSql("select * from family_bill where SERIAL_NUMBER = '"+map.get("SERIAL_NUMBER").replace(" ", "")+"'");
		if(mCrusor.getCount()==0){//�����ѯ���Ϊ�գ������ݿ��ڻ�δ���������Ϣʱ���룬�����ظ�����
		long insert = mDbHelper.insert("family_bill", values);
		if(insert>0){
//			result = saveDataToExcelFroomDB(rs,filename);
			result = true;//�������ݿ����ʱ����Ҫ����Excel���ش洢
		}
		}else{
			Log.i("INSERTDATA", "���ݿ����Ѵ��ڴ�����¼��");
			result = true;
		}
		mCrusor.close();
		return result;
	}
	
	/**
	 * ��SN�����ɾ��
	 * @param str ָ����SN��
	 * @return
	 */
	public int deleteFromDB(String SN){
		return mDbHelper.delete("family_bill", SN);
	}
	
	/**
	 * �����ݿ����SQL����
	 * @param sql
	 * @return
	 */
	public Cursor exeSQL(String sql){
		return mDbHelper.exeSql(sql);
	}
	
	/**
	 * �����κŽ��б������ݿ�����ɾ��
	 * @param batch ���κ�
	 * @return ��Ӱ�������
	 */
	public int deleteFromDBWithBatch(String batch){
		return mDbHelper.deleteWithBatch("family_bill", batch);
	}
	
	/**
	 * ��Զ�̷�������ѯ�����Ľ���洢�ڱ���Excel�ļ�
	 * @param rs Զ�̷������Ĳ�ѯ���
	 * @param filename �洢Ŀ���ļ����ļ�����
	 * @return �Ƿ�洢�ɹ����洢�ɹ��򷵻�true��ʧ���򷵻�false
	 */
	public boolean saveDataToExcelFroomDB(ResultSet rs,String filename){
		boolean isSaved = false;
		
		mylist.clear();
		ArrayList<String> beanList=new ArrayList<String>();
		try{
		beanList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(new Date()));
		beanList.add(rs.getString("SERIAL_NUMBER"));
		beanList.add(rs.getString("MO_NUMBER"));
		beanList.add(rs.getString("MODEL_NAME"));
		beanList.add(rs.getString("LAN_MAC"));
		beanList.add(rs.getString("BT_MAC"));
		beanList.add(rs.getString("Braodcast_CN"));
		beanList.add(rs.getString("WIFI_MAC"));
		beanList.add(rs.getString("Test_Time"));
		beanList.add(rs.getString("HDCP_Key14"));
		beanList.add(rs.getString("HDCP_Key20"));
		mylist.add(beanList);
		}catch(SQLException e){
			e.printStackTrace();
			isSaved = false;
		}
		if(!mylist.isEmpty()){
			boolean save1 = saveDataToExcel(mylist,filename);
			boolean save2 = saveTotalDataToExcel(mylist);
			isSaved = save1&&save2;
			if((!save1)&&save2){
				deleteLatestRowFromTotalData();
			}else if(save1&&(!save2)){
				if(deleteLatestRowFromData(filename)){
				Log.i("DELETEROW", "ɾ���ֱ����ݳɹ���");
				}else{
					Log.i("DELETEROW", "ɾ���ֱ�����ʧ�ܣ�");
				}
			}else if((!save1)&&(!save2)){
				deleteLatestRowFromTotalData();
				deleteLatestRowFromData(filename);
			}
		}
		
		return isSaved;
	}
	
	public boolean saveDataToExcel(ArrayList<ArrayList<String>> list,String filename) {
		file = new File(getSDPath() + File.separator+"ScanResultData");
		makeDir(file);
		File f = new File(file.toString() + File.separator+filename);
		if(f.exists()){//���Ŀ���ļ��Ѵ�������ɾ���ļ��ٴ�����������ԭExcel���֮���������
			f.delete();
		}
		ExcelUtils.initExcel(file.toString() + File.separator+filename, title);
//		ExcelUtils.writeObjListToExcel(getBillData(), getSDPath() + "/Family/bill.xls", this);
		boolean result = ExcelUtils.writeObjListToExcel(list, getSDPath() + File.separator+"ScanResultData"+File.separator+filename, contex);
		return result;
	}
	
	public boolean saveTotalDataToExcel(ArrayList<ArrayList<String>> list) {
		file = new File(getSDPath() + File.separator+"ScanResultData"+File.separator+"Total");
		makeDir(file);
		ExcelUtils.initExcel(file.toString() + File.separator+"total.xls", title);
//		ExcelUtils.writeObjListToExcel(getBillData(), getSDPath() + "/Family/bill.xls", this);
		boolean result = ExcelUtils.writeObjListToExcel(list, getSDPath() + File.separator+"ScanResultData"+File.separator+"Total"+File.separator+"total.xls", contex);
		return result;
	}
	
	/**
	 * ��ȡָ��Excel�����������
	 * @param filename
	 * @return ��������������ȡʧ���򷵻�-10
	 */
	public int getRowsFromExcel(String filename){
//		ExcelUtils.initExcel(getSDPath() + File.separator+"ScanResultData"+File.separator+filename, title);
		File f = new File(getSDPath() + File.separator+"ScanResultData"+File.separator+filename);
		if(f.exists()){
		return ExcelUtils.getExcelRows(getSDPath() + File.separator+"ScanResultData"+File.separator+filename)-1;//����ġ�-1���Ǽ�ȥExcelͷ��title����������
		}else{
			return 0;
		}
		}
	
	/**
	 * ɾ���ܱ�������һ��
	 * @return
	 */
	public boolean deleteLatestRowFromTotalData(){//ɾ���ܱ�������һ��
		return ExcelUtils.deleteLatestRow(getSDPath() + File.separator+"ScanResultData"+File.separator+"Total"+File.separator+"total.xls");
	}
	
	public int getScannedSize(String batchname){
		Cursor mCursor = mDbHelper.exeSql("select * from family_bill where batch =  '"+batchname+"'");
		return mCursor.getCount();
	}
	
	/**
	 * ɾ���ֱ�������һ��
	 * @param filename
	 * @return
	 */
	public boolean deleteLatestRowFromData(String filename){//ɾ���ֱ�������һ��
		return ExcelUtils.deleteLatestRow( getSDPath() + File.separator+"ScanResultData"+File.separator+filename);
	}
	
	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		String dir = sdDir.toString();
		return dir;

	}
	

}
