package intersky.guide.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import intersky.appbase.ScreenDefine;
import intersky.guide.GuideUtils;
import intersky.guide.presenter.GuidePresenter;
import intersky.guide.view.adapter.GuidePageAdapter;
import intersky.mywidget.NoScrollViewPager;

public class GuideActivity extends BaseActivity
{
	public final static int Guide_DISPLAY_LENGHT = 3000; // 延迟3秒
	public ScreenDefine mScreenDefine;
	public NoScrollViewPager mViewPager;
	public GuidePageAdapter mLoderPageAdapter;
	public ArrayList<View> mViews = new ArrayList<View>();
	public ArrayList<View> dianlist = new ArrayList<View>();
	public LinearLayout dians;
	public GuidePresenter mGuidePresenter = new GuidePresenter(this);
	public TextView leftbtn;
	public TextView rightbtn;
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mGuidePresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mGuidePresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mGuidePresenter.Start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onPause()
	{
		mGuidePresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mGuidePresenter.Resume();
		super.onResume();
	}


	public View.OnClickListener startNextListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			if(mViewPager.getCurrentItem() == GuideUtils.getInstance().pics.size() -1)
			mGuidePresenter.startNext();
			else
			{
				mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
				mGuidePresenter.setDian();
			}
		}
	};


	public View.OnClickListener startSkipListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mGuidePresenter.startNext();
		}
	};
}
