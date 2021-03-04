package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.handler.SailHandler;
import com.bigwiner.android.receiver.SailReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ComplaintActivity;
import com.bigwiner.android.view.activity.PicViewActivity;
import com.bigwiner.android.view.activity.SailActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;
import com.bigwiner.android.view.activity.SailMemberActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SailPresenter implements Presenter {

    public SailActivity mSailActivity;
    public SailHandler mSailHandler;
    public SailPresenter(SailActivity mSailActivity)
    {
        mSailHandler =new SailHandler(mSailActivity);
        this.mSailActivity = mSailActivity;
        mSailActivity.setBaseReceiver(new SailReceiver(mSailHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSailActivity, Color.argb(0, 255, 255, 255));
        mSailActivity.setContentView(R.layout.activity_sail);
        mSailActivity.mToolBarHelper.hidToolbar(mSailActivity, (RelativeLayout) mSailActivity.findViewById(R.id.buttomaciton));
        mSailActivity.measureStatubar(mSailActivity, (RelativeLayout) mSailActivity.findViewById(R.id.stutebar));
        mSailActivity.back = mSailActivity.findViewById(R.id.back);
        mSailActivity.back.setOnClickListener(mSailActivity.backListener);

        mSailActivity.desBtn = mSailActivity.findViewById(R.id.des);
        mSailActivity.memberBtn = mSailActivity.findViewById(R.id.person);
        mSailActivity.complaintBtn = mSailActivity.findViewById(R.id.redblack);
        mSailActivity.btnApply = mSailActivity.findViewById(R.id.join_in);
        if(BigwinerApplication.mApp.mAccount.issail)
        {
            mSailActivity.btnApply.setVisibility(View.INVISIBLE);
        }
        mSailActivity.btnApply.setOnClickListener(mSailActivity.applySailListener);
        mSailActivity.desBtn.setOnClickListener(mSailActivity.desListener);
        mSailActivity.memberBtn.setOnClickListener(mSailActivity.memberListener);
        mSailActivity.complaintBtn.setOnClickListener(mSailActivity.complaintListener);


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

    public void showDes()
    {
        Intent intent = new Intent(mSailActivity, PicViewActivity.class);
        intent.putExtra("id","saildes.jpg");
        intent.putExtra("title",mSailActivity.getString(R.string.sail_des_title));
        mSailActivity.startActivity(intent);
    }

    public void showMember()
    {
        if(BigwinerApplication.mApp.mAccount.issail)
        {
            Intent intent = new Intent(mSailActivity, SailMemberActivity.class);
            mSailActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mSailActivity,mSailActivity.getString(R.string.sail_error_member));
        }
    }

    public void showComplaint()
    {
//        if(BigwinerApplication.mApp.mAccount.issail)
//        {
//            Intent intent = new Intent(mSailActivity, ComplaintActivity.class);
//            mSailActivity.startActivity(intent);
//        }
//        else
//        {
//            AppUtils.showMessage(mSailActivity,mSailActivity.getString(R.string.sail_error_member));
//        }
        Intent intent = new Intent(mSailActivity, ComplaintActivity.class);
        mSailActivity.startActivity(intent);
    }

    public void doApplay() {
        Intent intent = new Intent(mSailActivity, SailApplyActivity.class);
        mSailActivity.startActivity(intent);
    }
}
