package com.accessmaster.view;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.ZysjSystemManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.view.View;

import androidx.core.content.FileProvider;

import com.accessmaster.R;
import com.accessmaster.asks.DeviceAsks;
import com.accessmaster.asks.MqttAsks;
import com.accessmaster.database.DBHelper;
import com.accessmaster.entity.Device;
import com.accessmaster.entity.Guest;
import com.accessmaster.entity.GuestFinger;
import com.accessmaster.entity.Location;
import com.accessmaster.entity.UserDefine;
import com.accessmaster.handler.AppHandler;
import com.accessmaster.handler.MainHandler;
import com.accessmaster.receiver.AppReceiver;
import com.accessmaster.receiver.ChatReceiver;
import com.accessmaster.service.MyMqttService;
import com.accessmaster.view.activity.ChatActivity;
import com.accessmaster.view.activity.MainActivity;
import com.accessmaster.view.activity.VideoActivity;
import com.accessmaster.view.adapter.GridAdapter;
import com.interksy.autoupdate.UpDataManager;
import com.interksy.autoupdate.UpdataDownloadThread;
import com.tencent.bugly.crashreport.CrashReport;


import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.apputils.ApkUtils;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.conversation.NotifictionManager;
import intersky.conversation.entity.Channel;
import intersky.filetools.FileUtils;
import intersky.filetools.PathUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;
import intersky.xpxnet.net.nettask.MqttTask;
import xpx.audio.AudioManager;
import xpx.sound.SoundManager;
import xpx.video.XpxCameraManager;
import xpx.video.XpxSignalingClient;

public class AccessMasterApplication extends Application   {

    public static final String BG_PATH = "bg";
    public static final String UPDATE_PATH = "update";
    public static final String UPDATE_NAME = "accessmaster.apk";
    public static final String ACCESS_MODE_FINGER = "01";
    public static final String ACCESS_MODE_IC = "02";
    public static final String ACTION_START_MQTT = "ACTION_START_MQTT";
    public static final String SHOW_ACCESS_SUCCESS_VIEW = "SHOW_ACCESS_SUCCESS_VIEW";
    public static final String ACTION_LOCATION_CHANGE = "ACTION_LOCATION_CHANGE";
    public static final String UPDATA_NOTIFICATIONID = "mast_updata_download";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    public static final int DOOR_DELY_TIME = 3;
    public static final int SCREEN_DELY_TIME = 60;
    public static final int CONNECT_DELY_TIME = 60;
    public int doordely = DOOR_DELY_TIME;
    public int connectdely = CONNECT_DELY_TIME;
    public int protectSecondmax = SCREEN_DELY_TIME;
    public int protectSecond = SCREEN_DELY_TIME;
    public static final int MAX_OPEN = 100;
    public AppActivityManager appActivityManager;
    public NetUtils netUtils;
    public MyMqttService myMqttService;
    public static AccessMasterApplication mApp;
    public String clidenid;
    public String connectid="";
    public Service service = new Service();
    public Service appservice = new Service();
    public FileUtils fileUtils;
    public HashMap<String, Guest> guestHashMap = new HashMap<String, Guest>();
    public HashMap<String,String> bg = new HashMap<String,String>();
    public boolean showfirst = true;
    public AppHandler appHandler = new AppHandler();
    public AppReceiver appReceiver = new AppReceiver();
    public File updata;
    public String versionName = "";
    public int versionCode = 1;
    public String newversionName = "";
    public int newversionCode = 1;
    public XpxCameraManager xpxCameraManager;
    public HashMap<String,Location> locationHashMap = new HashMap<String, Location>();
    public HashMap<String,Device> deviceHashMap = new HashMap<String, Device>();
    public GridAdapter gridAdapter;
    public HashMap<String,ArrayList<Location>> grops = new HashMap<String, ArrayList<Location>>();
    public ArrayList<Location> locations = new ArrayList<Location>();
    public SoundManager soundManager;
    public boolean isshow = false;
    public AudioManager audioManager;
    public ZysjSystemManager manager;
    public boolean isregister = false;
    public UpDataManager mUpDataManager;
    public UpdataDownloadThread.UpMessage last;
    public NotifictionManager notifictionManager;
    public NotifictionManager.NotificationOper oper;
    public String connectCid = "";
    public boolean stopFirst = false;
    public boolean chatresource = false;
    @SuppressLint("WrongConstant")
    public void onCreate() {
        mApp = this;
        gridAdapter = new GridAdapter(mApp);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mApp);
        strategy.setCrashHandleCallback(myCrashHandleCallback);
        strategy.setAppReportDelay(5000);
        CrashReport.initCrashReport(getApplicationContext(), "61ddc1f7c8", false,strategy);
        getService();
        getClientid();
        CrashReport.setUserId(clidenid+"_"+getName());

        initData();
        manager= (ZysjSystemManager) getSystemService("zysj");
        mApp.registerReceiver(appReceiver, appReceiver.intentFilter);
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        netUtils = NetUtils.init(mApp,5000,5000);
        fileUtils = FileUtils.init(mApp,getProvidePath,null,null);
        fileUtils.pathUtils.setBase("/accessmaster");
        audioManager = AudioManager.init(mApp);
        soundManager = SoundManager.init(mApp);
        soundManager.setSound(R.raw.call);
        xpxCameraManager = XpxCameraManager.init(mApp);
        XpxSignalingClient.init();
        getBgs();
        DBHelper.getInstance(mApp).scanGuest(guestHashMap);




        startMqtt();
        initOtherModule();
        getLocationAndUpdata();

        initView();
        updataProtectTime();
        CrashReport.setUserId(clidenid+"_"+getName());
        super.onCreate();
    }

    public void checkClienList() {
        if(AccessMasterApplication.mApp.service.sAddress.length() != 0 && AccessMasterApplication.mApp.service.sPort.length() != 0)
        {
            for(int i = 0 ; i < AccessMasterApplication.mApp.gridAdapter.devices.size() ; i++)
            {
                Device device = AccessMasterApplication.mApp.gridAdapter.devices.get(i);
                device.startcheck = true;
                MqttAsks.askLive(mApp,AccessMasterApplication.mApp.aPublic,device.cid);
                Message message = new Message();
                message.obj = device.cid;
                message.what = AppHandler.CHECK_SIMPLE_DEVICE;
                if(appHandler != null)
                    appHandler.sendMessageDelayed(message,3000);
            }

        }
        if(appHandler != null)
        {
            appHandler.sendEmptyMessageDelayed(AppHandler.CHECK_DEVICE,120*1000);
        }
    }

    public void isDeviceLive(String id) {
        Device device = AccessMasterApplication.mApp.deviceHashMap.get(id);
        if(device.startcheck == true)
        {
            AccessMasterApplication.mApp.deviceHashMap.remove(id);
            if(AccessMasterApplication.mApp.gridAdapter != null)
            {
                AccessMasterApplication.mApp.gridAdapter.devices.remove(device);
                Intent intent1 = new Intent(MainActivity.ACTION_UPDATA_MAIN_GRIDE);
                mApp.sendBroadcast(intent1);
            }
        }

    }

    public void setPassowrd(Intent intent)
    {
        String pass = intent.getStringExtra("pass");
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.USER_SETTING_SUPER_PASSOWRD, pass);
        e1.commit();
    }

    public void initData() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        protectSecondmax = sharedPre.getInt(UserDefine.SCREEN_DELY,SCREEN_DELY_TIME);
        connectdely = sharedPre.getInt(UserDefine.CONNECT_DELY,CONNECT_DELY_TIME);
        showfirst = false;
    }

    public void resetFirst() {
        appHandler.sendEmptyMessage(AppHandler.RESET_PROTECT_TIME);
    }

    public void checkUpdata()
    {
        if(isregister)
        {
            if(AccessMasterApplication.mApp.appservice.sAddress.length() != 0 && AccessMasterApplication.mApp.appservice.sPort.length() != 0)
            DeviceAsks.getUpdataInfo(mApp,appHandler,"03");
        }
        if(appHandler != null)
        {
            appHandler.removeMessages(AppHandler.CHECK_UPDATA);
            appHandler.sendEmptyMessageDelayed(AppHandler.CHECK_UPDATA,60000);
        }

    }

    public void initView()
    {
        manager.ZYSystemBar(0);
    }

    private void initOtherModule() {

        try {
            notifictionManager = NotifictionManager.init(mApp, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background);
            Channel channel = new  Channel();
            channel.leave = NotificationManager.IMPORTANCE_LOW;
            channel.id = UPDATA_NOTIFICATIONID;
            channel.name = UPDATA_NOTIFICATIONID;
            oper = notifictionManager.registerNotification(channel);

            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/accessmaster");
            if(file.exists() == false)
            {
                file.mkdirs();
            }
            GlideConfiguration.init(file);

            File file1 = new File(Environment.getExternalStorageDirectory().getPath()+"/accessmaster/apk");
            if(file1.exists() == false)
            {
                file1.mkdirs();
            }

            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(
                    mApp.getPackageName(), 0);
            versionName = packInfo.versionName;
            versionCode = packInfo.versionCode;
            AccessMasterApplication.mApp.mUpDataManager = UpDataManager.init(AccessMasterApplication.mApp
                    ,"","",
                    packInfo.versionName,packInfo.versionCode,file.getPath()+"/"+AccessMasterApplication.UPDATE_NAME,
                    AccessMasterApplication.mApp.updataOperation,AccessMasterApplication.mApp.getProvidePath);
            mUpDataManager.auto = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getLocationAndUpdata() {
        if(AccessMasterApplication.mApp.appservice.sAddress.length() == 0 || AccessMasterApplication.mApp.appservice.sPort.length() == 0)
        {
            if(mApp.appActivityManager.getCurrentActivity() != null)
                AppUtils.showMessage(mApp.appActivityManager.getCurrentActivity(),mApp.getString(R.string.mqtt_error_no_ip_port));
        }
        else
        {
            DeviceAsks.getLocation(mApp,appHandler);
            checkUpdata();
        }
    }

    public void setDely(Intent intent) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        if(intent.getIntExtra("screen",0) != 0)
        {
            e1.putInt(UserDefine.SCREEN_DELY,intent.getIntExtra("screen",0));
            protectSecondmax = intent.getIntExtra("screen",0);
        }
        if(intent.getIntExtra("connect",0) != 0)
        {
            e1.putInt(UserDefine.CONNECT_DELY,intent.getIntExtra("connect",0));
            connectdely = intent.getIntExtra("connect",0);
        }
    }

    public void updateUrls(NetObject netObject) {
        try {
            String json = netObject.result;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            HashMap<String,String> temp = new HashMap<String, String>();
            for(int i = 0 ; i < ja.length() ; i++) {
                XpxJSONObject jo = ja.getJSONObject(i);
                if(jo.getString("resourceTypeCode").equals("03"))
                {
                    temp.put(jo.getString("resourceUrl"),jo.getString("resourceUrl"));
                }
                if(jo.getString("resourceTypeCode").equals("01"))
                {
                    mApp.mUpDataManager.CHECK_VERSION_URL = jo.getString("resourceUrl");
                }
                if(jo.getString("resourceTypeCode").equals("02"))
                {
                    mApp.mUpDataManager.UPDATA_APP_URL = jo.getString("resourceUrl");
                }
            }
            if(mApp.mUpDataManager.CHECK_VERSION_URL.length() > 0 && mApp.mUpDataManager.UPDATA_APP_URL.length() > 0)
                mApp.mUpDataManager.checkVersin();
//            if(AccessMasterApplication.mApp.appActivityManager.getCurrentActivity() != null)
//            AppUtils.showMessage(mApp.appActivityManager.getCurrentActivity(),"获取到更新信息："+"url:"+mApp.mUpDataManager.CHECK_VERSION_URL+" apk:" +mApp.mUpDataManager.CHECK_VERSION_URL);

            return;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(AccessMasterApplication.mApp.appActivityManager.getCurrentActivity() != null)
        AppUtils.showMessage(mApp.appActivityManager.getCurrentActivity(),"获取到更新信息失败");
    }

    public void setregister() {
        mApp.isregister = true;
        SharedPreferences sharedPre = mApp.getSharedPreferences("AppDate", 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(appservice.sAddress+appservice.sPort, isregister);
        e.apply();
    }

    public void settime(Intent intent) {
        String time = intent.getStringExtra("time");
        setSysDate(Integer.valueOf(time.substring(0,4)),Integer.valueOf(time.substring(5,7))-1
                ,Integer.valueOf(time.substring(8,10)),Integer.valueOf(time.substring(11,13)),
                Integer.valueOf(time.substring(14,16)),Integer.valueOf(time.substring(17,19)));
    }


    public void setSysDate(int year,int month,int day,int hour,int minute,int second){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();
        if(when / 1000 < Integer.MAX_VALUE){
            ((AlarmManager)mApp.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    public void startMqtt() {
        if(AccessMasterApplication.mApp.service.sAddress.length() == 0 || AccessMasterApplication.mApp.service.sPort.length() == 0)
        {

            if(mApp.appActivityManager.getCurrentActivity() != null)
            AppUtils.showMessage(mApp.appActivityManager.getCurrentActivity(),mApp.getString(R.string.mqtt_error_no_ip_port));
        }
        else
        {
            AccessMasterApplication.mApp.myMqttService.startService(mApp);

        }
    }

    public void initMqtt() {
        Intent intent = new Intent(MyMqttService.ACTION_INIT_MQTT);
        intent.putExtra("clientid", AccessMasterApplication.mApp.clidenid);
        intent.putExtra("host","tcp://"+ AccessMasterApplication.mApp.service.sAddress+":"+ AccessMasterApplication.mApp.service.sPort);
        mApp.sendBroadcast(intent);
    }


    public void getClientid() {
        SharedPreferences sharedPre = mApp.getSharedPreferences("AppDate", 0);
        clidenid = sharedPre.getString("clidenid", AppUtils.getguid());
        SharedPreferences.Editor e = sharedPre.edit();
        if(appservice.sAddress.length() == 0 || appservice.sPort.length() == 0)
        {
            isregister = false;
        }
        else
        {
            isregister = sharedPre.getBoolean(appservice.sAddress+appservice.sPort, false);
            e.putBoolean(appservice.sAddress+appservice.sPort, isregister);
        }

        e.putString("clidenid", clidenid);
        e.apply();
    }
    public String getName() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        return sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME,"");
    }

    public void mqttStarted() {
        MqttAsks.getDevices(mApp,aPublic);

        MqttAsks.sendMaster(mApp,aPublic);
    }


    public void addDevice(Intent intent) {
        Device device = new Device();
        device.cid = intent.getStringExtra("cid");
        device.cname = intent.getStringExtra("cname");
        device.address = intent.getStringExtra("caddress");
        if(deviceHashMap.containsKey(device.cid) == false)
        {
            deviceHashMap.put(device.cid,device);
            gridAdapter.devices.add(device);
            Intent intent1 = new Intent(MainActivity.ACTION_UPDATA_MAIN_GRIDE);
            mApp.sendBroadcast(intent1);

        }
    }

    public void liveBack(Intent intent)
    {
        String cid = intent.getStringExtra("cid");
        Device device = deviceHashMap.get(cid);
        device.startcheck = false;
    }

    public void removeDevice(Intent intent) {
        Device device = new Device();
        device.cid = intent.getStringExtra("cid");
        if(deviceHashMap.containsKey(device.cid))
        {
            deviceHashMap.remove(device.cid);
            for(int i = 0 ; i < gridAdapter.devices.size() ; i ++ )
            {
                if(gridAdapter.devices.get(i).cid.equals(device.cid))
                {
                    gridAdapter.devices.remove(i);
                    break;
                }
            }
            Intent intent1 = new Intent(MainActivity.ACTION_UPDATA_MAIN_GRIDE);
            mApp.sendBroadcast(intent1);

        }
    }


    public MqttTask.Public aPublic = new MqttTask.Public(){

        @Override
        public void doPublc(String json, String topic,String id) {
            if(NetUtils.checkNetWorkState(mApp))
            MyMqttService.publish(json,topic,id);
        }

    };

    public void addGuest(Intent intent) {
        GuestFinger guestFinger = intent.getParcelableExtra("finger");
        if(!AccessMasterApplication.mApp.guestHashMap.containsKey(guestFinger.rid))
        {
            Guest guest = new Guest();
            guest.set(guestFinger);
            DBHelper.getInstance(mApp).addGuest(guest);
        }
        else
        {
            Guest guest = AccessMasterApplication.mApp.guestHashMap.get(guestFinger.rid);
            guest.set(guestFinger);
            DBHelper.getInstance(mApp).updataGuest(guest);
            DBHelper.getInstance(mApp).addGuestFinger(guestFinger);
        }
    }

    public void deleteGuest(Intent intent) {
        GuestFinger guestFinger = intent.getParcelableExtra("finger");
        if(AccessMasterApplication.mApp.guestHashMap.containsKey(guestFinger.rid))
        {
            DBHelper.getInstance(mApp).deleteGuestFinger(guestFinger);
        }
        else
        {
            Guest guest = AccessMasterApplication.mApp.guestHashMap.get(guestFinger.rid);
            AccessMasterApplication.mApp.guestHashMap.remove(guest);
            DBHelper.getInstance(mApp).deleteGuest(guest);
        }
    }



    public void showSuccessView(Intent intent, SuccessView successView, View location) {
        Guest guest = AccessMasterApplication.mApp.guestHashMap.get(intent.getStringExtra("rid"));
        successView.creatView(location,guest,intent.getStringExtra("mode"));
    }


    private void getBgs() {
        File photo = new File(fileUtils.pathUtils.getfilePath(BG_PATH));
        if(photo.isDirectory()) {
            File[] childFile = photo.listFiles();
            for (File f : childFile) {
                if(f.isFile())
                {
                    if(fileUtils.getFileType(f.getName()) == FileUtils.FILE_TYPE_PICTURE)
                    {
                        mApp.bg.put(f.getName(),f.getPath());
                    }
                }
            }
        }
    }

    private void getUpdate() {

        try {
            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mApp.getPackageName(), 0);
            versionName = packInfo.versionName;
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        File photo = new File(fileUtils.pathUtils.getfilePath(UPDATE_PATH));
        if(photo.isDirectory()) {
            File[] childFile = photo.listFiles();
            for (File f : childFile) {
                if(f.isFile())
                {
                    if(f.getName() == UPDATE_NAME)
                    {
                        mApp.updata = f;
                        String v[] = ApkUtils.apkInfo2(f.getPath(),mApp);
                        newversionName = v[0];
                        newversionCode = Integer.valueOf(v[1]);
                        return;
                    }
                }
            }
        }
    }


    public void updataProtectTime() {
        if(protectSecond > 0 )
        {
            if(stopFirst == false)
                protectSecond--;
        }
        if(showfirst == false && protectSecond == 0)
        {

            showFirst();
        }
        if(appHandler != null)
        appHandler.sendEmptyMessageDelayed(AppHandler.SHOW_FIRST_TIME,1000);
    }

    public void showFirst() {
        showfirst = true;
        if(mApp.appActivityManager.getCurrentActivity() != null)
        {
            Intent intent = new Intent(mApp.appActivityManager.getCurrentActivity(), VideoActivity.class);
            mApp.appActivityManager.getCurrentActivity().startActivity(intent);
        }
    }

    public void showContact(Intent intent1) {
        if(isshow == false)
        {
            if(chatresource == false)
            {
                isshow = true;
                Intent intent = new Intent(mApp.appActivityManager.getCurrentActivity(), ChatActivity.class);
                intent.putExtra("cid",intent1.getStringExtra("cid"));
                intent.putExtra("cname",intent1.getStringExtra("cname"));
                if(intent.hasExtra("mid") == false)
                {
                    intent.putExtra("isguest",false);
                }
                else
                {
                    intent.putExtra("isguest",true);
                }
                mApp.appActivityManager.getCurrentActivity().startActivity(intent);
            }
            else{
                MqttAsks.doBuesy(mApp,mApp.aPublic,intent1.getStringExtra("cid"),mApp.clidenid,false);
            }
        }
        else
        {
            Intent intent = new Intent(ChatReceiver.ACITON_RECEICE_CALL);
            intent.putExtra("cid",intent1.getStringExtra("cid"));
            intent.putExtra("cname",intent1.getStringExtra("cname"));
            mApp.sendBroadcast(intent);

        }
    }



    public void resetProtect() {
        if(showfirst == true)
        {
            showfirst = false;
            protectSecond = protectSecondmax;
            Intent intent = new Intent(AccessMasterApplication.ACTION_CLOSE);
            mApp.sendBroadcast(intent);
        }
        else
        {
            showfirst = false;
            protectSecond = protectSecondmax;
        }
    }

    public void setAlwaysOpen(String start,String close) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
    }

    public void setAlwaysClose(String start,String close) {

    }

    public boolean isSuper() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        boolean value = sharedPre.getBoolean(UserDefine.USER_SETTING_SUPER,false);
        return value;
    }

    public String getPassowrd() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String value = sharedPre.getString(UserDefine.USER_SETTING_SUPER_PASSOWRD,"");
        return value;
    }

    public void getService()
    {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        mApp.service.sAddress = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_IP,"");
        mApp.service.sPort = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_PORT,"");
        mApp.appservice.sAddress = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_IP,"");
        mApp.appservice.sPort = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_PORT,"");
    }


    public UpDataManager.NotificationOperation updataOperation = new UpDataManager.NotificationOperation() {
        @Override
        public void showProgress(String title, String content, long max, long min) {
            int imax = 1000;
            int ifinish = 0;
            if(max > 0)
            {
                ifinish = (int) (imax*min/max);
            }
            if(last  == null)
            {
                last = new UpdataDownloadThread.UpMessage();
                last.size = max;
                last.finishsize = min;
            }
            if(last.finishsize < min)
            {
                last.finishsize = min;
                oper.creatNotificationNopen(title,content,imax,ifinish);
            }

        }

        @Override
        public void showMesage(String title, String content) {
            oper.creatNotificationNopen(title,content);
        }

        @Override
        public void showCancle() {
            oper.cancleNotification();
        }
    };

    public static GetProvideGetPath getProvidePath = new GetProvideGetPath() {
        @Override
        public Uri getProvideGetPath(File file) {
            return FileProvider.getUriForFile(AccessMasterApplication.mApp, "com.accessmaster.fileprovider", file);
        }
    };



    public void setshow(Boolean show)
    {
        mApp.isshow = show;
    }

    public void getRegister()
    {
        SharedPreferences sharedPre = mApp.getSharedPreferences("AppDate", 0);
        AccessMasterApplication.mApp.isregister = sharedPre.getBoolean(appservice.sAddress+appservice.sPort,false);
    }

    public CrashReport.CrashHandleCallback myCrashHandleCallback = new CrashReport.CrashHandleCallback() {
        public Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("Key", "Value");
            return map;
        }

        @Override
        public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType,
                                                       String errorMessage, String errorStack) {

            Intent intent = AccessMasterApplication.mApp.getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("REBOOT","reboot");
            startActivity(intent);
            try {
                return "Extra data.".getBytes("UTF-8");
            } catch (Exception e) {
                return null;
            }

        }
    };
}
