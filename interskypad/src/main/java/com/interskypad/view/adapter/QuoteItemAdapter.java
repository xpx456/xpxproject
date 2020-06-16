package com.interskypad.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.asks.OrderAsks;
import com.interskypad.entity.Order;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.MainActivity;

import java.util.ArrayList;

import intersky.apputils.AppUtils;

public class QuoteItemAdapter extends BaseAdapter
{

	private ArrayList<Order> mOrderListItems;
	private MainActivity mContext;
	private LayoutInflater mInflater;
	public Handler handler;
	public QuoteItemAdapter(MainActivity context, ArrayList<Order> mOrderListItems, Handler handler)
	{
		this.mContext = context;
		this.mOrderListItems = mOrderListItems;
		this.handler = handler;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mOrderListItems.size();
	}

	@Override
	public Order getItem(int position)
	{
		// TODO Auto-generated method stub
		return mOrderListItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		Order mOrderListItem = getItem(position);
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.order_list_cell, null);
			holder.mid = (TextView) convertView.findViewById(R.id.oredrid);
			holder.mcustomer = (TextView) convertView.findViewById(R.id.ordercustomer);
			holder.mtime = (TextView) convertView.findViewById(R.id.ordertime);
			holder.mdescribe = (TextView) convertView.findViewById(R.id.orderdescribe);
			holder.other = (TextView) convertView.findViewById(R.id.orderother);
			holder.mListener = new QuoteListener(position);
			holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.ordierlayer);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mListener.setPosition(position);
		holder.mid.setText(mOrderListItem.id);
		holder.mcustomer.setText(mOrderListItem.c_name);
		holder.mtime.setText(mOrderListItem.time);
		holder.mdescribe.setText(mOrderListItem.memo);
		if(mOrderListItem.isselect)
		{
			holder.mLinearLayout.setBackgroundColor(Color.argb(255, 195, 195, 195));
		}
		else
		{
			holder.mLinearLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));

		}
		if (!mOrderListItem.issubmit)
		{
			holder.other.setTextColor(Color.argb(255, 85, 129, 204));
			holder.other.setText("提交报价单");
			holder.other.setOnClickListener(holder.mListener);
		}
		else
		{
			holder.other.setTextColor(Color.argb(255, 142, 142, 142));
			holder.other.setText("已提交");
		}
		return convertView;
	}

	private static class ViewHolder
	{
		private TextView mid;
		private TextView mcustomer;
		private TextView mtime;
		private TextView mdescribe;
		private TextView other;
		private LinearLayout mLinearLayout;
		private QuoteListener mListener;
	}

	private class QuoteListener implements OnClickListener
	{
		int mPosition = 0;

		public QuoteListener(int inPosition)
		{
			mPosition = inPosition;
		}

		public void setPosition(int inPosition)
		{
			this.mPosition = inPosition;
		}

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub

			// AppUtils.createLoadingDialog(mContext);
			if(InterskyPadApplication.mApp.isLogin == true)
			{
				AppUtils.creatDialogTowButton(mContext,mContext.getString(R.string.xml_quote_submit_order),mContext.getString(R.string.dialog_word_tip),mContext.getString(R.string.button_yes),mContext.getString(R.string.button_no)
						,new SubmitListener(mOrderListItems.get(mPosition)),null);
			}
			
		}

	}


	public class SubmitListener implements DialogInterface.OnClickListener{

		public Order order;

		public SubmitListener(Order order) {
			this.order = order;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			mContext.waitDialog.show();
			OrderAsks.submitOrder(handler,mContext, order);
		}
	}
}
