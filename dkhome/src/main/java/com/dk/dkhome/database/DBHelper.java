package com.dk.dkhome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.Eat;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.SportData;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.entity.UserWeight;
import com.dk.dkhome.utils.FoodManager;
import com.dk.dkhome.utils.WeightManager;
import com.dk.dkhome.view.DkhomeApplication;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.TimeUtils;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dkhome.db";
    public static final int DB_VERSION = 48;

    private static final String TABLE_RECORD = "TABLE_RECORD";
    private static final String RECORD_RECORDID = "RECORD_RECORDID";
    private static final String RECORD_USERID = "RECORD_USERID";
    private static final String RECORD_TIME = "RECORD_TIME";
    private static final String RECORD_DATE = "RECORD_DATE";
    private static final String RECORD_SPORTDATA = "RECORD_SPORTDATA";
    private static final String RECORD_FINISH = "RECORD_FINISH";
    private static final String RECORD_OPTATIONDATA = "RECORD_OPTATIONDATA";
    private static final String RECORD_AVERAGESPEED = "RECORD_AVERAGESPEED";
    private static final String RECORD_TOPSPEED = "RECORD_TOPSPEED";
    private static final String RECORD_DISTENCE = "RECORD_DISTENCE";
    private static final String RECORD_SUNLEAVEL = "RECORD_SUNLEAVEL";
    private static final String RECORD_DURING = "RECORD_DURING";


    private static final String TABLE_WEIGHT = "TABLE_WEIGHT";
    private static final String WEIGHT_WEIGHT_ID = "WEIGHT_WEIGHT_ID";
    private static final String WEIGHT_USER_ID = "WEIGHT_USER_ID";
    private static final String WEIGHT_WEIGHT = "WEIGHT_WEIGHT";
    private static final String WEIGHT_DATE = "WEIGHT_DATE";

    private static final String TABLE_PLAN = "TABLE_PLAN";
    private static final String PLAN_ID = "PLAN_ID";
    private static final String PLAN_NAME = "PLAN_NAME";
    private static final String PLAN_DATA = "PLAN_DATA";
    private static final String PLAN_DURING = "PLAN_DURING";
    private static final String PLAN_NOW = "PLAN_NOW";
    private static final String PLAN_CREAT = "PLAN_CREAT";
    private static final String PLAN_UID = "PLAN_UID";
    private static final String PLAN_CID = "PLAN_CID";
    private static final String PLAN_DIS = "PLAN_DIS";
    private static final String PLAN_TOTAL_CAR = "PLAN_TOTAL_CAR";
    private static final String PLAN_VIDEO_NAME = "PLAN_VIDEO_NAME";
    private static final String PLAN_VIDEO_PATH = "PLAN_VIDEO_PATH";
    private static final String PLAN_VIDEO_URL = "PLAN_VIDEO_URL";
    private static final String PLAN_VIDEO_IMG = "PLAN_VIDEO_IMG";
    private static final String PLAN_VIDEO_TIME = "PLAN_VIDEO_TIME";


    private static final String TABLE_DATA = "TABLE_DATA";
    private static final String DATA_ID = "DATA_ID";
    private static final String DATA_TIME = "DATA_TIME";
    private static final String DATA_DATA = "DATA_DATA";
    private static final String DATA_UID = "DATA_UID";
    private static final String DATA_BCARL = "DATA_BCARL";

    private static final String TABLE_EAT = "TABLE_EAT";
    private static final String EAT_ID = "EAT_ID";
    private static final String EAT_DATE = "EAT_DATE";
    private static final String EAT_JSON = "EAT_JSON";
    private static final String EAT_USER = "EAT_USER";
    private static final String EAT_CARL = "EAT_CARL";

    private static DBHelper mDBHelper;
    private SQLiteDatabase db = null;
    public static DBHelper getInstance(Context context) {
        if (null == mDBHelper) {

            mDBHelper = new DBHelper(context);
        }
        return mDBHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        openDB();
    }


    private void openDB() {
        if (null == db || !db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);


        String sql = "CREATE TABLE " + TABLE_RECORD + " (" + RECORD_RECORDID
                + " TEXT PRIMARY KEY," + RECORD_USERID + " TEXT," + RECORD_DATE
                + " TEXT," + RECORD_DURING + " TEXT,"  + RECORD_TIME + " TEXT,"
                + RECORD_SPORTDATA +" TEXT,"+ RECORD_OPTATIONDATA +" TEXT,"
                + RECORD_AVERAGESPEED +" TEXT,"+ RECORD_TOPSPEED +" TEXT,"
                + RECORD_DISTENCE +" TEXT," + RECORD_SUNLEAVEL +" TEXT,"
                + RECORD_FINISH + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);

        sql = "CREATE TABLE " + TABLE_WEIGHT + " (" + WEIGHT_WEIGHT_ID
                + " TEXT PRIMARY KEY," + WEIGHT_USER_ID+" TEXT,"  + WEIGHT_DATE
                +" TEXT,"  + WEIGHT_WEIGHT+ " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN);

        sql = "CREATE TABLE " + TABLE_PLAN + " (" + PLAN_ID
                + " TEXT PRIMARY KEY," + PLAN_NAME +" TEXT," + PLAN_DURING +" TEXT,"+ PLAN_NOW +" TEXT,"
                + PLAN_VIDEO_NAME +" TEXT,"+ PLAN_VIDEO_PATH +" TEXT,"+ PLAN_VIDEO_URL +" TEXT,"+ PLAN_VIDEO_IMG +" TEXT,"+ PLAN_VIDEO_TIME +" TEXT,"
                + PLAN_TOTAL_CAR +" TEXT,"+ PLAN_DIS +" TEXT,"+ PLAN_CID +" TEXT,"+ PLAN_CREAT +" TEXT,"+ PLAN_UID  +" TEXT," + PLAN_DATA + " TEXT)";
        db.execSQL(sql);


        sql = "CREATE TABLE " + TABLE_EAT + " (" + EAT_ID
                + " TEXT PRIMARY KEY," + EAT_JSON    +" TEXT,"  + EAT_DATE+" TEXT,"
                +" TEXT,"  + EAT_USER+" TEXT,"  + EAT_CARL+ " TEXT)";
        db.execSQL(sql);


        sql = "CREATE TABLE " + TABLE_DATA + " (" + DATA_ID
                + " TEXT PRIMARY KEY," + DATA_TIME +" TEXT,"   + DATA_DATA+" TEXT," + DATA_BCARL+" TEXT,"  + DATA_UID+ " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);


        String sql = "CREATE TABLE " + TABLE_RECORD + " (" + RECORD_RECORDID
                + " TEXT PRIMARY KEY," + RECORD_USERID + " TEXT," + RECORD_DATE
                + " TEXT," + RECORD_DURING + " TEXT,"  + RECORD_TIME + " TEXT,"
                + RECORD_SPORTDATA +" TEXT,"+ RECORD_OPTATIONDATA +" TEXT,"
                + RECORD_AVERAGESPEED +" TEXT,"+ RECORD_TOPSPEED +" TEXT,"
                + RECORD_DISTENCE +" TEXT," + RECORD_SUNLEAVEL +" TEXT,"
                + RECORD_FINISH + " TEXT)";
        db.execSQL(sql);


//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
//
//        sql = "CREATE TABLE " + TABLE_WEIGHT + " (" + WEIGHT_WEIGHT_ID
//                + " TEXT PRIMARY KEY," + WEIGHT_USER_ID+" TEXT,"  + WEIGHT_DATE
//                +" TEXT,"  + WEIGHT_WEIGHT+ " TEXT)";
//        db.execSQL(sql);

//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN);
//
//        sql = "CREATE TABLE " + TABLE_PLAN + " (" + PLAN_ID
//                + " TEXT PRIMARY KEY," + PLAN_NAME +" TEXT," + PLAN_DURING +" TEXT,"+ PLAN_NOW +" TEXT,"
//                + PLAN_VIDEO_NAME +" TEXT,"+ PLAN_VIDEO_PATH +" TEXT,"+ PLAN_VIDEO_URL +" TEXT,"+ PLAN_VIDEO_IMG +" TEXT,"+ PLAN_VIDEO_TIME +" TEXT,"
//                + PLAN_TOTAL_CAR +" TEXT,"+ PLAN_DIS +" TEXT,"+ PLAN_CID +" TEXT,"+ PLAN_CREAT +" TEXT,"+ PLAN_UID  +" TEXT," + PLAN_DATA + " TEXT)";
//        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EAT);

        sql = "CREATE TABLE " + TABLE_EAT + " (" + EAT_ID
                + " TEXT PRIMARY KEY," + EAT_JSON    +" TEXT,"  + EAT_DATE+" TEXT,"
                +" TEXT,"  + EAT_USER+" TEXT,"  + EAT_CARL+ " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        sql = "CREATE TABLE " + TABLE_DATA + " (" + DATA_ID
                + " TEXT PRIMARY KEY," + DATA_TIME +" TEXT,"   + DATA_DATA+" TEXT," + DATA_BCARL+" TEXT,"   + DATA_UID+ " TEXT)";
        db.execSQL(sql);
    }


    public SportData scanData(HashMap<String, SportData> datas) {
        String sql = "SELECT * FROM " + TABLE_DATA + " WHERE "+  DATA_UID + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {DkhomeApplication.mApp.mAccount.uid});
        datas.clear();
        SportData today = null;
        while (c.moveToNext()) {
            SportData sportData = new SportData();
            sportData.time = c.getString(c.getColumnIndex(DATA_TIME));
            sportData.json = c.getString(c.getColumnIndex(DATA_DATA));
            sportData.id = c.getString(c.getColumnIndex(DATA_ID));
            sportData.userid = c.getString(c.getColumnIndex(DATA_UID));
            sportData.baseCarl = Integer.valueOf(c.getString(c.getColumnIndex(DATA_BCARL)));
            sportData.initjson();
            datas.put(sportData.time,sportData);
            if(sportData.time.equals(TimeUtils.getDate()))
            {
                today = sportData;
            }
        }
        if(today == null)
        {
            today = new SportData();
            today.time = TimeUtils.getDate();
            today.userid = DkhomeApplication.mApp.mAccount.uid;
            today.baseCarl = EquipData.getBaseCarl(DkhomeApplication.mApp.mAccount);
            saveData(today);
            datas.put(today.time,today);
        }
        return today;
    }

    public void saveData(SportData sportData) {
        ContentValues cv = new ContentValues();
        cv.put(DATA_ID, sportData.id);
        cv.put(DATA_TIME, sportData.time);
        cv.put(DATA_DATA, sportData.praseDataJson());
        cv.put(DATA_UID, sportData.userid);
        cv.put(DATA_BCARL, String.valueOf(sportData.baseCarl));
        int iRet = (int) db.insert(TABLE_DATA, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_DATA, cv, DATA_ID + "=?", new String[]
                    {sportData.id});
        }
    }

    public void updataData(SportData sportData) {
        ContentValues cv = new ContentValues();
        cv.put(DATA_ID, sportData.id);
        cv.put(DATA_TIME, sportData.time);
        cv.put(DATA_DATA, sportData.praseDataJson());
        cv.put(DATA_UID, sportData.userid);
        cv.put(DATA_BCARL, String.valueOf(sportData.baseCarl));
        db.update(TABLE_DATA, cv, DATA_ID + "=?", new String[]
                {sportData.id});
    }

    public void scanPlans(HashMap<String, ArrayList<Course>> hashMapDay, HashMap<String, Course> hashMapAll) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_PLAN + " WHERE "+  PLAN_UID + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {DkhomeApplication.mApp.mAccount.uid});
        hashMapDay.clear();
        hashMapAll.clear();
        while (c.moveToNext()) {
            Course info = new Course();
            info.oid = c.getString(c.getColumnIndex(PLAN_ID));
            info.name = c.getString(c.getColumnIndex(PLAN_NAME));
            EquipData.praseStringData(info,c.getString(c.getColumnIndex(PLAN_DATA)));
            info.creat = c.getString(c.getColumnIndex(PLAN_CREAT));
            info.userid = c.getString(c.getColumnIndex(PLAN_UID));
            info.current = Integer.valueOf(c.getString(c.getColumnIndex(PLAN_NOW)));
            info.during = Integer.valueOf(c.getString(c.getColumnIndex(PLAN_DURING)));
            info.path = c.getString(c.getColumnIndex(PLAN_VIDEO_PATH));
            info.url = c.getString(c.getColumnIndex(PLAN_VIDEO_URL));
            info.cid = c.getString(c.getColumnIndex(PLAN_CID));
            info.img = c.getString(c.getColumnIndex(PLAN_VIDEO_IMG));
            info.videotime = Integer.valueOf(c.getString(c.getColumnIndex(PLAN_VIDEO_TIME)));
            info.videoname = c.getString(c.getColumnIndex(PLAN_VIDEO_NAME));
            info.dis = Double.valueOf(c.getString(c.getColumnIndex(PLAN_DIS)));
            info.totalCarl = Double.valueOf(c.getString(c.getColumnIndex(PLAN_TOTAL_CAR)));
            hashMapAll.put(info.oid,info);
            ArrayList<Course> courses = hashMapDay.get(info.creat);
            if(courses == null)
            {
                courses = new ArrayList<Course>();
                hashMapDay.put(info.creat, courses);
            }
            courses.add(0,info);
        }
        c.close();
    }

    public int addOptation(Course sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(PLAN_ID, sInfo.oid);
        cv.put(PLAN_DATA, EquipData.initStringData(sInfo));
        cv.put(PLAN_NAME, sInfo.name);
        cv.put(PLAN_CREAT, sInfo.creat);
        cv.put(PLAN_UID, sInfo.userid);
        cv.put(PLAN_NOW, String.valueOf(sInfo.current));
        cv.put(PLAN_DURING, String.valueOf(sInfo.during));
        cv.put(PLAN_VIDEO_NAME, sInfo.videoname);
        cv.put(PLAN_VIDEO_PATH, sInfo.path);
        cv.put(PLAN_VIDEO_URL, sInfo.url);
        cv.put(PLAN_VIDEO_IMG, sInfo.img);
        cv.put(PLAN_VIDEO_TIME, Integer.valueOf(sInfo.videotime));
        cv.put(PLAN_CID, sInfo.cid);
        cv.put(PLAN_TOTAL_CAR, String.valueOf(sInfo.totalCarl));
        cv.put(PLAN_DIS, String.valueOf(sInfo.dis));
        int iRet = (int) db.insert(TABLE_PLAN, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_PLAN, cv, PLAN_ID + "=?", new String[]
                    {sInfo.oid});
        }
        return iRet;
    }

    public void upDataOptation(Course sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(PLAN_ID, sInfo.oid);
        cv.put(PLAN_DATA, EquipData.initStringData(sInfo));
        cv.put(PLAN_NAME, sInfo.name);
        cv.put(PLAN_CREAT, sInfo.creat);
        cv.put(PLAN_UID, sInfo.userid);
        cv.put(PLAN_NOW, String.valueOf(sInfo.current));
        cv.put(PLAN_DURING, String.valueOf(sInfo.during));
        cv.put(PLAN_VIDEO_NAME, sInfo.videoname);
        cv.put(PLAN_VIDEO_PATH, sInfo.path);
        cv.put(PLAN_VIDEO_URL, sInfo.url);
        cv.put(PLAN_VIDEO_IMG, sInfo.img);
        cv.put(PLAN_VIDEO_TIME, Integer.valueOf(sInfo.videotime));
        cv.put(PLAN_CID, sInfo.cid);
        cv.put(PLAN_TOTAL_CAR, String.valueOf(sInfo.totalCarl));
        cv.put(PLAN_DIS, String.valueOf(sInfo.dis));
        db.update(TABLE_PLAN, cv, PLAN_ID + "=?", new String[]
                {sInfo.oid});
        return ;
    }

    public int deleteOptation(Course sInfo) {
        return db.delete(TABLE_PLAN, PLAN_ID + "=?", new String[]
                {sInfo.oid});
    }
    

    public UserWeight[] scanUserWeight(User user, WeightManager weightManager) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_WEIGHT+ " WHERE "+  WEIGHT_USER_ID + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {user.uid});
        UserWeight today = null;
        UserWeight last = null;
        UserWeight[] data = new UserWeight[2];
        ArrayList<UserWeight> userWeights = new ArrayList<UserWeight>();
        while (c.moveToNext()) {
            UserWeight info = new UserWeight();
            info.id = c.getString(c.getColumnIndex(WEIGHT_WEIGHT_ID));
            info.uid = c.getString(c.getColumnIndex(WEIGHT_USER_ID));
            info.date = c.getString(c.getColumnIndex(WEIGHT_DATE));
            info.weight = c.getString(c.getColumnIndex(WEIGHT_WEIGHT));
            weightManager.dayWeight.put(info.date,info);
            userWeights.add(0,info);

        }
        if(userWeights.size() >= 1)
        {
            today = userWeights.get(0);
        }
        if(userWeights.size() >= 2 )
        {
            last = userWeights.get(1);
        }
        c.close();
        if(today != null && last != null)
        {
            if(Integer.valueOf(today.weight) > Integer.valueOf(last.weight) )
            {
                weightManager.weightstate = WeightManager.STATE_UP;
            }
            else if(Integer.valueOf(today.weight) < Integer.valueOf(last.weight) )
            {
                weightManager.weightstate = WeightManager.STATE_DOWN;
            }
            else
            {
                weightManager.weightstate = WeightManager.STATE_SAME;
            }
        }
        else
        {
            weightManager.weightstate = WeightManager.STATE_SAME;
        }
        if(today == null)
        {
            today = new UserWeight();
            today.uid = DkhomeApplication.mApp.mAccount.uid;
            today.date = TimeUtils.getDate();
            today.weight = String.valueOf(DkhomeApplication.mApp.mAccount.lastweight);
            weightManager.dayWeight.put(today.date,today);
            addUserWeight(today);
        }
        data[0] = today;
        data[1] = last;
        return data;
    }

    public int addUserWeight(UserWeight sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(WEIGHT_WEIGHT_ID, sInfo.id);
        cv.put(WEIGHT_USER_ID, sInfo.uid);
        cv.put(WEIGHT_DATE, sInfo.date);
        cv.put(WEIGHT_WEIGHT, sInfo.weight);
        int iRet = (int) db.insert(TABLE_WEIGHT, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_WEIGHT, cv, WEIGHT_DATE + "=?", new String[]
                    {sInfo.date});
        }

        return iRet;
    }


    public int deleteUserWeight(User sInfo) {
        return db.delete(TABLE_WEIGHT, WEIGHT_USER_ID + "=?", new String[]
                {sInfo.uid});
    }


    public Eat scanEat(HashMap<String, Eat> dayEat) {
        String sql = "SELECT * FROM " + TABLE_EAT + " WHERE "+  EAT_USER + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {DkhomeApplication.mApp.mAccount.uid});
        dayEat.clear();
        Eat today = null;
        while (c.moveToNext()) {
            Eat eatData = new Eat();
            eatData.date = c.getString(c.getColumnIndex(EAT_DATE));
            eatData.json = c.getString(c.getColumnIndex(EAT_JSON));
            eatData.id = c.getString(c.getColumnIndex(EAT_ID));
            eatData.uid = c.getString(c.getColumnIndex(EAT_USER));
            eatData.carl = Integer.valueOf(c.getString(c.getColumnIndex(EAT_CARL)));
            dayEat.put(eatData.date,eatData);
            if(eatData.date.equals(TimeUtils.getDate()))
            {
                today = eatData;
            }
        }
        if(today == null)
        {
            today = new Eat();
            today.date = TimeUtils.getDate();
            today.uid = DkhomeApplication.mApp.mAccount.uid;
            today.carl = FoodManager.foodManager.totalCarl;
            addEat(today);
            dayEat.put(today.date,today);
        }
        return today;
    }

    public int addEat(Eat sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(EAT_USER, sInfo.uid);
        cv.put(EAT_ID, sInfo.id);
        cv.put(EAT_DATE, sInfo.date);
        cv.put(EAT_CARL, String.valueOf(sInfo.carl));
        cv.put(EAT_JSON, sInfo.json);
        int iRet = (int) db.insert(TABLE_EAT, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_EAT, cv, EAT_ID + "=?", new String[]
                    {sInfo.date});
        }

        return iRet;
    }

    public void updataEat(Eat sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(EAT_USER, sInfo.uid);
        cv.put(EAT_ID, sInfo.id);
        cv.put(EAT_DATE, sInfo.date);
        cv.put(EAT_CARL, String.valueOf(sInfo.carl));
        cv.put(EAT_JSON, sInfo.json);
        db.update(TABLE_EAT, cv, EAT_ID + "=?", new String[]
                {sInfo.date});
    }


}
