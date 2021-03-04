package com.accesscontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.accesscontrol.entity.AccessRecord;
import com.accesscontrol.entity.Guest;
import com.accesscontrol.entity.GuestFinger;
import com.accesscontrol.model.DaoSessionManager;
import com.accesscontrol.model.bean.VeinInfo;
import com.accesscontrol.model.entities.DaoSession;
import com.accesscontrol.model.entities.VeinDynamic;
import com.accesscontrol.model.entities.VeinDynamicDao;
import com.accesscontrol.view.AccessControlApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "accesscontrol.db";
    public static final int DB_VERSION = 5;

    public static final String TABLE_BG = "TABLE_BG";
    public static final String BG_URL = "BG_URL";

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
    private static final String FINGER_NAME = "FINGER_NAME";
    private static final String FINGER_GUEST_ID = "FINGER_GUEST_ID";
    private static final String FINGER_FEA = "FINGER_FEA";


    private static final String TABLE_ACCESS_RECORD = "TABLE_ACCESS_RECORD";
    private static final String ACCESS_RECORD_ID = "ACCESS_RECORD_ID";
    private static final String ACCESS_RECORD_USERID = "ACCESS_RECORD_USERID";
    private static final String ACCESS_RECORD_NAME = "ACCESS_RECORD_NAME";
    private static final String ACCESS_RECORD_LICENCE = "ACCESS_RECORD_LICENCE";
    private static final String ACCESS_RECORD_MODE = "ACCESS_RECORD_MODE";
    private static final String ACCESS_RECORD_TIME = "ACCESS_RECORD_TIME";
    private static final String ACCESS_RECORD_EQUIPMENT = "ACCESS_RECORD_EQUIPMENT";
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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGER);


        sql = "CREATE TABLE " + TABLE_FINGER + " (" + FINGER_FEA
                + " TEXT PRIMARY KEY,"
                + FINGER_NAME + " TEXT, "
                + FINGER_GUEST_ID + " TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_BG + " (" + BG_URL
                + " TEXT PRIMARY KEY)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_ACCESS_RECORD + " (" + ACCESS_RECORD_ID
                + " TEXT PRIMARY KEY,"+ ACCESS_RECORD_USERID + " TEXT,"
                + ACCESS_RECORD_NAME + " TEXT,"
                + ACCESS_RECORD_LICENCE + " TEXT,"
                + ACCESS_RECORD_MODE + " TEXT,"
                + ACCESS_RECORD_EQUIPMENT + " TEXT,"
                + ACCESS_RECORD_TIME + " TEXT)";
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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGER);


        sql = "CREATE TABLE " + TABLE_FINGER + " (" + FINGER_FEA
                + " TEXT PRIMARY KEY, "
                + FINGER_NAME + " TEXT, "
                + FINGER_GUEST_ID + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BG);

        sql = "CREATE TABLE " + TABLE_BG + " (" + BG_URL
                + " TEXT PRIMARY KEY)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESS_RECORD);

        sql = "CREATE TABLE " + TABLE_ACCESS_RECORD + " (" + ACCESS_RECORD_ID
                + " TEXT PRIMARY KEY,"+ ACCESS_RECORD_USERID + " TEXT,"
                + ACCESS_RECORD_NAME + " TEXT,"
                + ACCESS_RECORD_LICENCE + " TEXT,"
                + ACCESS_RECORD_MODE + " TEXT,"
                + ACCESS_RECORD_EQUIPMENT + " TEXT,"
                + ACCESS_RECORD_TIME + " TEXT)";
        db.execSQL(sql);
    }

    public void cleanAllrecord()
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESS_RECORD);

        String sql = "CREATE TABLE " + TABLE_ACCESS_RECORD + " (" + ACCESS_RECORD_ID
                + " TEXT PRIMARY KEY,"+ ACCESS_RECORD_USERID + " TEXT,"
                + ACCESS_RECORD_NAME + " TEXT,"
                + ACCESS_RECORD_LICENCE + " TEXT,"
                + ACCESS_RECORD_MODE + " TEXT,"
                + ACCESS_RECORD_EQUIPMENT + " TEXT,"
                + ACCESS_RECORD_TIME + " TEXT)";
        db.execSQL(sql);
    }

    public void scanBgs()
    {
        AccessControlApplication.mApp.bg.clear();
        String sql = "SELECT * FROM " + TABLE_BG;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            String url = c.getString(c.getColumnIndex(BG_URL));
            AccessControlApplication.mApp.bg.put(url,url);

        }
    }

    public void scanGuest(HashMap<String,Guest> guestHashMap) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_GUEST;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        guestHashMap.clear();
        while (c.moveToNext()) {
            Guest info = new Guest();
            info.name = c.getString(c.getColumnIndex(GUEST_NAME));
            info.rid = c.getString(c.getColumnIndex(GUEST_RECORDID));
            info.time = c.getString(c.getColumnIndex(GUEST_TIME));
            info.count = c.getString(c.getColumnIndex(GUEST_COUNT));
            info.type = c.getString(c.getColumnIndex(GUEST_TYPE));
            info.sex = c.getString(c.getColumnIndex(GUEST_SEX));
            info.cancard = c.getString(c.getColumnIndex(GUEST_CAR));
            info.canfinger = c.getString(c.getColumnIndex(GUEST_CARD));
            info.licence = c.getString(c.getColumnIndex(GUEST_LICENCE));
            info.items = c.getString(c.getColumnIndex(GUEST_ITEMS));
            info.mobil = c.getString(c.getColumnIndex(GUEST_MOBIL));
            info.address = c.getString(c.getColumnIndex(GUEST_ADDRESS));
            info.utime = c.getString(c.getColumnIndex(GUEST_USERFUL_TIME));
            getGuestFinger(info);
            guestHashMap.put(info.rid,info);
        }

        c.close();
    }

    public void scanGuest(ArrayList<Guest> guests) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_GUEST;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        guests.clear();
        while (c.moveToNext()) {
            Guest info = new Guest();
            info.name = c.getString(c.getColumnIndex(GUEST_NAME));
            info.rid = c.getString(c.getColumnIndex(GUEST_RECORDID));
            info.time = c.getString(c.getColumnIndex(GUEST_TIME));
            info.count = c.getString(c.getColumnIndex(GUEST_COUNT));
            info.type = c.getString(c.getColumnIndex(GUEST_TYPE));
            info.sex = c.getString(c.getColumnIndex(GUEST_SEX));
            info.cancard = c.getString(c.getColumnIndex(GUEST_CAR));
            info.canfinger = c.getString(c.getColumnIndex(GUEST_CARD));
            info.licence = c.getString(c.getColumnIndex(GUEST_LICENCE));
            info.items = c.getString(c.getColumnIndex(GUEST_ITEMS));
            info.mobil = c.getString(c.getColumnIndex(GUEST_MOBIL));
            info.address = c.getString(c.getColumnIndex(GUEST_ADDRESS));
            info.utime = c.getString(c.getColumnIndex(GUEST_USERFUL_TIME));
            //getGuestFinger(info);
            guests.add(info);
        }

        c.close();
    }

    public void scanGuest(ArrayList<Guest> guests,String keyword,boolean finger,boolean card) {
        openDB();
        Cursor c = null;
        if(finger == true && card == true)
        {
            String sql = "SELECT * FROM " + TABLE_GUEST  +" WHERE " + GUEST_CARD +" = ?"+ " AND " + GUEST_CAR +" = ?";
            c = db.rawQuery(sql, new String[]
                    {"1","1"});
        }
        else if(finger == true)
        {
            String sql = "SELECT * FROM " + TABLE_GUEST  +" WHERE " + GUEST_CARD +" = ?";
            c = db.rawQuery(sql, new String[]
                    {"1"});
        }
        else if(card == true)
        {
            String sql = "SELECT * FROM " + TABLE_GUEST  +" WHERE "  + GUEST_CAR +" = ?";
            c = db.rawQuery(sql, new String[]
                    {"1"});
        }
        else
        {
            String sql = "SELECT * FROM " + TABLE_GUEST;
            c = db.rawQuery(sql, new String[]
                    {});
        }
        guests.clear();
        while (c.moveToNext()) {
            Guest info = new Guest();
            info.name = c.getString(c.getColumnIndex(GUEST_NAME));
            info.rid = c.getString(c.getColumnIndex(GUEST_RECORDID));
            info.time = c.getString(c.getColumnIndex(GUEST_TIME));
            info.count = c.getString(c.getColumnIndex(GUEST_COUNT));
            info.type = c.getString(c.getColumnIndex(GUEST_TYPE));
            info.sex = c.getString(c.getColumnIndex(GUEST_SEX));
            info.cancard = c.getString(c.getColumnIndex(GUEST_CAR));
            info.canfinger = c.getString(c.getColumnIndex(GUEST_CARD));
            info.licence = c.getString(c.getColumnIndex(GUEST_LICENCE));
            info.items = c.getString(c.getColumnIndex(GUEST_ITEMS));
            info.mobil = c.getString(c.getColumnIndex(GUEST_MOBIL));
            info.address = c.getString(c.getColumnIndex(GUEST_ADDRESS));
            info.utime = c.getString(c.getColumnIndex(GUEST_USERFUL_TIME));
            if(keyword.length() > 0)
            {
                if(info.name.contains(keyword) || info.licence.contains(keyword) || info.rid.contains(keyword))
                {
                    guests.add(info);
                }
            }
            else
            {
                guests.add(info);
            }
        }

        c.close();
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
            info.cancard = c.getString(c.getColumnIndex(GUEST_CAR));
            info.canfinger = c.getString(c.getColumnIndex(GUEST_CARD));
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
            info.cancard = c.getString(c.getColumnIndex(GUEST_CAR));
            info.canfinger = c.getString(c.getColumnIndex(GUEST_CARD));
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
        cv.put(GUEST_CAR, sInfo.cancard);
        cv.put(GUEST_COUNT, sInfo.count);
        cv.put(GUEST_CARD, sInfo.canfinger);
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

    public void updataGuest(Guest sInfo) {
        ContentValues cv = new ContentValues();

        cv.put(GUEST_NAME, sInfo.name);
        cv.put(GUEST_ADDRESS, sInfo.address);
        cv.put(GUEST_TIME, sInfo.time);
        cv.put(GUEST_TYPE, sInfo.type);
        cv.put(GUEST_RECORDID, sInfo.rid);
        cv.put(GUEST_CAR, sInfo.cancard);
        cv.put(GUEST_COUNT, sInfo.count);
        cv.put(GUEST_CARD, sInfo.canfinger);
        cv.put(GUEST_SEX, sInfo.sex);
        cv.put(GUEST_MOBIL, sInfo.mobil);
        cv.put(GUEST_LICENCE, sInfo.licence);
        cv.put(GUEST_ITEMS, sInfo.items);
        cv.put(GUEST_USERFUL_TIME, sInfo.utime);
        cv.put(GUEST_FINGER, "");
        int iRet = (int) db.insert(TABLE_GUEST, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_GUEST, cv, GUEST_RECORDID + "=?", new String[]
                    {sInfo.rid});
        }

    }

    public void cleanGuest()
    {
        for(Map.Entry<String,Guest> map : AccessControlApplication.mApp.guestHashMap.entrySet())
        {
            Guest sInfo = map.getValue();
            DaoSession daoSession = DaoSessionManager.getInstance().getDaoSession(AccessControlApplication.mApp);
            VeinDynamicDao veinDynamicDao = daoSession.getVeinDynamicDao();
            List<VeinDynamic> veinDynamics = veinDynamicDao.queryBuilder().where(VeinDynamicDao.Properties.UserId.eq(sInfo.rid)).list();
            veinDynamicDao.deleteInTx(veinDynamics);
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUEST);
        String sql = "CREATE TABLE " + TABLE_GUEST + " (" + GUEST_RECORDID
                + " TEXT PRIMARY KEY," + GUEST_NAME + " TEXT," + GUEST_ADDRESS + " TEXT," + GUEST_TYPE
                + " TEXT," + GUEST_TIME + " TEXT,"  + GUEST_COUNT + " TEXT,"  + GUEST_LICENCE + " TEXT,"  + GUEST_CARD
                + " TEXT," + GUEST_ITEMS + " TEXT,"  + GUEST_MOBIL + " TEXT," + GUEST_SEX + " TEXT,"+ GUEST_USERFUL_TIME + " TEXT,"+ GUEST_FINGER + " TEXT,"
                + GUEST_CAR + " TEXT)";
        db.execSQL(sql);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGER);
        sql = "CREATE TABLE " + TABLE_FINGER + " (" + FINGER_FEA
                + " TEXT PRIMARY KEY,"
                + FINGER_NAME + " TEXT, "
                + FINGER_GUEST_ID + " TEXT)";
        db.execSQL(sql);
    }

    public int deleteGuest(Guest sInfo) {

        DaoSession daoSession = DaoSessionManager.getInstance().getDaoSession(AccessControlApplication.mApp);
        VeinDynamicDao veinDynamicDao = daoSession.getVeinDynamicDao();
        List<VeinDynamic> veinDynamics = veinDynamicDao.queryBuilder().where(VeinDynamicDao.Properties.UserId.eq(sInfo.rid)).list();
        veinDynamicDao.deleteInTx(veinDynamics);
        deleteGuestFinger(sInfo);
        return db.delete(TABLE_GUEST, GUEST_RECORDID + "=?", new String[]
                {sInfo.rid});
    }

    public void getGuestFinger(Guest guest)
    {
        openDB();

        String sql = "SELECT * FROM " + TABLE_FINGER + " WHERE " + FINGER_GUEST_ID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {guest.rid});
        while (c.moveToNext()) {
            String fea = c.getString(c.getColumnIndex(FINGER_FEA));
            String veinName = c.getString(c.getColumnIndex(FINGER_NAME));
            VeinInfo veinInfo = new VeinInfo(guest.rid, veinName);
            guest.fingers.put(fea, veinInfo);
        }
    }

    public void addGuestFinger(Guest guest)
    {
        openDB();
        for (String key : guest.fingers.keySet()) {
            ContentValues cv = new ContentValues();
            VeinInfo veinInfo = guest.fingers.get(key);
            cv.put(FINGER_FEA, key);
            cv.put(FINGER_GUEST_ID, guest.rid);
            cv.put(FINGER_NAME, veinInfo.getFingerName());
            db.insert(TABLE_FINGER, null, cv);
        }
    }

    public void addGuestFinger(GuestFinger guest)
    {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(FINGER_FEA, guest.finger);
        cv.put(FINGER_GUEST_ID, guest.rid);
        cv.put(FINGER_NAME, guest.fingerName);
        long id = db.insert(TABLE_FINGER, null, cv);
    }

    public void deleteGuestFinger(Guest guest) {
        openDB();
        db.delete(TABLE_FINGER, FINGER_GUEST_ID + "=?", new String[]
                {guest.rid});

    }

    public void deleteGuestFinger(GuestFinger guest) {
        openDB();
        db.delete(TABLE_FINGER, FINGER_FEA + "=?", new String[]
                {guest.finger});

    }

    public int addRecord(AccessRecord accessRecord) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(ACCESS_RECORD_ID, accessRecord.mRecordid);
        cv.put(ACCESS_RECORD_USERID, accessRecord.empAuthInfoId);
        cv.put(ACCESS_RECORD_NAME, accessRecord.name);
        cv.put(ACCESS_RECORD_MODE, accessRecord.authModeCode);
        cv.put(ACCESS_RECORD_LICENCE, accessRecord.licence);
        cv.put(ACCESS_RECORD_EQUIPMENT, accessRecord.entEqptNo);
        cv.put(ACCESS_RECORD_TIME, accessRecord.accessRecordTime);
        int iRet = (int) db.insert(TABLE_ACCESS_RECORD, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_ACCESS_RECORD, cv, ACCESS_RECORD_ID + "=?", new String[]
                    {accessRecord.mRecordid});
        }
        return iRet;
    }

    public AccessRecord getRecord(String id){
        openDB();
        String sql = "SELECT * FROM " + TABLE_ACCESS_RECORD + " WHERE " + ACCESS_RECORD_ID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {id});
        AccessRecord info = null;
        while (c.moveToNext()) {
            info = new AccessRecord();
            info.name = c.getString(c.getColumnIndex(ACCESS_RECORD_NAME));
            info.mRecordid = c.getString(c.getColumnIndex(ACCESS_RECORD_ID));
            info.authModeCode = c.getString(c.getColumnIndex(ACCESS_RECORD_MODE));
            info.accessRecordTime = c.getString(c.getColumnIndex(ACCESS_RECORD_TIME));
            info.empAuthInfoId = c.getString(c.getColumnIndex(ACCESS_RECORD_USERID));
            info.entEqptNo = c.getString(c.getColumnIndex(ACCESS_RECORD_EQUIPMENT));
            info.licence = c.getString(c.getColumnIndex(ACCESS_RECORD_LICENCE));
            break;
        }
        c.close();
        return info;
    }

    public void scanRecord(){

    }

    public void deleteRecord(){

    }
}