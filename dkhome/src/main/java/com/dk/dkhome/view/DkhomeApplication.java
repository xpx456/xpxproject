package com.dk.dkhome.view;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import com.dk.dkhome.R;
import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.Goal;
import com.dk.dkhome.entity.SportData;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.entity.UserDefine;
import com.dk.dkhome.entity.UserWeight;
import com.dk.dkhome.utils.FoodManager;
import com.dk.dkhome.utils.SportDataManager;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.utils.WeightManager;
import com.dk.dkhome.view.activity.BigwinerScan2Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.appbase.Local.LocalData;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.TimeUtils;
import intersky.filetools.FileUtils;
import intersky.guide.GuideUtils;
import intersky.guide.entity.GuidePic;
import intersky.scan.ScanUtils;
import intersky.xpxnet.net.NetUtils;

public class DkhomeApplication extends Application {


    public static final int EAT_CARL = 1200;
    public static final int NEED_CARL = 300;
    public static final String ACTION_UPDATA_PLAN = "ACTION_UPDATA_PLAN";
    public static final String ACTION_ADD_PLAN = "ACTION_ADD_PLAN";
    public static final String ACTION_DELETE_PLAN = "ACTION_DELETE_PLAN";
    public static final String HOME_PATH = "/dkhome";
    public static final String HEAD_PATH = "head";
    public AppActivityManager appActivityManager;
    public NetUtils netUtils;
    public static DkhomeApplication mApp;
    public User mAccount = new User();;
    public SportDataManager sportDataManager;
    public FoodManager foodManager;
    public WeightManager weightManager;
    public ArrayList<GuidePic> guidePics = new ArrayList<GuidePic>();
    public ArrayList<String> premission = new ArrayList<String>();
    public String client;
    public SharedPreferences sharedPre;
    public boolean showGuid = true;
    public FileUtils mFileUtils;
    public TestManager testManager;
    public Goal goal = new Goal();
    public void onCreate() {
        mApp = this;
        initBaseModule();
        initModule();
        initData();
        super.onCreate();
    }

    public void initData()
    {
        sharedPre = mApp.getSharedPreferences(UserDefine.USER_INFO, 0);
        premission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        premission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        premission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        premission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if(AppUtils.checkAppUnicode(mApp).length() > 0)
        {
            showGuid = false;
        }
        client = AppUtils.getAppUnicode(mApp);
        initUserData();
        sportDataManager = SportDataManager.init();
        foodManager = FoodManager.init();
        weightManager = WeightManager.init();
        guidePics.add(new GuidePic(R.drawable.dkbg1,mApp.getString(R.string.splash_1)));
        guidePics.add(new GuidePic(R.drawable.dkbg2,mApp.getString(R.string.splash_2)));
        guidePics.add(new GuidePic(R.drawable.dkbg3,mApp.getString(R.string.splash_3)));

    }

    private void initBaseModule()
    {
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        mFileUtils = FileUtils.init(mApp, mApp.getProvidePath,null,null,HOME_PATH);
        netUtils = NetUtils.init(mApp);
        GuideUtils.init(mApp);
        GlideConfiguration.init(new File(mFileUtils.pathUtils.getAppPath()));
        testManager = TestManager.init(mApp);
        ScanUtils.init(mApp);
        EquipData.init();

    }

    public void initModule()
    {

    }

    public void initUserData() {
        mAccount.uid = sharedPre.getString(UserDefine.USER_INFO_USERID,"");
        if(mAccount.uid.length() == 0)
        {
            mAccount.islogin = false;
            mAccount.uid = AppUtils.getguid();

        }
        else
        {
            mAccount.islogin = true;
        }
        SharedPreferences user = mApp.getSharedPreferences(mAccount.uid, 0);
        mAccount.name = user.getString(UserDefine.USER_INFO_USERNAME,"");
        mAccount.age = user.getString(UserDefine.USER_INFO_AGE,"20");
        mAccount.sex = user.getInt(UserDefine.USER_INFO_SEX,0);
        mAccount.tall = user.getInt(UserDefine.USER_INFO_TALL,175);
        mAccount.lastweight = user.getInt(UserDefine.USER_INFO_LAST_WEIGHT,77);
        mAccount.headpath = user.getString(UserDefine.USER_INFO_HEAD,"");
        mAccount.bgpath = user.getString(UserDefine.USER_INFO_BG,"");
        mAccount.creat = user.getString(UserDefine.USER_INFO_CREAT,"");
        goal.type = user.getInt(UserDefine.USER_GOAL_TYPE,0);
        goal.week = user.getInt(UserDefine.USER_GOAL_WEEK,0);
        goal.goalweight = user.getInt(UserDefine.USER_GOAL_WEIGHT,0);
        goal.daycarl = user.getInt(UserDefine.USER_GOAL_CARL,0);
        mApp.mFileUtils.pathUtils.setAppBase(HOME_PATH + "/" + mApp.mAccount.uid);

    }

    public void setAccount()
    {
        SharedPreferences.Editor editor = sharedPre.edit();
        String last = sharedPre.getString(UserDefine.USER_INFO_USERID,"");
        editor.putString(UserDefine.USER_INFO_USERID,mAccount.uid);
        editor.commit();
        SharedPreferences user = mApp.getSharedPreferences(mAccount.uid, 0);
        SharedPreferences.Editor userEditor = user.edit();
        userEditor.putString(UserDefine.USER_INFO_USERNAME,mAccount.name);
        userEditor.putString(UserDefine.USER_INFO_AGE,mAccount.age);
        userEditor.putInt(UserDefine.USER_INFO_SEX,mAccount.sex);
        userEditor.putInt(UserDefine.USER_INFO_TALL,mAccount.tall);
        userEditor.putInt(UserDefine.USER_INFO_LAST_WEIGHT,mAccount.lastweight);
        userEditor.putString(UserDefine.USER_INFO_HEAD,mAccount.headpath);
        userEditor.putString(UserDefine.USER_INFO_BG,mAccount.bgpath);
        if(last.length() == 0)
        {
            mAccount.creat = TimeUtils.getDate();
            userEditor.putString(UserDefine.USER_INFO_CREAT,mAccount.creat);
        }
        userEditor.commit();
    }

    public void setUser()
    {
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString(UserDefine.USER_INFO_USERID,mAccount.uid);
        editor.commit();
        SharedPreferences user = mApp.getSharedPreferences(mAccount.uid, 0);
        SharedPreferences.Editor userEditor = user.edit();
        userEditor.putString(UserDefine.USER_INFO_USERNAME,mAccount.name);
        userEditor.putString(UserDefine.USER_INFO_AGE,mAccount.age);
        userEditor.putInt(UserDefine.USER_INFO_SEX,mAccount.sex);
        userEditor.putString(UserDefine.USER_INFO_HEAD,mAccount.headpath);
        userEditor.commit();

    }

    public void setHealth()
    {
        SharedPreferences user = mApp.getSharedPreferences(mAccount.uid, 0);
        SharedPreferences.Editor userEditor = user.edit();
        userEditor.putInt(UserDefine.USER_INFO_TALL,mAccount.tall);
        userEditor.putInt(UserDefine.USER_INFO_LAST_WEIGHT,mAccount.lastweight);
        userEditor.commit();

    }

    public void setGoal() {
        SharedPreferences user = mApp.getSharedPreferences(mAccount.uid, 0);
        SharedPreferences.Editor userEditor = user.edit();
        userEditor.putInt(UserDefine.USER_GOAL_TYPE,goal.type);
        userEditor.putInt(UserDefine.USER_GOAL_WEEK,goal.week);
        userEditor.putInt(UserDefine.USER_GOAL_WEIGHT,goal.goalweight);
        userEditor.putInt(UserDefine.USER_GOAL_CARL,goal.daycarl);
        userEditor.commit();
    }


    public void updataPlan(Course course) {
        Intent intent = new Intent(ACTION_UPDATA_PLAN);
        intent.putExtra("plan", course);
        Course temp = sportDataManager.allPlans.get(course.oid);
        temp.setPlan(course);
        DBHelper.getInstance(mApp).addOptation(course);
        mApp.sendBroadcast(intent);
    }

    public void addPlan(Course course) {
        Intent intent = new Intent(ACTION_ADD_PLAN);
        intent.putExtra("plan", course);
        ArrayList<Course> courses = sportDataManager.dayPlans.get(TimeUtils.getDate());
        if(courses == null)
        {
            courses = new ArrayList<Course>();
            sportDataManager.dayPlans.put(TimeUtils.getDate(), courses);
        }
        courses.add(0, course);
        DBHelper.getInstance(mApp).addOptation(course);
        sportDataManager.allPlans.put(course.oid, course);
        mApp.sendBroadcast(intent);
    }

    public void deletePlan(Course course){
        Intent intent = new Intent(ACTION_DELETE_PLAN);
        intent.putExtra("plan", course);
        Course temp = sportDataManager.allPlans.get(course.oid);
        sportDataManager.allPlans.remove(temp);
        ArrayList<Course> courses = sportDataManager.dayPlans.get(temp.creat);
        if(courses != null)
        {
            courses.remove(temp);
        }
        DBHelper.getInstance(mApp).deleteOptation(course);
        mApp.sendBroadcast(intent);
    }

    public boolean initdata()
    {
        SharedPreferences sharedPre = mApp.getSharedPreferences(LocalData.SETTING_INFO, 0);
        boolean dark = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        return dark;
    }

    public boolean getDarkType() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(LocalData.SETTING_INFO, 0);
        boolean dark = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
        return dark;
    }

    public int getBuredCarl(String day)
    {
        double burnd = 0;
        SportData sportData = sportDataManager.dayData.get(day);
        if(sportData != null)
        burnd = sportData.gettotalCarl();
        return (int) burnd;
    }

    public void startScan(Activity context, String className, String title) {

        Intent intent = new Intent(context, BigwinerScan2Activity.class);
        intent.putExtra("class", className);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private static GetProvideGetPath getProvidePath = new GetProvideGetPath() {
        @Override
        public Uri getProvideGetPath(File file) {
            return FileProvider.getUriForFile(mApp, "com.dk.pad.fileprovider", file);
        }
    };
}
