package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.handler.SailApplyHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MeetingContactsListActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;
import com.bigwiner.android.view.activity.SourceSelectActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SailApplyPresenter implements Presenter {

    public SailApplyActivity mSailApplyActivity;
    public SailApplyHandler mSailApplyHandler;
    public SailApplyPresenter(SailApplyActivity mSailApplyActivity)
    {
        mSailApplyHandler =new SailApplyHandler(mSailApplyActivity);
        this.mSailApplyActivity = mSailApplyActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mSailApplyActivity.setContentView(R.layout.activity_sail_apply);
        mSailApplyActivity.mToolBarHelper.hidToolbar2(mSailApplyActivity);
        ToolBarHelper.setBgColor(mSailApplyActivity, mSailApplyActivity.mActionBar, Color.rgb(255, 255, 255));
        mSailApplyActivity.back = mSailApplyActivity.findViewById(R.id.back);

        mSailApplyActivity.cname = mSailApplyActivity.findViewById(R.id.cnamevalue);
        mSailApplyActivity.caddress = mSailApplyActivity.findViewById(R.id.caddressvalue);
        mSailApplyActivity.contact = mSailApplyActivity.findViewById(R.id.contactsvalue);
        mSailApplyActivity.phone = mSailApplyActivity.findViewById(R.id.mobilvalue);
        mSailApplyActivity.business = mSailApplyActivity.findViewById(R.id.businessvalue);
        mSailApplyActivity.commit = mSailApplyActivity.findViewById(R.id.join_in);
        mSailApplyActivity.cname.setText(BigwinerApplication.mApp.company.name);
        mSailApplyActivity.caddress.setText(BigwinerApplication.mApp.company.address);
        mSailApplyActivity.commit.setOnClickListener(mSailApplyActivity.applyListener);
        mSailApplyActivity.back.setOnClickListener(mSailApplyActivity.backListener);
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



    public void doapply() {
        mSailApplyActivity.waitDialog.show();
        SailAsks.applaySail(mSailApplyActivity,mSailApplyHandler
                ,mSailApplyActivity.contact.getText().toString(),mSailApplyActivity.phone.getText().toString(),mSailApplyActivity.business.getText().toString());
    }
}
