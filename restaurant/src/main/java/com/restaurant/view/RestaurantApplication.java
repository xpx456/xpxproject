package com.restaurant.view;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.finger.FingerManger;
import com.iccard.IcCardManager;
import com.interksy.autoupdate.UpDataManager;
import com.interksy.autoupdate.UpdataDownloadThread;
import com.restaurant.R;
import com.restaurant.asks.DeviceAsks;
import com.restaurant.database.DBHelper;
import com.restaurant.entity.Guest;
import com.restaurant.entity.GuestFinger;
import com.restaurant.entity.Location;
import com.restaurant.entity.UserDefine;
import com.restaurant.handler.AppHandler;
import com.restaurant.model.DaoSessionManager;
import com.restaurant.model.bean.CompareResult;
import com.restaurant.model.bean.VeinImageInfo;
import com.restaurant.model.bean.VeinInfo;
import com.restaurant.model.entities.DaoSession;
import com.restaurant.model.entities.VeinDynamic;
import com.restaurant.model.entities.VeinDynamicDao;
import com.restaurant.receiver.AppReceiver;
import com.restaurant.service.MyMqttService;
import com.restaurant.view.activity.VideoActivity;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.apputils.ApkUtils;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.conversation.NotifictionManager;
import intersky.conversation.entity.Channel;
import intersky.filetools.FileUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;
import intersky.xpxnet.net.Service;
import intersky.xpxnet.net.nettask.MqttTask;
import jx.vein.javajar.JXVeinJavaAPI;
import jx.vein.javajar.vein.GetUSBPermission;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xpx.audio.AudioManager;
import xpx.sound.SoundManager;

public class RestaurantApplication extends Application{


    public static final int DOOR_DELY_TIME = 3;
    public static final int SCREEN_DELY_TIME = 60;
    public static final int CONNECT_DELY_TIME = 60;
    public int doordely = DOOR_DELY_TIME;
    public int connectdely = CONNECT_DELY_TIME;
    public String CHECK_VERSION_URL = "";
    public String UPDATA_APP_URL = "";
    public int UPDTA_PROTECT_TIME = 60;
    public static final String BG_PATH = "bg";
    public static final String UPDATE_PATH = "apk";
    public static final String UPDATE_NAME = "restaurant.apk";
    public static final String ACCESS_MODE_FINGER = "01";
    public static final String ACCESS_MODE_IC = "02";
    public static final String ACTION_START_CREAT_MQTT = "ACTION_START_CREAT_MQTT";
    public static final String SHOW_ACCESS_SUCCESS_VIEW = "SHOW_ACCESS_SUCCESS_VIEW";
    public static final String ACTION_UPDATA_VIDEO_VIEW = "ACTION_UPDATA_VIDEO_VIEW";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    public static final String ACTION_UPDATA_SDCARD_DATA = "ACTION_UPDATA_SDCARD_DATA";
    public static final String ACTION_UPDATA_SDCARD_DATA_FINISH = "ACTION_UPDATA_SDCARD_DATA_FINISH";
    public static final int EVENT_INIT_BG = 12001;
    public static final int EVENT_INIT_UPDATA = 12002;
    public AppActivityManager appActivityManager;
    public NetUtils netUtils;
    public MyMqttService myMqttService;
    public static RestaurantApplication mApp;
    public String clidenid;
    public Service service = new Service();
    public Service appservice = new Service();
    public FingerManger fingerManger;
    public FileUtils fileUtils;
    public HashMap<String, Guest> guestHashMap = new HashMap<String, Guest>();
    public HashMap<String,String> bg = new HashMap<String,String>();
    public boolean showfirst = true;
    public int protectSecond = 0;
    public AppHandler appHandler = new AppHandler();
    public AppReceiver appReceiver = new AppReceiver();
    public File updata;
    public String versionName = "";
    public int versionCode = 1;
    public String newversionName = "";
    public int newversionCode = 1;
    public boolean isregister = false;
    public AudioManager audioManager;
    public File sdcard;
    public HashMap<String, Location> locationHashMap = new HashMap<String, Location>();
    public HashMap<String, ArrayList<Location>> grops = new HashMap<String, ArrayList<Location>>();
    public static final String ACTION_LOCATION_CHANGE = "ACTION_LOCATION_CHANGE";
    public static final String UPDATA_NOTIFICATIONID = "restaurant_updata_download";
    public ArrayList<Location> locations = new ArrayList<Location>();
    public UpDataManager mUpDataManager;
    public UpdataDownloadThread.UpMessage last;
    public NotifictionManager notifictionManager;
    public NotifictionManager.NotificationOper oper;
    public IcCardManager icCardManager;
    public boolean deviceinited = false;
    private ExecutorService executeService;
    private static String storagePath;
    public SoundManager soundManager;
    public void onCreate() {
        mApp = this;
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        soundManager = SoundManager.init(mApp);
        soundManager.setSound(R.raw.fail);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(mApp);
        strategy.setCrashHandleCallback(myCrashHandleCallback);
        strategy.setAppReportDelay(5000);
        CrashReport.initCrashReport(getApplicationContext(), "ea9efb01f8", false,strategy);
        getService();
        getClientid();
        CrashReport.setUserId(clidenid+"_"+getName());

        initData();

        mApp.registerReceiver(appReceiver, appReceiver.intentFilter);


        netUtils = NetUtils.init(mApp,5000,5000);
        fileUtils = FileUtils.init(mApp,getProvidePath,null,null);
        fileUtils.pathUtils.setAppBase("/restaurant");
        audioManager = AudioManager.init(mApp);
        DBHelper.getInstance(mApp).scanGuest(guestHashMap);
        initOtherModule();




        startMqtt();
        getLocationAndUpdata();


//        sdcard = new File(AppUtils.searchPath(mApp,fileUtils.pathUtils.getBasePath()));
        initView();
        updataProtectTime();
        initDevice();

        executeService = Executors.newCachedThreadPool();

        try {
            storagePath = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator;
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate();
    }

    public void getFileAccess()
    {
        SystemClock.sleep(1000);
        File file = new File("dev/bus/usb");
        if(!file.canRead() || !file.canWrite()){
            new GetUSBPermission();
            getFileAccess();
        }
    }

    public void initDevice()
    {
        fingerManger = FingerManger.init(mApp,fileUtils.pathUtils.getfilePath("db")+"/restaurant.db",FingerManger.TYPE_FINGER_RESTURANT,feaCheck,false);
        RestaurantApplication.mApp.icCardManager = IcCardManager.init(mApp,
                IcCardManager.TYPE_FINGER_RESTURANT);
        RestaurantApplication.mApp.deviceinited = true;
    }

    public void setPassowrd(Intent intent)
    {
        String pass = intent.getStringExtra("pass");
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.USER_SETTING_SUPER_PASSOWRD, pass);
        e1.commit();
    }


    public void checkUpdata()
    {
        if(isregister)
        {
            if(RestaurantApplication.mApp.appservice.sAddress.length() != 0 || RestaurantApplication.mApp.appservice.sPort.length() != 0)
            DeviceAsks.getUpdataInfo(mApp,appHandler);
        }
        appHandler.removeMessages(AppHandler.CHECK_UPDATA);
        appHandler.sendEmptyMessageDelayed(AppHandler.CHECK_UPDATA,60000);
    }

    public void initData() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        protectSecond = sharedPre.getInt(UserDefine.SCREEN_DELY,SCREEN_DELY_TIME);
        doordely = sharedPre.getInt(UserDefine.DOOR_DELY,DOOR_DELY_TIME);
        connectdely = sharedPre.getInt(UserDefine.CONNECT_DELY,CONNECT_DELY_TIME);
        showfirst = false;
    }

    public void initView() {
        Intent navIntent = new Intent("com.hyzn.sdk.switchNavBar");
        navIntent .putExtra("value", 0);
        mApp.sendBroadcast(navIntent);
//        Intent statusIntent = new Intent("com.hyzn.sdk.switchStatusBar");
//        statusIntent.putExtra("value", 0);
//        mApp.sendBroadcast(statusIntent);
    }

    public void initOtherModule() {
        try {

            File file1 = new File(Environment.getExternalStorageDirectory().getPath()+"/restaurant");
            if(file1.exists() == false)
            {
                file1.mkdirs();
            }
            GlideConfiguration.init(file1);

            notifictionManager = NotifictionManager.init(mApp, R.drawable.icon, R.drawable.icon);
            Channel channel = new  Channel();
            channel.leave = NotificationManager.IMPORTANCE_LOW;
            channel.id = UPDATA_NOTIFICATIONID;
            channel.name = UPDATA_NOTIFICATIONID;
            oper = notifictionManager.registerNotification(channel);

            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(
                    mApp.getPackageName(), 0);
            versionName = packInfo.versionName;
            versionCode = packInfo.versionCode;

            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/restaurant/apk");
            if(file.exists() == false)
            {
                file.mkdirs();
            }

            RestaurantApplication.mApp.mUpDataManager = UpDataManager.init(RestaurantApplication.mApp
                    ,RestaurantApplication.mApp.CHECK_VERSION_URL,RestaurantApplication.mApp.UPDATA_APP_URL,
                    packInfo.versionName,packInfo.versionCode,file.getPath()+"/"+RestaurantApplication.UPDATE_NAME,
                    RestaurantApplication.mApp.updataOperation,RestaurantApplication.mApp.getProvidePath);
            mUpDataManager.auto = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void getService() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        mApp.service.sAddress = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_IP,"");
        mApp.service.sPort = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_PORT,"");
        mApp.appservice.sAddress = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_IP,"");
        mApp.appservice.sPort = sharedPre.getString(UserDefine.USER_SETTING_SERVICE_APP_PORT,"");
    }

    public void startMqtt() {
        if(RestaurantApplication.mApp.service.sAddress.length() == 0 || RestaurantApplication.mApp.service.sPort.length() == 0)
        {
            if(mApp.appActivityManager.getCurrentActivity() != null)
                AppUtils.showMessage(mApp.appActivityManager.getCurrentActivity(),mApp.getString(R.string.mqtt_error_no_ip_port));
        }
        else
        {
            RestaurantApplication.mApp.myMqttService.startService(mApp);

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
                    RestaurantApplication.mApp.mUpDataManager.CHECK_VERSION_URL = jo.getString("resourceUrl");
                }
                if(jo.getString("resourceTypeCode").equals("02"))
                {
                    RestaurantApplication.mApp.mUpDataManager.UPDATA_APP_URL = jo.getString("resourceUrl");
                }
            }

            boolean updtata = false;
            if(temp.size() == bg.size())
            {
                for(Map.Entry<String,String> item : temp.entrySet())
                {
                    if(!bg.containsKey(item.getKey()))
                    {
                        updtata = true;
                    }
                }
            }
            else
            {
                updtata = true;
            }
            if(updtata)
            {
                bg = temp;
                mApp.sendBroadcast(new Intent(RestaurantApplication.ACTION_UPDATA_VIDEO_VIEW));
            }

            if(mApp.mUpDataManager.CHECK_VERSION_URL.length() > 0 && mApp.mUpDataManager.UPDATA_APP_URL.length() > 0)
            RestaurantApplication.mApp.mUpDataManager.checkVersin();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDely(Intent intent) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        if(intent.getIntExtra("dely",0) != 0)
        {
            e1.putInt(UserDefine.DOOR_DELY,intent.getIntExtra("dely",0));
            doordely = intent.getIntExtra("dely",0);
        }
        if(intent.getIntExtra("screen",0) != 0)
        {
            e1.putInt(UserDefine.SCREEN_DELY,intent.getIntExtra("screen",0));
            protectSecond = intent.getIntExtra("screen",0);
        }
        if(intent.getIntExtra("connect",0) != 0)
        {
            e1.putInt(UserDefine.CONNECT_DELY,intent.getIntExtra("connect",0));
            connectdely = intent.getIntExtra("connect",0);
        }



    }

    public void settime(Intent intent) {
//        String time = intent.getStringExtra("time");
//        setSysDate(Integer.valueOf(time.substring(0,4)),Integer.valueOf(time.substring(5,7))-1
//                ,Integer.valueOf(time.substring(8,10)),Integer.valueOf(time.substring(11,13)),
//                Integer.valueOf(time.substring(14,16)),Integer.valueOf(time.substring(17,19)));
    }

    private void setSysDate(int year,int month,int day,int hour,int minute,int second){
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        c.set(Calendar.DAY_OF_MONTH, day);
//        c.set(Calendar.HOUR_OF_DAY, hour);
//        c.set(Calendar.MINUTE, minute);
//        c.set(Calendar.SECOND, second);
//        c.set(Calendar.MILLISECOND, 0);
//        long when = c.getTimeInMillis();
//        if(when / 1000 < Integer.MAX_VALUE){
//            ((AlarmManager)mApp.getSystemService(Context.ALARM_SERVICE)).setTime(when);
//        }
    }

    public void setregister() {
        RestaurantApplication.mApp.isregister = true;
        SharedPreferences sharedPre = mApp.getSharedPreferences("AppDate", 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(appservice.sAddress+appservice.sPort, isregister);
        e.apply();

    }





    public void initMqtt() {
        Intent intent = new Intent(MyMqttService.ACTION_INIT_MQTT);
        intent.putExtra("clientid", RestaurantApplication.mApp.clidenid);
        intent.putExtra("host","tcp://"+ RestaurantApplication.mApp.service.sAddress+":"+ RestaurantApplication.mApp.service.sPort);
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

    public void addGuest(Intent intent) {
        GuestFinger guestFinger = intent.getParcelableExtra("finger");
        ArrayList<GuestFinger> fingers = intent.getParcelableArrayListExtra("fingers");

        if(!RestaurantApplication.mApp.guestHashMap.containsKey(guestFinger.rid))
        {
            Guest guest = new Guest();
            guest.set(guestFinger);
            for(int i = 0 ; i < fingers.size() ; i++)
            {
                GuestFinger guestFinger1 = fingers.get(i);
                guest.fingers.put(guestFinger1.finger, new VeinInfo(guestFinger1.rid, fingers.get(i).fingerName));
            }
            DBHelper.getInstance(mApp).addGuest(guest);
            RestaurantApplication.mApp.guestHashMap.put(guest.rid,guest);
        }
        else
        {
            Guest guest = RestaurantApplication.mApp.guestHashMap.get(guestFinger.rid);
            guest.set(guestFinger);
            DBHelper.getInstance(mApp).updataGuest(guest);
            DBHelper.getInstance(mApp).deleteGuestFinger(guest);
            guest.fingers.clear();
            for(int i = 0 ; i < fingers.size() ; i++)
            {
                GuestFinger guestFinger1 = fingers.get(i);
                guest.fingers.put(guestFinger1.finger, new VeinInfo(guestFinger1.rid, fingers.get(i).fingerName));
                DBHelper.getInstance(mApp).addGuestFinger(guestFinger1);
            }

        }
    }

    public void deleteGuest(Intent intent) {
        GuestFinger guestFinger = intent.getParcelableExtra("finger");
        if(RestaurantApplication.mApp.guestHashMap.containsKey(guestFinger.rid))
        {
            Guest guest = RestaurantApplication.mApp.guestHashMap.get(guestFinger.rid);
            RestaurantApplication.mApp.guestHashMap.remove(guestFinger.rid);
            DBHelper.getInstance(mApp).deleteGuestFinger(guest);
            DBHelper.getInstance(mApp).deleteGuest(guest);
        }
        else
        {
            Guest guest = new Guest();
            guest.set(guestFinger);
            DBHelper.getInstance(mApp).deleteGuest(guest);
            DBHelper.getInstance(mApp).deleteGuestFinger(guest);
        }
    }

    public void cleanGuest(Intent intent) {
        DBHelper.getInstance(mApp).cleanGuest();
        RestaurantApplication.mApp.guestHashMap.clear();
    }

    public void getLocationAndUpdata()
    {
        if(RestaurantApplication.mApp.appservice.sAddress.length() == 0 || RestaurantApplication.mApp.appservice.sPort.length() == 0)
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

    public void updataProtectTime() {
        if(protectSecond > 0 )
        {
            protectSecond--;
        }
        if(showfirst == false && protectSecond == 0)
        {
            showfirst = true;
            showFirst();
        }
        appHandler.sendEmptyMessageDelayed(AppHandler.SHOW_FIRST_TIME,1000);
    }

    public void showFirst() {
        Intent intent = new Intent(mApp.appActivityManager.getCurrentActivity(), VideoActivity.class);
        mApp.appActivityManager.getCurrentActivity().startActivity(intent);
    }

    public void resetFirst() {
        appHandler.sendEmptyMessage(AppHandler.RESET_PROTECT_TIME);
    }

    public void resetProtect() {
        if(showfirst == true)
        {
            showfirst = false;
            protectSecond = UPDTA_PROTECT_TIME;
            Intent intent = new Intent(RestaurantApplication.ACTION_CLOSE);
            mApp.sendBroadcast(intent);
        }
        else
        {
            showfirst = false;
            protectSecond = UPDTA_PROTECT_TIME;
        }
    }


    private void deleteRemoteFile(VeinImageInfo veinImageInfo) {
        veinImageInfo.getFeatFile().delete();
        veinImageInfo.getImgFile().delete();
    }


    private void writeFile(File file, byte[] data) {
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap GRAY2Bitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i];
            // set value
            rgba[i * 4 + 0] = b1;
            rgba[i * 4 + 1] = b1;
            rgba[i * 4 + 2] = b1;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    private void saveFile(VeinImageInfo veinImageInfo, byte[] img, byte feat[]) {
        String fileName = veinImageInfo.getCommonFileName();
        String localPath = storagePath + "vein" + File.separator + "vein_local" + File.separator;
        String remotePath = storagePath + "vein" + File.separator + "vein_remote" + File.separator;
        File localStorageImg = new File(localPath + fileName + "_img.bin");
        File localStorageFeat = new File(localPath + fileName + "_feat.bin");

        File localDir = new File(localPath);
        File remoteDir = new File(remotePath);
        if (!localDir.exists()) {
            localDir.mkdirs();
        }
        if (!remoteDir.exists()) {
            remoteDir.mkdirs();
        }

        FileOutputStream remoteStorageImg = null;
        try {
            remoteStorageImg = new FileOutputStream(remotePath + fileName + ".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File remoteStorageImgBase64 = new File(remotePath + fileName + ".txt");

        writeFile(localStorageImg, img);
        writeFile(localStorageFeat, feat);

        Bitmap bitmap = GRAY2Bitmap(img, JXVeinJavaAPI.width, JXVeinJavaAPI.height);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, remoteStorageImg);
        writeFile(remoteStorageImgBase64, Base64.encode(img, Base64.DEFAULT));
        veinImageInfo.setImgFile(new File(remotePath + fileName + ".png"));
        veinImageInfo.setFeatFile(remoteStorageImgBase64);
    }


    private void veinImagePost(VeinImageInfo veinImageInfo) {
        MediaType type = MediaType.parse("application/octet-stream");
        RequestBody imgFile = RequestBody.create(type, veinImageInfo.getImgFile());
        RequestBody featFile = RequestBody.create(type, veinImageInfo.getFeatFile());

        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.ALTERNATIVE)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"photoBase64\"; filename=\"vein.txt\""),
                        featFile
                )
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"photo\"; filename=\"vein.png\""),
                        imgFile
                )
                .addFormDataPart("deviceName", veinImageInfo.getDeviceName())
                .addFormDataPart("deviceNo", veinImageInfo.getDeviceNo())
                .addFormDataPart("createTime", veinImageInfo.getCreateTime())
                .addFormDataPart("name", veinImageInfo.getName())
                .addFormDataPart("fingerName", veinImageInfo.getFingerName())
                .addFormDataPart("base64", veinImageInfo.getBase64())
                .addFormDataPart("photoFilename", veinImageInfo.getPhotoFilename())
                .addFormDataPart("base64Filename", veinImageInfo.getBase64Filename())
                .build();
        ResposeResult resposeResult = DeviceAsks.sendVeinImage(multipartBody);
        if (resposeResult != null) {
            Log.w("resposeResult", resposeResult.isSuccess + ":" + resposeResult.result);
        } else {
            Log.w("resposeResult", "null");
        }
    }

    private CompareResult bulkMatch(byte[] feature, List<Map.Entry<String, Guest>> bulk, List<VeinDynamic> veinDynamics) throws Exception {
        float minDiffLevel = 1;
        long devHandle = fingerManger.jxVeinJavaAPI.jxInitForThread();
        String correctOne = null;
        String correctFingerName = "";
        for (Map.Entry<String, Guest> entry : bulk) {
            Guest guest = entry.getValue();
            for (String key : guest.fingers.keySet()) {
                byte[] decoded = Base64.decode(key, Base64.DEFAULT);

                float diffLevel = fingerManger.jxVeinJavaAPI.jxMatchVeinFeatEx(devHandle, decoded, feature);
                if (diffLevel < minDiffLevel) {
                    minDiffLevel = diffLevel;
                    correctOne = guest.rid;
                    correctFingerName = guest.fingers.get(key).getFingerName();
                }
            }
        }

        for (VeinDynamic veinDynamic: veinDynamics) {
            byte [] tmp = Base64.decode(veinDynamic.getFeat(), Base64.DEFAULT);

            float diffLevel = fingerManger.jxVeinJavaAPI.jxMatchVeinFeatEx(devHandle, tmp, feature);

            // userId + 手指 作为一个更新对象
            if (diffLevel < minDiffLevel) {
                minDiffLevel = diffLevel;
                correctOne = veinDynamic.getUserId();
                correctFingerName = veinDynamic.getFingerIndex();
            }

        }

        fingerManger.jxVeinJavaAPI.jxReleaseForThread(devHandle);
        return new CompareResult(minDiffLevel, correctOne, correctFingerName);
    }


    public FingerManger.FeaCheck feaCheck = new FingerManger.FeaCheck() {

        private Long time = 0L;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public String FeaCheck(byte[] fea) {

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = System.currentTimeMillis();

                DaoSession daoSession = DaoSessionManager.getInstance().getDaoSession(RestaurantApplication.mApp);
                VeinDynamicDao veinDynamicDao = daoSession.getVeinDynamicDao();
                List<VeinDynamic> veinDynamics = veinDynamicDao.queryBuilder().list();
                int extOneThird = veinDynamics.size() / 3;

                List<Map.Entry<String, Guest>> datas = guestHashMap.entrySet().stream().collect(Collectors.toList());
                int oneThird = datas.size() / 3;

                final List<Map.Entry<String, Guest>> bulkAbove = new ArrayList<>();
                final List<VeinDynamic> extAbove = new ArrayList<>();
                final List<Map.Entry<String, Guest>> bulkMiddle = new ArrayList<>();
                final List<VeinDynamic> extMiddle = new ArrayList<>();
                final List<Map.Entry<String, Guest>> bulkBlow = new ArrayList<>();
                final List<VeinDynamic> extBlow = new ArrayList<>();
                final CompareResult[] results = new CompareResult[3];

                for (int i=0;i<datas.size();i++) {
                    if (i < oneThird) {
                        bulkAbove.add(datas.get(i));
                    } else if (i < oneThird * 2) {
                        bulkMiddle.add(datas.get(i));
                    } else {
                        bulkBlow.add(datas.get(i));
                    }
                }

                for (int i=0;i<veinDynamics.size();i++) {
                    if (i < extOneThird) {
                        extAbove.add(veinDynamics.get(i));
                    } else if (i < extOneThird * 2) {
                        extMiddle.add(veinDynamics.get(i));
                    } else {
                        extBlow.add(veinDynamics.get(i));
                    }
                }

                Log.w("time", "prepare time:" + (System.currentTimeMillis() - time));

                CountDownLatch countDownLatch = new CountDownLatch(3);

                executeService.execute(() -> {
                    try {
                        results[0] = bulkMatch(fea, bulkAbove, extAbove);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                });

                executeService.execute(() -> {
                    try {
                        results[1] = bulkMatch(fea, bulkMiddle, extMiddle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                });

                executeService.execute(() -> {
                    try {
                        results[2] = bulkMatch(fea, bulkBlow, extBlow);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                });

                countDownLatch.await();

                Log.w("check", "mid time:" + (System.currentTimeMillis() - time));

                CompareResult tmpWinner = results[0].compareTo(results[1]) == -1 ? results[0] : results[1];
                CompareResult winner = results[2].compareTo(tmpWinner) == -1 ? results[2] : tmpWinner;

                Log.w("Ext", "value:" + winner.getDiffLevel());

                List<VeinDynamic> veinDynamicList = veinDynamicDao
                        .queryBuilder()
                        .where(VeinDynamicDao.Properties.UserId.eq(winner.getRid()),
                                VeinDynamicDao.Properties.FingerIndex.eq(winner.getFingerIndex()))
                        .orderAsc(VeinDynamicDao.Properties.Index)
                        .list();

                String rid = "";

                if (veinDynamicList.size() > 0) {
                    rid = winner.getDiffLevel() <= 0.29 ? winner.getRid() : "";
                } else {
                    rid = winner.getDiffLevel() <= 0.3 ? winner.getRid() : "";
                }

                if (winner.getDiffLevel() < 0.26 && winner.getDiffLevel() > 0.18) {

                    if (veinDynamicList.size() < 2) {
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        VeinDynamic veinDynamic = new VeinDynamic();
                        veinDynamic.setIndex(veinDynamicList.size() == 0? 1 : 2);
                        veinDynamic.setFeat(Base64.encodeToString(fea, Base64.DEFAULT));
                        veinDynamic.setUserId(winner.getRid());
                        veinDynamic.setFingerIndex(winner.getFingerIndex());
                        veinDynamic.setCreateTime(simpleDateFormat1.format(new Date()));
                        veinDynamicDao.insert(veinDynamic);
                    } else {
                        VeinDynamic veinDynamic1 = veinDynamicList.get(0);
                        VeinDynamic veinDynamic2 = veinDynamicList.get(1);

                        veinDynamic1.setFeat(Base64.encodeToString(fea, Base64.DEFAULT));
                        veinDynamic1.setIndex(2);
                        veinDynamic2.setIndex(1);

                        veinDynamicDao.save(veinDynamic1);
                        veinDynamicDao.save(veinDynamic2);
                    }
                }


                VeinImageInfo veinImageInfo = new VeinImageInfo();
                veinImageInfo.setDeviceNo(clidenid);
                veinImageInfo.setDeviceName(clidenid);
                if (guestHashMap.get(rid) != null) {
                    Guest guest = guestHashMap.get(rid);
                    veinImageInfo.setName(guest.name);
                    veinImageInfo.setFingerName(winner.getFingerIndex());
                }
                Date date = new Date();
                veinImageInfo.setCreateTime(simpleDateFormat.format(date));
                veinImageInfo.setBase64(Base64.encodeToString(fea, Base64.DEFAULT));
                veinImageInfo.genFileNames(date, rid);

                byte[] imgBuf = new byte[JXVeinJavaAPI.imgSize];
                JXVeinJavaAPI.getInstance().jxReadLastVeinImg(imgBuf);

                saveFile(veinImageInfo, imgBuf, fea);
                veinImagePost(veinImageInfo);
                deleteRemoteFile(veinImageInfo);

                Log.w("check", "finished time:" + (System.currentTimeMillis() - time));

                return rid;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    };

    public MqttTask.Public aPublic = new MqttTask.Public(){

        @Override
        public void doPublc(String json, String topic,String id) {
            if(NetUtils.checkNetWorkState(mApp))
            MyMqttService.publish(json,topic,id);
        }

    };


    public static GetProvideGetPath getProvidePath = new GetProvideGetPath() {
        @Override
        public Uri getProvideGetPath(File file) {
            return FileProvider.getUriForFile(RestaurantApplication.mApp, "com.restaurant.fileprovider", file);
        }
    };

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

    public String getName() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        return sharedPre.getString(UserDefine.USER_SETTING_CONTACT_EQUIP_NAME,"");
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

            Intent intent = RestaurantApplication.mApp.getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
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


    //    public void onGetSdcard(Intent intent) {
//        sdcard = new File(intent.getData().getPath());
//        praseSdcardData();
//    }

//    public void praseSdcardData() {
//        if(sdcard.exists())
//        {
//            File file = new File(sdcard.getPath()+"/restaurant/bg");
//            File fileup = new File(sdcard.getPath()+"/restaurant/apk/restaurant.apk");
//            if(file.exists())
//            {
//                new Thread()
//                {
//                    @Override
//                    public void run() {
//
//                        super.run();
//                        File[] files = file.listFiles();
//                        mApp.appHandler.sendEmptyMessage(AppHandler.RESET_PROTECT_TIME);
//                        resetProtect();
//                        Intent intent2 = new Intent(ACTION_UPDATA_SDCARD_DATA);
//                        mApp.sendBroadcast(intent2);
//
//                        for(int i = 0 ; i < files.length ;i++)
//                        {
//                            File file1 = files[i];
//                            if(fileUtils.getFileType(file1.getName()) == FileUtils.FILE_TYPE_PICTURE)
//                            {
//
//                                File file2 = new File(fileUtils.pathUtils.getfilePath("bg")+"/"+file1.getName());
//                                if(file2.exists())
//                                {
//                                    file2.delete();
//                                }
//                                FileUtils.copyFile(file1.getPath(),file2.getPath());
//                            }
//                        }
//                        if(mApp.appHandler != null)
//                            mApp.appHandler.sendEmptyMessage(RestaurantApplication.EVENT_INIT_BG);
//                        if(fileup.exists())
//                        {
//                            File file2 = new File(fileUtils.pathUtils.getfilePath("apk")+"/restaurant.apk");
//                            FileUtils.copyFile(fileup.getPath(),file2.getPath());
//                            if(mApp.appHandler != null)
//                                mApp.appHandler.sendEmptyMessage(RestaurantApplication.EVENT_INIT_UPDATA);
//                        }
//                        Intent intent3 = new Intent(ACTION_UPDATA_SDCARD_DATA_FINISH);
//                        mApp.sendBroadcast(intent3);
//                    }
//                }.start();
//            }
//        }
//    }

    //    public void getBgs() {
//        File photo = new File(fileUtils.pathUtils.getfilePath(BG_PATH));
//        if(photo.isDirectory()) {
//            File[] childFile = photo.listFiles();
//            for (File f : childFile) {
//                if(f.isFile())
//                {
//                    if(fileUtils.getFileType(f.getName()) == FileUtils.FILE_TYPE_PICTURE)
//                    {
//                        mApp.bg.put(f.getName(),f.getPath());
//                    }
//                }
//            }
//        }
//    }

//    public void getUpdate() {
//
//        try {
//            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
//            PackageInfo packInfo = packageManager.getPackageInfo(
//                    mApp.getPackageName(), 0);
//            versionName = packInfo.versionName;
//            versionCode = packInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        File photo = new File(fileUtils.pathUtils.getfilePath(UPDATE_PATH));
//        if(photo.isDirectory()) {
//            File[] childFile = photo.listFiles();
//            for (File f : childFile) {
//                if(f.isFile())
//                {
//                    String name = f.getName();
//                    if(f.getName().equals(UPDATE_NAME) )
//                    {
//                        mApp.updata = f;
//                        String v[] = ApkUtils.apkInfo2(f.getPath(),mApp);
//                        newversionName = v[0];
//                        newversionCode = Integer.valueOf(v[1]);
//                        return;
//                    }
//                }
//            }
//        }
//    }


    public boolean getUploadImage(){
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        return sharedPre.getBoolean(UserDefine.USER_SETTING_UPLOAD_IMG, false);
    }

    public void setUploadImage(boolean upload){
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putBoolean(UserDefine.USER_SETTING_UPLOAD_IMG, upload);
        e1.commit();
    }
}
