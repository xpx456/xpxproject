package com.accesscontrol.presenter;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.accesscontrol.R;
import com.accesscontrol.asks.DeviceAsks;
import com.accesscontrol.asks.MqttAsks;
import com.accesscontrol.database.DBHelper;
import com.accesscontrol.entity.Device;
import com.accesscontrol.entity.Guest;
import com.accesscontrol.entity.UserDefine;
import com.accesscontrol.handler.AppHandler;
import com.accesscontrol.handler.MainHandler;
import com.accesscontrol.receiver.MainReceiver;
import com.accesscontrol.service.AppService;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.SuccessView;
import com.accesscontrol.view.activity.ChatActivity;
import com.accesscontrol.view.activity.MainActivity;
import com.accesscontrol.view.activity.RegisterActivity;
import com.accesscontrol.view.activity.SettingActivity;
import com.gpio.IFPServer;
import com.iccard.ICCardReader;
import com.iccard.IcCardManager;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mainHandler;
    public static final long OPER_TIME = 2000;
    public long current = 0;
    public long failedCurrent = 0L;

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mainHandler));
    }

    @Override
    public void initView() {
        mMainActivity.flagFillBack = false;
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.setting = mMainActivity.findViewById(R.id.btnsetting);
        mMainActivity.contact = mMainActivity.findViewById(R.id.btncontact);
        mMainActivity.register = mMainActivity.findViewById(R.id.register);
        updataBtn();
        AccessControlApplication.mApp.startMqtt();
        mMainActivity.setting.setOnClickListener(settingListener);
        mMainActivity.contact.setOnClickListener(contactListener);
        mMainActivity.register.setOnClickListener(registerListener);
        mMainActivity.successView = new SuccessView(mMainActivity);
        if (AccessControlApplication.TEST_MODE == false)
            checkInitDevice();
        permission();
        checkClean();
    }

    public void checkClean()
    {
        if(TimeUtils.daysBetween(AccessControlApplication.mApp.lastcleantime,TimeUtils.getDate()) >= 7)
        {
            DBHelper.getInstance(mMainActivity).cleanAllrecord();
            AccessControlApplication.mApp.lastcleantime = TimeUtils.getDate();
            SharedPreferences sharedPre = mMainActivity.getSharedPreferences(UserDefine.USER_SETTING, 0);
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString(UserDefine.USER_CLEAN_RECORD, AccessControlApplication.mApp.lastcleantime);
            e1.commit();
        }
        if(mainHandler != null)
        {
            mainHandler.sendEmptyMessageDelayed(MainHandler.CHECK_CLEAN,1000*60*60);
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
        if (AccessControlApplication.mApp.icCardManager != null) {
            if (getCardId != null) {
                AccessControlApplication.mApp.icCardManager.getCardIds.remove(getCardId);
            }
        }
    }

    public void checkInitDevice() {
        if (AccessControlApplication.mApp.deviceinited) {
            AccessControlApplication.mApp.icCardManager.getCardIds.add(getCardId);
        } else {
            mainHandler.sendEmptyMessageDelayed(MainHandler.INIT_DEVICE, 100);
        }
    }

    public void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mMainActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mMainActivity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                intent.putExtra("extra_prefs_set_next_text", "完成");
                intent.putExtra("extra_prefs_set_back_text", "返回");
                mMainActivity.startActivity(intent);
                return;
            } else if (!Settings.System.canWrite(mMainActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + mMainActivity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                intent.putExtra("extra_prefs_set_next_text", "完成");
                intent.putExtra("extra_prefs_set_back_text", "返回");
                mMainActivity.startActivity(intent);
            } else {

            }
        } else {
            //Android6.0以下，不用动态声明权限
//            if (mFloatView!=null && mFloatView.isShow()==false) {
//                mFloatView.show();
//            }
        }
    }

    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);

                } else if (!Settings.System.canWrite(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);
                } else {
                    checkPassword(v);
                }
            } else {
                checkPassword(v);
            }
//            AccessControlApplication.mApp.audioManager.install(Environment.getExternalStorageDirectory().getPath()+"/accesshome/apk/"+AccessControlApplication.UPDATE_NAME);
        }
    };

    public View.OnClickListener registerListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);

                } else if (!Settings.System.canWrite(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(mMainActivity, RegisterActivity.class);
                    mMainActivity.startActivity(intent);
                }
            } else {
                Intent intent = new Intent(mMainActivity, RegisterActivity.class);
                mMainActivity.startActivity(intent);
            }
        }
    };

    public void checkMaster(NetObject netObject) {
        String json = netObject.result;
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            if (jsonObject.has("data")) {
                int count = jsonObject.getInt("data", 0);
                if (count > 0) {


                    Message message = new Message();
                    message.what = AppHandler.SET_CHAT_SHOW;
                    message.obj = true;
                    if (AccessControlApplication.mApp.appHandler != null)
                        AccessControlApplication.mApp.appHandler.sendMessage(message);

                    Intent intent = new Intent(mMainActivity, ChatActivity.class);
                    intent.putExtra("isguest", true);
                    intent.putExtra("count", count);
                    mMainActivity.startActivity(intent);
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppUtils.showMessage(mMainActivity, "终端设备不在线");
        AccessControlApplication.mApp.audioManager.speak(mMainActivity, "终端设备不在线");
    }

    public View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (AccessControlApplication.mApp.service.sAddress.length() > 0
                    && AccessControlApplication.mApp.service.sPort.length() > 0) {
                if (AccessControlApplication.mApp.canConnect()) {
                    if(AccessControlApplication.mApp.chatresource == false && AccessControlApplication.mApp.isshow == false)
                    DeviceAsks.getLiveMaster(mMainActivity, mainHandler);
                    else
                        AppUtils.showMessage(mMainActivity,"正在释放资源，请稍等一会再试");
                }
            } else {
                AppUtils.showMessage(mMainActivity, "您还未填写mqtt服务器ip或端口");
            }
            //AccessControlApplication.mApp.audioManager.install(Environment.getExternalStorageDirectory().getPath()+"/accesshome/apk");
        }
    };

    public void updataTime() {
        //mMainActivity.time.setText(TimeUtils.getDateAndTime());
        if (mainHandler != null)
            mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_TIME, 1000);
    }

    public void updataBtn() {
        if (AccessControlApplication.mApp.isregister == true) {
            if (AccessControlApplication.mApp.canConnect()) {
                mMainActivity.contact.setVisibility(View.VISIBLE);
            } else {
                mMainActivity.contact.setVisibility(View.INVISIBLE);
            }
            mMainActivity.register.setVisibility(View.INVISIBLE);
        } else {
            mMainActivity.contact.setVisibility(View.INVISIBLE);
            mMainActivity.register.setVisibility(View.VISIBLE);
        }

    }


    public void checkPassword(View v) {
        AppUtils.creatXpxDialogEdit2(mMainActivity, null
                , mMainActivity.getString(R.string.setting_base_password_title2),
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
        String passd = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD, "");
        if (passd.equals(pass) || (pass.length() == 0 && passd.length() == 0)) {
            Intent intent = new Intent(mMainActivity, SettingActivity.class);
            mMainActivity.startActivity(intent);
            popupWindow.dismiss();
        } else {
            AppUtils.showMessage(mMainActivity, "密码错误");
            AccessControlApplication.mApp.audioManager.speak(mMainActivity, "密码错误");
        }
    }

    public void icaccessSuccess(String id) {


        if (id != null) {
            for (Map.Entry<String, Guest> entry : AccessControlApplication.mApp.guestHashMap.entrySet()) {
                Guest guest = entry.getValue();
                if (guest.licence.equals(id)) {
                    if (guest.cancard.equals("1") && guest.type.equals("0")) {

                        mMainActivity.successView.creatView(mMainActivity.findViewById(R.id.activity_main), guest, AccessControlApplication.ACCESS_MODE_IC);
                        mainHandler.removeMessages(MainHandler.CLOSE_SUCCESS);
                        mainHandler.sendEmptyMessageDelayed(MainHandler.CLOSE_SUCCESS, AccessControlApplication.MAX_OPEN);
                        AccessControlApplication.mApp.LightGreen();
                        AccessControlApplication.mApp.openDoor();
                        AccessControlApplication.mApp.audioManager.speak(mMainActivity, "欢迎您");

                    } else {
                        AccessControlApplication.mApp.audioManager.speak(mMainActivity, "您没有权限");
                        AccessControlApplication.mApp.LightRed();
                    }
                    return;
                }
            }
            AccessControlApplication.mApp.audioManager.speak(mMainActivity, "验证失败");
        }
        AccessControlApplication.mApp.LightRed();


    }

    public void accessSuccess(Intent intent) {
        AccessControlApplication.mApp.resetFirst();
        if (System.currentTimeMillis() - current > OPER_TIME) {
            current = System.currentTimeMillis();
            if (intent != null) {
                Guest guest = AccessControlApplication.mApp.guestHashMap.get(intent.getStringExtra("rid"));
                if (guest != null) {
                    //mMainActivity.successView.creatView(mApp.findViewById(R.id.activity_xpx_video),guest, MainActivity.ACCESS_MODE_FINGER);
                    if (guest.canfinger.equals("1") && guest.type.equals("0")) {
                        IFPServer.setLightState(IFPServer.Light.Light_Red, false);
                        mMainActivity.successView.creatView(mMainActivity.findViewById(R.id.activity_main), guest, AccessControlApplication.ACCESS_MODE_FINGER);
                        mainHandler.removeMessages(MainHandler.CLOSE_SUCCESS);
                        mainHandler.sendEmptyMessageDelayed(MainHandler.CLOSE_SUCCESS, AccessControlApplication.MAX_OPEN);
                        AccessControlApplication.mApp.LightGreen();
                        AccessControlApplication.mApp.openDoor();
                        AccessControlApplication.mApp.audioManager.speak(mMainActivity, "欢迎您");
                        return;
                    } else {
                        AccessControlApplication.mApp.audioManager.speak(mMainActivity, "您没有权限");
                        AccessControlApplication.mApp.LightRed();
                        return;
                    }
                } else {
                    AccessControlApplication.mApp.audioManager.speak(mMainActivity, "验证失败");
                    AccessControlApplication.mApp.LightRed();
                    return;
                }
            } else {
                AccessControlApplication.mApp.audioManager.speak(mMainActivity, "验证失败");
                AccessControlApplication.mApp.LightRed();
                return;
            }
        }


    }

    public void accessFail() {
        if (System.currentTimeMillis() - failedCurrent > OPER_TIME) {
            failedCurrent = System.currentTimeMillis();
            AccessControlApplication.mApp.audioManager.speak(mMainActivity, "验证失败");
            AccessControlApplication.mApp.resetFirst();
            AccessControlApplication.mApp.LightRed();
        }

    }

    public IcCardManager.GetCardId getCardId = new IcCardManager.GetCardId() {

        @Override
        public void getGardId(String id) {
            if (System.currentTimeMillis() - current > OPER_TIME) {
                current = System.currentTimeMillis();
                String code = id.substring(4, 10);
                String real = code.substring(4, 6) + code.substring(2, 4) + code.substring(0, 2);
                int d = Integer.valueOf(real, 16);
                icaccessSuccess(String.valueOf(d));
            }
            AccessControlApplication.mApp.resetFirst();

        }
    };
}
