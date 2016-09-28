package com.zl.dbmail;

import java.util.ArrayList;
import java.util.List;

import com.zkc.barcodescan.R;
import com.zkc.barcodescan.activity.MainActivity;
import com.zl.showlist.BatchResult;
import com.zl.showlist.BatchResultAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class BatchHandleActivity extends Activity{
	private BatchDBHelper batchdbhelper;
	private ListView lv;
	private Button btnchange;
	private List<BatchResult> showlist = new ArrayList<BatchResult>();
	private BatchResultAdapter ada;
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x01){
				ada.notifyDataSetChanged();
			}else if(msg.what == 0x02){
				Toast.makeText(BatchHandleActivity.this, "成功解除锁定！", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.batch_layout);
		
		batchdbhelper = new BatchDBHelper(this);
		batchdbhelper.open();//打开数据库
		
		ada = new BatchResultAdapter(this, showlist);
		
		lv = (ListView)findViewById(R.id.listView1);
		lv.setAdapter(ada);
		btnchange = (Button)findViewById(R.id.btnchange);
		btnchange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRebuildDialog();
			}
		});
		
		new Thread(showRunnable).start();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, R.string.unlock);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == 1) {
		showUnlockDialog();
		} 
		return super.onOptionsItemSelected(item);
	}
	
	private Runnable showRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			showlist.clear();
				Cursor cur = batchdbhelper.exeSql("select * from batch_list ");
				while (cur.moveToNext()) {
					BatchResult qr = new BatchResult(cur.getString(2), cur.getString(3),cur.getString(4));
					showlist.add(qr);
				}
				cur.close();
//				ada.notifyDataSetChanged();
				Message msg = Message.obtain(mhandler);
				msg.what = 0x01;
				msg.sendToTarget();
		}
	};
	
private void showRebuildDialog(){
		
		LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.choose_view, null);
        final EditText edtbatch = (EditText) textEntryView.findViewById(R.id.new_batch);  
        final EditText edtsum = (EditText)textEntryView.findViewById(R.id.sum); 
		
		new AlertDialog.Builder(BatchHandleActivity.this).setIcon(android.R.drawable.btn_star)  
	    .setTitle("批次表")
	    .setIcon(android.R.drawable.btn_star)
	    .setView(textEntryView)
	    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
	                @Override  
	                public void onClick(DialogInterface dialog,int which) {
	                    // TODO Auto-generated method stub  
	                	
	                	String batchname = edtbatch.getText().toString();
	                	String batchsum = edtsum.getText().toString();
	                	
	                	if((!batchname.isEmpty())&&(MainActivity.isNumber(batchsum))&&(Integer.parseInt(batchsum)>0)&&(!batchname.equals("EMPTY"))&&(!batchname.equals("NO"))){
	                	
	                		if(batchdbhelper.updateSum(batchname, batchsum)>0){
	                			new Thread(showRunnable).start();
	                			Toast.makeText(BatchHandleActivity.this, "更新数据库成功！", Toast.LENGTH_SHORT).show();
	                		}else{
	                			Toast.makeText(BatchHandleActivity.this, "更新数据库失败！", Toast.LENGTH_SHORT).show();
	                		}
	                	
	                }else{
	                	Toast.makeText(BatchHandleActivity.this, "输入参数不符合要求，请重新输入！", Toast.LENGTH_SHORT).show();
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

private void showUnlockDialog(){
	
    final EditText edtinput = new EditText(BatchHandleActivity.this); 
	
	new AlertDialog.Builder(BatchHandleActivity.this).setIcon(android.R.drawable.btn_star)  
    .setTitle("解除锁定")
    .setIcon(android.R.drawable.btn_star)
    .setMessage("手动输入待解除锁定的批次：")
    .setView(edtinput)
    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                @Override  
                public void onClick(DialogInterface dialog,int which) { 
                    // TODO Auto-generated method stub  
                	
                String sn = edtinput.getText().toString();
                	
                	if((!sn.isEmpty())){
                	
                		if((batchdbhelper.updateFlag(sn,"NO"))>0){
                			Message msg = Message.obtain(mhandler);
                			msg.what = 0x02;
                			msg.sendToTarget();
                		}else{
                			Toast.makeText(BatchHandleActivity.this, "解除锁定失败！", Toast.LENGTH_SHORT).show();
                		}
                		
                		new Thread(showRunnable).start();
                	
                }else{
                	Toast.makeText(BatchHandleActivity.this, "输入参数不符合要求，请重新输入！", Toast.LENGTH_SHORT).show();
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

}
