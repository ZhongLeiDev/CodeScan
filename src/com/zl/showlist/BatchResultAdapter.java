package com.zl.showlist;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zkc.barcodescan.R;

public class BatchResultAdapter extends BaseAdapter{
	private List<BatchResult> resultlist;
	private Context context;
	private ViewHolder viewholder;
	
	public class ViewHolder{
		TextView tvbatchname;
		TextView tvbatchsize;
		TextView tvbatchflag;
	}
	
	public BatchResultAdapter(Context ctx,List<BatchResult> result){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.batch_item,
		            parent,false);
		}
		viewholder.tvbatchname = (TextView)convertView.findViewById(R.id.tvbatchname);
		viewholder.tvbatchsize = (TextView)convertView.findViewById(R.id.tvbatchsize);
		viewholder.tvbatchflag = (TextView)convertView.findViewById(R.id.tvbatchflag);
		BatchResult qres = resultlist.get(position);
		viewholder.tvbatchname.setText(qres.getBatchName());
		viewholder.tvbatchsize.setText(qres.getBatchSize());
		viewholder.tvbatchflag.setText(qres.getBatchFlag());
		return convertView;
	}
}
