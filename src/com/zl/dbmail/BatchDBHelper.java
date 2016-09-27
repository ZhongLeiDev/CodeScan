package com.zl.dbmail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BatchDBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "ldm_batch"; // DB name
	private Context mcontext;
	private static BatchDBHelper mBatchDbHelper;
	private SQLiteDatabase db;

	public BatchDBHelper(Context context) {
		super(context, DB_NAME, null, 13);//���ݿ�汾����11-->12����ʼ��ʱ�����onUpgrade����
		this.mcontext = context;
	}
	
	public synchronized static BatchDBHelper getInstance(Context context) { //����ģʽ��ȡ���ݿ����
		if (mBatchDbHelper == null) { 
			mBatchDbHelper = new BatchDBHelper(context); 
		}
		return mBatchDbHelper; 
		};

	public BatchDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	/**
	 * �û���һ��ʹ�����ʱ���õĲ��������ڻ�ȡ���ݿⴴ����䣨SW��,Ȼ�󴴽����ݿ�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
//		String sql = "create table if not exists batch_list(id integer primary key,create_time text,batch_name text,batch_size text)";
		/*---------------����������Ϣ�����������У�create_time�����ݱ���ʱ�䣩��batch_name���������ƣ���-------------------------------
		-----------------batch_size�������λ�����������scan_flag���Ƿ��Ѿ�ɨ����ɣ�---------------------------------------------------------------*/
		String sql = "create table if not exists batch_list(id integer primary key,create_time text,batch_name text,batch_size text,scan_flag text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("ALTER TABLE batch_list ADD  scan_flag TEXT DEFAULT 'NO';");
	}

	/* �����ݿ�,����Ѿ��򿪾�ʹ�ã����򴴽� */
	public BatchDBHelper open() {
		if (null == mBatchDbHelper) {
			mBatchDbHelper = new BatchDBHelper(mcontext);
		}
		db = mBatchDbHelper.getWritableDatabase();
		return this;
	}

	/* �ر����ݿ� */
	public void close() {
		db.close();
		mBatchDbHelper.close();
	}

	/**������� */
	public long insert(String tableName, ContentValues values) {
		return db.insert(tableName, null, values);
	}
	
	/**
	 * ɾ�����ݿ���ָ��batch_name(���κ�)������
	 * @param tableName
	 * @param batch_name
	 * @return
	 */
	public int delete(String tableName,String batch_name){
		return db.delete(tableName, "batch_name = ?", new String[]{batch_name});
	}
	
	public int deleteAll(String tableName){//ɾ�����ݿ�����������
		return db.delete(tableName, "1", new String[]{ });
	}
	
	public int deleteUnFinished( ){//ɾ�����ݿ������г���״̬Ϊ��NO��������
//		return db.delete("batch_list", "scan_flag = ?", new String[]{"NO"});
		return 1;
	}

	public int updateSum(String batchname,String sum){//�������ݿ��ڵ�ɨ������
		ContentValues values = new ContentValues();
		values.put("batch_size",sum);
		return db.update("batch_list", values, "batch_name=? ", new String[]{batchname});
	}
	
	public int updateFlag(String batchname,String flag){//�������ݿ��ڵ�ɨ����ɱ�־λ
		ContentValues values = new ContentValues();
		values.put("scan_flag",flag);
		return db.update("batch_list", values, "batch_name=? ", new String[]{batchname});
	}
	
	/**��ѯ����*/
	public Cursor findList(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public Cursor exeSql(String sql) {
		return db.rawQuery(sql, null);
	}
}
