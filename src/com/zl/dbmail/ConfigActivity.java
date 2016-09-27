package com.zl.dbmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.zkc.barcodescan.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends Activity{
	private static final String ProperDir = Environment.getExternalStorageDirectory()+ File.separator+"ScanResultData"+File.separator+"Config";
	private static final String Properfile = Environment.getExternalStorageDirectory()+ File.separator+"ScanResultData"+File.separator+"Config"+File.separator+"config.dat";
	private Properties properties;
	private TextView showmembers,tvlock;
	private Button btnaddmember,btndeletemember,btnclearmember;
//	private ScrollView sv;
	private List<String> memlist = new ArrayList<String>();
	private boolean islocked = false;
	
	@SuppressLint("HandlerLeak") 
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x01){
				memlist.add((String)msg.obj);
//				showmembers.append((String)msg.obj+"\r\n");
				updataShowText();
				properties.put("members", StringTransUtils.ListToString(memlist));
				saveConfig(ConfigActivity.this, Properfile, properties);
				Log.i("CONFIG", "收件人添加成功！");
				Toast.makeText(ConfigActivity.this, "收件人添加成功！", Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0x02){
				Toast.makeText(ConfigActivity.this, "收件人地址不合法，请重新输入！", Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0x03){
				int location = (Integer)msg.obj;
				memlist.remove(location);
				updataShowText();
				properties.put("members", StringTransUtils.ListToString(memlist));
				saveConfig(ConfigActivity.this, Properfile, properties);
				Log.i("CONFIG", "收件人删除成功！");
				Toast.makeText(ConfigActivity.this, "收件人删除成功！", Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0x04){
				Toast.makeText(ConfigActivity.this, "输入的标号不存在，请重新输入！", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_layout);
		File f = new File(ProperDir);
		makeDir(f);//初始化配置文件存储路径
		
		showmembers = (TextView)findViewById(R.id.tvshowmembers);
		showmembers.setMovementMethod(ScrollingMovementMethod.getInstance());//设置TextView可滚动
		
//		sv = (ScrollView)findViewById(R.id.sv1);
//		sv.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(islocked){
//					return false;
//				}else{
//				return false;
//				}
//			}
//		});
		
		tvlock = (TextView)findViewById(R.id.tvlock);
		tvlock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(islocked){
					islocked = false;
					tvlock.setBackground(getResources().getDrawable(R.drawable.unlock));
				}else{
					islocked = true;
					tvlock.setBackground(getResources().getDrawable(R.drawable.lock1));
				}
				
			}
		});
		
		btnaddmember = (Button)findViewById(R.id.btnaddmember);
		btnaddmember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				inputTitleDialog();
			}
		});
		
		btndeletemember = (Button)findViewById(R.id.btndeletemember);
		btndeletemember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDeleteDialog();
			}
		});
		
		btnclearmember = (Button)findViewById(R.id.btnclearmember);
		btnclearmember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showClearDialog();
			}
		});
		
		File pf = new File(Properfile);//初始化properfile文件
		if(pf.exists()){
		properties = loadConfig(this, Properfile);
		String str = (String)properties.get("members");
		if(!str.equals("L")){//由于序列化时是以“L”为起始位，故当list内容为空时反序列化时依然会存在“L”这个字符
		List<Object> templist = StringTransUtils.StringToList(str);
		for(int i=0;i<templist.size();i++){
			showmembers.append("【"+i+"】"+(String)templist.get(i)+"\r\n");
			memlist.add((String)templist.get(i));//将Object转化为Sting，List<Object>-->List<String>
		}
		}
		}else{
			properties = new Properties();
		}
	
	}
	
	private void updataShowText(){
		showmembers.setText("");
		for(int i=0;i<memlist.size();i++){
			showmembers.append("【"+i+"】"+(String)memlist.get(i)+"\n");
		}
	}
	
	private void inputTitleDialog() {//弹出带输入框的对话框

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.addmember)).setIcon(
        		android.R.drawable.btn_star).setView(inputServer).setNegativeButton(
                getString(R.string.quit), null);
        builder.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String member = inputServer.getText().toString();
                        if(CheckStringTypeUtils.isEmail(member)){
                        Message msg = Message.obtain(mhandler);
                        msg.what = 0x01;
                        msg.obj = member;
                        msg.sendToTarget();
                        }else{
                        	Message msg = Message.obtain(mhandler);
                            msg.what = 0x02;
                            msg.sendToTarget();
                        }
                    }
                });
        builder.show();
    }
	
	private void showDeleteDialog() {//弹出带输入框的对话框

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.deletelocationmember)).setIcon(
        		android.R.drawable.btn_star).setView(inputServer).setNegativeButton(
                getString(R.string.quit), null);
        builder.setPositiveButton(getString(R.string.deletemember),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String number = inputServer.getText().toString();
                        int inumber = Integer.parseInt(number);
                        if((inumber>=0)&&(inumber<memlist.size())){
                        Message msg = Message.obtain(mhandler);
                        msg.what = 0x03;
                        msg.obj = inumber;
                        msg.sendToTarget();
                        }else{
                        	Message msg = Message.obtain(mhandler);
                            msg.what = 0x04;
                            msg.sendToTarget();
                        }
                    }
                });
        builder.show();
    }
	
	private void showClearDialog(){//清空收件人列表提示框
		new AlertDialog.Builder(ConfigActivity.this).setIcon(android.R.drawable.btn_star)  
	    .setTitle("收件人列表").setMessage("确定要清空收件人列表？")  
	    .setPositiveButton("确定",new DialogInterface.OnClickListener() {  
	                @Override  
	                public void onClick(DialogInterface dialog,int which) {
	                    // TODO Auto-generated method stub  
	                	memlist.clear();
	    				showmembers.setText("");
	    				properties.put("members", StringTransUtils.ListToString(memlist));
	    				saveConfig(ConfigActivity.this, Properfile, properties);
	                }  
	            })  
	    .setNegativeButton("取消",new DialogInterface.OnClickListener() {  
	                @Override  
	                public void onClick(DialogInterface dialog,int which) {
	                    // TODO Auto-generated method stub  
	                    
	                }  
	            }).show();// show很关键   
	}
	
	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}
	
	public static Properties loadConfig(Context context, String file) { 
		Properties properties = new Properties(); 
		try { 
			FileInputStream s = new FileInputStream(file); 
			properties.load(s);
			} catch (Exception e) { 
				e.printStackTrace(); 
				} 
		return properties; 
		} 
	
	public static void saveConfig(Context context, String file, Properties properties) { 
		try { 
			FileOutputStream s = new FileOutputStream(file, false); 
			properties.store(s, ""); 
			} catch (Exception e){ 
				e.printStackTrace(); 
				} 
		}

}
