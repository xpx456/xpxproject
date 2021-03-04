package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.handler.SignResultHandler;
import com.bigwiner.android.prase.DetialPrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SignResultActivity;
import com.bigwiner.android.view.activity.SourceSelectActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SignResultPresenter implements Presenter {

    public SignResultActivity mSignResultActivity;
    public SignResultHandler mSignResultHandler;
    public SignResultPresenter(SignResultActivity mSignResultActivity)
    {
        mSignResultHandler =new SignResultHandler(mSignResultActivity);
        this.mSignResultActivity = mSignResultActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSignResultActivity, Color.argb(0, 255, 255, 255));
        mSignResultActivity.setContentView(R.layout.activity_signresult);
        mSignResultActivity.mToolBarHelper.hidToolbar(mSignResultActivity, (RelativeLayout) mSignResultActivity.findViewById(R.id.buttomaciton));
        mSignResultActivity.measureStatubar(mSignResultActivity, (RelativeLayout) mSignResultActivity.findViewById(R.id.stutebar));
        mSignResultActivity.back = mSignResultActivity.findViewById(R.id.back);
        mSignResultActivity.back.setOnClickListener(mSignResultActivity.backListener);
        mSignResultActivity.txt1 = mSignResultActivity.findViewById(R.id.txt1);
        mSignResultActivity.txt3 = mSignResultActivity.findViewById(R.id.txt3);
        mSignResultActivity.name = mSignResultActivity.findViewById(R.id.name);
        mSignResultActivity.count = mSignResultActivity.findViewById(R.id.txt2);
        mSignResultActivity.icon = mSignResultActivity.findViewById(R.id.img);
        mSignResultActivity.imf = mSignResultActivity.findViewById(R.id.imf2);
        mSignResultActivity.title = mSignResultActivity.findViewById(R.id.titletext);
        mSignResultActivity.name.setText(BigwinerApplication.mApp.mAccount.mUserName);
        mSignResultActivity.icon.setVisibility(View.INVISIBLE);
        mSignResultActivity.imf.setVisibility(View.INVISIBLE);
        mSignResultActivity.txt1.setVisibility(View.INVISIBLE);
        mSignResultActivity.txt3.setVisibility(View.INVISIBLE);
        Bundle bundle = mSignResultActivity.getIntent().getExtras();
        mSignResultActivity.id = bundle.getString("result");
        Meeting meeting = bundle.getParcelable("object");
        mSignResultActivity.waitDialog.show();
        DetialAsks.getMettingsSign(mSignResultActivity,mSignResultHandler,meeting.recordid);

    }


    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
    }

}
