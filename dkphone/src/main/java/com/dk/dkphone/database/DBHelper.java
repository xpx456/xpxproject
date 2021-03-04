package com.dk.dkphone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dk.dkphone.TestManager;
import com.dk.dkphone.entity.Optation;
import com.dk.dkphone.entity.SportData;
import com.dk.dkphone.entity.TestItem;
import com.dk.dkphone.entity.User;
import com.dk.dkphone.entity.UserWeight;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.TimeUtils;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dkphone.db";
    public static final int DB_VERSION = 26;

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
    private static final String RECORD_SUNCARL = "RECORD_SUNCARL";
    private static final String RECORD_DURING = "RECORD_DURING";


    private static final String TABLE_USER = "TABLE_USER";
    private static final String USER_ID = "USER_ID";
    private static final String USER_WEIGHT_ID = "USER_WEIGHT_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_AGE = "USER_AGE";
    private static final String USER_SEX = "USER_SEX";
    private static final String USER_PATH = "USER_PATH";
    private static final String USER_TOLL = "USER_TOLL";

    private static final String TABLE_WEIGHT = "TABLE_WEIGHT";
    private static final String WEIGHT_ID = "WEIGHT_ID";
    private static final String WEIGHT_USER_ID = "WEIGHT_USER_ID";
    private static final String WEIGHT_WEIGHT = "WEIGHT_WEIGHT";
    private static final String WEIGHT_DATE = "WEIGHT_DATE";

    private static final String TABLE_OPTATION = "TABLE_OPTATION";
    private static final String OPTATION_ID = "OPTATION_ID";
    private static final String OPTATION_NAME = "OPTATION_NAME";
    private static final String OPTATION_DATA = "OPTATION_DATA";


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
                + RECORD_SUNCARL +" TEXT,"
                + RECORD_FINISH + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

         sql = "CREATE TABLE " + TABLE_USER + " (" + USER_ID
                + " TEXT PRIMARY KEY," + USER_NAME + " TEXT," + USER_AGE
                + " TEXT," + USER_SEX + " TEXT," + USER_PATH + " TEXT,"
                 + USER_WEIGHT_ID +" TEXT,"  + USER_TOLL+ " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);

        sql = "CREATE TABLE " + TABLE_WEIGHT + " (" + WEIGHT_ID
                + " TEXT PRIMARY KEY," + WEIGHT_USER_ID + " TEXT," + WEIGHT_WEIGHT
                +" TEXT,"  + WEIGHT_DATE+ " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTATION);

        sql = "CREATE TABLE " + TABLE_OPTATION + " (" + OPTATION_ID
                + " TEXT PRIMARY KEY," + OPTATION_NAME
                +" TEXT,"  + OPTATION_DATA+ " TEXT)";
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
                + RECORD_SUNCARL +" TEXT,"
                + RECORD_FINISH + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        sql = "CREATE TABLE " + TABLE_USER + " (" + USER_ID
                + " TEXT PRIMARY KEY," + USER_NAME + " TEXT," + USER_AGE
                + " TEXT," + USER_SEX + " TEXT," + USER_PATH + " TEXT,"
                + USER_WEIGHT_ID +" TEXT,"  + USER_TOLL+ " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);

        sql = "CREATE TABLE " + TABLE_WEIGHT + " (" + WEIGHT_ID
                + " TEXT PRIMARY KEY," + WEIGHT_USER_ID + " TEXT," + WEIGHT_WEIGHT
                +" TEXT,"  + WEIGHT_DATE+ " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTATION);

        sql = "CREATE TABLE " + TABLE_OPTATION + " (" + OPTATION_ID
                + " TEXT PRIMARY KEY," + OPTATION_NAME
                +" TEXT,"  + OPTATION_DATA+ " TEXT)";
        db.execSQL(sql);
    }

    public SportData scanRecords(String userid) {
        openDB();
        SportData sportData = new SportData();
        String sql = "SELECT * FROM " + TABLE_RECORD + " WHERE "+  RECORD_USERID + " = ?";
//        int totaldis = 0;
//        int totalcurrent = 0;
        Cursor c = db.rawQuery(sql, new String[]
                {userid});
        while (c.moveToNext()) {
            TestItem info = new TestItem();
            info.uid = c.getString(c.getColumnIndex(RECORD_USERID));
            info.rid = c.getString(c.getColumnIndex(RECORD_RECORDID));
            info.time = c.getString(c.getColumnIndex(RECORD_TIME));
            info.date = c.getString(c.getColumnIndex(RECORD_DATE));
            info.sunleavel = Integer.valueOf(c.getString(c.getColumnIndex(RECORD_SUNLEAVEL)));
            info.averageSpeed = Double.valueOf(c.getString(c.getColumnIndex(RECORD_AVERAGESPEED)));
            info.topSpeed = Double.valueOf(c.getString(c.getColumnIndex(RECORD_TOPSPEED)));
            info.distence = Double.valueOf(c.getString(c.getColumnIndex(RECORD_DISTENCE)));
            info.current = Integer.valueOf(c.getString(c.getColumnIndex(RECORD_DURING)));
            info.allcarl = Double.valueOf(c.getString(c.getColumnIndex(RECORD_SUNCARL)));
            TestManager.praseStringData(info,c.getString(c.getColumnIndex(RECORD_SPORTDATA)));
//            TestManager.praseOpData(info,c.getString(c.getColumnIndex(RECORD_OPTATIONDATA)));
//            info.finish = Boolean.valueOf(c.getString(c.getColumnIndex(RECORD_FINISH)));
            sportData.allrecords.put(info.rid,info);
//            sportData.totalcount++;
//            if(info.finish)
//            {
//                sportData.finishcount++;
//            }


            sportData.sumdistence += info.distence;
            sportData.sumsecond += info.current;
            sportData.totalleave += info.sunleavel;
            if(sportData.topspeed < info.topSpeed)
            {
                sportData.topspeed = info.topSpeed;
            }

            ArrayList<TestItem> datalist = sportData.records.get(info.date);
            if(datalist == null)
            {
                datalist = new ArrayList<TestItem>();
                sportData.records.put(info.date,datalist);
            }
            if(info.date.equals(TimeUtils.getDate()))
            {
                sportData.daydistence += info.distence;
                sportData.daysecond += info.current;
                sportData.daycarl += info.allcarl;
            }

            datalist.add(info);
        }
        c.close();
        return sportData;
    }
//
//    public SportData scanRecords(String userid,String lastid) {
//        openDB();
//        SportData sportData = new SportData();
//        String sql = "SELECT * FROM " + TABLE_RECORD + " WHERE "+  RECORD_USERID + " = ?";
//        int totaldis = 0;
//        int totalcurrent = 0;
//        Cursor c = db.rawQuery(sql, new String[]
//                {userid});
//        while (c.moveToNext()) {
//            TestItem info = new TestItem();
//            info.uid = c.getString(c.getColumnIndex(RECORD_USERID));
//            info.rid = c.getString(c.getColumnIndex(RECORD_RECORDID));
//            info.time = c.getString(c.getColumnIndex(RECORD_TIME));
//            info.date = c.getString(c.getColumnIndex(RECORD_DATE));
//            info.sunleavel = Integer.valueOf(c.getString(c.getColumnIndex(RECORD_SUNLEAVEL)));
//            info.averageSpeed = Double.valueOf(c.getString(c.getColumnIndex(RECORD_AVERAGESPEED)));
//            info.topSpeed = Double.valueOf(c.getString(c.getColumnIndex(RECORD_TOPSPEED)));
//            info.distence = Double.valueOf(c.getString(c.getColumnIndex(RECORD_DISTENCE)));
//            info.current = Integer.valueOf(c.getString(c.getColumnIndex(RECORD_DURING)));
//            TestManager.praseStringData(info,c.getString(c.getColumnIndex(RECORD_SPORTDATA)));
////            TestManager.praseOpData(info,c.getString(c.getColumnIndex(RECORD_OPTATIONDATA)));
////            info.finish = Boolean.valueOf(c.getString(c.getColumnIndex(RECORD_FINISH)));
//            sportData.allrecords.put(info.rid,info);
////            if(info.finish == false && info.rid.equals(lastid))
////            {
////                sportData.last = info;
////            }
////
////            sportData.totalcount++;
////            if(info.finish)
////            {
////                sportData.finishcount++;
////            }
//
//
//            sportData.sumdistence += info.distence;
//            sportData.sumsecond += info.current;
//            sportData.totalleave += info.sunleavel;
//            if(sportData.topspeed < info.topSpeed)
//            {
//                sportData.topspeed = info.topSpeed;
//            }
//
//            ArrayList<TestItem> datalist = sportData.records.get(info.date);
//            if(datalist == null)
//            {
//                datalist = new ArrayList<TestItem>();
//                sportData.days++;
//                sportData.records.put(info.date,datalist);
//            }
//            datalist.add(info);
//        }
//        c.close();
//        return sportData;
//    }

    public int saveRecord(TestItem info) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(RECORD_RECORDID, info.rid);
        cv.put(RECORD_USERID, info.uid);
        cv.put(RECORD_TIME, info.time);
        cv.put(RECORD_DATE, info.date);
        cv.put(RECORD_SUNLEAVEL, String.valueOf(info.sunleavel));
        cv.put(RECORD_AVERAGESPEED, String.valueOf(info.averageSpeed));
        cv.put(RECORD_TOPSPEED, String.valueOf(info.topSpeed));
        cv.put(RECORD_DISTENCE, String.valueOf(info.distence));
        cv.put(RECORD_DURING, String.valueOf(info.current));
        cv.put(RECORD_SUNCARL, String.valueOf(info.allcarl));
        cv.put(RECORD_SPORTDATA, TestManager.initStringData(info));
//        cv.put(RECORD_OPTATIONDATA, info.hashopLeave.toString());
        cv.put(RECORD_OPTATIONDATA, "");
//        cv.put(RECORD_FINISH, String.valueOf(info.finish));
        cv.put(RECORD_FINISH, true);
        int iRet = (int) db.insert(TABLE_RECORD, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_RECORD, cv, RECORD_RECORDID + "=?", new String[]
                    {info.rid});
        }
        return iRet;
    }



    public void scanOptations(ArrayList<Optation> optations,HashMap<String,Optation> hashMap) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_OPTATION;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            Optation info = new Optation();
            info.oid = c.getString(c.getColumnIndex(OPTATION_ID));
            info.name = c.getString(c.getColumnIndex(OPTATION_NAME));
            info.data = c.getString(c.getColumnIndex(OPTATION_DATA));
            optations.add(info);
            hashMap.put(info.oid,info);
        }
        c.close();
    }

    public Optation scanOptations(ArrayList<Optation> optations,HashMap<String,Optation> hashMap,String last) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_OPTATION;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        Optation lastop = null;
        while (c.moveToNext()) {
            Optation info = new Optation();
            info.oid = c.getString(c.getColumnIndex(OPTATION_ID));
            info.name = c.getString(c.getColumnIndex(OPTATION_NAME));
            info.data = c.getString(c.getColumnIndex(OPTATION_DATA));
            optations.add(info);
            hashMap.put(info.oid,info);
            if(info.oid.equals(last))
            {
                lastop = info;
            }
        }
        c.close();
        return lastop;
    }

    public int addOptation(Optation sInfo)
    {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(OPTATION_ID, sInfo.oid);
        cv.put(OPTATION_DATA, sInfo.data);
        cv.put(OPTATION_NAME, sInfo.name);
        int iRet = (int) db.insert(TABLE_OPTATION, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_OPTATION, cv, OPTATION_ID + "=?", new String[]
                    {sInfo.oid});
        }
        return iRet;
    }

    public int deleteOptation(Optation sInfo) {
        return db.delete(TABLE_OPTATION, OPTATION_ID + "=?", new String[]
                {sInfo.oid});
    }


    public void scanUsers(ArrayList<User> users,HashMap<String,User> hashMap) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_USER;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            User info = new User();
            info.uid = c.getString(c.getColumnIndex(USER_ID));
            info.wid = c.getString(c.getColumnIndex(USER_WEIGHT_ID));
            info.name = c.getString(c.getColumnIndex(USER_NAME));
            info.age = c.getString(c.getColumnIndex(USER_AGE));
            info.sex = c.getString(c.getColumnIndex(USER_SEX));
            info.toll = c.getString(c.getColumnIndex(USER_TOLL));
            info.headpath = c.getString(c.getColumnIndex(USER_PATH));
            users.add(info);
            hashMap.put(info.uid,info);
        }

        c.close();
    }

    public User scanUsers(ArrayList<User> users,HashMap<String,User> hashMap,String lastuser) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_USER;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        User last = null;
        while (c.moveToNext()) {
            User info = new User();
            info.uid = c.getString(c.getColumnIndex(USER_ID));
            info.wid = c.getString(c.getColumnIndex(USER_WEIGHT_ID));
            info.name = c.getString(c.getColumnIndex(USER_NAME));
            info.age = c.getString(c.getColumnIndex(USER_AGE));
            info.sex = c.getString(c.getColumnIndex(USER_SEX));
            info.toll = c.getString(c.getColumnIndex(USER_TOLL));
            info.headpath = c.getString(c.getColumnIndex(USER_PATH));
            users.add(info);
            hashMap.put(info.uid,info);
            if(info.uid.equals(lastuser))
            {
                last = info;
            }
        }

        c.close();
        return last;
    }

    public int addUser(User sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, sInfo.uid);
        cv.put(USER_WEIGHT_ID, sInfo.wid);
        cv.put(USER_NAME, sInfo.name);
        cv.put(USER_SEX, sInfo.sex);
        cv.put(USER_AGE, sInfo.age);
        cv.put(USER_TOLL, sInfo.toll);
        cv.put(USER_PATH, sInfo.headpath);
        int iRet = (int) db.insert(TABLE_USER, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_USER, cv, USER_ID + "=?", new String[]
                    {sInfo.uid});
        }

        return iRet;
    }

    public int deleteUser(User sInfo) {
        return db.delete(TABLE_USER, USER_ID + "=?", new String[]
                {sInfo.uid});
    }

    public void scanUserWeight(String userid,ArrayList<UserWeight> userWeights,HashMap<String,UserWeight> hash) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_WEIGHT+ " WHERE "+  WEIGHT_USER_ID + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {userid});
        while (c.moveToNext()) {
            UserWeight info = new UserWeight();
            info.wid = c.getString(c.getColumnIndex(WEIGHT_ID));
            info.uid = c.getString(c.getColumnIndex(WEIGHT_USER_ID));
            info.date = c.getString(c.getColumnIndex(WEIGHT_DATE));
            info.weight = c.getString(c.getColumnIndex(WEIGHT_WEIGHT));
            userWeights.add(info);
            hash.put(info.date,info);
        }

        c.close();
    }

    public int addUserWeight(UserWeight sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(WEIGHT_ID, sInfo.wid);
        cv.put(WEIGHT_USER_ID, sInfo.uid);
        cv.put(WEIGHT_DATE, sInfo.date);
        cv.put(WEIGHT_WEIGHT, sInfo.weight);
        int iRet = (int) db.insert(TABLE_WEIGHT, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_WEIGHT, cv, WEIGHT_ID + "=?", new String[]
                    {sInfo.wid});
        }

        return iRet;
    }


    public int deleteUserWeight(User sInfo) {
        return db.delete(TABLE_WEIGHT, WEIGHT_USER_ID + "=?", new String[]
                {sInfo.uid});
    }

}
