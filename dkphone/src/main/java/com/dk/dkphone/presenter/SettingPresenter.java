package com.dk.dkphone.presenter;


import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.dk.dkphone.R;
import com.dk.dkphone.entity.UserDefine;
import com.dk.dkphone.view.DkPhoneApplication;
import com.dk.dkphone.view.activity.MainActivity;
import com.dk.dkphone.view.activity.SettingActivity;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;

public class SettingPresenter implements Presenter {

    public SettingActivity mSettingActivity;
    public PopupWindow popupWindow;
    public SettingPresenter(SettingActivity SettingActivity) {
        mSettingActivity = SettingActivity;
    }

    @Override
    public void initView() {

        mSettingActivity.setContentView(R.layout.activity_setting);
        mSettingActivity.root = mSettingActivity.findViewById(R.id.setting);
        mSettingActivity.root.setFocusable(true);
        mSettingActivity.root.setFocusableInTouchMode(true);
        mSettingActivity.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingActivity.finish();
            }
        });
        mSettingActivity.input = mSettingActivity.findViewById(R.id.input);
        mSettingActivity.input.setOnClickListener(hidInputListener);
        mSettingActivity.setContentView(R.layout.activity_setting);
//        mSettingActivity.setAudio = mSettingActivity.findViewById(R.id.base_audio_value);
//        mSettingActivity.setAudio.setOnClickListener(setAudioListener);

        mSettingActivity.selectMax = mSettingActivity.findViewById(R.id.select_max_value);
        mSettingActivity.selectMax.setOnClickListener(setMaxListner);
//        mSettingActivity.workVolue = mSettingActivity.findViewById(R.id.widget_work_port_value);
//        mSettingActivity.workVolue.setOnClickListener(workListener);

//        mSettingActivity.wifiSet = mSettingActivity.findViewById(R.id.net_wifiset_value);
//        mSettingActivity.wifiSet.setOnClickListener(wifiSetListner);
        mSettingActivity.nowVersion = mSettingActivity.findViewById(R.id.verson_now_value);
        mSettingActivity.updataVersion = mSettingActivity.findViewById(R.id.version_new_value);
        mSettingActivity.btnUpdata = mSettingActivity.findViewById(R.id.version_new_check);
        mSettingActivity.btnUpdata.setOnClickListener(updataListener);
//        mSettingActivity.basePasswordValue = mSettingActivity.findViewById(R.id.base_password_sound_value);
//        mSettingActivity.basePasswordValue.setOnClickListener(setPasswordListner);

//        mSettingActivity.widgetVolue = mSettingActivity.findViewById(R.id.widget_volume_port_value);
//        mSettingActivity.widgetVolue.setOnClickListener(seekBarAudioListener);
        
//        mSettingActivity.netSet = mSettingActivity.findViewById(R.id.net_mac_value);
//        mSettingActivity.netSet.setOnClickListener(setNetListner);

        mSettingActivity.speedCheck = (RadioButton) mSettingActivity.findViewById(R.id.speed);
        mSettingActivity.speedCheck.setOnCheckedChangeListener(mModeCheckListener);
        mSettingActivity.roundCheck = (RadioButton) mSettingActivity.findViewById(R.id.round);
        mSettingActivity.roundCheck.setOnCheckedChangeListener(mModeCheckListener);
        mSettingActivity.testCheck = (RadioButton) mSettingActivity.findViewById(R.id.test);
        mSettingActivity.testCheck.setOnCheckedChangeListener(mModeCheckListener);

        mSettingActivity.showCheck = (RadioButton) mSettingActivity.findViewById(R.id.show_logo);
        mSettingActivity.showCheck.setOnCheckedChangeListener(mlogoCheckListener);
        mSettingActivity.unshowCheck = (RadioButton) mSettingActivity.findViewById(R.id.unshow_logo);
        mSettingActivity.unshowCheck.setOnCheckedChangeListener(mlogoCheckListener);

        updataView();
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
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }


    private void updataView() {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        //基础设置
        mSettingActivity.selectMax.setText(String.valueOf(sharedPre.getInt(UserDefine.USER_SETTING_MAX_SELECT,24)));
        mSettingActivity.nowVersion.setText(DkPhoneApplication.mApp.versionName);
        if(DkPhoneApplication.mApp.mUpDataManager.updataVersionCode > DkPhoneApplication.mApp.mUpDataManager.oldVersionCode
        && DkPhoneApplication.mApp.mUpDataManager.finish == true)
        {
            mSettingActivity.updataVersion.setText(DkPhoneApplication.mApp.mUpDataManager.updataVersionName);
            mSettingActivity.btnUpdata.setVisibility(View.VISIBLE);
        }
        else
        {
            mSettingActivity.btnUpdata.setVisibility(View.INVISIBLE);
        }
        if(DkPhoneApplication.mApp.testManager.xpxUsbManager.SPEED_MODE == 0)
        {
            mSettingActivity.speedCheck.setChecked(true);
        }
        else if(DkPhoneApplication.mApp.testManager.xpxUsbManager.SPEED_MODE == 1)
        {
            mSettingActivity.roundCheck.setChecked(true);
        }
        else
        {
            mSettingActivity.testCheck.setChecked(true);
        }

        if(DkPhoneApplication.mApp.LOGO_MODE)
        {
            mSettingActivity.showCheck.setChecked(true);
        }
        else
        {
            mSettingActivity.unshowCheck.setChecked(true);
        }


//        mSettingActivity.basePasswordValue.setText(sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD,""));
    }


    private View.OnClickListener hidInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) mSettingActivity.getSystemService(mSettingActivity.INPUT_METHOD_SERVICE);
//            imm2.hideSoftInputFromWindow(name.getWindowToken(), 0);

        }
    };

    public View.OnClickListener seekBarAudioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
            intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", mSettingActivity.getString(R.string.button_word_finish));
            intent.putExtra("extra_prefs_set_back_text", mSettingActivity.getString(R.string.button_word_back));
            mSettingActivity.startActivity(intent);
        }
    };

    private View.OnClickListener setNetListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", mSettingActivity.getString(R.string.button_word_finish));
            intent.putExtra("extra_prefs_set_back_text", mSettingActivity.getString(R.string.button_word_back));
            mSettingActivity.startActivity(intent);
        }
    };



    private View.OnClickListener wifiSetListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", "完成");
            intent.putExtra("extra_prefs_set_back_text", "返回");
            mSettingActivity.startActivity(intent);
        }
    };

    private View.OnClickListener updataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doUpdata();
        }
    };

    private void doUpdata() {
        File file = new File(DkPhoneApplication.mApp.mUpDataManager.path);
        if(file.exists() )
            mSettingActivity.startActivity(DkPhoneApplication.mApp.fileUtils.openfile(
                    new File(DkPhoneApplication.mApp.mUpDataManager.path)));
        else
            AppUtils.showMessage(mSettingActivity,mSettingActivity.getString(R.string.setting_file_not_exist));
    }


    private View.OnClickListener setPasswordListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow =  AppUtils.creatXpxDialogEdit(mSettingActivity,null
                    ,mSettingActivity.getString(R.string.setting_base_password_title2),
                    mSettingActivity.basePasswordValue.getText().toString(),getPasswordListner,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    };

    private AppUtils.GetEditText getPasswordListner = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setPassword(s);
        }
    };

    private void setPassword(String pass) {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.USER_SETTING_SUPER_PASSOWRD, pass);
        e1.commit();
        mSettingActivity.basePasswordValue.setText(pass);
    }

    private View.OnClickListener setMaxListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit(mSettingActivity, null, mSettingActivity.getString(R.string.setting_select_max_title2)
                    , mSettingActivity.selectMax.getText().toString(), getNaxListener, v, InputType.TYPE_CLASS_NUMBER);
        }
    };


    public AppUtils.GetEditText getNaxListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setMax(s);
        }
    };


    private void setMax(String pass) {
        if(Integer.valueOf(pass) <= 32)
        {
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putInt(UserDefine.USER_SETTING_MAX_SELECT, Integer.valueOf(pass));
            e1.commit();
            mSettingActivity.selectMax.setText(pass);
            DkPhoneApplication.mApp.maxSelect = Integer.valueOf(pass);
            Intent intent = new Intent(MainActivity.ACTION_SET_MAX);
            mSettingActivity.sendBroadcast(intent);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"最大档位超出范围");
        }
    }


    public View.OnClickListener setAudioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setAudio();
        }
    };

    public void setAudio()
    {
        DkPhoneApplication.mApp.audioManager.audioSetting(mSettingActivity);
    }



    private AppUtils.GetEditText2 getPasswordListner2 = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s,PopupWindow popupWindow) {
            setPassword2(s, popupWindow);
        }
    };


    public View.OnClickListener workListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPassword2(v);
        }
    };

    public void checkPassword2(View v)
    {
        AppUtils.creatXpxDialogEdit2(mSettingActivity,null
                ,mSettingActivity.getString(R.string.setting_base_password_title2),
                "",getPasswordListner2,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    private void setPassword2(String pass,PopupWindow popupWindow) {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, "");
        if(passd.equals(pass) ||(pass.length() == 0 && passd.length() == 0))
        {
            Intent in =new Intent();
            in.setAction("elc.view.show");
            mSettingActivity.sendBroadcast(in);
            Intent intent1 = new Intent(Settings.ACTION_SETTINGS);
            mSettingActivity.startActivity(intent1);
            popupWindow.dismiss();

        }
        else{
            AppUtils.showMessage(mSettingActivity,mSettingActivity.getString(R.string.setting_passord_error));
            DkPhoneApplication.mApp.audioManager.speak(mSettingActivity,mSettingActivity.getString(R.string.setting_passord_error));
        }
    }

    public CompoundButton.OnCheckedChangeListener mModeCheckListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(buttonView.getId() == R.id.speed)
            {
                if(isChecked)
                {
                    setSpeedValue(0);
                }
            }
            else if(buttonView.getId() == R.id.round)
            {
                if(isChecked)
                {
                    setSpeedValue(1);
                }
            }
            else
            {
                if(isChecked)
                {
                    setSpeedValue(2);
                }
            }
        }
    };


    public void setSpeedValue(int value)
    {
        SharedPreferences.Editor editor = DkPhoneApplication.mApp.sharedPre.edit();
        editor.putInt(UserDefine.SPEED_MODE,value);
        editor.commit();
        DkPhoneApplication.mApp.testManager.xpxUsbManager.SPEED_MODE = value;
    }



    public CompoundButton.OnCheckedChangeListener mlogoCheckListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(buttonView.getId() == R.id.show_logo)
            {
                if(isChecked)
                {
                    setLogoValue(true);
                }
                else
                {
                    setLogoValue(false);
                }
            }
            else
            {
                if(isChecked)
                {
                    setLogoValue(false);
                }
                else
                {
                    setLogoValue(true);
                }
            }

        }
    };


    public void setLogoValue(Boolean value)
    {
        SharedPreferences.Editor editor = DkPhoneApplication.mApp.sharedPre.edit();
        editor.putBoolean(UserDefine.LOGO_MOOD,value);
        editor.commit();
        Intent intent = new Intent(UserDefine.ACTION_LOGO_MOOD);
        mSettingActivity.sendBroadcast(intent);
    }
}
