package com.zl.activity;

import com.zkc.barcodescan.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity{
	private Button btnjdt,btnsoap;
	private MyApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_layout);
		
		app = (MyApplication)getApplication();
		btnjdt = (Button)findViewById(R.id.btnjdt);
		btnsoap = (Button)findViewById(R.id.btnsoap);
		btnjdt.setOnClickListener(onclick);
		btnsoap.setOnClickListener(onclick);
		
	}
	
	private OnClickListener onclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnjdt){
				app.setQueryType(app.JDTQUERY);
				startActivity(new Intent(MainMenuActivity.this,JdtMainActivity.class));
			}else if(v == btnsoap){
				app.setQueryType(app.SOAPQUERY);
				startActivity(new Intent(MainMenuActivity.this,SoapMainActivity.class));
			}
			
		}
	};

}
