package com.exhibition.view;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.exhibition.database.DBHelper;
import com.exhibition.entity.Magezine;
import com.exhibition.entity.UserDefine;
import com.exhibition.handler.AppHandler;
import com.exhibition.view.activity.VideoActivity;
import com.finger.FingerManger;
import com.iccard.IcCardManager;
//import com.iccard.IcCardManager;
//import com.iccard.handler.IcCardHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.filetools.FileUtils;

public class ExhibitionApplication extends Application {
    public static final String ACCOUNT = "admin";
    public static final String PASSWORD = "admin";
    public static final boolean TEST = false;
    public static final String ACTION_UPDATA_TIMEOUT = "ACTION_UPDATA_TIMEOUT";
    public static final String EXHIBITION_DATA = "EXHIBITION_DATA";
    public static final String DATA_SETTING = "DATA_SETTING";
    public static final String VIDEO_PATH = "video";
    public static final String PHOTO_PATH = "photo";
    public static final String ABOUT_PATH = "about";
    public static final String SETTING_PATH = "setting";
    public static final String FINGER_PATH = "finger";
    public static final String UPDATA_PATH = "updata";
    public static final String UPDATA_NAME = "exhibition.apk";
    public static final String ACTION_SET_NAME = "ACTION_SET_NAME";
    public static final int CHECK_TIME_OUT = 10000;
    public static final int SET_TIME_MAX = 10001;
    public static final int SET_VIDEO_SHOW = 10002;
    public static final int SET_VIDEO_HID = 10003;
    public static final int TIME_MAX = 300;
    public static final int MAX_FINGER_SIZE = 3;
    public AppActivityManager appActivityManager;
    public static ExhibitionApplication mApp;
    public int timeoud = TIME_MAX;
    public boolean videoshow = false;
    public FileUtils fileUtils;
    public AppHandler handler = new AppHandler();
    public File video = null;
    public File fingerbase;
    public ArrayList<File> photos = new ArrayList<File>();
    public ArrayList<Magezine> magezines = new ArrayList<Magezine>();
    public Magezine select;
    public File apk;
    public FingerManger fingerManger;
    public IcCardManager icCardManager;
    public boolean isadmin = false;
    public String deviceid = "";


//    public IcCardManager icCardManager;
    public void onCreate() {
        mApp = this;
        deviceid = AppUtils.getAppUnicode(mApp);
        fileUtils = FileUtils.init(mApp,null,null,null);
        fileUtils.pathUtils.setAppBase("/exhibtion");
        DBHelper.getInstance(mApp);
        if(DBHelper.getInstance(mApp).lastdb == 0)
            DBHelper.getInstance(mApp).lastdb = DBHelper.DB_VERSION;
        if(TEST == false)
        {
            icCardManager = IcCardManager.init(mApp,IcCardManager.TYPE_FINGER_EXHIBITION);
            if(DBHelper.getInstance(mApp).lastdb != DBHelper.DB_VERSION)
                fingerManger = FingerManger.init(mApp,fileUtils.pathUtils.getfilePath("db")+"/exhibtion.db",FingerManger.TYPE_FINGER_EXHIBITION,null,true);
            else
                fingerManger = FingerManger.init(mApp,fileUtils.pathUtils.getfilePath("db")+"/exhibtion.db",FingerManger.TYPE_FINGER_EXHIBITION,null,false);
        }
        DBHelper.getInstance(mApp).lastdb = DBHelper.DB_VERSION;
        File file = new File(fileUtils.pathUtils.getAppPath());
        GlideConfiguration.init(file);
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        initData();
        checkTimeOut();
        super.onCreate();
    }

    public void initData() {
        initSetting();
        initVideoAndPhoto();
    }

    public void checkTimeOut() {
        if(appActivityManager.activityStack != null)
        {
            if(timeoud == 0 && videoshow == false)
            {
                startVideo();
            }
            else if(timeoud > 0 && videoshow == false)
            {
                timeoud--;
//                mApp.sendBroadcast(new Intent(ACTION_UPDATA_TIMEOUT));
            }
        }

        if(handler != null)
        handler.sendEmptyMessageDelayed(CHECK_TIME_OUT,1000);
    }

    public void startVideo() {
        isadmin = false;
        setVideoshow();
        Intent intent = new Intent(appActivityManager.getCurrentActivity(), VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appActivityManager.getCurrentActivity().startActivity(intent);
    }

    public void setTimeMax() {
        handler.sendEmptyMessage(SET_TIME_MAX);
    }

    public void setVideoshow() {
        handler.sendEmptyMessage(SET_VIDEO_SHOW);
    }

    public void setVideoHid() {
        handler.sendEmptyMessage(SET_VIDEO_HID);
    }

    private void initSetting() {
        fileUtils.pathUtils.getfilePath(UPDATA_PATH);
        fingerbase = new File(fileUtils.pathUtils.getfilePath(FINGER_PATH));
        apk = new File(fileUtils.pathUtils.getfilePath(UPDATA_PATH)+"/"+UPDATA_NAME);
    }

    public void initVideoAndPhoto() {
        File video = new File(fileUtils.pathUtils.getfilePath(VIDEO_PATH));
        File photo = new File(fileUtils.pathUtils.getfilePath(PHOTO_PATH));
        File about = new File(fileUtils.pathUtils.getfilePath(ABOUT_PATH));
        mApp.photos.clear();
        magezines.clear();
        mApp.video = null;
        select = null;
        video.mkdirs();
        photo.mkdirs();
        about.mkdirs();


        if(video.isDirectory()) {
            File[] childFile = video.listFiles();
            for (File f : childFile) {
                if(f.isFile())
                {
                    if(fileUtils.getFileType(f.getName()) == FileUtils.FILE_TYPE_VIDEO)
                    {
                        mApp.video = f;
                        break;
                    }
                }
            }
        }
        if(photo.isDirectory()) {
            File[] childFile = photo.listFiles();
            for (File f : childFile) {
                if(f.isFile())
                {
                    if(fileUtils.getFileType(f.getName()) == FileUtils.FILE_TYPE_PICTURE)
                    {
                        mApp.photos.add(f);
                    }
                }
            }
        }

        if(about.isDirectory()) {
            File[] childFile = about.listFiles();
            for (File f : childFile) {
                if(f.isDirectory())
                {
                    Magezine magazine = new Magezine();
                    magezines.add(magazine);
                    magazine.name = f.getName();
                    File[] mag = f.listFiles();
                    for(File f1 : mag)
                    {
                        if(fileUtils.getFileType(f1.getName()) == FileUtils.FILE_TYPE_PICTURE)
                        {
                            magazine.pages.add(f1);
                        }
                    }
                    magazine.first = magazine.pages.get(magazine.pages.size()-1);
                    magazine.pages.remove(magazine.first);
                }
            }
        }
        if(magezines.size() > 0)
        {
            select = magezines.get(0);
        }
    }

    private JSONObject creadNewSettingJson() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","");
            jsonObject.put("print",false);
            jsonObject.put("licence",true);
            jsonObject.put("face",false);
            jsonObject.put("safe",true);
            jsonObject.put("password",PASSWORD);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    private JSONObject mesureSettingJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            if(jsonObject.has("name") == false) {
                jsonObject.put("name","");
            }
            if(jsonObject.has("print") == false) {
                jsonObject.put("print",false);
            }
            if(jsonObject.has("licence") == false) {
                jsonObject.put("licence",true);
            }
            if(jsonObject.has("face") == false) {
                jsonObject.put("face",false);
            }
            if(jsonObject.has("safe") == false) {
                jsonObject.put("safe",true);
            }
            if(jsonObject.has("password") == false) {
                jsonObject.put("password",PASSWORD);
            }
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initData(Intent intent)
    {
        File usb = new File(intent.getData().getPath());
        if(usb.exists())
        {
            File video = new File(usb.getPath()+"/exhibtion/"+VIDEO_PATH);
            File photo = new File(usb.getPath()+"/exhibtion/"+PHOTO_PATH);
            if(video.isDirectory()) {
                File[] childFile = video.listFiles();
                for (File f : childFile) {
                    if(f.isFile())
                    {
                        if(fileUtils.getFileType(f.getName()) == FileUtils.FILE_TYPE_VIDEO)
                        {
                            mApp.video = f;
                            break;
                        }
                    }
                }
            }
            if(photo.isDirectory()) {
                File[] childFile = photo.listFiles();
                for (File f : childFile) {
                    if(f.isFile())
                    {
                        if(fileUtils.getFileType(f.getName()) == FileUtils.FILE_TYPE_PICTURE)
                        {
                            mApp.photos.add(f);
                        }
                    }
                }
            }
        }
    }

    public String getName() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String name = sharedPre.getString(UserDefine.SETTING_NAME,"");
        return name;
    }

    public void setName(String name) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.SETTING_NAME,name);
        e1.commit();
        Intent intent = new Intent(ExhibitionApplication.ACTION_SET_NAME);
        mApp.sendBroadcast(intent);
    }


    public void setPrint(boolean print) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putBoolean(UserDefine.SETTING_PRINT,print);
        e1.commit();

    }


    public boolean getPrint() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        boolean print = sharedPre.getBoolean(UserDefine.SETTING_PRINT,false);
        return print;
    }


    public void setSafe(boolean print) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putBoolean(UserDefine.SETTING_SAFE,print);
        e1.commit();
    }


    public boolean getSafe() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        boolean print = sharedPre.getBoolean(UserDefine.SETTING_SAFE,false);
        return print;
    }

    public void setLic(boolean print) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putBoolean(UserDefine.SETTING_LIC,print);
        e1.commit();
    }


    public boolean getLic() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        boolean print = sharedPre.getBoolean(UserDefine.SETTING_LIC,false);
        return print;
    }

    public void setPassword(String print) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString(UserDefine.SETTING_PASSWORD,print);
        e1.commit();
    }


    public String getPassword() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        String print = sharedPre.getString(UserDefine.SETTING_PASSWORD,"");
        return print;
    }
}
