package com.exhibition.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.PopupWindow;

import com.exhibition.R;
import com.exhibition.entity.UserDefine;
import com.exhibition.handler.MainHandler;
import com.exhibition.receiver.MainReceiver;
import com.exhibition.view.BaseSettingView;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.QueryView;
import com.exhibition.view.SafeSettingView;
import com.exhibition.view.SystemSettingView;
import com.exhibition.view.activity.AboutActivity;
import com.exhibition.view.activity.MainActivity;
import com.exhibition.view.activity.RegisterActivity;

import org.json.JSONException;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mMainHandler;
    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mMainHandler = new MainHandler(mMainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mMainHandler));
    }

    @Override
    public void initView() {
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.flagFillBack = false;
        mMainActivity.title = mMainActivity.findViewById(R.id.maintitle);
        mMainActivity.btn1 = mMainActivity.findViewById(R.id.main_btn1);
        mMainActivity.btn2 = mMainActivity.findViewById(R.id.main_btn2);
        mMainActivity.btn3 = mMainActivity.findViewById(R.id.main_btn3);
        mMainActivity.btn4 = mMainActivity.findViewById(R.id.main_btn4);
        mMainActivity.btn5 = mMainActivity.findViewById(R.id.main_btn5);
        mMainActivity.btn6 = mMainActivity.findViewById(R.id.main_btn6);
        mMainActivity.exist = mMainActivity.findViewById(R.id.exist);
        mMainActivity.btn1.setOnClickListener(registerListener);
        mMainActivity.btn2.setOnClickListener(aboutListener);
        mMainActivity.btn3.setOnClickListener(querListener);
        mMainActivity.btn4.setOnClickListener(baseSettingListener);
        mMainActivity.btn5.setOnClickListener(netSettingListener);
        mMainActivity.btn6.setOnClickListener(updateListener);
        mMainActivity.exist.setOnClickListener(existListner);
        mMainActivity.queryView = new QueryView(mMainActivity);
        mMainActivity.baseSettingView = new BaseSettingView(mMainActivity);
        mMainActivity.safeSettingView = new SafeSettingView(mMainActivity);
        mMainActivity.systemSettingView = new SystemSettingView(mMainActivity);
        mMainActivity.title.setText(mMainActivity.getString(R.string.main_title1)+ExhibitionApplication.mApp.getName()+mMainActivity.getString(R.string.main_title2));
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
        ExhibitionApplication.mApp.fingerManger.startReconize();
    }
    //1168 689
    public View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //mMainActivity.registerView.creatView(mMainActivity.findViewById(R.id.activity_main3));
            Intent intent = new Intent(mMainActivity, RegisterActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener aboutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAbout();
        }
    };

    public View.OnClickListener querListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.queryView.creatView(mMainActivity.findViewById(R.id.activity_main));
        }
    };

    public View.OnClickListener baseSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ExhibitionApplication.mApp.getSafe())
            {
                checkPassword(v);
            }
            else
            mMainActivity.baseSettingView.creatView(mMainActivity.findViewById(R.id.activity_main));
        }
    };

    public View.OnClickListener netSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ExhibitionApplication.mApp.getSafe())
            {
                checkPassword1(v);
            }
            else
            mMainActivity.safeSettingView.creatView(mMainActivity.findViewById(R.id.activity_main));
        }
    };

    public View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ExhibitionApplication.mApp.getSafe())
            {
                checkPassword2(v);
            }
            else
            mMainActivity.systemSettingView.creatView(mMainActivity.findViewById(R.id.activity_main));
        }
    };

    public View.OnClickListener existListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.finish();
        }
    };

    public void updataTimeout() {
        ExhibitionApplication.mApp.setTimeMax();
    }

    private void startAbout() {
        Intent intent = new Intent(mMainActivity, AboutActivity.class);
        mMainActivity.startActivity(intent);
    }

    public void updataName()
    {
        mMainActivity.title.setText(mMainActivity.getString(R.string.main_title1)+ExhibitionApplication.mApp.getName()+mMainActivity.getString(R.string.main_title2));
    }


    public void checkPassword(View v) {
        AppUtils.creatXpxDialogEdit2(mMainActivity, null
                , "请输入密码",
                "", getPasswordListner, v, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private AppUtils.GetEditText2 getPasswordListner = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s, PopupWindow popupWindow) {
            setPassword(s, popupWindow);

        }
    };

    private void setPassword(String pass, PopupWindow popupWindow) {
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.SETTING_PASSWORD, "");
        if (passd.equals(pass) || (pass.length() == 0 && passd.length() == 0)) {
            mMainActivity.baseSettingView.creatView(mMainActivity.findViewById(R.id.activity_main));
            popupWindow.dismiss();
        } else {
            AppUtils.showMessage(mMainActivity, "密码错误");
        }
    }






    public void checkPassword1(View v) {
        AppUtils.creatXpxDialogEdit2(mMainActivity, null
                , "请输入密码",
                "", getPasswordListner1, v, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private AppUtils.GetEditText2 getPasswordListner1 = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s, PopupWindow popupWindow) {
            setPassword1(s, popupWindow);
        }
    };

    private void setPassword1(String pass, PopupWindow popupWindow) {
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.SETTING_PASSWORD, "");
        if (passd.equals(pass) || (pass.length() == 0 && passd.length() == 0)) {
            mMainActivity.safeSettingView.creatView(mMainActivity.findViewById(R.id.activity_main));
            popupWindow.dismiss();
        } else {
            AppUtils.showMessage(mMainActivity, "密码错误");
        }
    }






    public void checkPassword2(View v) {
        AppUtils.creatXpxDialogEdit2(mMainActivity, null
                , "请输入密码",
                "", getPasswordListner2, v, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private AppUtils.GetEditText2 getPasswordListner2 = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s, PopupWindow popupWindow) {
            setPassword2(s, popupWindow);
        }
    };

    private void setPassword2(String pass, PopupWindow popupWindow) {
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.SETTING_PASSWORD, "");
        if (passd.equals(pass) || (pass.length() == 0 && passd.length() == 0)) {
            mMainActivity.systemSettingView.creatView(mMainActivity.findViewById(R.id.activity_main));
            popupWindow.dismiss();
        } else {
            AppUtils.showMessage(mMainActivity, "密码错误");
        }
    }
}
