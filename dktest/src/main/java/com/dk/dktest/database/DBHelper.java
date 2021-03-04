package com.dk.dktest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dk.dktest.entity.TestRecord;
import com.dk.dktest.view.DkTestApplication;

import java.util.ArrayList;
import java.util.List;

import intersky.xpxnet.net.Service;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dktest.db";
    public static final int DB_VERSION = 3;

    private static final String TABLE_RECORD = "TABLE_RECORD";
    private static final String RECORD_NAME = "RECORD_NAME";
    private static final String RECORD_RECORDID = "RECORD_RECORDID";
    private static final String RECORD_DAY = "RECORD_DAY";
    private static final String RECORD_TIME = "RECORD_TIME";
    private static final String RECORD_DATA = "RECORD_DATA";
    private static final String RECORD_DURING = "RECORD_DURING";

    public boolean isfirst = false;
    private static DBHelper mDBHelper;
    private SQLiteDatabase db = null;
    private SQLiteDatabase db2 = null;
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
                + " TEXT PRIMARY KEY," + RECORD_NAME + " TEXT," + RECORD_DATA
                + " TEXT," + RECORD_DURING + " TEXT,"  + RECORD_TIME +" TEXT,"  + RECORD_DAY + " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);


        String sql = "CREATE TABLE " + TABLE_RECORD + " (" + RECORD_RECORDID
                + " TEXT PRIMARY KEY," + RECORD_NAME  + " TEXT," + RECORD_DATA
                + " TEXT," + RECORD_DURING + " TEXT,"  + RECORD_TIME +" TEXT,"  + RECORD_DAY+ " TEXT)";
        db.execSQL(sql);
    }

    public List<Service> scanRecords(String day,String key) {
        openDB();
        List<Service> servers = new ArrayList<Service>();
        DkTestApplication.mApp.testRecords.clear();
        String sql = "SELECT * FROM " + TABLE_RECORD + " WHERE "+  RECORD_DAY + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {day});
        while (c.moveToNext()) {
            TestRecord info = new TestRecord();
            info.name = c.getString(c.getColumnIndex(RECORD_NAME));
            info.rid = c.getString(c.getColumnIndex(RECORD_RECORDID));
            info.time = c.getString(c.getColumnIndex(RECORD_TIME));
            info.day = c.getString(c.getColumnIndex(RECORD_DAY));
            info.during = Integer.valueOf(c.getString(c.getColumnIndex(RECORD_DURING)));
            info.data = c.getString(c.getColumnIndex(RECORD_DATA));
            DkTestApplication.mApp.testRecords.add(info);
            if(info.name.contains(key))
            {
                DkTestApplication.mApp.testSRecords.add(info);
            }
        }

        c.close();

        return servers;
    }

    public List<Service> scanRecords() {
        openDB();
        List<Service> servers = new ArrayList<Service>();
        DkTestApplication.mApp.testRecords.clear();
        String sql = "SELECT * FROM " + TABLE_RECORD;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            TestRecord info = new TestRecord();
            info.name = c.getString(c.getColumnIndex(RECORD_NAME));
            info.rid = c.getString(c.getColumnIndex(RECORD_RECORDID));
            info.time = c.getString(c.getColumnIndex(RECORD_TIME));
            info.day = c.getString(c.getColumnIndex(RECORD_DAY));
            info.during = Integer.valueOf(c.getString(c.getColumnIndex(RECORD_DURING)));
            info.data = c.getString(c.getColumnIndex(RECORD_DATA));
            DkTestApplication.mApp.testRecords.add(info);
        }

        c.close();

        return servers;
    }

    public int addRecord(TestRecord sInfo) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(RECORD_NAME, sInfo.name);
        cv.put(RECORD_RECORDID, sInfo.rid);
        cv.put(RECORD_TIME, sInfo.time);
        cv.put(RECORD_DAY, sInfo.day);
        cv.put(RECORD_DURING, String.valueOf(sInfo.during));
        cv.put(RECORD_DATA, sInfo.data);

        int iRet = (int) db.insert(TABLE_RECORD, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_RECORD, cv, RECORD_RECORDID + "=?", new String[]
                    {sInfo.rid});
        }

        return iRet;
    }



    public int deleteServer(TestRecord sInfo) {
        return db.delete(TABLE_RECORD, RECORD_RECORDID + "=?", new String[]
                {sInfo.rid});
    }


}
