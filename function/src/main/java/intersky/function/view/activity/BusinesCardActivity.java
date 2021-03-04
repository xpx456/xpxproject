package intersky.function.view.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;

import intersky.apputils.AppUtils;
import intersky.function.R;
import intersky.function.entity.BusinessCardModel;
import intersky.function.handler.BusinesHandler;
import intersky.function.presenter.BusinesCardPresenter;
import intersky.function.view.adapter.BusinesAdapter;
import intersky.mywidget.SearchViewLayout;

@SuppressLint("ClickableViewAccessibility")
public class BusinesCardActivity extends BaseActivity

{


	public ListView mListView;
	public BusinesAdapter mBusinesAdapter;
	public BusinesAdapter mSearchBusinesAdapter;
	public SearchViewLayout searchViewLayout;
	public  ArrayList<BusinessCardModel> mBusinesItems = new ArrayList<BusinessCardModel>();
	public  ArrayList<BusinessCardModel> mSearchBusinesItems = new ArrayList<BusinessCardModel>();
	public ArrayList<BusinessCardModel> mtBusinesItems = new ArrayList<BusinessCardModel>();
	public static String mIndexes[];
	public static String mSearchIndexes[];
	public static boolean isscan = false;
	public boolean isShowSearch = false;
	public BusinesCardPresenter mBusinesCardPresenter = new BusinesCardPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mBusinesCardPresenter.Create();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mBusinesCardPresenter.Pause();

	}


	public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange()
	{

		@Override
		public void doTextChange(boolean visiable) {
			if(mBusinesCardPresenter.mBusinesHandler!= null)
				mBusinesCardPresenter.mBusinesHandler.sendEmptyMessage(BusinesHandler.EVENT_SCHANGE);
		}
	};

	public View.OnClickListener mBackListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			finish();

		}
	};

	public View.OnClickListener mUpLoadBusines = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mBusinesCardPresenter.upLoadBusines();
		}
	};

	public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// TODO Auto-generated method stub
			mBusinesCardPresenter.itemClick(position);
		}

	};






	public static void orderCustomer(ArrayList<BusinessCardModel> mCustomerItems,
			BusinessCardModel mCustomerItem)
	{
		// Collections.sort(List list, comparator);
		mIndexes = new String[mCustomerItems.size() + 1];
		for (int i = 0; i < mCustomerItems.size(); i++)
		{
			mIndexes[i] = mCustomerItems.get(i).pingyin;
		}
		mIndexes[mCustomerItems.size()] = mCustomerItem.pingyin;
		Arrays.sort(mIndexes);
		for (int i = 0; i < mCustomerItems.size(); i++)
		{
			if (mIndexes[i].equals(mCustomerItem.pingyin))
			{
				mCustomerItems.add(i, mCustomerItem);
				return;
			}
		}
		mCustomerItems.add(mCustomerItem);
	}


	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	    switch (requestCode) {
	        case BusinesHandler.REQUEST_CODE_ASK_PERMISSIONS:
	        	if(grantResults.length > 0)
				{
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						// Permission Granted
						mBusinesCardPresenter.getContacts();
					} else {
						// Permission Denied
						AppUtils.showMessage(this, this.getString(R.string.keyword_readcontactswrong));
					}
				}
	        	else
				{
					AppUtils.showMessage(this, this.getString(R.string.keyword_readcontactswrong));
				}
	            break;
	        default:
	            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	    }
	}
	
}
