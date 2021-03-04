package com.intersky.android.presenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.intersky.R;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.FunctionSelectActivity;
import com.intersky.android.view.activity.SafeActivity;
import com.intersky.android.view.adapter.FunctionAddAdapter;
import com.umeng.commonsdk.debug.I;

import intersky.appbase.Presenter;
import intersky.document.DocumentManager;
import intersky.function.FunctionUtils;
import intersky.function.entity.Function;
import intersky.function.view.activity.WebMessageActivity;
import intersky.mail.MailManager;
import intersky.mywidget.SearchViewLayout;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class FunctionSelectPresenter implements Presenter {

	public FunctionSelectActivity mFunctionSelectActivity;
	public boolean showshearch;
	public FunctionSelectPresenter(FunctionSelectActivity mFunctionSelectActivity) {
		this.mFunctionSelectActivity = mFunctionSelectActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mFunctionSelectActivity.setContentView(R.layout.activity_select_function);
		mFunctionSelectActivity.functionAddAdapter = new FunctionAddAdapter( FunctionUtils.getInstance().mFunctions,mFunctionSelectActivity,mFunctionSelectActivity.getIntent().getBooleanExtra("search",false) );
		mFunctionSelectActivity.functionSearchAddAdapter = new FunctionAddAdapter( mFunctionSelectActivity.search,mFunctionSelectActivity,mFunctionSelectActivity.getIntent().getBooleanExtra("search",false) );
		mFunctionSelectActivity.btnAdd = mFunctionSelectActivity.findViewById(R.id.btnadd);
		mFunctionSelectActivity.titleAdd = mFunctionSelectActivity.findViewById(R.id.addtitle);
		mFunctionSelectActivity.buttomlayer = mFunctionSelectActivity.findViewById(R.id.buttom_btns);
		mFunctionSelectActivity.back = mFunctionSelectActivity.findViewById(R.id.back);
		mFunctionSelectActivity.back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mFunctionSelectActivity.finish();
			}
		});
		mFunctionSelectActivity.searchView = mFunctionSelectActivity.findViewById(R.id.search);
		mFunctionSelectActivity.searchView.setDotextChange(doTextChange);
		mFunctionSelectActivity.listView = mFunctionSelectActivity.findViewById(R.id.functionlist);
		mFunctionSelectActivity.listView.setLayoutManager(new LinearLayoutManager(mFunctionSelectActivity));
		if(mFunctionSelectActivity.getIntent().getBooleanExtra("search",false) == true)
		{
			TextView textView = mFunctionSelectActivity.findViewById(R.id.title);
			textView.setText(mFunctionSelectActivity.getString(R.string.adduse));
			mFunctionSelectActivity.buttomlayer.setVisibility(View.INVISIBLE);
		}
		else
		{
			TextView textView = mFunctionSelectActivity.findViewById(R.id.title);
			textView.setText(mFunctionSelectActivity.getString(R.string.use));
			mFunctionSelectActivity.buttomlayer.setVisibility(View.VISIBLE);
		}
		mFunctionSelectActivity.listView.setAdapter(mFunctionSelectActivity.functionAddAdapter);
		mFunctionSelectActivity.functionAddAdapter.setOnItemClickListener(onItemClickListener);
		mFunctionSelectActivity.functionSearchAddAdapter.setOnItemClickListener(onItemClickListener);
		mFunctionSelectActivity.btnAdd.setOnClickListener(onAddListener);
		initData();
	}

	public void initData()
	{
		mFunctionSelectActivity.oldcount = FunctionUtils.getInstance().myFunction.size();
		mFunctionSelectActivity.add.addAll(FunctionUtils.getInstance().myFunction);
		mFunctionSelectActivity.add.remove(mFunctionSelectActivity.add.size()-1);
		mFunctionSelectActivity.other.addAll(FunctionUtils.getInstance().otherFunction);
		mFunctionSelectActivity.titleAdd.setText(mFunctionSelectActivity.getString(R.string.select_now)+"："+String.valueOf(mFunctionSelectActivity.add.size())
				+mFunctionSelectActivity.getString(R.string.unit1));
		mFunctionSelectActivity.btnAdd.setText(mFunctionSelectActivity.getString(R.string.confirm_add));
	}

	public void updateData()
	{
		mFunctionSelectActivity.titleAdd.setText(mFunctionSelectActivity.getString(R.string.select_now)+"："+String.valueOf(mFunctionSelectActivity.add.size())
				+mFunctionSelectActivity.getString(R.string.unit1));
		mFunctionSelectActivity.btnAdd.setText(mFunctionSelectActivity.getString(R.string.confirm_add));
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}

	public FunctionAddAdapter.OnItemClickListener onItemClickListener = new FunctionAddAdapter.OnItemClickListener()
	{

		@Override
		public void onItemClick(Function mFunction, int position, View view) {
			if(mFunctionSelectActivity.getIntent().getBooleanExtra("search",false) == true)
			{

				if(mFunction.typeName.toLowerCase().equals(Function.DOCUMENT))
				{
					DocumentManager.startDocumentMain(mFunctionSelectActivity);
				}
				else if(mFunction.typeName.toLowerCase().equals(Function.MAIL))
				{
					MailManager.startMailMain(mFunctionSelectActivity);;
				}
				else if(mFunction.typeName.toLowerCase().equals(Function.SEARCH))
				{
					Intent intent = new Intent(mFunctionSelectActivity, WebMessageActivity.class);
					intent.putExtra("isurl", true);
					intent.putExtra("smart", true);
					intent.putExtra("url", NetUtils.getInstance().praseUrl(InterskyApplication.mApp.mService,"/app/smart_search","token="+NetUtils.getInstance().token
							+"&userid="+ InterskyApplication.mApp.mAccount.mRecordId+"&cid="+ InterskyApplication.mApp.mAccount.mCompanyId));
					mFunctionSelectActivity.startActivity(intent);
				}
				else if(mFunction.typeName.toLowerCase().equals(Function.ADD))
				{
					Intent intent = new Intent(mFunctionSelectActivity, FunctionSelectActivity.class);
					mFunctionSelectActivity.startActivity(intent);
				}
				else
				{
					FunctionUtils.getInstance().startFunction(mFunctionSelectActivity,mFunction,InterskyApplication.mApp.mAccount.iscloud);
				}
			}
			else
			{
				if(mFunction.select)
				{
					mFunction.select = false;
					mFunctionSelectActivity.add.remove(mFunction);
					mFunctionSelectActivity.other.add(mFunction);

				}
				else
				{
					mFunction.select = true;
					mFunctionSelectActivity.add.add(mFunction);
					mFunctionSelectActivity.other.remove(mFunction);
				}
				updateData();
				mFunctionSelectActivity.functionAddAdapter.notifyItemChanged(position);
				mFunctionSelectActivity.functionSearchAddAdapter.notifyItemChanged(position);
			}
			

		}
	};

	public View.OnClickListener onAddListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FunctionUtils.getInstance().myFunction.clear();
			FunctionUtils.getInstance().myFunction.addAll(mFunctionSelectActivity.add);
			Function tmpB;
			tmpB = new Function();
			tmpB.mCaption = "添加常用";
			tmpB.typeName = Function.ADD;
			FunctionUtils.getInstance().myFunction.add(tmpB);
			FunctionUtils.getInstance().saveMyFunction();
			FunctionUtils.getInstance().otherFunction.clear();
			FunctionUtils.getInstance().otherFunction.addAll(mFunctionSelectActivity.other);
			Intent intent = new Intent(FunctionSelectActivity.ACTION_UPDATA_WORK_ITEMS);
			mFunctionSelectActivity.sendBroadcast(intent);
			mFunctionSelectActivity.finish();
		}
	};

	public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange() {
		@Override
		public void doTextChange(boolean visiable) {
			if(visiable == true)
			{
				mFunctionSelectActivity.search.clear();
				for(int i = 0 ; i < FunctionUtils.getInstance().mFunctions.size() ; i++)
				{
					Function function = FunctionUtils.getInstance().mFunctions.get(i);
					if(function.mCaption.contains(mFunctionSelectActivity.searchView.getText().toLowerCase()))
					{
						mFunctionSelectActivity.search.add(function);
					}
				}
				if(showshearch == false)
				{
					showshearch = true;
					mFunctionSelectActivity.listView.setAdapter(mFunctionSelectActivity.functionSearchAddAdapter);
				}
				mFunctionSelectActivity.functionSearchAddAdapter.notifyDataSetChanged();
			}
			else
			{
				if(showshearch == true)
				{
					showshearch = false;
					mFunctionSelectActivity.listView.setAdapter(mFunctionSelectActivity.functionAddAdapter);
//					if(mFunctionSelectActivity.getIntent().getBooleanExtra("search",false) == true)
//					{
//						mFunctionSelectActivity.functionSearchAddAdapter.notifyDataSetChanged();
//					}
//					else
//					{
//
//					}

				}
			}
		}
	};
}
