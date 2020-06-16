package com.intersky.strang.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import intersky.xpxnet.net.Service;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "intersky.db";
    public static final int DB_VERSION = 47;
    public static final String OLD_DB_NAME = "InterSky.db";

    private static final String TABLE_SERVICE = "TABLE_SERVICE";
    private static final String SERVICE_NAME = "SERVICE_NAME";
    private static final String SERVICE_RECORDID = "SERVICE_RECORDID";
    private static final String SERVICE_ADDRESS = "SERVICE_ADDRESS";
    private static final String SERVICE_TYPE = "SERVICE_TYPE";
    private static final String SERVICE_PORT = "SERVICE_PORT";
    private static final String SERVICE_CODE = "SERVICE_CODE";
    private static final String SERVICE_HTTPS = "SERVICE_HTTPS";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);


        String sql = "CREATE TABLE " + TABLE_SERVICE + " (" + SERVICE_RECORDID
                + " TEXT PRIMARY KEY," + SERVICE_NAME + " TEXT," + SERVICE_ADDRESS + " TEXT," + SERVICE_TYPE
                + " TEXT," + SERVICE_PORT + " TEXT,"  + SERVICE_HTTPS + " TEXT," + SERVICE_CODE + " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 46)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);


            String sql = "CREATE TABLE " + TABLE_SERVICE + " (" + SERVICE_RECORDID
                    + " TEXT PRIMARY KEY," + SERVICE_NAME + " TEXT," + SERVICE_ADDRESS + " TEXT," + SERVICE_TYPE
                    + " TEXT," + SERVICE_PORT + " TEXT,"  + SERVICE_HTTPS + " TEXT," + SERVICE_CODE + " TEXT)";
            db.execSQL(sql);
        }
    }

    public List<Service> scanServer() {
        openDB();
        List<Service> servers = new ArrayList<Service>();

        String sql = "SELECT * FROM " + TABLE_SERVICE;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            Service info = new Service();
            info.sName = c.getString(c.getColumnIndex(SERVICE_NAME));
            info.sAddress = c.getString(c.getColumnIndex(SERVICE_ADDRESS));
            info.sPort = c.getString(c.getColumnIndex(SERVICE_PORT));
            long port = Long.valueOf(info.sPort);
            if(Long.valueOf(port) > 65535 || Long.valueOf(port) < 0) {
                info.sPort = "80";
            }

            info.sType = Boolean.valueOf(c.getString(c
                    .getColumnIndex(SERVICE_TYPE)));
            info.sCode = c.getString(c.getColumnIndex(SERVICE_CODE));
            info.sRecordId = c.getString(c.getColumnIndex(SERVICE_RECORDID));
            info.https = Boolean.valueOf(c.getString(c
                    .getColumnIndex(SERVICE_HTTPS)));
            servers.add(info);
        }

        c.close();

        return servers;
    }

    public Service getServerInfo(String id) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_SERVICE + " WHERE " + SERVICE_RECORDID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {id});

        Service info = null;
        while (c.moveToNext()) {
            info = new Service();
            info.sName = c.getString(c.getColumnIndex(SERVICE_NAME));
            info.sAddress = c.getString(c.getColumnIndex(SERVICE_ADDRESS));
            info.sPort = c.getString(c.getColumnIndex(SERVICE_PORT));
            info.sType = Boolean.valueOf(c.getString(c
                    .getColumnIndex(SERVICE_TYPE)));
            info.sCode = c.getString(c.getColumnIndex(SERVICE_CODE));
            info.sRecordId = c.getString(c.getColumnIndex(SERVICE_RECORDID));
            info.https = Boolean.getBoolean(c.getString(c.getColumnIndex(SERVICE_HTTPS)));
            break;
        }

        c.close();

        return info;
    }

    public int addServer(Service sInfo) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(SERVICE_NAME, sInfo.sName);
        cv.put(SERVICE_ADDRESS, sInfo.sAddress);
        cv.put(SERVICE_PORT, sInfo.sPort);
        cv.put(SERVICE_TYPE, String.valueOf(sInfo.sType));
        cv.put(SERVICE_RECORDID, sInfo.sRecordId);
        cv.put(SERVICE_CODE, sInfo.sCode);
        cv.put(SERVICE_HTTPS, String.valueOf(sInfo.https));

        int iRet = (int) db.insert(TABLE_SERVICE, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_SERVICE, cv, SERVICE_RECORDID + "=?", new String[]
                    {sInfo.sRecordId});
        }

        return iRet;
    }

    public int deleteServer(Service sInfo) {
        return db.delete(TABLE_SERVICE, SERVICE_RECORDID + "=?", new String[]
                {sInfo.sRecordId});
    }


}
