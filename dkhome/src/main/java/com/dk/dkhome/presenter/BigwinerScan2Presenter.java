package com.dk.dkhome.presenter;

import android.hardware.Camera;
import android.widget.TextView;


import com.dk.dkhome.R;
import com.dk.dkhome.view.activity.BigwinerScan2Activity;

import intersky.scan.presenter.BaseMipcaCapture2Presenter;
import mining.app.zxing.camera.CameraManager;

/**
 * Created by xpx on 2017/8/18.
 */

public class BigwinerScan2Presenter extends BaseMipcaCapture2Presenter {

    public BigwinerScan2Activity mBigwinerScanActivity;
    public BigwinerScan2Presenter(BigwinerScan2Activity mBigwinerScanActivity)
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
        mBigwinerScanActivity.setContentView(R.layout.activity_scan);
        //mBigwinerScanActivity.measureStatubar(mBigwinerScanActivity, (RelativeLayout) mBigwinerScanActivity.findViewById(R.id.stutebar));
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
