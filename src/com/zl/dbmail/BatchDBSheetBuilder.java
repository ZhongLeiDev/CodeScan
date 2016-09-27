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
	
	public void closeDB(){//�ر����ݿ�
		mBatchDbHelper.close();
	}
	
	/**
	 * ���±����������ݿ��ڵ�ɨ����ɱ�־λ
	 * @param batchname ��������
	 * @param flag ɨ���־λ
	 * @return ��Ӱ�������
	 */
	public int updateFlag(String batchname,String flag){
		return mBatchDbHelper.updateFlag(batchname, flag);
	}
	
	/**
	 * ɾ��δ��ɵ�����
	 * @return
	 */
	public int deleteUnFinished(){
		return mBatchDbHelper.deleteUnFinished();
	}
	
	/**
	 * �����ݿ�д��������Ϣ
	 * @param batchname ��������
	 * @param batchsize ������������
	 * @return д���Ƿ�ɹ�
	 */
	public boolean saveBatch(String batchname,String batchsize){
		boolean isSaved = false;
		ContentValues values = new ContentValues();
		values.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).format(new Date()));
		values.put("batch_name",batchname);
		values.put("batch_size", batchsize);
		values.put("scan_flag", "NO");
		Cursor mCrusor = mBatchDbHelper.exeSql("select * from batch_list where batch_name = '"+batchname+"'");
		if(mCrusor.getCount()==0){//�����ѯ���Ϊ�գ������ݿ��ڻ�δ���������Ϣʱ���룬�����ظ�����
			long insert = mBatchDbHelper.insert("batch_list", values);
			if(insert>0){
				isSaved = true;
			}
			}else{
				Log.i("INSERTDATA", "���ݿ����Ѵ��ڴ�����¼��");
			}
			mCrusor.close();
		return isSaved;
	}
	
	/**
	 * ��ȡ���µ����κţ�����ǰ�༭������
	 * @return ����Ϊ3��List������list.get(0)�õ�����ʱ�䣬list.get(1)�õ��������ƣ�list.get(2)�õ�ɨ����������ֵ��
	 * ��ȡ�õ�ֵΪ��EMPTY��ʱ��˵�����ݿ��ڻ�û�м�¼
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
			Log.i("BatchList", "�����б�Ϊ�գ�" );
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
			Log.i("BatchList", "�����б�Ϊ�գ�" );
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
