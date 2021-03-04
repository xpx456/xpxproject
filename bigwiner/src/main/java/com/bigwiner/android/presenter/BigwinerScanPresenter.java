package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.view.activity.BigwinerScanActivity;

import intersky.scan.presenter.BaseMipcaCapturePresenter;
import mining.app.zxing.camera.CameraManager;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class BigwinerScanPresenter extends BaseMipcaCapturePresenter {

    public BigwinerScanActivity mBigwinerScanActivity;
    public BigwinerScanPresenter(BigwinerScanActivity mBigwinerScanActivity)
    {
        super(mBigwinerScanActivity);
        this.mBigwinerScanActivity = mBigwinerScanActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mBigwinerScanActivity, Color.argb(0, 255, 255, 255));
        mBigwinerScanActivity.setContentView(R.layout.activity_scan);
        mBigwinerScanActivity.mToolBarHelper.hidToolbar2(mBigwinerScanActivity, (RelativeLayout) mBigwinerScanActivity.findViewById(R.id.buttomaciton));
        mBigwinerScanActivity.measureStatubar(mBigwinerScanActivity, (RelativeLayout) mBigwinerScanActivity.findViewById(R.id.stutebar));
        if(mBigwinerScanActivity.getIntent().hasExtra("meeting"))
        {
            mBigwinerScanActivity.meeting = mBigwinerScanActivity.getIntent().getParcelableExtra("meeting");
            mBigwinerScanActivity.object = mBigwinerScanActivity.meeting;
        }
        if(mBigwinerScanActivity.getIntent().hasExtra("title"))
        {
            TextView textView = mBigwinerScanActivity.findViewById(R.id.titletext);
            textView.setText(mBigwinerScanActivity.getIntent().getStringExtra("title"));
        }
        mBigwinerScanActivity.back = mBigwinerScanActivity.findViewById(R.id.back);
        mBigwinerScanActivity.mTextView = mBigwinerScanActivity.findViewById(R.id.text);
        mBigwinerScanActivity.light = mBigwinerScanActivity.findViewById(R.id.lightlyaer);
        mBigwinerScanActivity.back.setOnClickListener(mBigwinerScanActivity.backListener);
        mBigwinerScanActivity.light.setOnClickListener(mBigwinerScanActivity.lightListener);

    }

    public void doLight() {
        if (mBigwinerScanActivity.hasClosed) {
            mBigwinerScanActivity.parameters = CameraManager.get().getCamera().getParameters();
            mBigwinerScanActivity.parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 开启
            CameraManager.get().getCamera().setParameters(mBigwinerScanActivity.parameters);
            mBigwinerScanActivity.hasClosed = false;
            mBigwinerScanActivity.mTextView.setText(mBigwinerScanActivity.getString(R.string.meeting_light_close));
        } else {
            mBigwinerScanActivity.parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);// 关闭
            CameraManager.get().getCamera().setParameters(mBigwinerScanActivity.parameters);
            mBigwinerScanActivity.hasClosed = true;
            mBigwinerScanActivity.mTextView.setText(mBigwinerScanActivity.getString(R.string.meeting_light));
        }
    }
}
