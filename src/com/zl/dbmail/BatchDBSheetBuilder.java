package com.zl.dbmail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class BatchDBSheetBuilder {
	private Context contex;
	private BatchDBHelper mBatchDbHelper = null;
	
	public BatchDBSheetBuilder(Context ctx){
		this.contex = ctx;
		if (mBatchDbHelper == null) { 
//			mBatchDbHelper = new BatchDBHelper(ctx); 
			mBatchDbHelper = BatchDBHelper.getInstance(contex);
			mBatchDbHelper.open();
		} 
	}
	
//	public synchronized BatchDBHelper getInstance(Context context) {
//		if (mBatchDbHelper == null) { 
//			mBatchDbHelper = new BatchDBHelper(context); 
//			mBatchDbHelper.open();
//		} 
//		return mBatchDbHelper; 
//		}
	
	public void closeDB(){//关闭数据库
		mBatchDbHelper.close();
	}
	
	/**
	 * 更新本地批次数据库内的扫描完成标志位
	 * @param batchname 批次名称
	 * @param flag 扫描标志位
	 * @return 受影响的行数
	 */
	public int updateFlag(String batchname,String flag){
		return mBatchDbHelper.updateFlag(batchname, flag);
	}
	
	/**
	 * 删除未完成的批次
	 * @return
	 */
	public int deleteUnFinished(){
		return mBatchDbHelper.deleteUnFinished();
	}
	
	/**
	 * 向数据库写入批次信息
	 * @param batchname 批次名称
	 * @param batchsize 批次数量限制
	 * @return 写入是否成功
	 */
	public boolean saveBatch(String batchname,String batchsize){
		boolean isSaved = false;
		ContentValues values = new ContentValues();
		values.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(new Date()));
		values.put("batch_name",batchname);
		values.put("batch_size", batchsize);
		values.put("scan_flag", "NO");
		Cursor mCrusor = mBatchDbHelper.exeSql("select * from batch_list where batch_name = '"+batchname+"'");
		if(mCrusor.getCount()==0){//如果查询结果为空，即数据库内还未插入此条信息时插入，避免重复插入
			long insert = mBatchDbHelper.insert("batch_list", values);
			if(insert>0){
				isSaved = true;
			}
			}else{
				Log.i("INSERTDATA", "数据库中已存在此条记录！");
			}
			mCrusor.close();
		return isSaved;
	}
	
	/**
	 * 获取最新的批次号，即当前编辑的批次
	 * @return 长度为3的List，其中list.get(0)得到创建时间，list.get(1)得到批次名称，list.get(2)得到扫描数量限制值，
	 * 当取得的值为“EMPTY”时则说明数据库内还没有记录
	 */
	public List<String> getLatestBatch(){
		List<String> batchlist = new ArrayList<String>();
		Cursor mCursor = mBatchDbHelper.exeSql("select * from batch_list ");
		if(mCursor.moveToLast()){
		batchlist.add(mCursor.getString(1));
		batchlist.add(mCursor.getString(2));
		batchlist.add(mCursor.getString(3));
		batchlist.add(mCursor.getString(4));
		}else{
			Log.i("BatchList", "批次列表为空！" );
			batchlist.add("EMPTY");
			batchlist.add("EMPTY");
			batchlist.add("EMPTY");
			batchlist.add("EMPTY");
		}
		mCursor.close();
		return batchlist;
	}
	
	public List<String> getCurrentBatch(String batch){
		List<String> batchlist = new ArrayList<String>();
		Cursor mCursor = mBatchDbHelper.exeSql("select * from batch_list where batch_name = '"+batch+"'");
		if(mCursor.getCount() == 1){
			while(mCursor.moveToNext()){
		batchlist.add(mCursor.getString(1));
		batchlist.add(mCursor.getString(2));
		batchlist.add(mCursor.getString(3));
		batchlist.add(mCursor.getString(4));
			}
		}else{
			Log.i("BatchList", "批次列表为空！" );
			batchlist.add("EMPTY");
			batchlist.add("EMPTY");
			batchlist.add("EMPTY");
			batchlist.add("EMPTY");
		}
		mCursor.close();
		return batchlist;
	}
	
	public String[] getBatchList(){
		int size = 0;
		Cursor mCursor = mBatchDbHelper.exeSql("select * from batch_list where scan_flag = 'NO'");
		size = mCursor.getCount();
		String [] ss= new String[size];
		int i = 0;
		while(mCursor.moveToNext()){
		ss[i] = mCursor.getString(2);	
		i++;
		}
		mCursor.close();
			return ss;
	}

}
