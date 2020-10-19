package com.exhibition.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import com.exhibition.entity.Guest;
import com.exhibition.utils.FingerUtils;
import com.exhibition.view.ExhibitionApplication;
import com.finger.entity.Finger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "exhibition.db";
    public static final int DB_VERSION = 24;

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


    private static final String TABLE_FINGER = "TABLE_FINGER";
    private static final String FINGER_GUEST_ID = "FINGER_GUEST_ID";
    private static final String FINGER_FEA = "FINGER_FEA";
    private static final String FINGER_DATA = "FINGER_DATA";
    private static DBHelper mDBHelper;
    private SQLiteDatabase db = null;
    public int lastdb = 0;
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

        sql = "CREATE TABLE " + TABLE_FINGER + " (" + FINGER_FEA
                + " TEXT PRIMARY KEY,"+ FINGER_DATA + " TEXT,"
                + FINGER_GUEST_ID + " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        lastdb = oldVersion;

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUEST);


        String sql = "CREATE TABLE " + TABLE_GUEST + " (" + GUEST_RECORDID
                + " TEXT PRIMARY KEY," + GUEST_NAME + " TEXT," + GUEST_ADDRESS + " TEXT," + GUEST_TYPE
                + " TEXT," + GUEST_TIME + " TEXT,"  + GUEST_COUNT + " TEXT,"  + GUEST_LICENCE + " TEXT,"  + GUEST_CARD
                + " TEXT," + GUEST_ITEMS + " TEXT,"  + GUEST_MOBIL + " TEXT," + GUEST_SEX + " TEXT,"+ GUEST_USERFUL_TIME + " TEXT,"+ GUEST_FINGER + " TEXT,"
                + GUEST_CAR + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGER);
        sql = "CREATE TABLE " + TABLE_FINGER + " (" + FINGER_FEA
                + " TEXT PRIMARY KEY,"+ FINGER_DATA + " TEXT,"
                + FINGER_GUEST_ID + " TEXT)";
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
            getGuestFinger(info);
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
            getGuestFinger(info);
            servers.put(info.rid,info);
        }

        c.close();

        return servers;
    }

    public HashMap<String,Guest> scanGuest(String time, String keyword,ArrayList<Guest> guests) {
        openDB();
        guests.clear();
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
            getGuestFinger(info);
            guests.add(info);
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
            getGuestFinger(info);
            break;
        }

        c.close();

        return info;
    }

    public Guest getGuestInfoIc(String id) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_GUEST + " WHERE " + GUEST_CARD
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
            getGuestFinger(info);
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
        cv.put(GUEST_FINGER, "");
        addGuestFinger(sInfo);


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

    public void getGuestFinger(Guest guest)
    {
        String sql = "SELECT * FROM " + TABLE_FINGER + " WHERE " + FINGER_GUEST_ID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {guest.rid});
        while (c.moveToNext())
        {
            Finger finger = new Finger();
            finger.gid = c.getString(c.getColumnIndex(FINGER_FEA));
            finger.rid = c.getString(c.getColumnIndex(FINGER_GUEST_ID));
            praseFea(finger,c.getString(c.getColumnIndex(FINGER_DATA)));
            guest.fingers.add(finger);
            FingerUtils.getFingerImage(finger);
        }
    }

    public Finger getFinger(String id)
    {
        String sql = "SELECT * FROM " + TABLE_FINGER + " WHERE " + FINGER_FEA
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {id});
        while (c.moveToNext())
        {
            Finger finger = new Finger();
            finger.gid = c.getString(c.getColumnIndex(FINGER_FEA));
            finger.rid = c.getString(c.getColumnIndex(FINGER_GUEST_ID));
            praseFea(finger,c.getString(c.getColumnIndex(FINGER_DATA)));
            return finger;
        }
        return null;
    }


    public void addGuestFinger(Guest guest)
    {
        openDB();
        deleteGuestFinger(guest);
        for (int i = 0 ; i < guest.fingers.size() ; i++) {
            ContentValues cv = new ContentValues();
            cv.put(FINGER_FEA, guest.fingers.get(i).gid);
            cv.put(FINGER_GUEST_ID, guest.rid);
            cv.put(FINGER_DATA, praseFeaJson(guest.fingers.get(i)));
            db.insert(TABLE_FINGER, null, cv);
            FingerUtils.saveFingerImage(guest.fingers.get(i));
            ExhibitionApplication.mApp.fingerManger.saveFea(guest.fingers.get(i));
        }
    }

    public void addGuestFinger(Finger guest)
    {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(FINGER_FEA, guest.gid);
        cv.put(FINGER_GUEST_ID, guest.rid);
        cv.put(FINGER_DATA, praseFeaJson(guest));
        long id = db.insert(TABLE_FINGER, null, cv);
        long a = id;
        FingerUtils.saveFingerImage(guest);
    }

    public void deleteGuestFinger(Guest guest) {
        openDB();
        FingerUtils.cleanFingers(guest);
        db.delete(TABLE_FINGER, FINGER_GUEST_ID + "=?", new String[]
                {guest.rid});

    }

    public void deleteGuestFinger(Finger guest) {
        openDB();
        db.delete(TABLE_FINGER, FINGER_FEA + "=?", new String[]
                {guest.gid});

    }

    public String praseFeaJson(Finger finger) {

        JSONArray ja = new JSONArray();
        for(int i = 0 ; i < finger.feas.size() ;i++)
        {
            ja.put(String.valueOf(Base64.encodeToString(finger.feas.get(i),Base64.DEFAULT)));
        }
        return ja.toString();
    }

    public void praseFea(Finger finger,String json) {

        try {
            JSONArray ja = new JSONArray(json);
            for(int i = 0 ; i < ja.length() ; i++)
            {
                finger.feas.add(Base64.decode(ja.getString(i),Base64.DEFAULT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void measureFinger(String json,Guest guest)
//    {
//        try {
//            JSONArray jsonArray = new JSONArray(json);
//            for(int i = 0 ; i < jsonArray.length() ; i++)
//            {
//                JSONObject jo = jsonArray.getJSONObject(i);
//                Finger finger = new Finger();
//                finger.rid = jo.getString("rid");
//                finger.gid = jo.getString("gid");
//                FingerUtils.getFingerImage(finger);
//                guest.fingers.add(finger);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String measureFingerJson(Guest guest)
//    {
//        try {
//            JSONArray jsonArray = new JSONArray();
//            for(int i = 0 ; i < guest.fingers.size() ; i++)
//            {
//                JSONObject jo = new JSONObject();
//                jo.put("gid",guest.fingers.get(i).gid);
//                jo.put("rid",guest.fingers.get(i).rid);
//                jsonArray.put(jo);
//            }
//            return jsonArray.toString();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
}
