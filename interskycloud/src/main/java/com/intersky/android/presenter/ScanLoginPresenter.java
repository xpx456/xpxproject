package com.intersky.android.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.intersky.R;
import com.intersky.android.handler.ScanLoginHandler;
import com.intersky.android.view.activity.MainActivity;
import com.intersky.android.view.activity.ScanLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ScanLoginPresenter implements Presenter {

    public ScanLoginHandler mScanLoginHandler;
    public ScanLoginActivity mScanLoginActivity;
    public ScanLoginPresenter(ScanLoginActivity mScanLoginActivity)
    {
        this.mScanLoginActivity = mScanLoginActivity;
        this.mScanLoginHandler = new ScanLoginHandler(mScanLoginActivity);
    }

    @Override
    public void Create() {
        initView();
    }
//{"serial":"C7BDC05A-00C8-407B-8596-14F023561622","type":1}
    @Override
    public void initView() {
        mScanLoginActivity.setContentView(R.layout.activity_scan_login);
        Intent data = mScanLoginActivity.getIntent();
        Bundle bundle = data.getExtras();
        mScanLoginActivity.nomalLayer = mScanLoginActivity.findViewById(R.id.nomallayer);
        mScanLoginActivity.errorLayer = mScanLoginActivity.findViewById(R.id.errorlayer);
        mScanLoginActivity.txtError = mScanLoginActivity.findViewById(R.id.errortxt);
        mScanLoginActivity.btnOkLogin = mScanLoginActivity.findViewById(R.id.btn_oklogin);
        mScanLoginActivity.result = bundle.getString("result");
        try {
            JSONObject jsonObject = new JSONObject(mScanLoginActivity.result);
            mScanLoginActivity.serial = jsonObject.getString("serial");
            mScanLoginActivity.type = jsonObject.getString("type");
            mScanLoginActivity.errorLayer.setVisibility(View.GONE);
            mScanLoginActivity.btnOkLogin.setOnClickListener(mScanLoginActivity.loginListener);
        } catch (JSONException e) {
            e.printStackTrace();
            mScanLoginActivity.nomalLayer.setVisibility(View.GONE);
            mScanLoginActivity.txtError.setText(mScanLoginActivity.result);
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





    public void doFinish()
    {
        Intent mainIntent = new Intent();
        mainIntent.setClass(mScanLoginActivity, MainActivity.class);;
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mScanLoginActivity.startActivity(mainIntent);
        mScanLoginActivity.finish();
    }
}
