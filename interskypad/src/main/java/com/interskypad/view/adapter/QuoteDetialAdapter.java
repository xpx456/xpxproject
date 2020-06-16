package com.interskypad.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.manager.ProducterManager;
import com.interskypad.view.InterskyPadApplication;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;

public class QuoteDetialAdapter extends BaseAdapter
{
	private ArrayList<Catalog> mCatalogGridItems;
	private Context mContext;
	private LayoutInflater mInflater;
	private Handler mhandler;
	
	public QuoteDetialAdapter(Context context, ArrayList<Catalog> mCatalogGridItems){
		this.mContext = context;
		this.mCatalogGridItems = mCatalogGridItems;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mCatalogGridItems.size();
	}

	@Override
	public Catalog getItem(int position)
	{
		// TODO Auto-generated method stub
		return mCatalogGridItems.get(position);
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
		Catalog mCatalogGridItem = getItem(position);
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.quote_detial_item, null);
			holder.mImg=(ImageView) convertView.findViewById(R.id.quote_list_detial_null);
			holder.mItemId=(TextView)convertView.findViewById(R.id.quote_list_detial_itemid);
			holder.mTtemName=(TextView)convertView.findViewById(R.id.quote_list_detial_name);
			holder.mItemPrice = (TextView)convertView.findViewById(R.id.quote_list_detial_price);
			holder.mItemUnit = (TextView)convertView.findViewById(R.id.quote_list_detial_unit);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		//holder.mListener.setPosition(position);
		holder.mItemId.setText(mCatalogGridItem.mItemNo);
		holder.mTtemName.setText(mCatalogGridItem.mENGItemName);
		holder.mItemPrice.setText(mCatalogGridItem.mSalesPrice);
		holder.mItemUnit.setText(mCatalogGridItem.mUnit);
		RequestOptions options = new RequestOptions()
				.placeholder(R.drawable.temp);
		if(InterskyPadApplication.mApp.isLogin)
			Glide.with(mContext).load(ProducterManager.getInstance().getProductPhotoUrl(mCatalogGridItem.mPhoto)).apply(options).into(new MySimpleTarget(holder.mImg));
		else
		{
			File file1 = new File(InterskyPadApplication.mApp.mFileUtils.pathUtils.getfilePath("/photo")+"/"+mCatalogGridItem.mPhoto);
			if(file1.exists() && !file1.isDirectory())
			{
				Glide.with(mContext).load(file1).apply(options).into(new MySimpleTarget(holder.mImg));
			}
			else
			{
				holder.mImg.setImageResource(R.drawable.temp);
			}

		}
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView mImg;
		private TextView mItemId;
		private TextView mTtemName;
		private TextView mItemPrice;
		private TextView mItemUnit;
	}


}
