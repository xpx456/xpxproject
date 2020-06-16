package com.interskypad.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.manager.OrderManager;
import com.interskypad.manager.ProducterManager;
import com.interskypad.view.InterskyPadApplication;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;

public class CatalogCridAdapter extends BaseAdapter
{
	public ArrayList<Catalog> mCatalogGridItems;
	private Context mContext;
	private Handler mhandler;
	private LayoutInflater mInflater;

	public CatalogCridAdapter(Context context, ArrayList<Catalog> mCatalogGridItems, Handler mhandler)
	{
		this.mContext = context;
		this.mhandler = mhandler;
		this.mCatalogGridItems = mCatalogGridItems;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.catalog_grid_item, null);
			holder.mListener = new CatalogListener(position);
			holder.mEnName = (TextView) convertView.findViewById(R.id.gride_enname);
			holder.mItemNo = (TextView) convertView.findViewById(R.id.gride_itemno);
			holder.mItemImage = (ImageView) convertView.findViewById(R.id.gride_img);
			holder.mCatalog = (ImageView) convertView.findViewById(R.id.gride_quote);
			holder.mSelect = (TextView) convertView.findViewById(R.id.gride_select);
			holder.mLine = (RelativeLayout) convertView.findViewById(R.id.gride_line);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mCatalog.setTag(mCatalogGridItem);
		holder.mListener.setPosition(position);
		holder.mEnName.setText(mCatalogGridItem.mENGItemName);
		holder.mItemNo.setText(mCatalogGridItem.mItemNo);
		holder.mCatalog.setOnClickListener(holder.mListener);
		if (OrderManager.getInstance().isInCart(mCatalogGridItem) == false)
		{
			holder.mSelect.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.mSelect.setVisibility(View.VISIBLE);
		}
		if (position%4 == 3)
		{
			holder.mLine.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.mLine.setVisibility(View.VISIBLE);
		}
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.temp);

        if(InterskyPadApplication.mApp.isLogin)
		{
			String url = ProducterManager.getInstance().getProductPhotoUrl(mCatalogGridItem.mPhoto);
			Glide.with(mContext).load(url).apply(options).into(new MySimpleTarget(holder.mItemImage));
//			File file1 = new File(InterskyPadApplication.mApp.mFileUtils.pathUtils.getBasePath()+"/interskypad/71ee1c80-4edd-11e9-be2e-1831bfdfd6ee.jpg");
//			Glide.with(mContext).load(file1).apply(options).into(new MySimpleTarget(holder.mItemImage));
		}
        else
		{
			File file1 = new File(InterskyPadApplication.mApp.mFileUtils.pathUtils.getfilePath("/photo")+"/"+mCatalogGridItem.mPhoto.replaceAll("/",""));
			if(file1.exists() )
			{
				Glide.with(mContext).load(file1).apply(options).into(new MySimpleTarget(holder.mItemImage));
			}
			else
			{
				holder.mItemImage.setImageResource(R.drawable.temp);
			}

		}
		return convertView;
	}

	private static class ViewHolder
	{
		private TextView mEnName;
		private TextView mItemNo;
		private ImageView mItemImage;
		private ImageView mCatalog;
		private TextView mSelect;
		private CatalogListener mListener;
		private RelativeLayout mLine;
	}

	private class CatalogListener implements OnClickListener
	{
		int mPosition;

		public CatalogListener(int inPosition)
		{
			mPosition = inPosition;
		}

		public void setPosition(int inPosition)
		{
			this.mPosition = inPosition;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Catalog catalog = mCatalogGridItems.get(mPosition);
			if(OrderManager.getInstance().isInCart(catalog))
			{
				OrderManager.getInstance().deleteCatalog(catalog);
			}
			else
			{

				OrderManager.getInstance().addCatalog(catalog);
			}
		}

	}
}
