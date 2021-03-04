package com.bigwiner.android.presenter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.handler.ScanResultHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ScanResultActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ScanResultPresenter implements Presenter {

    public ScanResultActivity mScanResultActivity;
    public ScanResultHandler mScanResultHandler;
    public ScanResultPresenter(ScanResultActivity mScanResultActivity)
    {
        mScanResultHandler =new ScanResultHandler(mScanResultActivity);
        this.mScanResultActivity = mScanResultActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mScanResultActivity, Color.argb(0, 255, 255, 255));
        mScanResultActivity.setContentView(R.layout.activity_scanresult);
        mScanResultActivity.mToolBarHelper.hidToolbar(mScanResultActivity, (RelativeLayout) mScanResultActivity.findViewById(R.id.buttomaciton));
        mScanResultActivity.measureStatubar(mScanResultActivity, (RelativeLayout) mScanResultActivity.findViewById(R.id.stutebar));
        mScanResultActivity.back = mScanResultActivity.findViewById(R.id.back);
        mScanResultActivity.back.setOnClickListener(mScanResultActivity.backListener);
        mScanResultActivity.imf = mScanResultActivity.findViewById(R.id.imf2);
        Bundle bundle = mScanResultActivity.getIntent().getExtras();
        mScanResultActivity.id = bundle.getString("result");
        mScanResultActivity.waitDialog.show();
        ContactsAsks.scanCode(mScanResultActivity,mScanResultHandler,mScanResultActivity.id);

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
