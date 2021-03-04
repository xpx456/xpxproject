package com.bigwiner.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.PasswordHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ForgetActivity;
import com.bigwiner.android.view.activity.PasswordActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.RegisterActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class PasswordPresenter implements Presenter {

    public PasswordActivity mPasswordActivity;
    public PasswordHandler mPasswordHandler;

    public PasswordPresenter(PasswordActivity mPasswordActivity) {
        this.mPasswordActivity = mPasswordActivity;
        mPasswordHandler = new PasswordHandler(mPasswordActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mPasswordActivity, Color.argb(0, 255, 255, 255));
        mPasswordActivity.setContentView(R.layout.activity_password);
        mPasswordActivity.back = mPasswordActivity.findViewById(R.id.back);
        mPasswordActivity.mToolBarHelper.hidToolbar(mPasswordActivity, (RelativeLayout) mPasswordActivity.findViewById(R.id.buttomaciton));
        mPasswordActivity.measureStatubar(mPasswordActivity, (RelativeLayout) mPasswordActivity.findViewById(R.id.stutebar));
        mPasswordActivity.mRegiester = mPasswordActivity.findViewById(R.id.regiest);
        mPasswordActivity.oldNumber = mPasswordActivity.findViewById(R.id.old_text);
        mPasswordActivity.error = mPasswordActivity.findViewById(R.id.error);
        mPasswordActivity.error2 = mPasswordActivity.findViewById(R.id.error2);
        mPasswordActivity.btnSubmit = mPasswordActivity.findViewById(R.id.login_btn);
        mPasswordActivity.phoneNumber = (EditText) mPasswordActivity.findViewById(R.id.phone_text);
        mPasswordActivity.passWord = (EditText) mPasswordActivity.findViewById(R.id.password_text);
        mPasswordActivity.phoneLayer = (RelativeLayout) mPasswordActivity.findViewById(R.id.phone_number);
        mPasswordActivity.passwordLayer = (RelativeLayout) mPasswordActivity.findViewById(R.id.password_number);
        mPasswordActivity.btnSubmit.setOnClickListener(mPasswordActivity.doPasswordListener);
        mPasswordActivity.back.setOnClickListener(mPasswordActivity.backListener);
        mPasswordActivity.phoneNumber.addTextChangedListener(mPasswordActivity.onTxtChangeListener);
        mPasswordActivity.passWord.addTextChangedListener(mPasswordActivity.onTxtChangeListener2);
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
        mPasswordHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    //操作方法


    public void doPassword() {
        if(mPasswordActivity.error.getVisibility()== View.INVISIBLE && mPasswordActivity.error2.getVisibility()== View.INVISIBLE
        && mPasswordActivity.passWord.getText().length() > 0 && mPasswordActivity.oldNumber.getText().length() > 0)
        {
            mPasswordActivity.waitDialog.show();
            LoginAsks.doChangeRegister(mPasswordActivity,mPasswordHandler,mPasswordActivity.phoneNumber.getText().toString(),mPasswordActivity.oldNumber.getText().toString());
        }
        else
        {

        }
    }

    public boolean checkPassword()
    {
//        Pattern pattern = Pattern.compile(mPasswordActivity.PW_PATTERN);
//        Matcher matcher = pattern.matcher(mPasswordActivity.phoneNumber.getText());
//        return matcher.matches();
        return true;
    }

    public void checkConfirm()
    {
        if(mPasswordActivity.passWord.getText().toString().equals(mPasswordActivity.phoneNumber.getText().toString()))
        {
            mPasswordActivity.error2.setVisibility(View.INVISIBLE);
        }
        else if(mPasswordActivity.passWord.length() > 0)
        {
            mPasswordActivity.error2.setVisibility(View.VISIBLE);
        }
        else {
            mPasswordActivity.error2.setVisibility(View.INVISIBLE);
        }
    }
}
