package com.bigwiner.android.presenter;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.handler.GetPasswordHandler;
import com.bigwiner.android.view.activity.GetPasswordActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class GetPasswordPresenter implements Presenter {

    public GetPasswordActivity mGetPasswordActivity;
    public GetPasswordHandler mGetPasswordHandler;

    public GetPasswordPresenter(GetPasswordActivity mGetPasswordActivity) {
        this.mGetPasswordActivity = mGetPasswordActivity;
        mGetPasswordHandler = new GetPasswordHandler(mGetPasswordActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mGetPasswordActivity, Color.argb(0, 255, 255, 255));
        mGetPasswordActivity.setContentView(R.layout.activity_getpassword);
        mGetPasswordActivity.mToolBarHelper.hidToolbar(mGetPasswordActivity, (RelativeLayout) mGetPasswordActivity.findViewById(R.id.buttomaciton));
        mGetPasswordActivity.measureStatubar(mGetPasswordActivity, (RelativeLayout) mGetPasswordActivity.findViewById(R.id.stutebar));
        mGetPasswordActivity.btnGetPassword = (TextView) mGetPasswordActivity.findViewById(R.id.phone_text);

        mGetPasswordActivity.btnGetPassword.setOnClickListener(mGetPasswordActivity.nextListener);
    }

    ;

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }



    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mSplashActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mSplashActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }










}
