package com.exhibition.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.location.AMapLocationListener;
import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;
import com.exhibition.handler.LoginHandler;
import com.exhibition.receiver.LoginReceiver;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.activity.LoginActivity;
import com.exhibition.view.activity.MainActivity;
import com.finger.FingerManger;
import com.finger.entity.Finger;

import org.json.JSONException;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.conturypick.DbHelper;

public class LoginPresenter implements Presenter {

    public LoginActivity mLoginActivity;
    public LoginHandler mLoginHandler;
    public LoginPresenter(LoginActivity loginActivity) {
        mLoginActivity = loginActivity;
        mLoginHandler = new LoginHandler(mLoginActivity);
        mLoginActivity.setBaseReceiver(new LoginReceiver(mLoginHandler));
    }

    @Override
    public void initView() {
        mLoginActivity.flagFillBack = false;
        mLoginActivity.setContentView(R.layout.activity_login);
        mLoginActivity.title = mLoginActivity.findViewById(R.id.login_title);
        mLoginActivity.hid = mLoginActivity.findViewById(R.id.activity_login);
        mLoginActivity.botton1 = mLoginActivity.findViewById(R.id.button1);
        mLoginActivity.botton2 = mLoginActivity.findViewById(R.id.button2);
        mLoginActivity.image1 = mLoginActivity.findViewById(R.id.buttonimg1);
        mLoginActivity.image2 = mLoginActivity.findViewById(R.id.buttonimg2);
        mLoginActivity.title1 = mLoginActivity.findViewById(R.id.button1title);
        mLoginActivity.title2 = mLoginActivity.findViewById(R.id.button2title);
        mLoginActivity.user = mLoginActivity.findViewById(R.id.usernameedit);
        mLoginActivity.password = mLoginActivity.findViewById(R.id.passwordedit);
        mLoginActivity.btnLogin = mLoginActivity.findViewById(R.id.login_btn);
        mLoginActivity.logingOut = mLoginActivity.findViewById(R.id.login_out);
        mLoginActivity.lastSecond = mLoginActivity.findViewById(R.id.count);
        mLoginActivity.fingerlayer = mLoginActivity.findViewById(R.id.fingerinarea);
        mLoginActivity.passwordlayer = mLoginActivity.findViewById(R.id.passwordloginarea);
        mLoginActivity.imageView = mLoginActivity.findViewById(R.id.fingerimg);
        mLoginActivity.lastSecond.setText(String.valueOf(ExhibitionApplication.mApp.timeoud));
        mLoginActivity.btnLogin.setOnClickListener(loginListener);
        mLoginActivity.logingOut.setOnClickListener(logoutListener);
        mLoginActivity.hid.setOnClickListener(hidinputListener);
        mLoginActivity.title.setText(mLoginActivity.getString(R.string.main_title1)+ExhibitionApplication.mApp.getName()+mLoginActivity.getString(R.string.main_title2));
        if(ExhibitionApplication.mApp.TEST == false)
        ExhibitionApplication.mApp.icCardManager.getCardIds.add(mLoginActivity.getCardId);
        setPasswordLoginBtn();
        updataTime();
    }

    @Override
    public void Create() {
        initView();
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
        if(ExhibitionApplication.mApp.TEST == false)
        ExhibitionApplication.mApp.icCardManager.getCardIds.remove(mLoginActivity.getCardId);
    }

    public void updataName()
    {
        mLoginActivity.title.setText(mLoginActivity.getString(R.string.main_title1)+ExhibitionApplication.mApp.getName()+mLoginActivity.getString(R.string.main_title2));
    }

    public void updataTime() {

        mLoginActivity.lastSecond.setText(String.valueOf(ExhibitionApplication.mApp.timeoud));
        if(mLoginHandler != null)
        {
            mLoginHandler.removeMessages(LoginHandler.UPDATA_TIME);
            mLoginHandler.sendEmptyMessageDelayed(LoginHandler.UPDATA_TIME,1000);
        }

    }

    public void updataTimeout() {
        ExhibitionApplication.mApp.setTimeMax();
    }

    public void setPasswordLoginBtn() {
        mLoginActivity.botton1.setBackgroundResource(R.drawable.login_round_green_btn);
        mLoginActivity.botton2.setBackgroundResource(R.drawable.finger_get_bg);
        mLoginActivity.image1.setImageResource(R.drawable.face);
        mLoginActivity.image2.setImageResource(R.drawable.finger);
        mLoginActivity.title1.setText(mLoginActivity.getString(R.string.face));
        mLoginActivity.title2.setText(mLoginActivity.getString(R.string.finger));
        mLoginActivity.botton1.setOnClickListener(faceListener);
        mLoginActivity.botton2.setOnClickListener(fingerListener);
        mLoginActivity.passwordlayer.setVisibility(View.VISIBLE);
        mLoginActivity.fingerlayer.setVisibility(View.INVISIBLE);
        if(ExhibitionApplication.mApp.TEST == false)
        ExhibitionApplication.mApp.fingerManger.stopReconize();
    }

    public void setFaceLoginBtn() {

        AppUtils.showMessage(mLoginActivity,mLoginActivity.getString(R.string.login_no_function));
//        mLoginActivity.botton1.setBackgroundResource(R.drawable.login_round_blue_btn);
//        mLoginActivity.botton2.setBackgroundResource(R.drawable.finger_get_bg);
//        mLoginActivity.image1.setImageResource(R.drawable.code);
//        mLoginActivity.image2.setImageResource(R.drawable.finger);
//        mLoginActivity.title1.setText(mLoginActivity.getString(R.string.code));
//        mLoginActivity.title2.setText(mLoginActivity.getString(R.string.finger));
//        mLoginActivity.botton1.setOnClickListener(codeListener);
//        mLoginActivity.botton2.setOnClickListener(fingerListener);
    }

    public void setFingerLoginBtn() {
        mLoginActivity.botton1.setBackgroundResource(R.drawable.login_round_blue_btn);
        mLoginActivity.botton2.setBackgroundResource(R.drawable.login_round_green_btn);
        mLoginActivity.image1.setImageResource(R.drawable.code);
        mLoginActivity.image2.setImageResource(R.drawable.face);
        mLoginActivity.title1.setText(mLoginActivity.getString(R.string.code));
        mLoginActivity.title2.setText(mLoginActivity.getString(R.string.face));
        mLoginActivity.botton1.setOnClickListener(codeListener);
        mLoginActivity.botton2.setOnClickListener(faceListener);
        mLoginActivity.fingerlayer.setVisibility(View.VISIBLE);
        mLoginActivity.passwordlayer.setVisibility(View.INVISIBLE);
        mLoginActivity.imageView.setImageResource(R.drawable.finger);
        if(ExhibitionApplication.mApp.TEST == false)
        ExhibitionApplication.mApp.fingerManger.startReconize();
    }

    public void doLogin() {

        String password = ExhibitionApplication.mApp.getPassword();
        if(mLoginActivity.user.getText().toString().equals(ExhibitionApplication.ACCOUNT) && mLoginActivity.password.getText().toString().equals(password))
        {
            mLoginActivity.cangetdata = false;
            if(ExhibitionApplication.mApp.TEST == false)
            ExhibitionApplication.mApp.fingerManger.stopReconize();
            ExhibitionApplication.mApp.isadmin = true;
            Intent intent = new Intent(mLoginActivity, MainActivity.class);
            mLoginActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mLoginActivity,mLoginActivity.getString(R.string.login_error));
        }
    }

    public void praseLoginImf(Intent intent)
    {
        if(intent.getBooleanExtra("success",false) == true)
        {
            String fearid = intent.getStringExtra("feaid");
            String id = fearid.substring(0,36);
            Finger finger = DBHelper.getInstance(mLoginActivity).getFinger(id);
            Guest guest = null;
            if(finger != null)
            {
                guest = DBHelper.getInstance(mLoginActivity).getGuestInfo(finger.rid);
            }

            if(guest == null)
            {
                AppUtils.showMessage(mLoginActivity,mLoginActivity.getString(R.string.login_error2));
                ExhibitionApplication.mApp.fingerManger.startReconize();
            }
            else
            {
                mLoginActivity.cangetdata = false;
                if(ExhibitionApplication.mApp.TEST == false)
                ExhibitionApplication.mApp.fingerManger.stopReconize();
                AppUtils.showMessage(mLoginActivity,guest.name+mLoginActivity.getString(R.string.login_welcome));
                Intent intent1 = new Intent(mLoginActivity, MainActivity.class);
                mLoginActivity.startActivity(intent1);
            }

        }
        else
        {
            AppUtils.showMessage(mLoginActivity,mLoginActivity.getString(R.string.login_error1));
            ExhibitionApplication.mApp.fingerManger.startReconize();
        }
    }


    public View.OnClickListener codeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setPasswordLoginBtn();
        }
    };

    public View.OnClickListener faceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setFaceLoginBtn();
        }
    };

    public View.OnClickListener fingerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setFingerLoginBtn();
        }
    };

    public View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doLogin();
        }
    };

    public View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ExhibitionApplication.mApp.startVideo();
//            System.exit(0);
        }
    };

    public View.OnClickListener hidinputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) mLoginActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(mLoginActivity.user.getWindowToken(), 0);
            imm2.hideSoftInputFromWindow(mLoginActivity.password.getWindowToken(), 0);
        }
    };

    public void praseIcCard(String id) {
        if(mLoginActivity.cangetdata == true)
        {
            String mid = ExhibitionApplication.mApp.icCardManager.praseIcCardIs(id);
            Guest guest = DBHelper.getInstance(mLoginActivity).getGuestInfoIc(mid);
            if(guest == null)
            {
                AppUtils.showMessage(mLoginActivity,mLoginActivity.getString(R.string.login_error2));
                ExhibitionApplication.mApp.fingerManger.startReconize();
            }
            else
            {
                mLoginActivity.cangetdata = false;
                if(ExhibitionApplication.mApp.TEST == false)
                ExhibitionApplication.mApp.fingerManger.stopReconize();
                AppUtils.showMessage(mLoginActivity,guest.name);
                Intent intent1 = new Intent(mLoginActivity, MainActivity.class);
                mLoginActivity.startActivity(intent1);
            }
        }

    }

}
