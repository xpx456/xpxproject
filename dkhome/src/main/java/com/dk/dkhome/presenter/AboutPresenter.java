package com.dk.dkhome.presenter;

import android.widget.ImageView;


import com.dk.dkhome.R;
import com.dk.dkhome.view.activity.AboutActivity;

import intersky.appbase.Presenter;

public class AboutPresenter implements Presenter {

	public AboutActivity mAboutActivity;
	
	public AboutPresenter(AboutActivity mAboutActivity) {
		this.mAboutActivity = mAboutActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mAboutActivity.setContentView(R.layout.activity_about);
		ImageView back = mAboutActivity.findViewById(R.id.back);
		back.setOnClickListener(mAboutActivity.mBackListener);
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

}
