package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.handler.ConfirmCodeHandler;
import com.bigwiner.android.view.activity.ConfirmCodeActivity;
import com.bigwiner.android.view.activity.PasswordActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class ConfirmCodePresenter implements Presenter {

    public ConfirmCodeActivity mConfirmCodeActivity;
    public ConfirmCodeHandler mConfirmCodeHandler;

    public ConfirmCodePresenter(ConfirmCodeActivity mConfirmCodeActivity) {
        this.mConfirmCodeActivity = mConfirmCodeActivity;
        mConfirmCodeHandler = new ConfirmCodeHandler(mConfirmCodeActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mConfirmCodeActivity, Color.argb(0, 255, 255, 255));
        mConfirmCodeActivity.setContentView(R.layout.activity_confirmcode);
        mConfirmCodeActivity.mToolBarHelper.hidToolbar(mConfirmCodeActivity, (RelativeLayout) mConfirmCodeActivity.findViewById(R.id.buttomaciton));
        mConfirmCodeActivity.measureStatubar(mConfirmCodeActivity, (RelativeLayout) mConfirmCodeActivity.findViewById(R.id.stutebar));
        mConfirmCodeActivity.back = mConfirmCodeActivity.findViewById(R.id.back);
        mConfirmCodeActivity.title = mConfirmCodeActivity.findViewById(R.id.title);
        mConfirmCodeActivity.back.setOnClickListener(mConfirmCodeActivity.backListener);
        mConfirmCodeActivity.title.setText(mConfirmCodeActivity.getIntent().getStringExtra("title"));
        mConfirmCodeActivity.phoneNumber = (TextView) mConfirmCodeActivity.findViewById(R.id.phone_number);

        mConfirmCodeActivity.imf1 = (TextView) mConfirmCodeActivity.findViewById(R.id.imf);
        mConfirmCodeActivity.imf1.setText(mConfirmCodeActivity.getString(R.string.confirm_code_send_to)+mConfirmCodeActivity.getIntent().getStringExtra("phone"));
        mConfirmCodeActivity.imf2 = (TextView) mConfirmCodeActivity.findViewById(R.id.imf2);
        mConfirmCodeActivity.imf1.setText(String.valueOf(mConfirmCodeActivity.second)+mConfirmCodeActivity.getString(R.string.confirm_code_reget));
        mConfirmCodeActivity.btnConfirmCode = (TextView) mConfirmCodeActivity.findViewById(R.id.register_btn);

        mConfirmCodeActivity.btnConfirmCode.setOnClickListener(mConfirmCodeActivity.nextListener);
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

    //操作方法
    public void setPassWord() {
        Intent intent = new Intent(mConfirmCodeActivity, PasswordActivity.class);
        intent.putExtra("phone",mConfirmCodeActivity.getIntent().getStringExtra("phone"));
        mConfirmCodeActivity.startActivity(intent);
    }

    public void startTime()
    {
        mConfirmCodeActivity.second = ConfirmCodeActivity.CODE_SECOND;
        mConfirmCodeHandler.sendEmptyMessageDelayed(ConfirmCodeActivity.EVENT_UPDATA_CODE_SECOND,1000);
    }

    public void updataTime()
    {
        mConfirmCodeActivity.second--;
        if(mConfirmCodeActivity.second > 0)
        {
            mConfirmCodeHandler.sendEmptyMessageDelayed(ConfirmCodeActivity.EVENT_UPDATA_CODE_SECOND,1000);
        }
        else
        {

        }
    }

}
