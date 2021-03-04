package com.intersky.strang.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intersky.strang.R;

import java.util.List;

import intersky.xpxnet.net.Service;

public class ServerListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Service> mData;
	
	public ServerListAdapter(Context context, List<Service> data)
	{
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Service item = (Service) this.getItem(position);
		LinearLayout Cell = null;

		Cell = (LinearLayout) mInflater.inflate( R.layout.server_list_cell, Cell );
		//Name
		TextView name = (TextView) Cell.findViewById(R.id.name);
		//IP Address
		TextView ipAddress = (TextView) Cell.findViewById( R.id.ipAddress );
		//port
		TextView port = (TextView) Cell.findViewById(R.id.port);
		name.setText( item.sName);
		if( item.sType )
		{
			ipAddress.setText( item.sAddress );
		}
		else
		{
			ipAddress.setText( item.sCode );
		}
		port.setText( item.sPort );

		return Cell;
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

}