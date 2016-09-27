package com.zl.showlist;

import java.util.List;

import com.zkc.barcodescan.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QueryResultAdapter extends BaseAdapter{
	private List<QueryResult> resultlist;
	private Context context;
	private ViewHolder viewholder;
	
	public class ViewHolder{
		TextView tvout;
		TextView tvserial;
	}
	
	public QueryResultAdapter(Context ctx,List<QueryResult> result){
		this.context = ctx;
		this.resultlist = result;
		viewholder = new ViewHolder();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return resultlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return resultlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.dbresult_item,
		            parent,false);
		}
		viewholder.tvout = (TextView)convertView.findViewById(R.id.tvoutboundtime);
		viewholder.tvserial = (TextView)convertView.findViewById(R.id.tvserialnumber);
		QueryResult qres = resultlist.get(position);
		viewholder.tvout.setText(qres.getOutboundTime());
		viewholder.tvserial.setText(qres.getSerialNumber());
		return convertView;
	}

}
