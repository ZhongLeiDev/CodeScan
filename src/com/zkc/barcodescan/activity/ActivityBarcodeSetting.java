package com.zkc.barcodescan.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zkc.Service.CaptureService;
import com.zkc.barcodescan.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ActivityBarcodeSetting extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_barcodesetting);
		setListAdapter(new SimpleAdapter(this,getData("simple-list-item-2"),android.R.layout.simple_list_item_2,new String[]{"title", "description"},new int[]{android.R.id.text1, android.R.id.text2}));
	}
	
	/**
     * 当List的项被选中时触发
     */
    protected void onListItemClick(ListView listView, View v, int position, long id) {
    	switch (position) {
		case 0:
			if(CaptureService.serialPort!=null)
			{
				CaptureService.serialPort.Write(CaptureService.defaultSetting1D);
				Toast toast = Toast.makeText(this, getResources().getString(R.string.action_setsuccess), Toast.LENGTH_LONG);
		        toast.show();
			}
			break;
		case 1:
			if(CaptureService.serialPort!=null)
			{
				CaptureService.serialPort.Write(CaptureService.dataTypeFor1D);
				Toast toast = Toast.makeText(this, getResources().getString(R.string.action_setsuccess), Toast.LENGTH_LONG);
		        toast.show();
			}
			break;
		default:
			break;
		}
    }
	
	/**
     * 构造SimpleAdapter的第二个参数，类型为List<Map<?,?>>
     * @param title
     * @return
     */
    private List<Map<String, String>> getData(String title) {
    	List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
    	
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("title", getResources().getString(R.string.action_reset));
    		map.put("description", getResources().getString(R.string.action_reset_desc));
    		listData.add(map);
    		
    		map = new HashMap<String, String>();
    		map.put("title", getResources().getString(R.string.action_datatype));
    		map.put("description", getResources().getString(R.string.action_datatype_desc));
    		listData.add(map);
    	
    	return listData;
    }
}
