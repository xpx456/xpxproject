package com.restaurant.presenter;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.restaurant.R;
import com.restaurant.asks.DeviceAsks;
import com.restaurant.entity.Device;
import com.restaurant.entity.UserDefine;
import com.restaurant.handler.SettingHandler;
import com.restaurant.receiver.SettingReceiver;
import com.restaurant.view.QueryView;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.activity.MainActivity;
import com.restaurant.view.activity.SettingActivity;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.Res;
import intersky.mywidget.XpxSpinnerView;

public class SettingPresenter implements Presenter {

    public SettingActivity mSettingActivity;
    public PopupWindow popupWindow;
    public SettingHandler settingHandler;
    public SettingPresenter(SettingActivity SettingActivity) {
        mSettingActivity = SettingActivity;
        settingHandler = new SettingHandler(SettingActivity);
        mSettingActivity.setBaseReceiver(new SettingReceiver(settingHandler));
    }

    @Override
    public void initView() {
        mSettingActivity.flagFillBack = false;
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
        mSettingActivity.input = mSettingActivity.findViewById(R.id.input);
        mSettingActivity.input.setOnClickListener(hidInputListener);
        mSettingActivity.setContentView(R.layout.activity_setting);
        mSettingActivity.servicePort = mSettingActivity.findViewById(R.id.net_service_port_value);
        mSettingActivity.setAudio = mSettingActivity.findViewById(R.id.base_audio_value);
        mSettingActivity.setAudio.setOnClickListener(setAudioListener);
        mSettingActivity.servicePort.setOnClickListener(setPortListner);
        mSettingActivity.serviceIp = mSettingActivity.findViewById(R.id.net_service_ip_value);
        mSettingActivity.serviceIp.setOnClickListener(setIpListner);

        mSettingActivity.rebootvolue = mSettingActivity.findViewById(R.id.widget_reboot_port_value);
        mSettingActivity.rebootvolue.setOnClickListener(rebootListener);

        mSettingActivity.equipName = mSettingActivity.findViewById(R.id.contact_equipment_name_value);
        mSettingActivity.netip = mSettingActivity.findViewById(R.id.net_ip_value);
        mSettingActivity.netmac = mSettingActivity.findViewById(R.id.net_mac_address_value);

        mSettingActivity.serviceAppPort = mSettingActivity.findViewById(R.id.net_app_service_port_value);
        mSettingActivity.serviceAppPort.setOnClickListener(setAppPortListner);
        mSettingActivity.serviceAppIp = mSettingActivity.findViewById(R.id.net_app_service_ip_value);
        mSettingActivity.serviceAppIp.setOnClickListener(setAppIpListner);

        mSettingActivity.uploadvalue = mSettingActivity.findViewById(R.id.widget_upload_port_value);
        mSettingActivity.uploadvalue.setOnClickListener(uploadListener);


        mSettingActivity.workVolue = mSettingActivity.findViewById(R.id.widget_work_port_value);
        mSettingActivity.workVolue.setOnClickListener(workListener);

        mSettingActivity.wifiSet = mSettingActivity.findViewById(R.id.net_wifiset_value);
        mSettingActivity.wifiSet.setOnClickListener(wifiSetListner);
        mSettingActivity.nowVersion = mSettingActivity.findViewById(R.id.verson_now_value);
        mSettingActivity.updataVersion = mSettingActivity.findViewById(R.id.version_new_value);
        mSettingActivity.btnUpdata = mSettingActivity.findViewById(R.id.version_new_check);
        mSettingActivity.btnUpdata.setOnClickListener(updataListener);
        mSettingActivity.basePasswordValue = mSettingActivity.findViewById(R.id.base_password_sound_value);
        mSettingActivity.basePasswordValue.setOnClickListener(setPasswordListner);
        mSettingActivity.equipNumber = mSettingActivity.findViewById(R.id.contact_equipment_code_value);
        mSettingActivity.equipNumber.setOnClickListener(equipCodeShowLitener);

        mSettingActivity.widgetVolue = mSettingActivity.findViewById(R.id.widget_volume_port_value);
        mSettingActivity.widgetVolue.setOnClickListener(seekBarAudioListener);


        mSettingActivity.netSet = mSettingActivity.findViewById(R.id.net_mac_value);
        mSettingActivity.netSet.setOnClickListener(setNetListner);

        mSettingActivity.membervolue = mSettingActivity.findViewById(R.id.widget_member_port_value);
        mSettingActivity.membervolue.setOnClickListener(memberListener);

        updataView();
        if (RestaurantApplication.mApp.appservice.sAddress.length() != 0 && RestaurantApplication.mApp.appservice.sPort.length() != 0)
            DeviceAsks.getDeviceInfo(mSettingActivity, settingHandler, RestaurantApplication.mApp.clidenid);
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

    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    public void updataView(Device device) {
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
        String ip = intent.getStringExtra("EthIpAddress");
        mSettingActivity.netip.setText(ip);
        mSettingActivity.netmac.setText(AppUtils.getLocalMacAddressFromIp(mSettingActivity,ip));
    }

    private void updataView() {
        SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        //基础设置

        if(AppUtils.getNetWorkState(mSettingActivity) == 2)
        {
            Intent intent=new Intent("com.hyzn.sdk.CtlEthernet");
            intent.putExtra("mode", 0);
            mSettingActivity.sendBroadcast(intent);
        }
        else
        {
            mSettingActivity.netip.setText(AppUtils.getLocalIpAddress2(mSettingActivity));

            mSettingActivity.netmac.setText(AppUtils.getLocalMacAddressFromIp(mSettingActivity));
        }

        mSettingActivity.equipName.setText(sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME
                , mSettingActivity.getString(R.string.button_word_noname)));
        mSettingActivity.serviceIp.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_IP,
                ""));
        mSettingActivity.servicePort.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_PORT,
                ""));
        mSettingActivity.serviceAppIp.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_IP,
                ""));
        mSettingActivity.serviceAppPort.setText(sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_PORT,
                ""));
        mSettingActivity.nowVersion.setText(RestaurantApplication.mApp.versionName);
        if(RestaurantApplication.mApp.mUpDataManager.updataVersionCode > RestaurantApplication.mApp.mUpDataManager.oldVersionCode
        && RestaurantApplication.mApp.mUpDataManager.finish == true)
        {
            mSettingActivity.updataVersion.setText(RestaurantApplication.mApp.mUpDataManager.updataVersionName);
            mSettingActivity.btnUpdata.setVisibility(View.VISIBLE);
        }
        else
        {
            mSettingActivity.btnUpdata.setVisibility(View.INVISIBLE);
        }

        //mSettingActivity.equipNumber.setText(RestaurantApplication.mApp.clidenid);
        mSettingActivity.basePasswordValue.setText(sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD,""));
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

    private View.OnClickListener setPortListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow = AppUtils.creatXpxDialogEdit(mSettingActivity,null,mSettingActivity.getString(R.string.setting_net_service_port_title2)
                    ,mSettingActivity.servicePort.getText().toString(),getPortListener,v, InputType.TYPE_CLASS_NUMBER);
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
            RestaurantApplication.mApp.service.sPort = pass;
            Intent intent = new Intent(RestaurantApplication.ACTION_START_CREAT_MQTT);
            mSettingActivity.sendBroadcast(intent);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"端口超出范围");
            //RestaurantApplication.mApp.audioManager.speak(mSettingActivity,"端口超出范围");
        }
    }

    private View.OnClickListener setIpListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow = AppUtils.creatXpxDialogEdit(mSettingActivity,null,mSettingActivity.getString(R.string.setting_net_service_ip_title2)
                    ,mSettingActivity.serviceIp.getText().toString(),getIpListener,v,InputType.TYPE_CLASS_TEXT);
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
            RestaurantApplication.mApp.service.sAddress = tpass;
            Intent intent = new Intent(RestaurantApplication.ACTION_START_CREAT_MQTT);
            mSettingActivity.sendBroadcast(intent);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"ip或域名格式不正确");
            //RestaurantApplication.mApp.audioManager.speak(mSettingActivity,"ip或域名格式不正确");
        }
    }


    private View.OnClickListener setAppPortListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow =  AppUtils.creatXpxDialogEdit(mSettingActivity,null,mSettingActivity.getString(R.string.setting_net_app_service_port_title2)
                    ,mSettingActivity.serviceAppPort.getText().toString(),getAppPortListener,v,InputType.TYPE_CLASS_NUMBER);
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
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_SERVICE_APP_PORT, pass);
            e1.commit();
            mSettingActivity.serviceAppPort.setText(pass);
            RestaurantApplication.mApp.appservice.sPort = pass;
            if(RestaurantApplication.mApp.appservice.sPort.length() > 0 && RestaurantApplication.mApp.appservice.sAddress.length() > 0)
                DeviceAsks.getLocation(RestaurantApplication.mApp,RestaurantApplication.mApp.appHandler);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"端口超出范围");
            //RestaurantApplication.mApp.audioManager.speak(mSettingActivity,"端口超出范围");
        }
    }

    private View.OnClickListener setAppIpListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow = AppUtils.creatXpxDialogEdit(mSettingActivity,null,mSettingActivity.getString(R.string.setting_net_app_service_ip_title2)
                    ,mSettingActivity.serviceAppIp.getText().toString(),getAppIpListener,v,InputType.TYPE_CLASS_TEXT);
        }
    };

    public AppUtils.GetEditText getAppIpListener = new AppUtils.GetEditText() {
        @Override
        public void onEditText(String s) {
            setAppIp(s);
        }
    };

    private void setAppIp(String pass) {
        if(AppUtils.checkUrl(pass)) {
            String tpass = pass;
//            if (pass.startsWith("http://") || pass.startsWith("http://")) {
//            } else {
//                tpass = "http://" + pass;
//            }
            SharedPreferences sharedPre = mSettingActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_SETTING_SERVICE_APP_IP, tpass);
            e1.commit();
            mSettingActivity.serviceAppIp.setText(tpass);
            RestaurantApplication.mApp.appservice.sAddress = tpass;
            if(RestaurantApplication.mApp.appservice.sPort.length() > 0 && RestaurantApplication.mApp.appservice.sAddress.length() > 0)
                DeviceAsks.getLocation(RestaurantApplication.mApp,RestaurantApplication.mApp.appHandler);
        }
        else
        {
            AppUtils.showMessage(mSettingActivity,"ip或域名格式不正确");
            //RestaurantApplication.mApp.audioManager.speak(mSettingActivity,"ip或域名格式不正确");
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

    private View.OnClickListener updataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doUpdata();
        }
    };

    private void doUpdata() {
        File file = new File(RestaurantApplication.mApp.mUpDataManager.path);
        if(file.exists() )
            mSettingActivity.startActivity(RestaurantApplication.mApp.fileUtils.openfile(
                    new File(RestaurantApplication.mApp.mUpDataManager.path)));
        else
            AppUtils.showMessage(mSettingActivity,"该文件不存在");
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

    private View.OnClickListener equipCodeShowLitener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow =AppUtils.creatXpxDialog(mSettingActivity,null
                    ,mSettingActivity.getString(R.string.setting_contact_equipment_code_title2),
                    RestaurantApplication.mApp.clidenid,okListener,"复制",v);
        }
    };

    public View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = RestaurantApplication.mApp.clidenid;
            ClipboardManager clip = (ClipboardManager) mSettingActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setPrimaryClip(ClipData.newPlainText("url", str));
            AppUtils.showMessage(mSettingActivity,"链接已复制到剪贴板");
        }
    };

    public View.OnClickListener setAudioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setAudio();
        }
    };

    public void setAudio()
    {
        RestaurantApplication.mApp.audioManager.audioSetting(mSettingActivity);
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
            Intent navIntent = new Intent("com.hyzn.sdk.switchNavBar");
            navIntent .putExtra("value", 1);
            mSettingActivity.sendBroadcast(navIntent);
            Intent intent1 = new Intent(Settings.ACTION_SETTINGS);
            mSettingActivity.startActivity(intent1);
            popupWindow.dismiss();

        }
        else{
            AppUtils.showMessage(mSettingActivity,"密码错误");
            RestaurantApplication.mApp.audioManager.speak(mSettingActivity,"密码错误");
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
            //mSettingActivity.queryView.creatView(mSettingActivity.findViewById(R.id.setting));
//            if(mSettingActivity.queryView.queryListAdapter != null)
//            {
//                mSettingActivity.queryView.queryListAdapter.notifyDataSetChanged();
//            }
            settingHandler.sendEmptyMessageDelayed(SettingHandler.SHOW_MEMBER,500);
//            Intent intent = new Intent(mSettingActivity, SettingActivity.class);
//            mSettingActivity.startActivity(intent);
        }
        else{
            AppUtils.showMessage(mSettingActivity,"密码错误");
            RestaurantApplication.mApp.audioManager.speak(mSettingActivity,"密码错误");
        }
    }

    public View.OnClickListener uploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(RestaurantApplication.mApp.getUploadImage() == true)
            {
                RestaurantApplication.mApp.setUploadImage(false);
                mSettingActivity.uploadvalue.setText(mSettingActivity.getString(R.string.setting_upload_title1));
            }
            else
            {
                RestaurantApplication.mApp.setUploadImage(true);
                mSettingActivity.uploadvalue.setText(mSettingActivity.getString(R.string.setting_upload_title2));
            }
        }
    };

    public View.OnClickListener rebootListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPassword1(v);
        }
    };

    public void checkPassword1(View v)
    {
        AppUtils.creatXpxDialogEdit2(mSettingActivity,null
                ,mSettingActivity.getString(R.string.setting_base_password_title2),
                "",getPasswordListner1,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
            Intent navIntent = new Intent("com.hyzn.sdk.switchNavBar");
            navIntent .putExtra("value", 1);
            mSettingActivity.sendBroadcast(navIntent);
            Intent intent1 = new Intent(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_HOME);
            intent1.addCategory(Intent.CATEGORY_DEFAULT);
            intent1.setClassName("android", "com.android.internal.app.ResolverActivity");
            mSettingActivity.startActivity(intent1);

            popupWindow.dismiss();

        }
        else{
            AppUtils.showMessage(mSettingActivity,"密码错误");
        }
    }

}
