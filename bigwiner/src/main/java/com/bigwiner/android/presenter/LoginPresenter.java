package com.bigwiner.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bigwiner.R;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.LoginHandler;
import com.bigwiner.android.handler.MainHandler;
import com.bigwiner.android.receiver.LoginReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ForgetActivity;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.PickActivity;
import com.bigwiner.android.view.activity.RegisterActivity;
import com.bigwiner.android.view.activity.SafeActivity;
import com.bumptech.glide.Glide;
import com.tencent.bugly.crashreport.CrashReport;


import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.conturypick.Country;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.nettask.DownloadTask;
import xpx.com.toolbar.utils.ToolBarHelper;

public class LoginPresenter implements Presenter {

    public LoginActivity mLoginActivity;
    public LoginHandler mLoginHandler;
    public LoginPresenter(LoginActivity mLoginActivity) {
        this.mLoginActivity = mLoginActivity;
        mLoginHandler = new LoginHandler(mLoginActivity);
        mLoginActivity.setBaseReceiver(new LoginReceiver(mLoginHandler));
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mLoginActivity.setContentView(R.layout.activity_login);
        if(BigwinerApplication.mApp.mUpDataManager != null)
        BigwinerApplication.mApp.mUpDataManager.checkVersin();
        mLoginActivity.mToolBarHelper.hidToolbar2(mLoginActivity);
        ToolBarHelper.setBgColor(mLoginActivity, mLoginActivity.mActionBar, Color.rgb(255, 255, 255));
        mLoginActivity.mRegiester = mLoginActivity.findViewById(R.id.regiest);
        mLoginActivity.areaTxt = mLoginActivity.findViewById(R.id.area_text);
        mLoginActivity.btnArea = mLoginActivity.findViewById(R.id.phone_title);
        mLoginActivity.arename = mLoginActivity.findViewById(R.id.name_title);
        mLoginActivity.phoneNumber = (EditText) mLoginActivity.findViewById(R.id.phone_text);
        mLoginActivity.passWord = (EditText) mLoginActivity.findViewById(R.id.password_text);
        mLoginActivity.showPassword = (ImageView) mLoginActivity.findViewById(R.id.password_show_icon);
        mLoginActivity.phoneLayer = (RelativeLayout) mLoginActivity.findViewById(R.id.phone_number);
        mLoginActivity.passwordLayer = (RelativeLayout) mLoginActivity.findViewById(R.id.password_number);
        mLoginActivity.mForget = (TextView) mLoginActivity.findViewById(R.id.forget);
        mLoginActivity.btnLogin = (TextView) mLoginActivity.findViewById(R.id.login_btn);
        mLoginActivity.mForget.setOnClickListener(mLoginActivity.mForgetListener);
        mLoginActivity.showPassword.setOnClickListener(mLoginActivity.showPasswordListener);
        mLoginActivity.mRegiester.setOnClickListener(mLoginActivity.startRegisterListener);
        mLoginActivity.btnLogin.setOnClickListener(mLoginActivity.doLoginListener);
        mLoginActivity.safe = mLoginActivity.findViewById(R.id.a6);
        SpannableString content = new SpannableString(mLoginActivity.safe.getText().toString());
        content.setSpan(new UnderlineSpan(), 1, mLoginActivity.safe.getText().toString().length()-1, 0);
        mLoginActivity.safe.setText(content);
        mLoginActivity.safe.setOnClickListener(mLoginActivity.safeListener);
        mLoginActivity.btnArea.setOnClickListener(mLoginActivity.areaListener);
        mLoginActivity.areaTxt.setOnClickListener(mLoginActivity.areaListener);
        checkUser();
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
        mLoginHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub

        initView();
    }

    //操作方法

    public void showPassword() {
        if (mLoginActivity.showPassowrd == false) {
            mLoginActivity.showPassowrd = true;
            mLoginActivity.passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mLoginActivity.showPassword.setImageResource(R.drawable.showt2x);
        } else {
            mLoginActivity.showPassowrd = false;
            mLoginActivity.passWord.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mLoginActivity.showPassword.setImageResource(R.drawable.showf2x);
        }

    }

    public void doLogin()
    {
        Login(mLoginActivity.phoneNumber.getText().toString(),mLoginActivity.passWord.getText().toString());
    }

    public void startRegister()
    {
        Intent intent = new Intent(mLoginActivity, RegisterActivity.class);
        intent.putExtra("title",mLoginActivity.getString(R.string.btn_regiest));
        intent.putExtra("btn",mLoginActivity.getString(R.string.btn_regiest));
        mLoginActivity.startActivity(intent);
    }

    public void startForget()
    {
        Intent intent = new Intent(mLoginActivity, RegisterActivity.class);
        intent.putExtra("title",mLoginActivity.getString(R.string.forget_password_set));
        intent.putExtra("btn",mLoginActivity.getString(R.string.forget_submit));
        mLoginActivity.startActivity(intent);
    }

    public void cleanLogin() {

    }

    //网络方法
    private void Login(String username,String password)
    {
        SharedPreferences sharedPre = mLoginActivity.getSharedPreferences(UserDefine.LAST_USER, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        String usernameid = sharedPre.getString(UserDefine.USER_NAME,"");
        e.putString(UserDefine.USER_NAME, username);
        e.commit();
        SharedPreferences sharedPre1 = mLoginActivity.getSharedPreferences(username, 0);
        SharedPreferences.Editor e1 = sharedPre1.edit();
        e1.putString(UserDefine.USER_PASSWORD, password);
        e1.commit();
        mLoginActivity.waitDialog.show();
        BigwinerApplication.mApp.mAccount.mUserName = username;
        BigwinerApplication.mApp.mAccount.mPassword = password;
        LoginAsks.doLogin(mLoginActivity,mLoginHandler,username,password,mLoginActivity.areaTxt.getText().toString().replace("+",""));

    }

    public void checkUser()
    {
        mLoginActivity.phoneNumber.setText(BigwinerApplication.mApp.mAccount.mUserName);
        mLoginActivity.passWord.setText(BigwinerApplication.mApp.mAccount.mPassword);
        if(BigwinerApplication.mApp.mAccount.islogin)
        {
            Login(BigwinerApplication.mApp.mAccount.mUserName,BigwinerApplication.mApp.mAccount.mPassword);
        }
    }

    public void startMain()
    {
        Intent intent = new Intent(mLoginActivity, MainActivity.class);
        if(mLoginActivity.getIntent().hasExtra("shareopen"))
        {
            intent.putExtra("shareopen",true);
            intent.putExtra("type",mLoginActivity.getIntent().getStringExtra("type"));
            intent.putExtra("detialid",mLoginActivity.getIntent().getStringExtra("detialid"));
            intent.putExtra("typemodule",mLoginActivity.getIntent().getStringExtra("typemodule"));
        }
        mLoginActivity.startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mLoginActivity.backflag)
            {
                BigwinerApplication.mApp.exist();
            }
            else
            {
                AppUtils.showMessage(mLoginActivity,mLoginActivity.getString(R.string.system_exist));
                mLoginActivity.backflag = true;
                mLoginHandler.sendEmptyMessageDelayed(MainHandler.BACK_TIME_UPDATE,2000);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void showSafe() {
        Intent intent = new Intent(mLoginActivity, SafeActivity.class);
        mLoginActivity.startActivity(intent);
    }


    public void startArea() {
        Intent intent = new Intent(mLoginActivity, PickActivity.class);
        intent.setAction(RegisterActivity.ACTION_AREA);
        mLoginActivity.startActivity(intent);
    }

    public void setArecode(Intent data) {
        mLoginActivity.areaTxt.setText(data.getStringExtra("code"));
        mLoginActivity.arename.setText(data.getStringExtra("name"));
    }
}
