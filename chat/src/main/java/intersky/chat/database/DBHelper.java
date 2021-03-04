package intersky.chat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Conversation;
import intersky.chat.ChatUtils;
import intersky.xpxnet.net.Service;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "source.db";
    public static final int DB_VERSION = 9;

    private static final String TABLE_SOURCE = "TABLE_SOURCE";
    private static final String SOURCE_NAME = "SOURCE_NAME";
    private static final String SOURCE_RECORDID = "SOURCE_RECORDID";
    private static final String SOURCE_ADDRESS = "SOURCE_ADDRESS";
    private static final String SOURCE_TYPE = "SOURCE_TYPE";
    private static final String SOURCE_PATH = "SOURCE_PATH";
    private static final String SOURCE_ORDER = "SOURCE_ORDER";
    private static final String SERVICE_FINISH = "SERVICE_FINISH";
    private static final String SOURCE_OWNERID = "SOURCE_OWNERID";

    private static final String TABLE_HEAD = "TABLE_HEAD";
    private static final String HEAD_OWNERID = "HEAD_OWNERID";
    private static final String HEAD_PATH = "HEAD_PATH";
    private static final String HEAD_PATH2 = "HEAD_PATH2";
    private static final String HEAD_ID = "HEAD_ID";

    private static DBHelper mDBHelper;
    private SQLiteDatabase db = null;
    public static DBHelper getInstance(Context context) {
        if (null == mDBHelper) {

            mDBHelper = new DBHelper(context);
        }
        return mDBHelper;
    }

    private DBHelper(Context context) {
        super(context, context.getPackageName()+"."+DB_NAME, null, DB_VERSION);
        openDB();
    }


    private void openDB() {
        if (null == db || !db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOURCE);


        String sql = "CREATE TABLE " + TABLE_SOURCE + " (" + SOURCE_RECORDID
                + " TEXT PRIMARY KEY," + SOURCE_NAME + " TEXT," + SOURCE_ADDRESS + " TEXT," + SOURCE_TYPE + " TEXT," + SOURCE_OWNERID
                + " TEXT," + SOURCE_PATH + " TEXT,"  + SERVICE_FINISH + " TEXT," + SOURCE_ORDER + " TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_HEAD + " (" + HEAD_ID
                + " TEXT PRIMARY KEY," + HEAD_OWNERID + " TEXT," + HEAD_PATH2  + " TEXT," + HEAD_PATH + " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 4)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOURCE);


            String sql = "CREATE TABLE " + TABLE_SOURCE + " (" + HEAD_OWNERID
                    + " TEXT PRIMARY KEY," + SOURCE_NAME + " TEXT," + SOURCE_ADDRESS + " TEXT," + SOURCE_TYPE + " TEXT," + SOURCE_OWNERID
                    + " TEXT," + SOURCE_PATH + " TEXT,"  + SERVICE_FINISH + " TEXT," + SOURCE_ORDER + " TEXT)";
            db.execSQL(sql);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEAD);
            sql = "CREATE TABLE " + TABLE_HEAD + " (" + HEAD_ID
                    + " TEXT PRIMARY KEY," + HEAD_OWNERID + " TEXT,"  + HEAD_PATH2  + " TEXT,"+ HEAD_PATH + " TEXT)";
            db.execSQL(sql);
        }
        else
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEAD);
            String sql = "CREATE TABLE " + TABLE_HEAD + " (" + HEAD_ID
                    + " TEXT PRIMARY KEY," + HEAD_OWNERID + " TEXT," + HEAD_PATH2  + " TEXT," + HEAD_PATH + " TEXT)";
            db.execSQL(sql);
        }
    }

    public List<Conversation> scanSource(HashMap<String,Conversation> conversationHashMap,String ownerid,HashMap<String,ArrayList<Conversation>> listHashMap) {
        openDB();
        List<Conversation> conversations = new ArrayList<Conversation>();

        String sql = "SELECT * FROM " + TABLE_SOURCE  + " WHERE " + SOURCE_OWNERID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {ownerid});
        while (c.moveToNext()) {
            Conversation info = new Conversation();
            info.sourceName = c.getString(c.getColumnIndex(SOURCE_NAME));
            info.sourceId = c.getString(c.getColumnIndex(SOURCE_ADDRESS));
            info.sourcePath = c.getString(c.getColumnIndex(SOURCE_PATH));
            info.sourceType = Integer.valueOf(c.getString(c
                    .getColumnIndex(SOURCE_TYPE)));
            info.detialId = c.getString(c.getColumnIndex(SOURCE_ORDER));
            info.mRecordId = c.getString(c.getColumnIndex(SOURCE_RECORDID));
            info.finish = Boolean.valueOf(c.getString(c
                    .getColumnIndex(SERVICE_FINISH)));
            conversationHashMap.put(info.mRecordId,info);
            if(info.finish == false)
            conversations.add(info);
            ArrayList<Conversation> list = listHashMap.get(info.detialId);
            if(list == null)
            {
                list = new ArrayList<Conversation>();
                listHashMap.put(info.detialId,list);
            }
            list.add(info);
        }
        c.close();

        return conversations;
    }

    public Conversation getSourceInfo(String id) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_SOURCE + " WHERE " + SOURCE_RECORDID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {id});

        Conversation info = null;
        while (c.moveToNext()) {
            info = new Conversation();
            info.sourceName = c.getString(c.getColumnIndex(SOURCE_NAME));
            info.sourceId = c.getString(c.getColumnIndex(SOURCE_ADDRESS));
            info.sourcePath = c.getString(c.getColumnIndex(SOURCE_PATH));
            info.sourceType = Integer.valueOf(c.getString(c
                    .getColumnIndex(SOURCE_TYPE)));
            info.detialId = c.getString(c.getColumnIndex(SOURCE_ORDER));
            info.mRecordId = c.getString(c.getColumnIndex(SOURCE_RECORDID));
            info.finish = Boolean.valueOf(c.getString(c
                    .getColumnIndex(SERVICE_FINISH)));
            break;
        }

        c.close();

        return info;
    }

    public int addSource(Conversation sInfo,String ownerid) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(SOURCE_NAME, sInfo.sourceName);
        cv.put(SOURCE_ADDRESS, sInfo.sourceId);
        cv.put(SOURCE_PATH, sInfo.sourcePath);
        cv.put(SOURCE_TYPE, String.valueOf(sInfo.sourceType));
        cv.put(SOURCE_RECORDID, sInfo.mRecordId);
        cv.put(SOURCE_ORDER, sInfo.detialId);
        cv.put(SOURCE_OWNERID, ownerid);
        cv.put(SERVICE_FINISH, String.valueOf(sInfo.finish));

        int iRet = (int) db.insert(TABLE_SOURCE, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_SOURCE, cv, SOURCE_RECORDID + "=?", new String[]
                    {sInfo.mRecordId});
        }

        return iRet;
    }

    public int deleteSource(Conversation sInfo) {
        return db.delete(TABLE_SOURCE, SOURCE_RECORDID + "=?", new String[]
                {sInfo.mRecordId});
    }


    public void scanHead(HashMap<String, Attachment> head, String ownerid) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_HEAD  + " WHERE " + HEAD_OWNERID
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {ownerid});
        while (c.moveToNext()) {
            Attachment attachment = new Attachment();
            attachment.mRecordid =  c.getString(c.getColumnIndex(HEAD_ID));
            attachment.mUrl =  c.getString(c.getColumnIndex(HEAD_PATH));
            attachment.mUrltemp =  c.getString(c.getColumnIndex(HEAD_PATH2));
            head.put(attachment.mRecordid,attachment);
        }
        c.close();
    }

    public int addHead(Attachment sInfo,String ownerid) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(HEAD_ID, sInfo.mRecordid);
        cv.put(HEAD_PATH, sInfo.mUrl);
        cv.put(HEAD_PATH2, sInfo.mUrltemp);
        cv.put(HEAD_OWNERID, ownerid);

        int iRet = (int) db.insert(TABLE_HEAD, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_HEAD, cv, HEAD_ID + "=?", new String[]
                    {sInfo.mRecordid});
        }

        return iRet;
    }

    public int addHead(String rid,String ownerid) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(HEAD_ID, rid);
        cv.put(HEAD_PATH, "");
        cv.put(HEAD_PATH2, "");
        cv.put(HEAD_OWNERID, ownerid);

        int iRet = (int) db.insert(TABLE_HEAD, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_HEAD, cv, HEAD_ID + "=?", new String[]
                    {rid});
        }

        return iRet;
    }
}
