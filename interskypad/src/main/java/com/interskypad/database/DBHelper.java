package com.interskypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.interskypad.entity.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intersky.xpxnet.net.Service;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "interskypad.db";
    public static final int DB_VERSION = 2;


    private static final String TABLE_MODIFY = "TABLE_MODIFY";
    private static final String MODIFY_SERVICE = "MODIFY_SERVICE";
    private static final String MODIFY_DATE = "MODIFY_DATE";


    private static final String TABLE_SERVICE = "TABLE_SERVICE";
    private static final String SERVICE_NAME = "SERVICE_NAME";
    private static final String SERVICE_RECORDID = "SERVICE_RECORDID";
    private static final String SERVICE_ADDRESS = "SERVICE_ADDRESS";
    private static final String SERVICE_TYPE = "SERVICE_TYPE";
    private static final String SERVICE_PORT = "SERVICE_PORT";
    private static final String SERVICE_CODE = "SERVICE_CODE";

    private static final String TABLE_CUSTOMER = "TABLE_CUSTOMER";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String CUSTOMER_PHOME = "CUSTOMER_PHOME";
    private static final String CUSTOMER_MOBIL = "CUSTOMER_MOBIL";
    private static final String CUSTOMER_MAIL = "CUSTOMER_MAIL";
    private static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
    private static final String CUSTOMER_MEMO = "CUSTOMER_MEMO";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        String sql = "CREATE TABLE " + TABLE_SERVICE + " (" + SERVICE_RECORDID
                + " TEXT PRIMARY KEY," + SERVICE_NAME + " TEXT," + SERVICE_ADDRESS + " TEXT," + SERVICE_TYPE
                + " TEXT," + SERVICE_PORT + " TEXT,"  + SERVICE_CODE + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        sql = "CREATE TABLE " + TABLE_CUSTOMER + " (" + CUSTOMER_NAME
                + " TEXT PRIMARY KEY," + CUSTOMER_PHOME + " TEXT," + CUSTOMER_MOBIL + " TEXT," + CUSTOMER_MAIL
                + " TEXT," + CUSTOMER_ADDRESS + " TEXT,"  + CUSTOMER_MEMO + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODIFY);
        sql = "CREATE TABLE " + TABLE_MODIFY + " (" + MODIFY_SERVICE
                + " TEXT PRIMARY KEY,"  + MODIFY_DATE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        if(oldVersion < 2)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
            String sql = "CREATE TABLE " + TABLE_CUSTOMER + " (" + CUSTOMER_NAME
                    + " TEXT PRIMARY KEY," + CUSTOMER_PHOME + " TEXT," + CUSTOMER_MOBIL + " TEXT," + CUSTOMER_MAIL
                    + " TEXT," + CUSTOMER_ADDRESS + " TEXT,"  + CUSTOMER_MEMO + " TEXT)";
            db.execSQL(sql);
        }

    }


    public String getModifyInfo(String service) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_MODIFY + " WHERE " + MODIFY_SERVICE
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {service});
        String date = "";
        Service info = null;
        while (c.moveToNext()) {
            date = c.getString(c.getColumnIndex(MODIFY_DATE));
            break;
        }
        c.close();
        return date;
    }

    public void addModify(String service,String date) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(MODIFY_SERVICE, service);
        cv.put(MODIFY_DATE, date);
        int iRet = (int) db.insert(TABLE_MODIFY, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_MODIFY, cv, MODIFY_SERVICE + "=?", new String[]
                    {service});
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
            info.sType = Boolean.valueOf(c.getString(c
                    .getColumnIndex(SERVICE_TYPE)));
            info.sCode = c.getString(c.getColumnIndex(SERVICE_CODE));
            info.sRecordId = c.getString(c.getColumnIndex(SERVICE_RECORDID));
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









    public List<Customer> scanCustomer(HashMap<String,Customer> hashCustomer) {
        openDB();
        List<Customer> customers = new ArrayList<Customer>();

        String sql = "SELECT * FROM " + TABLE_CUSTOMER;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            Customer info = new Customer();
            info.setName(c.getString(c.getColumnIndex(CUSTOMER_NAME)));
            info.phone = c.getString(c.getColumnIndex(CUSTOMER_PHOME));
            info.mobil = c.getString(c.getColumnIndex(CUSTOMER_MOBIL));
            info.mail = c.getString(c.getColumnIndex(CUSTOMER_MAIL));
            info.address = c.getString(c.getColumnIndex(CUSTOMER_ADDRESS));
            info.memo = c.getString(c.getColumnIndex(CUSTOMER_MEMO));
            if(info.getName().length() > 0)
            {
                customers.add(info);
                hashCustomer.put(info.getName(),info);
            }

        }

        c.close();

        return customers;
    }


    public int addCustomer(Customer sInfo) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(CUSTOMER_NAME, sInfo.getName());
        cv.put(CUSTOMER_PHOME, sInfo.phone);
        cv.put(CUSTOMER_MOBIL, sInfo.mobil);
        cv.put(CUSTOMER_MAIL, sInfo.mail);
        cv.put(CUSTOMER_ADDRESS, sInfo.address);
        cv.put(CUSTOMER_MEMO, sInfo.memo);
        int iRet = (int) db.insert(TABLE_CUSTOMER, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_CUSTOMER, cv, CUSTOMER_NAME + "=?", new String[]
                    {sInfo.getName()});
        }

        return iRet;
    }

    public int deleteCustomer(Customer sInfo) {
        return db.delete(TABLE_CUSTOMER, CUSTOMER_NAME + "=?", new String[]
                {sInfo.getName()});
    }
}
