package com.zkc.barcodescan.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zkc.Service.CaptureService;
import com.zkc.barcodescan.R;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ActivityQrcodeSetting extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_qrcodesetting);
		setListAdapter(new SimpleAdapter(this, getData("simple-list-item-2"),
				android.R.layout.simple_list_item_2, new String[] { "title",
						"description" }, new int[] { android.R.id.text1,
						android.R.id.text2 }));
		

		/*//鎭㈠鍑哄巶璁剧疆
		String str="nls000601;00001000;0006000";
		//鍏佽璇嗚鎵�鏈夌被鍨�
		str="nls0006010;nls0302003;nls0006000";
		byte[] buffer=str.getBytes(Charset.forName("US-ASCII"));
		Log.i(TAG, "string is:"+bytesToString(buffer).toString());
		//CaptureService.serialPort.Write(buffer);
		CaptureService.serialPort.Write(new byte[]{0x1b,0x31});*/
	}

	protected void onListItemClick(ListView listView, View v, int position,
			long id) {
		switch (position) {
		case 0:
			if (CaptureService.serialPort != null) {
				CaptureService.serialPort
						.Write(CaptureService.defaultSetting2D);
				Toast toast = Toast.makeText(this,
						getResources().getString(R.string.action_setsuccess),
						Toast.LENGTH_LONG);
				toast.show();
			}
			break;
		case 1:
			if (CaptureService.serialPort != null) {
				CaptureService.serialPort.Write(CaptureService.dataTypeFor2D);
				Toast toast = Toast.makeText(this,
						getResources().getString(R.string.action_setsuccess),
						Toast.LENGTH_LONG);
				toast.show();
			}
			break;
		case 2:
			Intent intent = new Intent();
			intent.setClass(ActivityQrcodeSetting.this,
					DialogEncodingActivity.class);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 取锟斤拷锟街凤拷
		Bundle bundle = data.getExtras();
		String str = bundle.getString("str");

		SharedPreferences.Editor editor = CaptureService.sharedPreferences
				.edit();
		editor.putString("encoding", str);
		editor.commit();
		CaptureService.str_encoding=str;
	}

	/**
	 * 锟斤拷锟斤拷SimpleAdapter锟侥第讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟轿狶ist<Map<?,?>>
	 * 
	 * @param title
	 * @return
	 */
	private List<Map<String, String>> getData(String title) {
		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();

		// Reset
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", getResources().getString(R.string.action_reset));
		map.put("description",
				getResources().getString(R.string.action_reset_desc));
		listData.add(map);

		// Set data type
		map = new HashMap<String, String>();
		map.put("title", getResources().getString(R.string.action_datatype));
		map.put("description",
				getResources().getString(R.string.action_datatype_desc));
		listData.add(map);

		// Set encoding type
		map = new HashMap<String, String>();
		map.put("title", getResources().getString(R.string.action_encodingtype));
		map.put("description",
				getResources().getString(R.string.action_encodingtype_desc));
		listData.add(map);

		return listData;
	}
	
	 /**
	  * 灏哹yte鏁扮粍杞崲涓哄瓧绗︿覆褰㈠紡琛ㄧず鐨勫崄鍏繘鍒舵暟鏂逛究鏌ョ湅
	  */
	 public static StringBuffer bytesToString(byte[] bytes)
	 {
	  StringBuffer sBuffer = new StringBuffer();
	  for (int i = 0; i < bytes.length; i++)
	  {
	   String s = Integer.toHexString(bytes[i] & 0xff);
	   if (s.length() < 2)
	    sBuffer.append('0');
	   sBuffer.append(s + " ");
	  }
	  return sBuffer;
	 }


	 public static byte charToByte(char c)
	 {
	  return (byte) "0123456789abcdef".indexOf(c);
	 }
}
