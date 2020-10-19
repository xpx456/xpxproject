package com.accessmaster.presenter;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;


import androidx.core.app.ActivityCompat;

import com.accessmaster.R;
import com.accessmaster.asks.DeviceAsks;
import com.accessmaster.asks.MqttAsks;
import com.accessmaster.entity.Device;
import com.accessmaster.entity.UserDefine;
import com.accessmaster.handler.AppHandler;
import com.accessmaster.handler.MainHandler;
import com.accessmaster.receiver.MainReceiver;
import com.accessmaster.view.AccessMasterApplication;
import com.accessmaster.view.activity.ChatActivity;
import com.accessmaster.view.activity.MainActivity;
import com.accessmaster.view.activity.RegisterActivity;
import com.accessmaster.view.activity.SettingActivity;
import com.accessmaster.view.adapter.GridAdapter;
import com.accessmaster.view.adapter.GrigPageAdapter;

import org.json.JSONException;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import intersky.appbase.PermissionCode;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mainHandler;

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mainHandler));
    }

    @Override
    public void initView() {
        mMainActivity.flagFillBack = false;
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.gridView = mMainActivity.findViewById(R.id.grid);
        mMainActivity.setting =mMainActivity.findViewById(R.id.setting);
        mMainActivity.setting.setOnClickListener(settingListener);
        mMainActivity.register = mMainActivity.findViewById(R.id.register);
        mMainActivity.register.setOnClickListener(doRegisterListener);
        mMainActivity.gridView.setAdapter(AccessMasterApplication.mApp.gridAdapter);
        mMainActivity.gridView.setOnItemClickListener(onItemClickListener);
        updataBtn();
        permission();
        if(mainHandler != null)
        {

        }
        if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            AppUtils.getPermission(Manifest.permission.RECORD_AUDIO, mMainActivity, PermissionCode.PERMISSION_REQUEST_AUDIORECORD, mainHandler);
        }
        if(AccessMasterApplication.mApp.appHandler != null)
        {
            AccessMasterApplication.mApp.appHandler.sendEmptyMessageDelayed(AppHandler.CHECK_DEVICE,120*1000);
        }
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
    }

    public View.OnClickListener doRegisterListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!Settings.canDrawOverlays(mMainActivity))
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);

                }
                else if ( !Settings.System.canWrite(mMainActivity))
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mMainActivity, RegisterActivity.class);
                    mMainActivity.startActivity(intent);
                }
            }
            else{
                Intent intent = new Intent(mMainActivity, RegisterActivity.class);
                mMainActivity.startActivity(intent);
            }
        }
    };

    public void updataBtn()
    {
        if(AccessMasterApplication.mApp.isregister)
        {
            mMainActivity.gridView.setVisibility(View.VISIBLE);
            mMainActivity.register.setVisibility(View.INVISIBLE);
        }
        else
        {
            mMainActivity.gridView.setVisibility(View.INVISIBLE);
            mMainActivity.register.setVisibility(View.VISIBLE);
        }
    }

    public void permission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(mMainActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mMainActivity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                intent.putExtra("extra_prefs_set_next_text", "完成");
                intent.putExtra("extra_prefs_set_back_text", "返回");
                mMainActivity.startActivity(intent);
                return;
            } else {

            }
        } else {

        }
    }


    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!Settings.canDrawOverlays(mMainActivity))
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);

                }
                else
                {
                    checkPassword(v);
                }
            }
            else{
                checkPassword(v);

            }
        }
    };



    public View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            {
                AppUtils.getPermission(Manifest.permission.RECORD_AUDIO, mMainActivity, PermissionCode.PERMISSION_REQUEST_AUDIORECORD, mainHandler);
            }
            else{
                if(AccessMasterApplication.mApp.chatresource == false)
                {
                    Device device = (Device) adapterView.getAdapter().getItem(i);
                    if(AccessMasterApplication.mApp.service.sAddress.length() != 0
                            && AccessMasterApplication.mApp.service.sPort.length() != 0)
                        MqttAsks.showView(mMainActivity,AccessMasterApplication.mApp.aPublic,device.cid);
                    else
                        AppUtils.showMessage(mMainActivity,"请先配置服务器");
                }
                else
                {
                    AppUtils.showMessage(mMainActivity,"正在释放资源，请稍等一会再试");
                }
            }

        }
    };

    public void checkMaster(NetObject netObject) {
        String json = netObject.result;
        Device device = (Device) netObject.item;
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            if(jsonObject.has("data"))
            {
                boolean count = jsonObject.getBoolean("data",false);
                if(count == true)
                {
                    Message message = new Message();
                    message.what = AppHandler.SET_CHAT_SHOW;
                    message.obj = true;
                    if (AccessMasterApplication.mApp.appHandler != null)
                        AccessMasterApplication.mApp.appHandler.sendMessage(message);
                    Intent intent = new Intent(mMainActivity, ChatActivity.class);
                    intent.putExtra("cid",device.cid);
                    intent.putExtra("isguest",false);
                    mMainActivity.startActivity(intent);
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppUtils.showMessage(mMainActivity,"客户端设备不在线");
        AccessMasterApplication.mApp.audioManager.speak(mMainActivity,"客户端设备不在线");
    }


    public void updataTime() {
        //mMainActivity.time.setText(TimeUtils.getDateAndTime());
        mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_TIME,1000);
    }


    public void updataView() {
        AccessMasterApplication.mApp.gridAdapter.notifyDataSetChanged();
    }

    public void checkPassword(View v)
    {
        AppUtils.creatXpxDialogEdit2(mMainActivity,null
                ,mMainActivity.getString(R.string.setting_base_password_title2),
                "",getPasswordListner,v,   InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private AppUtils.GetEditText2 getPasswordListner = new AppUtils.GetEditText2() {
        @Override
        public void onEditText(String s, PopupWindow popupWindow) {
            setPassword(s,  popupWindow);
        }
    };

    private void setPassword(String pass, PopupWindow popupWindow) {
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String passd = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, "");
        if(passd.equals(pass) ||(pass.length() == 0 && passd.length() == 0))
        {
            Intent intent = new Intent(mMainActivity, SettingActivity.class);
            mMainActivity.startActivity(intent);
            popupWindow.dismiss();
        }
        else{
            AppUtils.showMessage(mMainActivity,"密码错误");
            AccessMasterApplication.mApp.audioManager.speak(mMainActivity,"密码错误");
        }
    }

}
