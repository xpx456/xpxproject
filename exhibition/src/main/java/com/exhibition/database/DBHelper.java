package com.exhibition.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.exhibition.entity.Guest;
import com.exhibition.utils.FingerUtils;
import com.finger.entity.Finger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "exhibition.db";
    public static final int DB_VERSION = 4;

    private static final String TABLE_GUEST = "TABLE_GUEST";
    private static final String GUEST_NAME = "GUEST_NAME";
    private static final String GUEST_RECORDID = "GUEST_RECORDID";
    private static final String GUEST_ADDRESS = "GUEST_ADDRESS";
    private static final String GUEST_TYPE = "GUEST_TYPE";
    private static final String GUEST_TIME = "GUEST_TIME";
    private static final String GUEST_CAR = "GUEST_CAR";
    private static final String GUEST_COUNT = "GUEST_COUNT";
    private static final String GUEST_LICENCE = "GUEST_LICENCE";
    private static final String GUEST_CARD = "GUEST_CARD";
    private static final String GUEST_ITEMS = "GUEST_ITEMS";
    private static final String GUEST_MOBIL = "GUEST_MOBIL";
    private static final String GUEST_SEX = "GUEST_SEX";
    private static final String GUEST_USERFUL_TIME = "GUEST_USERFUL_TIME";
    private static final String GUEST_FINGER = "GUEST_FINGER";
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUEST);


        String sql = "CREATE TABLE " + TABLE_GUEST + " (" + GUEST_RECORDID
                + " TEXT PRIMARY KEY," + GUEST_NAME + " TEXT," + GUEST_ADDRESS + " TEXT," + GUEST_TYPE
                + " TEXT," + GUEST_TIME + " TEXT,"  + GUEST_COUNT + " TEXT,"  + GUEST_LICENCE + " TEXT,"  + GUEST_CARD
                + " TEXT," + GUEST_ITEMS + " TEXT,"  + GUEST_MOBIL + " TEXT," + GUEST_SEX + " TEXT,"+ GUEST_USERFUL_TIME + " TEXT,"+ GUEST_FINGER + " TEXT,"
                + GUEST_CAR + " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUEST);


        String sql = "CREATE TABLE " + TABLE_GUEST + " (" + GUEST_RECORDID
                + " TEXT PRIMARY KEY," + GUEST_NAME + " TEXT," + GUEST_ADDRESS + " TEXT," + GUEST_TYPE
                + " TEXT," + GUEST_TIME + " TEXT,"  + GUEST_COUNT + " TEXT,"  + GUEST_LICENCE + " TEXT,"  + GUEST_CARD
                + " TEXT," + GUEST_ITEMS + " TEXT,"  + GUEST_MOBIL + " TEXT," + GUEST_SEX + " TEXT,"+ GUEST_USERFUL_TIME + " TEXT,"+ GUEST_FINGER + " TEXT,"
                + GUEST_CAR + " TEXT)";
        db.execSQL(sql);
    }

    public List<Guest> scanGuest() {
        openDB();
        List<Guest> servers = new ArrayList<Guest>();

        String sql = "SELECT * FROM " + TABLE_GUEST;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            Guest info = new Guest();
            info.name = c.getString(c.getColumnIndex(GUEST_NAME));
            info.rid = c.getString(c.getColumnIndex(GUEST_RECORDID));
            info.time = c.getString(c.getColumnIndex(GUEST_TIME));
            info.count = c.getString(c.getColumnIndex(GUEST_COUNT));
            info.type = c.getString(c.getColumnIndex(GUEST_TYPE));
            info.sex = c.getString(c.getColumnIndex(GUEST_SEX));
            info.car = c.getString(c.getColumnIndex(GUEST_CAR));
            info.card = c.getString(c.getColumnIndex(GUEST_CARD));
            info.licence = c.getString(c.getColumnIndex(GUEST_LICENCE));
            info.items = c.getString(c.getColumnIndex(GUEST_ITEMS));
            info.mobil = c.getString(c.getColumnIndex(GUEST_MOBIL));
            info.address = c.getString(c.getColumnIndex(GUEST_ADDRESS));
            info.utime = c.getString(c.getColumnIndex(GUEST_USERFUL_TIME));
            measureFinger(c.getString(c.getColumnIndex(GUEST_FINGER)),info);
            servers.add(info);
        }

        c.close();

        return servers;
    }

    public HashMap<String,Guest> scanGuest(String time, String keyword) {
        openDB();
        HashMap<String,Guest> servers = new HashMap<String,Guest>();
        String sql = "SELECT * FROM " + TABLE_GUEST  +" WHERE " + GUEST_TIME +" = ?"+" AND "+ GUEST_NAME + " LIKE ?";
        Cursor c = db.rawQuery(sql, new String[]
                {time,"%"+keyword+"%"});
        while (c.moveToNext()) {
            Guest info = new Guest();
            info.name = c.getString(c.getColumnIndex(GUEST_NAME));
            info.rid = c.getString(c.getColumnIndex(GUEST_RECORDID));
            info.time = c.getString(c.getColumnIndex(GUEST_TIME));
            info.count = c.getString(c.getColumnIndex(GUEST_COUNT));
            info.type = c.getString(c.getColumnIndex(GUEST_TYPE));
            info.sex = c.getString(c.getColumnIndex(GUEST_SEX));
            info.car = c.getString(c.getColumnIndex(GUEST_CAR));
            info.card = c.getString(c.getColumnIndex(GUEST_CARD));
            info.licence = c.getString(c.getColumnIndex(GUEST_LICENCE));
            info.items = c.getString(c.getColumnIndex(GUEST_ITEMS));
            info.mobil = c.getString(c.getColumnIndex(GUEST_MOBIL));
            info.address = c.getString(c.getColumnIndex(GUEST_ADDRESS));
            info.utime = c.getString(c.getColumnIndex(GUEST_USERFUL_TIME));
            measureFinger(c.getString(c.getColumnIndex(GUEST_FINGER)),info);
            servers.put(info.rid,info);
        }

        c.close();

        return servers;
    }

    public Guest getGuestInfo(String id) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_GUEST + " WHERE " + GUEST_RECORDID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {id});

        Guest info = null;
        while (c.moveToNext()) {
            info = new Guest();
            info.name = c.getString(c.getColumnIndex(GUEST_NAME));
            info.rid = c.getString(c.getColumnIndex(GUEST_RECORDID));
            info.time = c.getString(c.getColumnIndex(GUEST_TIME));
            info.count = c.getString(c.getColumnIndex(GUEST_COUNT));
            info.type = c.getString(c.getColumnIndex(GUEST_TYPE));
            info.sex = c.getString(c.getColumnIndex(GUEST_SEX));
            info.car = c.getString(c.getColumnIndex(GUEST_CAR));
            info.card = c.getString(c.getColumnIndex(GUEST_CARD));
            info.licence = c.getString(c.getColumnIndex(GUEST_LICENCE));
            info.items = c.getString(c.getColumnIndex(GUEST_ITEMS));
            info.mobil = c.getString(c.getColumnIndex(GUEST_MOBIL));
            info.address = c.getString(c.getColumnIndex(GUEST_ADDRESS));
            info.utime = c.getString(c.getColumnIndex(GUEST_USERFUL_TIME));
            measureFinger(c.getString(c.getColumnIndex(GUEST_FINGER)),info);
            break;
        }

        c.close();

        return info;
    }

    public int addGuest(Guest sInfo) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(GUEST_NAME, sInfo.name);
        cv.put(GUEST_ADDRESS, sInfo.address);
        cv.put(GUEST_TIME, sInfo.time);
        cv.put(GUEST_TYPE, sInfo.type);
        cv.put(GUEST_RECORDID, sInfo.rid);
        cv.put(GUEST_CAR, sInfo.car);
        cv.put(GUEST_COUNT, sInfo.count);
        cv.put(GUEST_CARD, sInfo.card);
        cv.put(GUEST_SEX, sInfo.sex);
        cv.put(GUEST_MOBIL, sInfo.mobil);
        cv.put(GUEST_LICENCE, sInfo.licence);
        cv.put(GUEST_ITEMS, sInfo.items);
        cv.put(GUEST_USERFUL_TIME, sInfo.utime);
        cv.put(GUEST_FINGER, measureFingerJson(sInfo));

        int iRet = (int) db.insert(TABLE_GUEST, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_GUEST, cv, GUEST_RECORDID + "=?", new String[]
                    {sInfo.rid});
        }

        return iRet;
    }

    public int deleteGuest(Guest sInfo) {
        return db.delete(TABLE_GUEST, GUEST_RECORDID + "=?", new String[]
                {sInfo.rid});
    }


    public void measureFinger(String json,Guest guest)
    {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0 ; i < jsonArray.length() ; i++)
            {
                JSONObject jo = jsonArray.getJSONObject(i);
                Finger finger = new Finger();
                finger.rid = jo.getString("rid");
                finger.gid = jo.getString("gid");
                FingerUtils.getFingerImage(finger);
                guest.fingers.add(finger);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String measureFingerJson(Guest guest)
    {
        try {
            JSONArray jsonArray = new JSONArray();
            for(int i = 0 ; i < guest.fingers.size() ; i++)
            {
                JSONObject jo = new JSONObject();
                jo.put("gid",guest.fingers.get(i).gid);
                jo.put("rid",guest.rid);
                jsonArray.put(jo);
            }
            return jsonArray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
