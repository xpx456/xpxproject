package com.exhibition.view;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import com.exhibition.handler.AppHandler;
import com.exhibition.view.activity.MainActivity;
import com.exhibition.view.activity.VideoActivity;
import com.finger.FingerManger;
//import com.iccard.IcCardManager;
//import com.iccard.handler.IcCardHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import intersky.appbase.AppActivityManager;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.filetools.FileUtils;

public class ExhibitionApplication extends Application {
    public static final String ACCOUNT = "admin";
    public static final String PASSWORD = "admin";
    public static final String ACTION_UPDATA_TIMEOUT = "ACTION_UPDATA_TIMEOUT";
    public static final String EXHIBITION_DATA = "EXHIBITION_DATA";
    public static final String DATA_SETTING = "DATA_SETTING";
    public static final String VIDEO_PATH = "video";
    public static final String PHOTO_PATH = "photo";
    public static final String SETTING_PATH = "setting";
    public static final String FINGER_PATH = "finger";
    public static final String SETTING_NAME = "setting.txt";
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
    public JSONObject setjson = new JSONObject();
    public File video = null;
    public File fingerbase;
    public ArrayList<File> photos = new ArrayList<File>();
    public File apk;
    public FingerManger fingerManger;
    public boolean isadmin = false;
//    public IcCardManager icCardManager;
    public void onCreate() {
        mApp = this;

        fileUtils = FileUtils.init(mApp,null,null,null);
        fileUtils.pathUtils.setBase("/exhibtion");
//        icCardManager = IcCardManager.init(mApp,IcCardManager.TYPE_FINGER_EXHIBITION);
        //fingerManger = FingerManger.init(mApp,fileUtils.pathUtils.getfilePath("db")+"/exhibtion.db");
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
                mApp.sendBroadcast(new Intent(ACTION_UPDATA_TIMEOUT));
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
        File setpath = new File(fileUtils.pathUtils.getfilePath(SETTING_PATH));
        fileUtils.pathUtils.getfilePath(UPDATA_PATH);
        fingerbase = new File(fileUtils.pathUtils.getfilePath(FINGER_PATH));
        File setfile = new File(fileUtils.pathUtils.getfilePath(SETTING_PATH)+"/"+SETTING_NAME);
        apk = new File(fileUtils.pathUtils.getfilePath(UPDATA_PATH)+"/"+UPDATA_NAME);
        if(setfile.exists())
        {
            String readjson = fileUtils.getFileContent(setfile);
            JSONObject jsonObject = mesureSettingJson(readjson);
            if(jsonObject != null)
            {
                setjson = jsonObject;
            }
            setfile.delete();
            fileUtils.writeTxtToFile(setjson.toString(),setpath.getPath()+"/",setfile.getName());
        }
        else
        {
            setjson = creadNewSettingJson();
            fileUtils.writeTxtToFile(setjson.toString(),setpath.getPath()+"/",setfile.getName());
        }

    }

    public void initVideoAndPhoto() {
        File video = new File(fileUtils.pathUtils.getfilePath(VIDEO_PATH));
        File photo = new File(fileUtils.pathUtils.getfilePath(PHOTO_PATH));
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
            File[] childFile = video.listFiles();
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

    public void saveSetting() {
        File setpath = new File(fileUtils.pathUtils.getfilePath(SETTING_PATH));
        File setfile = new File(fileUtils.pathUtils.getfilePath(SETTING_PATH)+"/"+SETTING_NAME);
        if(setfile.exists())
        {
            setfile.delete();
        }
        fileUtils.writeTxtToFile(setjson.toString(),setpath.getPath()+"/",setfile.getName());
    }

}
