package com.restaurant.presenter;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finger.entity.Finger;
import com.iccard.ICCardBaseReader;
import com.iccard.ICCardReader;
import com.iccard.IcCardManager;
import com.restaurant.R;
import com.restaurant.asks.DinnerAsks;
import com.restaurant.entity.Guest;
import com.restaurant.entity.GuestFinger;
import com.restaurant.entity.UserDefine;
import com.restaurant.handler.MainHandler;
import com.restaurant.prase.MqttPrase;
import com.restaurant.receiver.MainReceiver;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.SuccessView;
import com.restaurant.view.activity.MainActivity;
import com.restaurant.view.activity.RegisterActivity;
import com.restaurant.view.activity.SettingActivity;
import com.yanzhenjie.permission.Setting;

import org.json.JSONException;

import java.util.Locale;
import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.Res;
import intersky.apputils.TimeUtils;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mainHandler;
    public static final long OPER_TIME = 1000;
    public long current = 0;
    static String permission[] = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW,
    };

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mainHandler));
    }

    @Override
    public void initView() {

        mMainActivity.flagFillBack = false;
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.time = mMainActivity.findViewById(R.id.timevalue);
        mMainActivity.type = mMainActivity.findViewById(R.id.typevalue);
        mMainActivity.setting = mMainActivity.findViewById(R.id.setting);
        mMainActivity.register = mMainActivity.findViewById(R.id.register);
        if(RestaurantApplication.mApp.isregister == false)
        {
            mMainActivity.register.setVisibility(View.VISIBLE);
            mMainActivity.register.setOnClickListener(doRegisterListener);
        }
        else{
            mMainActivity.register.setVisibility(View.INVISIBLE);
        }
        mMainActivity.setting.setOnClickListener(settingListener);
        mMainActivity.successView = new SuccessView(mMainActivity);
        mMainActivity.version = mMainActivity.findViewById(R.id.version);
        mMainActivity.version.setText("v"+RestaurantApplication.mApp.mUpDataManager.oldVersionName);
        permission();
        updataTime();
        checkInitDevice();
    }

    public void showMessage()
    {
        AppUtils.showMessage(mMainActivity,"已经找到读卡器设备");
       // RestaurantApplication.mApp.audioManager.speak(mMainActivity,"已经找到读卡器设备");


    }

    public void showMessageUN()
    {
        AppUtils.showMessage(mMainActivity,"未找到读卡器设备正在重新搜索");
       // RestaurantApplication.mApp.audioManager.speak(mMainActivity,"未找到读卡器设备正在重新搜索");

    }

    public void checkInitDevice()
    {
        if(RestaurantApplication.mApp.deviceinited)
        {
            RestaurantApplication.mApp.icCardManager.getCardIds.add(getCardId);
        }
        else
        {
            mainHandler.sendEmptyMessageDelayed(MainHandler.INIT_DEVICE,100);
        }
    }

    public void updataBtn(){
        if(RestaurantApplication.mApp.isregister == true)
        {
            mMainActivity.register.setVisibility(View.INVISIBLE);
        }
        else
        {
            mMainActivity.register.setVisibility(View.VISIBLE);
        }
    }

    public void permission(){
        if (Build.VERSION.SDK_INT > 23) {
            if(!Settings.canDrawOverlays(mMainActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + mMainActivity.getPackageName()));
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
        RestaurantApplication.mApp.icCardManager.getCardIds.remove(getCardId);
    }

    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);
//                    if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
//                        mTextToSpeech.speak("刷卡成功", TextToSpeech.QUEUE_ADD,null,"speek");
//                    }

                }
                else
                {
                    checkPassword(v);
                }
            }
            else
            {
                checkPassword(v);
            }

        }
    };

    public View.OnClickListener doRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + mMainActivity.getPackageName()));
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

    public void speach()
    {

    }

    public void updataTime() {
        mMainActivity.time.setText(TimeUtils.getDateAndTime());
        mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_TIME,1000);
    }

    public void creatSuccess() {
        mMainActivity.successView.creatView(mMainActivity.findViewById(R.id.activity_main));
        mainHandler.sendEmptyMessageDelayed(MainHandler.CLOSE_SUCCESS,8000);
    }

    public void closeSuccess()
    {
        mMainActivity.successView.hidView();
    }

    public void creatSuccess(String[] data) {
        mMainActivity.successView.creatView(mMainActivity.findViewById(R.id.activity_main),data);
        RestaurantApplication.mApp.audioManager.speak(mMainActivity,mMainActivity.getString(R.string.audio_success)+
                mMainActivity.getString(R.string.audio_last)+data[1]+mMainActivity.getString(R.string.audio_last2));
        mainHandler.sendEmptyMessageDelayed(MainHandler.CLOSE_SUCCESS,3000);
    }

    public void praseIcCard(String id) {
        String mid = id;
    }


    public void accessSuccess(Intent intent)
    {
        RestaurantApplication.mApp.resetFirst();
        if(System.currentTimeMillis()-current > OPER_TIME) {
            current = System.currentTimeMillis();
            if(RestaurantApplication.mApp.isregister == true)
            {
                if(intent != null)
                {
                    Guest guest = RestaurantApplication.mApp.guestHashMap.get(intent.getStringExtra("rid"));
                    if(guest != null)
                    {
//                AppUtils.showMessage(mMainActivity,"识别为"+guest.name);
                        if(guest.canfinger.equals("1")  && guest.type.equals("0"))
                        {
                            senduplod(intent.getStringExtra("rid"),RestaurantApplication.ACCESS_MODE_FINGER);
                            return;
                        }
                        else
                        {
                            RestaurantApplication.mApp.audioManager.speak(mMainActivity,"您没有权限");
                        }

                    }
                    else
                    {
                        RestaurantApplication.mApp.audioManager.speak(mMainActivity,"验证失败");
                    }
                }
                else
                    RestaurantApplication.mApp.audioManager.speak(mMainActivity,"验证失败");
            }
            else
            {
                RestaurantApplication.mApp.audioManager.speak(mMainActivity,"请先注册设备");
            }
        }


    }

    public void accessFail()
    {
        if(System.currentTimeMillis()-current > OPER_TIME)
        {
            current = System.currentTimeMillis();
            if(RestaurantApplication.mApp.isregister == true)
            {
                RestaurantApplication.mApp.audioManager.speak(mMainActivity,"验证失败");
            }
            else
            {
                RestaurantApplication.mApp.audioManager.speak(mMainActivity,"请先注册设备");
            }
        }
        RestaurantApplication.mApp.resetFirst();
    }


    public void icaccessSuccess(String id)
    {

        if(id != null)
        {
            for(Map.Entry<String , Guest> entry : RestaurantApplication.mApp.guestHashMap.entrySet())
            {
                Guest guest = entry.getValue();
                if(guest.licence.equals(id)  && guest.type.equals("0") )
                {
                    if(guest.cancard.equals("1"))
                    {
                        String[] data = new String[2];
                        data[0] = guest.rid;
                        data[1] = RestaurantApplication.ACCESS_MODE_IC;
                        Message message = new Message();
                        message.what = MainHandler.UPLOAD_DATA;
                        message.obj = data;
                        if(mainHandler != null)
                            mainHandler.sendMessage(message);
                    }
                    else
                    {
                        RestaurantApplication.mApp.audioManager.speak(mMainActivity,"您没有权限");
                    }
                    return;
                }
            }
        }
        RestaurantApplication.mApp.audioManager.speak(mMainActivity,"验证失败");
        //senduplod(id,RestaurantApplication.ACCESS_MODE_IC);
    }

    public void senduplod(String id,String mode) {
        if(mMainActivity.waitDialog.isshow() == false)
        {
            mMainActivity.waitDialog.show();
            Guest guest = RestaurantApplication.mApp.guestHashMap.get(id);
//            AppUtils.showMessage(mMainActivity,"识别为"+guest.name);
            DinnerAsks.sendDinner(mMainActivity,mainHandler,id,mode);
        }
    }

    public void sendSuccess(NetObject netObject) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(netObject.result);
            if(jsonObject.has("data"))
            {
                if(jsonObject.getString("data").length() > 0)
                {

                    String[] data = jsonObject.getString("data").split(",");
                    if(data.length > 1)
                        creatSuccess(data);
                }
                else
                {
                    AppUtils.showMessage(mMainActivity, jsonObject.getString("message","error"));
                    RestaurantApplication.mApp.audioManager.speak(mMainActivity,jsonObject.getString("message",""));
                }
            }
            else
            {
                AppUtils.showMessage(mMainActivity, jsonObject.getString("message","error"));
                RestaurantApplication.mApp.audioManager.speak(mMainActivity,jsonObject.getString("message",""));
            }
            return;
        } catch (JSONException e) {
            e.printStackTrace();
            AppUtils.showMessage(mMainActivity, "连接服务器失败");
        }

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
            setPassword(s,popupWindow);
        }
    };

    private void setPassword(String pass,PopupWindow popupWindow) {
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
            RestaurantApplication.mApp.audioManager.speak(mMainActivity,"密码错误");
        }
    }

    public IcCardManager.GetCardId getCardId = new IcCardManager.GetCardId() {
        @Override
        public void getGardId(String id) {

            if(System.currentTimeMillis()-current > OPER_TIME) {
                current = System.currentTimeMillis();
                if(RestaurantApplication.mApp.isregister == true)
                {
                    String code = id.substring(4,10);
                    String real = code.substring(4,6)+code.substring(2,4)+code.substring(0,2);
                    int d = Integer.valueOf(real,16);
                    icaccessSuccess(String.valueOf(d));
                }
                else
                {
                    RestaurantApplication.mApp.audioManager.speak(mMainActivity,"请先注册设备");
                }
            }


            RestaurantApplication.mApp.resetFirst();
        }
    };
}
