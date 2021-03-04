package com.intersky.android.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.view.activity.ServiceSettingActivity;

import java.util.List;

import intersky.mywidget.SwipeRevealLayout;
import intersky.xpxnet.net.Service;

public class ServerListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Service> mData;
	public View.OnClickListener deleteListener;
	public ServerListAdapter(Context context, List<Service> data, View.OnClickListener deleteListener)
	{
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = data;
		this.deleteListener = deleteListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Service item = (Service) this.getItem(position);
		convertView = mInflater.inflate(R.layout.server_list_cell, null);
		//Name
		RelativeLayout mian = convertView.findViewById(R.id.main);
		mian.setTag(item);
		mian.setOnClickListener(onClickListener);
		TextView delete = convertView.findViewById(R.id.delete);
		delete.setTag(item);
		if(deleteListener != null)
		delete.setOnClickListener(deleteListener);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		//IP Address
		TextView ipAddress = (TextView) convertView.findViewById( R.id.ipAddress );
		TextView iptitle = convertView.findViewById(R.id.ipAddresstitle);
		//port
		TextView port = (TextView) convertView.findViewById(R.id.port);
		name.setText( item.sName);
		if( item.sType )
		{
			iptitle.setText(mContext.getString(R.string.servicesetting_address));
			ipAddress.setText( item.sAddress );
		}
		else
		{
			iptitle.setText(mContext.getString(R.string.servicesetting_code));
			ipAddress.setText( item.sCode );
		}
		port.setText( item.sPort );

		return convertView;
	}

	@Override
	public int getCount()
	{
		return mData.size();
	}

	@Override
	public Object getItem(int position )
	{
		return mData.get( position );
	}

	@Override
	public long getItemId( int position )
	{
		return position;
	}

	public View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Service service = (Service) v.getTag();
			Intent intent = new Intent(mContext, ServiceSettingActivity.class);
			intent.putExtra("service",service);
			mContext.startActivity(intent);
		}
	};




}