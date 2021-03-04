package com.bigwiner.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.handler.ForgetHandler;
import com.bigwiner.android.view.activity.ForgetActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import intersky.appbase.Presenter;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xpx.com.toolbar.utils.ToolBarHelper;

public class ForgetPresenter implements Presenter {

    public ForgetActivity mForgetActivity;
    public ForgetHandler mForgetHandler;

    public ForgetPresenter(ForgetActivity mForgetActivity) {
        this.mForgetActivity = mForgetActivity;
        mForgetHandler = new ForgetHandler(mForgetActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mForgetActivity, Color.argb(0, 255, 255, 255));
        mForgetActivity.setContentView(R.layout.activity_forget);
        mForgetActivity.mToolBarHelper.hidToolbar(mForgetActivity, (RelativeLayout) mForgetActivity.findViewById(R.id.buttomaciton));
        mForgetActivity.measureStatubar(mForgetActivity, (RelativeLayout) mForgetActivity.findViewById(R.id.stutebar));

        mForgetActivity.phoneNumber = (EditText) mForgetActivity.findViewById(R.id.phone_text);
        mForgetActivity.passWord = (EditText) mForgetActivity.findViewById(R.id.password_text);
        mForgetActivity.code = (EditText) mForgetActivity.findViewById(R.id.code_text);
        mForgetActivity.passWordConfirm = (EditText) mForgetActivity.findViewById(R.id.password_confirm_text);

        mForgetActivity.phoneNumberIcon = (ImageView) mForgetActivity.findViewById(R.id.phone_icon);
        mForgetActivity.passWordIcon = (ImageView) mForgetActivity.findViewById(R.id.password_icon);
        mForgetActivity.codeIcon = (ImageView) mForgetActivity.findViewById(R.id.code_icon);
        mForgetActivity.passWordConfirmIcon = (ImageView) mForgetActivity.findViewById(R.id.password_confirm_icon);

        mForgetActivity.showPassword = (ImageView) mForgetActivity.findViewById(R.id.password_show_icon);
        mForgetActivity.showPasswordConfirm = (ImageView) mForgetActivity.findViewById(R.id.password_confirm_show_icon);

        mForgetActivity.phoneLayer = (RelativeLayout) mForgetActivity.findViewById(R.id.phone_number);
        mForgetActivity.passwordLayer = (RelativeLayout) mForgetActivity.findViewById(R.id.password_number);
        mForgetActivity.passwordConfirmLayer = (RelativeLayout) mForgetActivity.findViewById(R.id.password_confirm_number);
        mForgetActivity.codeLayer = (RelativeLayout) mForgetActivity.findViewById(R.id.code_number);

        mForgetActivity.btnGetCode = (TextView) mForgetActivity.findViewById(R.id.code_get_btn);
        mForgetActivity.btnForget = (TextView) mForgetActivity.findViewById(R.id.register_btn);

        mForgetActivity.phoneNumber.setOnFocusChangeListener(mForgetActivity.phoneNumberChange);
        mForgetActivity.passWord.setOnFocusChangeListener(mForgetActivity.passwardChange);
        mForgetActivity.passWordConfirm.setOnFocusChangeListener(mForgetActivity.passwardConfirmChange);
        mForgetActivity.code.setOnFocusChangeListener(mForgetActivity.codeChange);

        mForgetActivity.showPassword.setOnClickListener(mForgetActivity.showPasswordListener);
        mForgetActivity.showPasswordConfirm.setOnClickListener(mForgetActivity.showPasswordConfirmListener);
        mForgetActivity.btnGetCode.setOnClickListener(mForgetActivity.getCodeListener);
        mForgetActivity.btnForget.setOnClickListener(mForgetActivity.ForgetListener);
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
    public void passwordChange(boolean iselect) {
//        if (iselect) {
//            mForgetActivity.passWordIcon.setImageResource(R.drawable.password_hl2x);
//            mForgetActivity.passwordLayer.setBackgroundResource(R.drawable.shape_login_bgs);
//        } else {
//            mForgetActivity.passWordIcon.setImageResource(R.drawable.password_mor2x);
//            mForgetActivity.passwordLayer.setBackgroundResource(R.drawable.shape_login_bg);
//        }

    }

    public void phoneChange(boolean iselect) {
//        if (iselect) {
//            mForgetActivity.phoneNumberIcon.setImageResource(R.drawable.phone_hl2x);
//            mForgetActivity.phoneLayer.setBackgroundResource(R.drawable.shape_login_bgs);
//        } else {
//            mForgetActivity.phoneNumberIcon.setImageResource(R.drawable.phone_mor2x);
//            mForgetActivity.phoneLayer.setBackgroundResource(R.drawable.shape_login_bg);
//        }

    }

    public void passwordConfirmChange(boolean iselect) {
//        if (iselect) {
//            mForgetActivity.passWordConfirmIcon.setImageResource(R.drawable.password_hl2x);
//            mForgetActivity.passwordConfirmLayer.setBackgroundResource(R.drawable.shape_login_bgs);
//        } else {
//            mForgetActivity.passWordConfirmIcon.setImageResource(R.drawable.password_mor2x);
//            mForgetActivity.passwordConfirmLayer.setBackgroundResource(R.drawable.shape_login_bg);
//        }

    }

    public void codeChange(boolean iselect) {
//        if (iselect) {
//            mForgetActivity.codeIcon.setImageResource(R.drawable.password_hl2x);
//            mForgetActivity.codeLayer.setBackgroundResource(R.drawable.shape_login_bgs);
//        } else {
//            mForgetActivity.codeIcon.setImageResource(R.drawable.password_mor2x);
//            mForgetActivity.codeLayer.setBackgroundResource(R.drawable.shape_login_bg);
//        }

    }

    public void showPassword() {
        if (mForgetActivity.showPassowrd == false) {
            mForgetActivity.showPassowrd = true;
            mForgetActivity.passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mForgetActivity.showPassword.setImageResource(R.drawable.showt2x);
        } else {
            mForgetActivity.showPassowrd = false;
            mForgetActivity.passWord.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mForgetActivity.showPassword.setImageResource(R.drawable.showf2x);
        }

    }

    public void showPasswordConfirm() {
        if (mForgetActivity.showPassowrdConfirm == false) {
            mForgetActivity.showPassowrdConfirm = true;
            mForgetActivity.passWordConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mForgetActivity.showPasswordConfirm.setImageResource(R.drawable.showt2x);
        } else {
            mForgetActivity.showPassowrdConfirm = false;
            mForgetActivity.passWordConfirm.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mForgetActivity.showPasswordConfirm.setImageResource(R.drawable.showf2x);
        }

    }



    public void startForget()
    {
        doForget();
    }


    //网络方法
    public void doForget()
    {

    }

    public void getCode()
    {

    }

    public void checkUser()
    {


    }

    public void verCode()
    {

    }

    //其他方法

    public void updataTime()
    {
        mForgetActivity.second--;
        if(mForgetActivity.second > 0)
        {
            mForgetActivity.btnGetCode.setText(String.valueOf(mForgetActivity.second)+"秒后再获取");
            mForgetHandler.sendEmptyMessageDelayed(ForgetActivity.EVENT_UPDATA_CODE_SECOND,1000);
        }
        else
        {
            mForgetActivity.btnGetCode.setEnabled(true);
            mForgetActivity.btnGetCode.setBackgroundResource(R.drawable.shape_login_bg_btn);
            mForgetActivity.btnGetCode.setText(mForgetActivity.getString(R.string.btn_getcode));
        }
    }

}
