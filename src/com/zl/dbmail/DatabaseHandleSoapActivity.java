package com.zl.dbmail;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.zkc.barcodescan.R;
import com.zl.showlist.QueryResult;
import com.zl.showlist.QueryResultAdapter;
import com.zl.soap.DBUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseHandleSoapActivity extends Activity{
	private DBHelper mDbHelper;
	private ListView lv;
	private TextView edtstart,edtstop,date,tvshowdetail;
	private Button btnquery,btnclear;
	private List<QueryResult> queryresultlist = new ArrayList<QueryResult>();
	private QueryResultAdapter ada;
	private boolean isstart = false;
	private boolean isqueryandrebuild = false;
	private File file;
	private String excelpath = DBSheetBuilder.getSDPath() + File.separator+"ScanResultData"+File.separator+"Query"+File.separator+"query.xls";
	private String[] title = {"Outbound_time", "SERIAL_NUMBER", "MO_NUMBER", "MODEL_NAME", "LAN_MAC", "BT_MAC", "Broadcast_CN", "WIFI_MAC", "Test_Time", "HDCP_Key14", "HDCP_Key20"};//Excel表表头
	private ArrayList<ArrayList<String>> mylist = new ArrayList<ArrayList<String>>();//数据存储列表
	private static final String Properfile = Environment.getExternalStorageDirectory()+ File.separator+"ScanResultData"+File.separator+"Config"+File.separator+"config.dat";//联系人列表
	private String[ ] receiver = new String[ ]{ };
	private String batchname = "L123";
	private ProgressDialog mydialog;
	
	private BatchDBSheetBuilder batchbuilder;
	private String[ ] batchunlock = new String[]{};
	
	private DBUtils dbu;
	
	@SuppressLint("HandlerLeak") 
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x01){
				ada.notifyDataSetChanged();
				tvshowdetail.setText("数据库初始化查询成功，只显示前100条数据！");
				tvshowdetail.setTextColor(Color.BLACK);
			}else if(msg.what == 0x02){
				ada.notifyDataSetChanged();
				tvshowdetail.setText("已查询到"+String.valueOf((Integer)msg.obj)+"条数据！");
				tvshowdetail.setTextColor(Color.BLACK);
				
				if(isqueryandrebuild){//将数据存储放到主线程进行，其实效果都差不多
					if(saveQueryDataToExcel(mylist)){//Excel生成成功
						Message msg1 = Message.obtain(mhandler);
						msg1.what = 0x03;
						msg1.sendToTarget();
					}else{
						Message msg1 = Message.obtain(mhandler);
						msg1.what = 0x04;
						msg1.sendToTarget();
					}
					}
				
			}else if(msg.what == 0x03){
				tvshowdetail.append("Excel生成成功！");
				tvshowdetail.setTextColor(Color.GREEN);
			}else if(msg.what == 0x04){
				tvshowdetail.append("Excel生成失败！");
				tvshowdetail.setTextColor(Color.RED);
			}else if(msg.what == 0x05){
				tvshowdetail.setText(batchname+"批次存储成功！");
				tvshowdetail.setTextColor(Color.GREEN);
				mydialog.cancel();
			}else if(msg.what == 0x06){
				tvshowdetail.setText(batchname+"批次存储失败！");
				tvshowdetail.setTextColor(Color.RED);
				mydialog.cancel();
			}else if(msg.what == 0x07){
				tvshowdetail.setText(batchname+"批次查询结果为空！");
				tvshowdetail.setTextColor(Color.RED);
				mydialog.cancel();
			}else if(msg.what == 0x10){
				tvshowdetail.setText("撤销操作顺利完成！");
				tvshowdetail.setTextColor(Color.GREEN);
				mydialog.cancel();
			}else if(msg.what == 0x11){
				tvshowdetail.setText("撤销操作失败！");
				tvshowdetail.setTextColor(Color.RED);
				mydialog.cancel();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.database_layout);
		mDbHelper = new DBHelper(this);
		mDbHelper.open();//打开数据库
		
		dbu = DBUtils.getInstance();//以单例模式初始化dbu
		
		batchbuilder = new BatchDBSheetBuilder(this);
		batchunlock = batchbuilder.getBatchList();
		
		//--------------------------等待框---------------------------
				mydialog = new ProgressDialog(this);
				mydialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mydialog.setTitle("等待操作完成");
				mydialog.setMessage("正在处理，请稍候...");
				mydialog.setCancelable(true);
		
		lv = (ListView)findViewById(R.id.listView1);
		edtstart = (TextView)findViewById(R.id.start);
		edtstop = (TextView)findViewById(R.id.stop);
		tvshowdetail = (TextView)findViewById(R.id.tvshowdetail);
		btnquery = (Button)findViewById(R.id.query);
		btnclear = (Button)findViewById(R.id.clear);
		edtstart.setOnClickListener(click);
		edtstop.setOnClickListener(click);
		btnquery.setOnClickListener(click);
		btnclear.setOnClickListener(click);
		
//		btnquery.setVisibility(View.GONE);//隐藏查询按钮，显示清空按钮
		
		queryresultlist.add(new QueryResult("出库时间", "SN码"));
		ada = new QueryResultAdapter(this, queryresultlist);
		lv.setAdapter(ada);
		
		isstart = true;
		new Thread(queryRunnable).start();
		
		File pf = new File(Properfile);//初始化properfile文件
		if(pf.exists()){
		Properties properties = ConfigActivity.loadConfig(this, Properfile);
		String str = (String)properties.get("members");
		if(!str.equals("L")){//由于序列化时是以“L”为起始位，故当list内容为空时反序列化时依然会存在“L”这个字符
		List<Object> templist = StringTransUtils.StringToList(str);
		receiver = new String[templist.size()];
		for(int i=0;i<templist.size();i++){
			receiver[i]=(String)templist.get(i);
		}
		}
		}
	
	}
	
	private OnClickListener click = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnquery){
				if((btnquery.getText().toString().equals("查询结果并存储"))||(btnquery.getText().toString().equals("仅查询结果"))){
				if((!edtstart.getText().toString().equals("开始时间"))&&(!edtstop.getText().toString().equals("结束时间"))){
					if((edtstart.getText().toString()+" 00:00:00").compareTo(edtstop.getText().toString()+" 23:59:59")<0){//判断starttime是否小于stoptime
						new Thread(queryRunnable).start();
					}else{
						Log.i("TIMECOMPARE", "开始时间大于或等于结束时间！");
						tvshowdetail.setText("开始时间大于或等于结束时间！");
						tvshowdetail.setTextColor(Color.RED);
					}
				}else{
					Log.i("TIMEEDIT", "填写的时间段不完整！");
					tvshowdetail.setText("填写的时间段不完整！");
					tvshowdetail.setTextColor(Color.RED);
				}
				}else if(btnquery.getText().toString().equals("发送邮件")){
					sendEmail();
				}else if(btnquery.getText().toString().equals("批次查询并存储")){
					showBatchQueryDialog();
				}else if(btnquery.getText().toString().equals("批次撤销")){
					showRevocationDialog();
				}
			}else if(v == btnclear){
				//-------------------------------->对用户隐藏，避免误操作<-------------------------------------
//				int number = mDbHelper.deleteAll("family_bill");
//				Toast.makeText(DatabaseHandle.this, "删除数据："+number, Toast.LENGTH_SHORT).show();
			}else if(v == edtstart){
				date = edtstart;
				getDialog().show();
			}else if(v == edtstop){
				date = edtstop;
				getDialog().show(); 
			}
			
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//菜单选择
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, R.string.query_1);
		menu.add(0, 2, 2, R.string.query_2);
		menu.add(0, 3, 3, R.string.query_3);
		menu.add(0, 4, 4, R.string.query_4);
		menu.add(0, 5, 5, R.string.query_5);
		return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == 1) {
			isqueryandrebuild = false;
			btnquery.setText("仅查询结果");
		} else if (item.getItemId() == 2) {
		isqueryandrebuild = true;
		btnquery.setText("查询结果并存储");
		initExcelFile();
		} else if (item.getItemId() == 3) {
			btnquery.setText("发送邮件");
		} else if (item.getItemId() == 4) {
			btnquery.setText("批次查询并存储");
		} else if (item.getItemId() == 5) {
			btnquery.setText("批次撤销");
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void sendEmail(){//发送邮件
    	Intent email = new Intent(android.content.Intent.ACTION_SEND);  
    	// 附件    
    	 File file = new File(excelpath);
    	 Log.i("FilePath",file.getAbsolutePath());
    	 if(file.exists()){
    	//邮件发送类型：带附件的邮件  
    	email.setType("application/octet-stream");
    	 //邮件接收者（数组，可以是多位接收者）  
//    	String[] emailReciver = new String[]{"zhonglei@tcl.com"};
    	String[] emailReciver = receiver;
    	
    	if(receiver.length != 0){
    	  
    	String  emailTitle = "出库扫描结果列表";  
    	String emailContent = "附件为出库扫描的结果表格，请查收。";  
    	//设置邮件地址  
    	email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);  
    	//设置邮件标题  
    	 email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);  
    	//设置发送的内容  
    	email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);  
    	//附件  
    	email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));  
    	 //调用系统的邮件系统  
    	startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
    	}else{
    		Toast.makeText(DatabaseHandleSoapActivity.this, "收件人未指定！", Toast.LENGTH_SHORT).show();
    	}
    	 }else{
    		 Toast.makeText(DatabaseHandleSoapActivity.this, "指定附件不存在，请先查询生成表格后再发送！", Toast.LENGTH_SHORT).show();
    	 }
    }
	
	
	
	private Runnable queryRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			queryresultlist.clear();
			queryresultlist.add(new QueryResult("出库时间", "SN码"));
			if(isstart){
				Cursor cur = mDbHelper.exeSql("select * from family_bill limit 100");
				while (cur.moveToNext()) {
					QueryResult qr = new QueryResult(cur.getString(1), cur.getString(2));
					queryresultlist.add(qr);
				}
				isstart = false;
				cur.close();
//				ada.notifyDataSetChanged();
				Message msg = Message.obtain(mhandler);
				msg.what = 0x01;
				msg.sendToTarget();
			}else{
				if(isqueryandrebuild){
				mylist.clear();//清空存储列表
				}
				Cursor cur = mDbHelper.exeSql("select * from family_bill where Outbound_time > '"+edtstart.getText().toString()+" 00:00:00"+"' and Outbound_time < '"+edtstop.getText().toString()+" 23:59:59"+"'");
				int number = cur.getCount();
				while (cur.moveToNext()) {
					QueryResult qr = new QueryResult(cur.getString(1), cur.getString(2));
					queryresultlist.add(qr);
					if(isqueryandrebuild){
					setDataToCell(cur);
					}
				}
				cur.close();
//				ada.notifyDataSetChanged();
				Message msg = Message.obtain(mhandler);
				msg.what = 0x02;
				msg.obj = number;
				msg.sendToTarget();
				Log.i("QUERYRESULT", "已获取查询结果！");
//				if(isqueryandrebuild){
//				if(saveQueryDataToExcel(mylist)){//Excel生成成功
//					Message msg1 = Message.obtain(mhandler);
//					msg1.what = 0x03;
//					msg1.sendToTarget();
//				}else{
//					Message msg1 = Message.obtain(mhandler);
//					msg1.what = 0x04;
//					msg1.sendToTarget();
//				}
//				}
			}
			
		}
		
	};
	
	private Runnable batchQuery = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Cursor cur = mDbHelper.exeSql("select * from family_bill where batch = '"+batchname+"'");
			int count = cur.getCount();
			if(count>0){
				mylist.clear();
			while(cur.moveToNext()){
				setDataToCell(cur);
			}
			if(saveBatchDataToExcel(mylist)){
				Message msg = Message.obtain(mhandler);
				msg.what = 0x05;//批次存储成功
				msg.obj = count;
				msg.sendToTarget();
			}else{
				Message msg = Message.obtain(mhandler);
				msg.what = 0x06;//批次存储失败
				msg.sendToTarget();
			}
			}else{
				Message msg = Message.obtain(mhandler);
				msg.what = 0x07;//批次查询结果为空
				msg.sendToTarget();
			}
			cur.close();
		}
		
	};
	
	private Runnable batchRevocation = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if(dbu.setDefaultOutbondState(batchname)){
				 mDbHelper.deleteWithBatch("family_bill", batchname);
				 Message msg = Message.obtain(mhandler);
				 msg.what = 0x10;//远程数据库操作成功
				 msg.sendToTarget();
			}else{
				 Message msg = Message.obtain(mhandler);
				 msg.what = 0x11;//远程数据库操作失败
				 msg.sendToTarget();
			}
				 
		}
		
	};
	
	private void showBatchQueryDialog(){
		
		final EditText edtinput = new EditText(DatabaseHandleSoapActivity.this); 
		
		new AlertDialog.Builder(DatabaseHandleSoapActivity.this).setIcon(android.R.drawable.btn_star)  
	    .setTitle("按批次查询并生成Excel表")
	    .setIcon(android.R.drawable.btn_star)
	    .setMessage("手动输入批次号：")
	    .setView(edtinput)
	    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog,int which) { 
	                    // TODO Auto-generated method stub  
	                	 String batch = edtinput.getText().toString();
	                	 if(!batch.isEmpty()){
	                		 batchname = batch;
	                		 if(isUnlockedAndSuite(batch)){
	                		 mydialog.show();
	                		 new Thread(batchQuery).start();
	                		 }else{
	                			 Toast.makeText(DatabaseHandleSoapActivity.this, "输入批次已锁定或不存在！", Toast.LENGTH_SHORT).show();
	                		 }
	                	 }else{
	                		 Toast.makeText(DatabaseHandleSoapActivity.this, "请输入批次号！", Toast.LENGTH_SHORT).show();
	                	 }
	                
	                }
	            })  
	    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog,int which) {
	                    // TODO Auto-generated method stub  
	                    
	                }  
	            }).show();// show很关键   
	}
	
private void showRevocationDialog(){
		
		final EditText edtinput = new EditText(DatabaseHandleSoapActivity.this); 
		
		new AlertDialog.Builder(DatabaseHandleSoapActivity.this).setIcon(android.R.drawable.btn_star)  
	    .setTitle("按批次撤销操作")
	    .setIcon(android.R.drawable.btn_star)
	    .setMessage("手动输入批次号：")
	    .setView(edtinput)
	    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog,int which) { 
	                    // TODO Auto-generated method stub  
	                	String batch = edtinput.getText().toString();
	                	if(!batch.isEmpty()){
	                	batchname = batch;
	                	if(isUnlockedAndSuite(batch)){
	                	mydialog.show();
	                	new Thread(batchRevocation).start();
	                	}else{
	                		Toast.makeText(DatabaseHandleSoapActivity.this, "输入批次已锁定或不存在！", Toast.LENGTH_SHORT).show();
	                	}
	                	}else{
	                		 Toast.makeText(DatabaseHandleSoapActivity.this, "请输入批次号！", Toast.LENGTH_SHORT).show();
	                	}
	                }
	            })  
	    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog,int which) {
	                    // TODO Auto-generated method stub  
	                    
	                }  
	            }).show();// show很关键   
	}
	
	private void setDataToCell(Cursor cur){
		ArrayList<String> beanList=new ArrayList<String>();
		beanList.add(cur.getString(1));
		beanList.add(cur.getString(2));
		beanList.add(cur.getString(3));
		beanList.add(cur.getString(4));
		beanList.add(cur.getString(5));
		beanList.add(cur.getString(6));
		beanList.add(cur.getString(7));
		beanList.add(cur.getString(8));
		beanList.add(cur.getString(9));
		beanList.add(cur.getString(10));
		beanList.add(cur.getString(11));
		mylist.add(beanList);
	}
	
	public boolean saveQueryDataToExcel(ArrayList<ArrayList<String>> list) {
		file = new File(DBSheetBuilder.getSDPath() + File.separator+"ScanResultData"+File.separator+"Query");
		DBSheetBuilder.makeDir(file);
		ExcelUtils.initExcel(file.toString() + File.separator+"query.xls", title);
		boolean result = ExcelUtils.writeObjListToExcel(list, excelpath, getApplicationContext());
		return result;
	}
	
	public boolean saveBatchDataToExcel(ArrayList<ArrayList<String>> list) {
		file = new File(DBSheetBuilder.getSDPath() + File.separator+"ScanResultData"+File.separator+"Query");
		DBSheetBuilder.makeDir(file);
		String fpath = file.toString() + File.separator+batchname.replace("\n", "a")+".xls";//将批次号中存在的回车符替换为a,曾经出现的一个BUG，现在已经完善
		File file = new File(fpath);
		if(file.exists()){//若文件已存在，则先删除，然后再新建文件，避免在文件后面添加数据
			file.delete();
		}
		ExcelUtils.initExcel(fpath, title);
		boolean result = ExcelUtils.writeObjListToExcel(list, fpath, getApplicationContext());
		return result;
	}
	
	private void initExcelFile(){
		File f = new File(excelpath);
		if(f.exists()){
			f.delete();
		}
	}
	
	private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {
	        int mYear = year;
	        String mm;
	        String dd;

	        int mMonth = monthOfYear + 1;
	        mm = String.valueOf(mMonth);
	        if (mm.length() < 2)
	            mm = "0" + mm;

	        int mDay = dayOfMonth;
	        dd = String.valueOf(mDay);
	        if (dd.length() < 2)
	            dd = "0" + dd;

	        date.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
	    }
	};

	@Override
	protected Dialog onCreateDialog(int id) {
	     final Calendar c = Calendar.getInstance();
	     int mYear = c.get(Calendar.YEAR);
	     int mMonth = c.get(Calendar.MONTH);
	     int mDay = c.get(Calendar.DAY_OF_MONTH);

	    switch (id) {
	    case 0:
	        return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
	                mDay);
	    }
	    return null;
	}
	
	private Dialog getDialog(){
		 final Calendar c = Calendar.getInstance();
	     int mYear = c.get(Calendar.YEAR);
	     int mMonth = c.get(Calendar.MONTH);
	     int mDay = c.get(Calendar.DAY_OF_MONTH);
	     
         return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
	                mDay);
	
	}
	
	private boolean isUnlockedAndSuite(String str){//判断是否为非锁定批次并且批次已存在
		boolean result =false;
		for(int i = 0;i<batchunlock.length;i++){
			if(batchunlock[i].equals(str)){
				result = true;
			}
		}
		return result;
	}
	

}
