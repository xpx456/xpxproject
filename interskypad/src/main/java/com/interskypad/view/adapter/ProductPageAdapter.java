package com.interskypad.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.interskypad.R;
import com.interskypad.entity.Catalog;
import com.interskypad.manager.ProducterManager;
import com.interskypad.view.InterskyPadApplication;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;

public class ProductPageAdapter extends PagerAdapter
{
	public ArrayList<View> views;
	public ArrayList<Catalog> catalogs;
	public boolean showdetial = false;
	public Context context;
	public View.OnClickListener showBigImageListener;
	public ProductPageAdapter(Context context,ArrayList<View> views, ArrayList<Catalog> catalogs,View.OnClickListener showBigImageListener)
	{
		super();
		this.views = views;
		this.context = context;
		this.catalogs = catalogs;
		this.showBigImageListener = showBigImageListener;
	}

	@Override
	public int getCount()
	{
		return catalogs.size();
	}


	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object)
	{
		// TODO Auto-generated method stub
		// return super.getItemPosition(object);
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2)
	{
		// TODO Auto-generated method stub
//		((ViewPager) arg0).removeView(view.get(arg1));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		// TODO Auto-generated method stub
		try {
			View mview = views.get(position % views.size());
			ViewParent vp =mview.getParent();
			initData(mview,catalogs.get(position));
			if (vp!=null){
				ViewGroup parent = (ViewGroup)vp;
				parent.removeView(mview);
			}
			container.addView(mview);
		} catch (Exception e) {
		}
		return views.get(position % views.size());

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0)
	{
		// TODO Auto-generated method stub
		// ((ViewPager) arg0).removeAllViews();
	}

	@Override
	public void finishUpdate(View arg0)
	{
		// TODO Auto-generated method stub

	}

	public void initData(View view ,Catalog mCatalogGridItem) {
		TextView mText[] = new TextView[18];
		ImageView mItemImage = (ImageView) view.findViewById(R.id.detial_img);
		RequestOptions options = new RequestOptions()
				.placeholder(R.drawable.temp);
		if(InterskyPadApplication.mApp.isLogin)
			Glide.with(context).load(ProducterManager.getInstance().getProductPhotoUrl(mCatalogGridItem.mPhoto)).apply(options).into(new MySimpleTarget(mItemImage));
		else
		{
			File file1 = new File(InterskyPadApplication.mApp.mFileUtils.pathUtils.getfilePath("/photo")+"/"+mCatalogGridItem.mPhoto);
			if(file1.exists() && !file1.isDirectory())
			{
				Glide.with(context).load(file1).apply(options).into(new MySimpleTarget(mItemImage));
			}
			else
			{
				mItemImage.setImageResource(R.drawable.temp);
			}

		}
		mText[0] = (TextView) view.findViewById(R.id.detial_itemno_e);
		mText[1] = (TextView) view.findViewById(R.id.detial_barcode_e);
		mText[2] = (TextView) view.findViewById(R.id.detial_ename_e);
		mText[3] = (TextView) view.findViewById(R.id.detial_cbm_e);
		mText[4] = (TextView) view.findViewById(R.id.detial_package_e);
		mText[5] = (TextView) view.findViewById(R.id.detial_unit_e);
		mText[6] = (TextView) view.findViewById(R.id.detial_salesprice_e);
		mText[7] = (TextView) view.findViewById(R.id.detial_pcsctn_e);

		mText[8] = (TextView) view.findViewById(R.id.detial_minimumqty_e);
		mText[9] = (TextView) view.findViewById(R.id.detial_especifcation_e);
		mText[10] = (TextView) view.findViewById(R.id.detial_gw_e);
		mText[11] = (TextView) view.findViewById(R.id.detial_nw_e);
		mText[12] = (TextView) view.findViewById(R.id.detial_emeno_e);
		mText[13] = (TextView) view.findViewById(R.id.detial_factory_name_e);
		mText[14] = (TextView) view.findViewById(R.id.detial_factory_number_e);
		mText[15] = (TextView) view.findViewById(R.id.detial_factory_cillbill_e);
		mText[16] = (TextView) view.findViewById(R.id.detial_factory_price_e);
		mText[17] = (TextView) view.findViewById(R.id.detial_factory_rate_e);

		mText[0].setText(mCatalogGridItem.mItemNo);
		mText[1].setText(mCatalogGridItem.mBarcode);
		mText[2].setText(mCatalogGridItem.mENGItemName);
		mText[3].setText(mCatalogGridItem.mOuterVolume);
		mText[4].setText(mCatalogGridItem.mPacking);
		mText[5].setText(mCatalogGridItem.mUnit);
		mText[6].setText(mCatalogGridItem.mSalesPrice);
		mText[7].setText(mCatalogGridItem.mOuterCapacity);
		mText[8].setText(mCatalogGridItem.mMinimumQty);
		mText[9].setText(mCatalogGridItem.mENGSpecification);
		mText[10].setText(mCatalogGridItem.mOuterGrossWeight);
		mText[11].setText(mCatalogGridItem.mOuterNetWeight);
		mText[12].setText(mCatalogGridItem.mENGMemo);
		mText[13].setText(mCatalogGridItem.mSupplierShortName);
		mText[14].setText(mCatalogGridItem.mSupplierItemNo);
		mText[15].setText(mCatalogGridItem.mCanBill);
		mText[16].setText(mCatalogGridItem.mPurchasePrice);
		mText[17].setText(mCatalogGridItem.mRebate);

		RelativeLayout mShowlayer;
		mShowlayer = (RelativeLayout) view.findViewById(R.id.detial_unshow_infmation_f);
		if(showdetial)
		{
			mShowlayer.setVisibility(View.VISIBLE);
		}
		else
		{
			mShowlayer.setVisibility(View.INVISIBLE);
		}
		mItemImage.setOnClickListener(showBigImageListener);
	}

	public void setShowdetial(boolean showdetial)
	{
		for(int i = 0 ; i < views.size() ; i++)
		{
			View view = views.get(i);
			RelativeLayout mShowlayer;
			mShowlayer = (RelativeLayout) view.findViewById(R.id.detial_unshow_infmation_f);
			if(showdetial == true)
			{
				mShowlayer.setVisibility(View.VISIBLE);
			}
			else
			{
				mShowlayer.setVisibility(View.INVISIBLE);
			}
		}
	}


}