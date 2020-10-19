package com.dk.dkphone.view;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;

import androidx.core.content.FileProvider;

import com.dk.dkphone.R;
import com.dk.dkphone.TestManager;
import com.dk.dkphone.database.DBHelper;
import com.dk.dkphone.entity.Optation;
import com.dk.dkphone.entity.SportData;
import com.dk.dkphone.entity.User;
import com.dk.dkphone.entity.UserDefine;
import com.dk.dkphone.entity.UserWeight;
import com.dk.dkphone.handler.AppHandler;
import com.dk.dkphone.view.activity.BigwinerScan2Activity;
import com.dk.dkphone.view.activity.VideoActivity;
import com.interksy.autoupdate.UpDataManager;
import com.interksy.autoupdate.UpdataDownloadThread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.TimeUtils;
import intersky.conversation.NotifictionManager;
import intersky.conversation.entity.Channel;
import intersky.filetools.FileUtils;
import intersky.scan.ScanUtils;
import xpx.audio.AudioManager;

public class DkPadApplication extends Application {


    public static final int UPDTA_PROTECT_TIME = 60;
    public static final int OPTATION_ITEM_PER_SECOND = 60;
    public static final String ACTION_LOCATION_CHANGE = "ACTION_LOCATION_CHANGE";
    public static final String UPDATA_NOTIFICATIONID = "dkphone_updata_download";
    public static final String CHECK_VERSION_URL = "http://www.intersky.com.cn/app/android.version/bigwiner.txt";
    public static final String UPDATA_APP_URL = "http://www.intersky.com.cn/app/android.version/bigwiner.apk";

    public int maxSelect = 1;
    public String versionName = "";
    public int versionCode = 1;
    public static final String UPDATE_NAME = "dkphone.apk";
    public AppActivityManager appActivityManager;
    public static DkPadApplication mApp;
    public TestManager testManager;
    public HashMap<String, User> hashMap = new HashMap<String, User>();
    public ArrayList<User> users = new ArrayList<>();
    public User selectUser;
    public User pk;
    public ArrayList<Optation> optations = new ArrayList<>();
    public HashMap<String, Optation> hashoptations = new HashMap<String, Optation>();
    public Optation selectOptation;
    public SportData sportData;
    public ArrayList<File> bgs = new ArrayList<File>();
    public FileUtils fileUtils;
    public int maxProtectSecond = 0;
    public int protectSecond = 0;
    public boolean stopScreenProtect = false;
    public boolean flagScreenProtect = false;
    public SharedPreferences sharedPre;
    public UpDataManager mUpDataManager;
    public UpdataDownloadThread.UpMessage last;
    public NotifictionManager notifictionManager;
    public NotifictionManager.NotificationOper oper;
    public AudioManager audioManager;
    public AppHandler appHandler = new AppHandler();

    public void onCreate() {
        mApp = this;
        sharedPre = mApp.getSharedPreferences(UserDefine.USER_SETTING, 0);
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        ScanUtils.init(mApp);
        fileUtils = FileUtils.init(mApp,getProvidePath,null,null);
        fileUtils.pathUtils.setBase("/dkphone");
        testManager = TestManager.init();
        initOtherModule();
        audioManager = AudioManager.init(mApp);
        initData();
//        initBgs();
        initUsers();
        initOptation();
        updtatScreenProtect();

        Intent in =new Intent();
        in.setAction("elc.view.hide");
        sendBroadcast(in);

        super.onCreate();
    }

    public void initOtherModule() {
        try {

            File file1 = new File(Environment.getExternalStorageDirectory().getPath()+"/dkphone");
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

            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/dkphone/apk");
            if(file.exists() == false)
            {
                file.mkdirs();
            }

            DkPadApplication.mApp.mUpDataManager = UpDataManager.init(DkPadApplication.mApp
                    ,DkPadApplication.mApp.CHECK_VERSION_URL,DkPadApplication.mApp.UPDATA_APP_URL,
                    packInfo.versionName,packInfo.versionCode,file.getPath()+"/"+DkPadApplication.UPDATE_NAME,
                    DkPadApplication.mApp.updataOperation,DkPadApplication.mApp.getProvidePath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setLastUser(User user) {
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString(UserDefine.SETTING_LAST_UEER,user.uid);
        editor.commit();
    }

    public void setLastOptation(Optation optation){
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString(UserDefine.SETTING_LAST_OPTATION,optation.oid);
        editor.commit();
    }

    public void initBgs() {
        bgs.clear();
        File bgs = new File(fileUtils.pathUtils.getfilePath("bg"));
        if(bgs.isDirectory())
        {
            File[] files = bgs.listFiles();
            for(int i = 0 ; i < files.length ; i++)
            {
                if(fileUtils.getFileType(files[i].getName()) == FileUtils.FILE_TYPE_PICTURE)
                {
                    mApp.bgs.add(files[0]);
                }
            }
        }
    }

    public void updtatScreenProtect() {
        if(protectSecond > 0)
        {
            if(stopScreenProtect == false)
            protectSecond--;
        }
        else
        {
            if(flagScreenProtect == false)
            {
                flagScreenProtect = true;
                Intent intent = new Intent(appActivityManager.getCurrentActivity(), VideoActivity.class);
                appActivityManager.getCurrentActivity().startActivity(intent);
            }
        }
        appHandler.sendEmptyMessageDelayed(AppHandler.SET_UPDATA_SCREEN_PROTECT,1000);
    }

    public void setProtectState(Boolean stop) {
        stopScreenProtect = stop;
    }

    public void setProtectTime(int time) {
        protectSecond = time;
    }

    public void appSetProtectState(Boolean stop) {
        Message msg = new Message();
        msg.what = AppHandler.SET_SCREEN_PROTECT_STATE;
        msg.obj = stop;
        appHandler.sendMessage(msg);
    }

    public void appSetProtectTime(int time) {
        Message msg = new Message();
        msg.what = AppHandler.SET_PROTECT_TIME;
        msg.obj = time;
        appHandler.sendMessage(msg);
    }

    public static GetProvideGetPath getProvidePath = new GetProvideGetPath() {
        @Override
        public Uri getProvideGetPath(File file) {
            return FileProvider.getUriForFile(DkPadApplication.mApp, "com.dk.dkphone.fileprovider", file);
        }
    };


    private void initData() {
        maxProtectSecond = sharedPre.getInt(UserDefine.SETTING_SCREEN_PROTECT_TIME,360);
        maxSelect = sharedPre.getInt(UserDefine.USER_SETTING_MAX_SELECT,24);
        protectSecond = maxProtectSecond;
    }

    private void initUsers() {

        String lastuer = sharedPre.getString(UserDefine.SETTING_LAST_UEER,"");
        if(lastuer.length() > 0)
        {
            selectUser = DBHelper.getInstance(mApp).scanUsers(users,hashMap,lastuer);
        }
        else
        {
            DBHelper.getInstance(mApp).scanUsers(users,hashMap);
        }
        if(hashMap.size() == 0)
        {
            User man = new User();
            man.age = "35";
            man.name = "张三";
            man.sex = mApp.getString(R.string.male);
            man.toll = "180";
            UserWeight userWeight = new UserWeight();
            userWeight.date = TimeUtils.getDate();
            userWeight.uid = man.uid;
            userWeight.weight = "75";
            man.wid = userWeight.wid;
            DBHelper.getInstance(mApp).addUser(man);
            DBHelper.getInstance(mApp).addUserWeight(userWeight);
            users.add(man);
            hashMap.put(man.uid,man);
        }
        if(selectUser == null)
            selectUser = users.get(0);

        User add = new User();
        users.add(add);
        users.remove(selectUser);

        String testid = sharedPre.getString(UserDefine.SETTING_LAST_TEST,"");
        sportData = DBHelper.getInstance(mApp).scanRecords(selectUser.uid,testid);

    }

    private void initOptation()
    {

        String lastop = sharedPre.getString(UserDefine.SETTING_LAST_OPTATION,"");
        if(lastop.length() == 0)
        {
            DBHelper.getInstance(mApp).scanOptations(optations,hashoptations);
        }
        else
        {
            selectOptation = DBHelper.getInstance(mApp).scanOptations(optations,hashoptations,lastop);
        }
        if(optations.size() == 0)
        {
            Optation optation = new Optation();
            optation.name = "计划1";
            optation.data = "[20,20,20,23,20,20,20,20,10,10,10]";
            DBHelper.getInstance(mApp).addOptation(optation);
            optations.add(optation);
            hashoptations.put(optation.oid,optation);
        }

        if(selectOptation == null)
        selectOptation = optations.get(0);

        Optation optation = new Optation();
        optations.add(optation);
        optations.remove(selectOptation);

        if(sportData.last == null)
        {
            DkPadApplication.mApp.testManager.setNewTest(selectOptation,selectUser);
        }
        else
        {
            DkPadApplication.mApp.testManager.setTest(sportData.last);
        }

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

    public void startScan(Activity context, String className) {

        Intent intent = new Intent(context, BigwinerScan2Activity.class);
        intent.putExtra("class", className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (className.length() == 0) {
            context.startActivityForResult(intent, ScanUtils.SCAN_FINISH);
        } else {
            context.startActivity(intent);
        }

    }

    public void startScan(Activity context, String className, String title) {

        Intent intent = new Intent(context, BigwinerScan2Activity.class);
        intent.putExtra("class", className);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void startScan(Activity context) {
        Intent intent = new Intent(context, BigwinerScan2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivityForResult(intent, ScanUtils.SCAN_FINISH);
    }


}
