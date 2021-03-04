package com.accesscontrol.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.accesscontrol.R;
import com.accesscontrol.asks.DeviceAsks;
import com.accesscontrol.entity.Device;
import com.accesscontrol.entity.UserDefine;
import com.accesscontrol.handler.AppHandler;
import com.accesscontrol.handler.SettingHandler;
import com.accesscontrol.receiver.SettingReceiver;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.QueryView;
import com.accesscontrol.view.activity.MainActivity;
import com.accesscontrol.view.activity.SettingActivity;
import com.yanzhenjie.permission.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.XpxSpinnerView;


/**
 * Created by xpx on 2017/8/18.
 */

public class SettingPresenter implements Presenter {

    public SettingActivity mSettingActivity;
    public SettingHandler mSettingHandler;
    public SettingPresenter(SettingActivity mSettingActivity) {
        this.mSettingActivity = mSettingActivity;
        this.mSettingHandler = new SettingHandler(mSettingActivity);
        mSettingActivity.setBaseReceiver(new SettingReceiver(mSettingHandler));
    }

    @Override
    public void Create() {
        initView();

    }

    @Override
    public void initView() {
        mSettingActivity.setContentView(R.layout.activity_setting);
        mSettingActivity.queryView = new QueryView(mSettingActivity);
        mSettingActivity.root = mSettingActivity.findViewById(R.id.setting);
        mSettingActivity.root.setFocusable(true);
        mSettingActivity.root.setFocusableInTouchMode(true);
        mSettingActivity.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingActivity.finish();
            }
        });
//
        mSettingActivity.input = mSettingActivity.findViewById(R.id.input);
        mSettingActivity.input.setOnClickListener(hidInputListener);
        mSettingActivity.basePasswordValue = mSettingActivity.findViewById(R.id.base_password_sound_value);
        mSettingActivity.basePasswordValue.setOnClickListener(setPasswordListner);
        mSettingActivity.rebootvolue = mSettingActivity.findViewById(R.id.widget_reboot_port_value);
        mSettingActivity.rebootvolue.setOnClickListener(rebootListener);
        mSettingActivity.workVolue = mSettingActivity.findViewById(R.id.widget_work_port_value);
        mSettingActivity.workVolue.setOnClickListener(workListener);
        mSettingActivity.membervolue = mSettingActivity.findViewById(R.id.widget_member_port_value);
        mSettingActivity.membervolue.setOnClickListener(memberListener);

        mSettingActivity.uploadvalue = mSettingActivity.findViewById(R.id.widget_upload_port_value);
        mSettingActivity.uploadvalue.setOnClickListener(uploadListener);

        mSettingActivity.netip = mSettingActivity.findViewById(R.id.net_ip_value);
        mSettingActivity.netmac = mSettingActivity.findViewById(R.id.net_mac_address_value);

        mSettingActivity.widgetVolue = mSettingActivity.findViewById(R.id.widget_volume_port_value);
        mSettingActivity.widgetVolue.setOnClickListener(seekBarAudioListener);
        mSettingActivity.setAudio = mSettingActivity.findViewById(R.id.base_audio_value);
        mSettingActivity.setAudio.setOnClickListener(setAudioListener);
        mSettingActivity.equipName = mSettingActivity.findViewById(R.id.contact_equipment_name_value);
        //mSettingActivity.equipName.setOnClickListener(equipNameLitener);
        mSettingActivity.equipNumber = mSettingActivity.findViewById(R.id.contact_equipment_code_value);
        mSettingActivity.equipNumber.setOnClickListener(equipCodeShowLitener);

        mSettingActivity.netSet = mSettingActivity.findViewById(R.id.net_mac_value);
        mSettingActivity.netSet.setOnClickListener(setNetListner);

        mSettingActivity.servicePort = mSettingActivity.findViewById(R.id.net_service_port_value);
        mSettingActivity.servicePort.setOnClickListener(setPortListner);
        mSettingActivity.serviceIp = mSettingActivity.findViewById(R.id.net_service_ip_value);
        mSettingActivity.serviceIp.setOnClickListener(setIpListner);

        mSettingActivity.serviceAppPort = mSettingActivity.findViewById(R.id.net_app_service_port_value);
        mSettingActivity.serviceAppPort.setOnClickListener(setAppPortListner);
        mSettingActivity.serviceAppIp = mSettingActivity.findViewById(R.id.net_app_service_ip_value);
        mSettingActivity.serviceAppIp.setOnClickListener(setAppIpListner);

        mSettingActivity.wifiSet = mSettingActivity.findViewById(R.id.net_wifiset_value);
        mSettingActivity.wifiSet.setOnClickListener(wifiSetListner);

        mSettingActivity.nowVersion = mSettingActivity.findViewById(R.id.verson_now_value);
        mSettingActivity.updataVersion = mSettingActivity.findViewById(R.id.version_new_value);
        mSettingActivity.btnUpdata = mSettingActivity.findViewById(R.id.version_new_check);
        mSettingActivity.btnUpdata.setOnClickListener(updataListener);
        updataView();
        if (AccessControlApplication.mApp.appservice.sAddress.length() != 0 && AccessControlApplication.mApp.appservice.sPort.length() != 0)
            DeviceAsks.getDeviceInfo(mSettingActivity, mSettingHandler, AccessControlApplication.mApp.clidenid);
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

    public void updataView(Device device) {

        if(AccessControlApplication.mApp.getUploadImage() == true)
        {
            mSettingActivity.uploadvalue.setText(mSettingActivity.getString(R.string.setting_upload_title2));
        }
        else
        {
            mSettingActivity.uploadvalue.setText(mSettingActivity.getString(R.string.setting_upload_title1));
        }

        if (device != null) {
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME, device.cname);
            mSettingActivity.equipName.setText(device.cname);
            e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESS, device.address);
            e1.putString(UserDefine.USER_SETTING_CONTACT_EQUIP_ADDRESSID, device.addressid);
            e1.putString(UserDefine.REGISTER_ACCESS, device.isaccess);
            e1.putString(UserDefine.REGISTER_ATTDENCE, device.isattence);
            e1.commit();
            Intent intent = new Intent(MainActivity.ACTION_UPDTATA_BTN);
            mSettingActivity.sendBroadcast(intent);

        }
    }

    public void setGetip(Intent intent) {
        String ip = intent.getStringExtra("ipAddr");
        mSettingActivity.netip.setText(ip);
        mSettingActivity.netmac.setText(AppUtils.getLocalMacAddressFromIp(mSettingActivity,ip));
    }


    private void updataView() {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        //基础设置

        mSettingActivity.basePasswordValue.setText(sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, ""));

        Intent intent1 = new Intent("com.ynh.getip");
        mSettingActivity.sendBroadcast(intent1);

        //界面设置

        mSettingActivity.equipName.setText(sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME
                , mSettingActivity.getString(R.string.button_word_noname)));
        mSettingActivity.equipNumber.setText(AccessControlApplication.mApp.clidenid);

//        mSettingActivity.netip.setText(AppUtils.getLocalIpAddress2(mSettingActivity));
//

        mSettingActivity.serviceIp.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_IP,
                "192.168.14.16"));
        mSettingActivity.servicePort.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_PORT,
                "1883"));
        mSettingActivity.serviceAppIp.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_IP,
                "192.168.14.8"));
        mSettingActivity.serviceAppPort.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_PORT,
                "8766"));

        mSettingActivity.nowVersion.setText(AccessControlApplication.mApp.versionName);
        if (AccessControlApplication.mApp.mUpDataManager.updataVersionCode > AccessControlApplication.mApp.mUpDataManager.oldVersionCode
                && AccessControlApplication.mApp.mUpDataManager.finish == true) {
            mSettingActivity.updataVersion.setText(AccessControlApplication.mApp.mUpDataManager.updataVersionName);
            mSettingActivity.btnUpdata.setVisibility(View.VISIBLE);
        } else {
            mSettingActivity.btnUpdata.setVisibility(View.INVISIBLE);
        }
    }


    public View.OnClickListener setAudioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setAudio();
        }
    };

    public void setAudio() {
        AccessControlApplication.mApp.audioManager.audioSetting(mSettingActivity);
    }


    private View.OnClickListener hidInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) mSettingActivity.getSystemService(mSettingActivity.INPUT_METHOD_SERVICE);
//            imm2.hideSoftInputFromWindow(name.getWindowToken(), 0);

        }
    };

    //基础设置

    private View.OnClickListener setPasswordListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit(mSettingActivity, null
                    , mSettingActivity.getString(R.string.setting_base_password_title2),
                    mSettingActivity.basePasswordValue.getText().toString(), getPasswordListner, v, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

    //界面设置

    public SeekBar.OnSeekBarChangeListener seekBarLightListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Settings.System.putInt(mSettingActivity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
            Uri uri = Settings.System
                    .getUriFor("screen_brightness_mode");
            mSettingActivity.getContentResolver().notifyChange(uri, null);
            Settings.System.putInt(mSettingActivity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, progress);
            Uri uri2 = Settings.System
                    .getUriFor("screen_brightness");
            mSettingActivity.getContentResolver().notifyChange(uri2, null);

        }


        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public View.OnClickListener seekBarAudioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
            intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", "完成");
            intent.putExtra("extra_prefs_set_back_text", "返回");
            mSettingActivity.startActivity(intent);
        }
    };


    //通信
    private View.OnClickListener equipNameLitener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialog(mSettingActivity, null
                    , mSettingActivity.getString(R.string.setting_contact_equipment_name_title2),
                    mSettingActivity.equipName.getText().toString(), null, v);
        }
    };


    private View.OnClickListener equipCodeShowLitener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialog(mSettingActivity, null
                    , mSettingActivity.getString(R.string.setting_contact_equipment_code_title2),
                    mSettingActivity.equipNumber.getText().toString(), null, v);
        }
    };

    //net
    private View.OnClickListener setPortListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit(mSettingActivity, null, mSettingActivity.getString(R.string.setting_net_service_port_title2)
                    , mSettingActivity.servicePort.getText().toString(), getPortListener, v, InputType.TYPE_CLASS_NUMBER);
        }
    };


    public AppUtils.GetEditText getPortListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setPort(s);
        }
    };


    private void setPort(String pass) {
        if(AppUtils.checkPort(Integer.valueOf(pass)))
        {
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_SERVICE_PORT, pass);
            e1.commit();
            mSettingActivity.servicePort.setText(pass);
            AccessControlApplication.mApp.service.sPort = pass;
            Intent intent = new Intent(AccessControlApplication.ACTION_START_MQTT);
            mSettingActivity.sendBroadcast(intent);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"端口超出范围");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"端口超出范围");
        }
    }

    private View.OnClickListener setIpListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit(mSettingActivity, null, mSettingActivity.getString(R.string.setting_net_service_ip_title2)
                    , mSettingActivity.serviceIp.getText().toString(), getIpListener, v, InputType.TYPE_CLASS_TEXT);
        }
    };

    public AppUtils.GetEditText getIpListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setIp(s);
        }
    };

    private void setIp(String pass) {
        if(AppUtils.checkUrl(pass)) {
            String tpass = pass;
//            if (pass.startsWith("http://") || pass.startsWith("http://")) {
//            } else {
//                tpass = "http://" + pass;
//            }
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_SERVICE_IP, tpass);
            e1.commit();
            mSettingActivity.serviceIp.setText(tpass);
            AccessControlApplication.mApp.service.sAddress = tpass;
            Intent intent = new Intent(AccessControlApplication.ACTION_START_MQTT);
            mSettingActivity.sendBroadcast(intent);
        }
        else{
            AppUtils.showMessage(mSettingActivity,"ip或域名格式不正确");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"ip或域名格式不正确");
        }


    }


    private View.OnClickListener setAppPortListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit(mSettingActivity, null, mSettingActivity.getString(R.string.setting_net_app_service_port_title2)
                    , mSettingActivity.serviceAppPort.getText().toString(), getAppPortListener, v, InputType.TYPE_CLASS_NUMBER);
        }
    };

    public AppUtils.GetEditText getAppPortListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setAppPort(s);
        }
    };

    private void setAppPort(String pass) {
        if(AppUtils.checkPort(Integer.valueOf(pass)))
        {
            String oldip = "";
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            oldip = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_PORT,"");
            e1.putString(UserDefine.USER_SETTING_SERVICE_APP_PORT, pass);
            e1.commit();
            mSettingActivity.serviceAppPort.setText(pass);
            AccessControlApplication.mApp.appservice.sPort = pass;
            if(!oldip.equals(pass))
            {
                AccessControlApplication.mApp.getRegister();
                AccessControlApplication.mApp.getLocationAndUpdata();
                Intent intent = new Intent(MainActivity.ACTION_UPDTATA_BTN);
                mSettingActivity.sendBroadcast(intent);
            }

        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"端口超出范围");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"端口超出范围");
        }
    }

    private View.OnClickListener setAppIpListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppUtils.creatXpxDialogEdit(mSettingActivity, null, mSettingActivity.getString(R.string.setting_net_app_service_ip_title2)
                    , mSettingActivity.serviceAppIp.getText().toString(), getAppIpListener, v, InputType.TYPE_CLASS_TEXT);
        }
    };

    public AppUtils.GetEditText getAppIpListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setAppIp(s);
        }
    };

    private void setAppIp(String pass) {
        if(AppUtils.checkUrl(pass))
        {
            String oldip = "";
            String tpass = pass;
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            oldip = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_IP,"");
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_SERVICE_APP_IP, tpass);
            e1.commit();
            mSettingActivity.serviceAppIp.setText(tpass);
            AccessControlApplication.mApp.appservice.sAddress = tpass;
            if(!oldip.equals(pass))
            {
                AccessControlApplication.mApp.getRegister();
                AccessControlApplication.mApp.getLocationAndUpdata();
                Intent intent = new Intent(MainActivity.ACTION_UPDTATA_BTN);
                mSettingActivity.sendBroadcast(intent);
            }

        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"ip或域名格式不正确");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"ip或域名格式不正确");
        }

    }


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

    private View.OnClickListener setNetListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", "完成");
            intent.putExtra("extra_prefs_set_back_text", "返回");
            mSettingActivity.startActivity(intent);
        }
    };


    //版本信息
    private View.OnClickListener updataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doUpdata();
        }
    };

    private void doUpdata() {
        File file = new File(AccessControlApplication.mApp.mUpDataManager.path);
        if(file.exists())
//            mSettingActivity.startActivity(AccessControlApplication.mApp.fileUtils.openfile(
//                    new File(AccessControlApplication.mApp.mUpDataManager.path)));
        AccessControlApplication.mApp.mUpDataManager.install(AccessControlApplication.mApp.mUpDataManager.path);
        else
            AppUtils.showMessage(mSettingActivity,"该文件不存在");
//        Intent intent = new Intent("com.android.56iq.otaupgrade");
//        intent.putExtra("path",AppUtils.getAssetsCacheFile(mSettingActivity, "update.zip"));//otaPath 为 ota 包所在的本地路径
//        mSettingActivity.sendBroadcast(intent);


    }


    public View.OnClickListener rebootListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPassword1(v);
        }
    };


    public View.OnClickListener workListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPassword2(v);
        }
    };

    public View.OnClickListener uploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(AccessControlApplication.mApp.getUploadImage() == true)
            {
                AccessControlApplication.mApp.setUploadImage(false);
                mSettingActivity.uploadvalue.setText(mSettingActivity.getString(R.string.setting_upload_title1));
            }
            else
            {
                AccessControlApplication.mApp.setUploadImage(true);
                mSettingActivity.uploadvalue.setText(mSettingActivity.getString(R.string.setting_upload_title2));
            }
        }
    };




    public void checkPassword1(View v)
    {
        AppUtils.creatXpxDialogEdit2(mSettingActivity,null
                ,mSettingActivity.getString(R.string.setting_base_password_title2),
                "",getPasswordListner1,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void checkPassword2(View v)
    {
        AppUtils.creatXpxDialogEdit2(mSettingActivity,null
                ,mSettingActivity.getString(R.string.setting_base_password_title2),
                "",getPasswordListner2,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private AppUtils.GetEditText2 getPasswordListner1 = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s, PopupWindow popupWindow) {
            setPassword1(s, popupWindow);
        }
    };

    private void setPassword1(String pass,PopupWindow popupWindow) {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, "");
        if(passd.equals(pass) ||(pass.length() == 0 && passd.length() == 0))
        {
            //mSettingActivity.sendBroadcast(new Intent().setAction("android.intent.action.reboot"));
            Intent intent = new Intent("android.intent.always.hideNaviBar");
            intent.putExtra("always",false);
            mSettingActivity.sendBroadcast(intent);
            Intent intent1 = new Intent(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_HOME);
            intent1.addCategory(Intent.CATEGORY_DEFAULT);
            intent1.setClassName("android", "com.android.internal.app.ResolverActivity");
            mSettingActivity.startActivity(intent1);

            popupWindow.dismiss();

        }
        else{
            AppUtils.showMessage(mSettingActivity,"密码错误");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"密码错误");
        }
    }


    private AppUtils.GetEditText2 getPasswordListner2 = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s,PopupWindow popupWindow) {
            setPassword2(s, popupWindow);
        }
    };

    private void setPassword2(String pass,PopupWindow popupWindow) {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, "");
        if(passd.equals(pass) ||(pass.length() == 0 && passd.length() == 0))
        {
            Intent intent = new Intent("android.intent.always.hideNaviBar");
            intent.putExtra("always",false);//true为一直隐藏，false为取消一直隐藏
            mSettingActivity.sendBroadcast(intent);
            Intent intent1 = new Intent(Settings.ACTION_SETTINGS);
            mSettingActivity.startActivity(intent1);
            popupWindow.dismiss();

        }
        else{
            AppUtils.showMessage(mSettingActivity,"密码错误");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"密码错误");
        }
    }

    public View.OnClickListener memberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPassword3(v);
        }
    };

    public void checkPassword3(View v)
    {
        AppUtils.creatXpxDialogEdit2(mSettingActivity,null
                ,mSettingActivity.getString(R.string.setting_base_password_title2),
                "",getPasswordListner3,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private AppUtils.GetEditText2 getPasswordListner3 = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s,PopupWindow popupWindow) {
            setPassword3(s, popupWindow);
        }
    };


    private void setPassword3(String pass,PopupWindow popupWindow) {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, "");
        if(passd.equals(pass) ||(pass.length() == 0 && passd.length() == 0))
        {
            popupWindow.dismiss();
            mSettingActivity.queryView.creatView(mSettingActivity.findViewById(R.id.setting));

        }
        else{
            AppUtils.showMessage(mSettingActivity,"密码错误");
            AccessControlApplication.mApp.audioManager.speak(mSettingActivity,"密码错误");
        }
    }
}
