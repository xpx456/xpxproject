package com.bigwiner.android.presenter;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;

import com.bigwiner.android.view.activity.AboutActivity;
import com.bigwiner.android.view.activity.SafeActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class AboutPresenter implements Presenter {

    public AboutActivity mAboutActivity;
    public AboutPresenter(AboutActivity mAboutActivity)
    {
        this.mAboutActivity = mAboutActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mAboutActivity, Color.argb(0, 255, 255, 255));
        mAboutActivity.setContentView(R.layout.activity_about);
        mAboutActivity.mToolBarHelper.hidToolbar(mAboutActivity, (RelativeLayout) mAboutActivity.findViewById(R.id.buttomaciton));
        mAboutActivity.measureStatubar(mAboutActivity, (RelativeLayout) mAboutActivity.findViewById(R.id.stutebar));
        mAboutActivity.back = mAboutActivity.findViewById(R.id.back);
        mAboutActivity.safe = mAboutActivity.findViewById(R.id.a6);
        mAboutActivity.back.setOnClickListener(mAboutActivity.backListener);
        mAboutActivity.safe.setOnClickListener(mAboutActivity.safeListener);
        PackageManager packageManager = mAboutActivity.getPackageManager();
        try
        {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mAboutActivity.getPackageName(), 0);
            TextView version = (TextView) mAboutActivity.findViewById(R.id.version_name);
            version.setText("V" + packInfo.versionName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public void showSafe() {
        Intent intent = new Intent(mAboutActivity, SafeActivity.class);
        mAboutActivity.startActivity(intent);
    }


}
