package intersky.conversation.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import intersky.appbase.ConversationOrderTimeDes;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.apputils.TimeUtils;
import intersky.conversation.ConversationManager;
import intersky.conversation.BigWinerConversationManager;

public class BigWinerDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "bigwiner_conversation.db";
    public static final int DB_VERSION = 8;

    private static final String TABLE_CONVERSATION = "TABLE_CONVERSATION";
    private static final String CONVERSATION_RECORDID = "CONVERSATION_RECORDID";
    private static final String CONVERSATION_TIME = "CONVERSATION_TIME";
    private static final String CONVERSATION_TITLE = "CONVERSATION_TITLE";
    private static final String CONVERSATION_TYPE = "CONVERSATION_TYPE";
    private static final String CONVERSATION_SUBJECT = "CONVERSATION_SUBJECT";
    private static final String CONVERSATION_DETIALID = "CONVERSATION_DETIALID";
    private static final String CONVERSATION_HIT = "CONVERSATION_HIT";
    private static final String CONVERSATION_SOURCE_ID = "CONVERSATION_SOURCE_ID";
    private static final String CONVERSATION_SOURCE_PATH = "CONVERSATION_SOURCE_PATH";
    private static final String CONVERSATION_SOURCE_TYPE = "CONVERSATION_SOURCE_TYPE";
    private static final String CONVERSATION_OWNER_RECORDID = "CONVERSATION_OWNER_RECORDID";
    private static final String CONVERSATION_SOURCE_NAME= "CONVERSATION_SOURCE_NAME";
    private static final String CONVERSATION_SOURCE_SIZE= "CONVERSATION_SOURCE_SIZE";
    private static final String CONVERSATION_SERVICE= "CONVERSATION_SOURCE_SERVICE";
    private static final String CONVERSATION_READ = "CONVERSATION_READ";
    private static final String CONVERSATION_ISSENDTO = "CONVERSATION_ISSENDTO";
    private static final String CONVERSATION_ISSEND = "CONVERSATION_ISSEND";
    private static final String CONVERSATION_TOP = "CONVERSATION_TOP";


    private static final String TABLE_CONTACTS = "TABLE_CONTACTS";
    private static final String CONTACTS_KEY = "CONTACTS_KEY";
    private static final String CONTACTS_RECORDID = "CONTACTS_RECORDID";
    private static final String CONTACTS_ACCREDITATION = "CONTACTS_ACCREDITATION";
    private static final String CONTACTS_AREA = "CONTACTS_AREA";
    private static final String CONTACTS_BUSINESSAREA = "CONTACTS_BUSINESSAREA";
    private static final String CONTACTS_BUSINESSTYPE = "CONTACTS_BUSINESSTYPE";
    private static final String CONTACTS_EMAIL = "CONTACTS_EMAIL";
    private static final String CONTACTS_FAX = "CONTACTS_FAX";
    private static final String CONTACTS_NAME = "CONTACTS_NAME";
    private static final String CONTACTS_DES = "CONTACTS_DES";
    private static final String CONTACTS_MOBIL = "CONTACTS_MOBIL";
    private static final String CONTACTS_OWNER_RECORDID = "CONTACTS_OWNER_RECORDID";
    private static final String CONTACTS_RNAME= "CONTACTS_RNAME";
    private static final String CONTACTS_CONFIRM= "CONTACTS_CONFIRM";
    private static final String CONTACTS_ICON= "CONTACTS_ICON";
    private static final String CONTACTS_COVER = "CONTACTS_COVER";
    private static final String CONTACTS_SEX = "CONTACTS_SEX";
    private static final String CONTACTS_STATUE = "CONTACTS_STATUE";
    private static final String CONTACTS_PHONE = "CONTACTS_PHONE";

    private static BigWinerDBHelper mDBHelper;
    private SQLiteDatabase db = null;

    public Context context;

    public static BigWinerDBHelper getInstance(Context context) {
        if (null == mDBHelper) {

            mDBHelper = new BigWinerDBHelper(context);
        }
        return mDBHelper;
    }

    private BigWinerDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        openDB();
    }


    private void openDB() {
        if (null == db || !db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        String sql = "CREATE TABLE " + TABLE_CONTACTS + " (" + CONTACTS_KEY
                + " TEXT PRIMARY KEY," + CONTACTS_ACCREDITATION + " TEXT," + CONTACTS_RECORDID + " TEXT,"
                + CONTACTS_AREA + " TEXT," + CONTACTS_BUSINESSAREA + " TEXT,"
                + CONTACTS_BUSINESSTYPE + " TEXT," + CONTACTS_EMAIL + " TEXT,"
                + CONTACTS_FAX + " TEXT," + CONTACTS_NAME + " TEXT,"
                + CONTACTS_DES + " TEXT," + CONTACTS_MOBIL + " TEXT,"
                + CONTACTS_OWNER_RECORDID + " TEXT," + CONTACTS_RNAME + " TEXT," + CONTACTS_CONFIRM + " TEXT,"+ CONTACTS_PHONE + " TEXT,"
                + CONTACTS_ICON + " TEXT,"+ CONTACTS_COVER + " TEXT,"+ CONTACTS_SEX + " TEXT,"+ CONTACTS_STATUE + " TEXT)";
        db.execSQL(sql);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);

        sql = "CREATE TABLE " + TABLE_CONVERSATION + " (" + CONVERSATION_RECORDID
                + " TEXT PRIMARY KEY," + CONVERSATION_SERVICE + " TEXT,"
                + CONVERSATION_TIME + " TEXT," + CONVERSATION_TITLE + " TEXT,"
                + CONVERSATION_TYPE + " TEXT," + CONVERSATION_SUBJECT + " TEXT,"
                + CONVERSATION_OWNER_RECORDID + " TEXT," + CONVERSATION_DETIALID + " TEXT,"
                + CONVERSATION_SOURCE_ID + " TEXT," + CONVERSATION_SOURCE_TYPE + " TEXT,"
                + CONVERSATION_READ + " TEXT," + CONVERSATION_SOURCE_NAME + " TEXT," + CONVERSATION_SOURCE_SIZE + " TEXT,"
                + CONVERSATION_ISSENDTO + " TEXT,"+ CONVERSATION_SOURCE_PATH + " TEXT,"+ CONVERSATION_ISSEND + " TEXT,"+ CONVERSATION_HIT + " TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);


       String sql = "CREATE TABLE " + TABLE_CONVERSATION + " (" + CONVERSATION_RECORDID
                + " TEXT PRIMARY KEY," + CONVERSATION_SERVICE + " TEXT,"
                + CONVERSATION_TIME + " TEXT," + CONVERSATION_TITLE + " TEXT,"
                + CONVERSATION_TYPE + " TEXT," + CONVERSATION_SUBJECT + " TEXT,"
                + CONVERSATION_OWNER_RECORDID + " TEXT," + CONVERSATION_DETIALID + " TEXT,"
                + CONVERSATION_SOURCE_ID + " TEXT," + CONVERSATION_SOURCE_TYPE + " TEXT,"
                + CONVERSATION_READ + " TEXT," + CONVERSATION_SOURCE_NAME + " TEXT," + CONVERSATION_SOURCE_SIZE + " TEXT,"
                + CONVERSATION_ISSENDTO + " TEXT,"+ CONVERSATION_SOURCE_PATH + " TEXT,"+ CONVERSATION_ISSEND + " TEXT,"+ CONVERSATION_HIT + " TEXT)";
        db.execSQL(sql);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        sql = "CREATE TABLE " + TABLE_CONTACTS + " (" + CONTACTS_KEY
                + " TEXT PRIMARY KEY," + CONTACTS_ACCREDITATION + " TEXT,"+ CONTACTS_RECORDID + " TEXT,"
                + CONTACTS_AREA + " TEXT," + CONTACTS_BUSINESSAREA + " TEXT,"
                + CONTACTS_BUSINESSTYPE + " TEXT," + CONTACTS_EMAIL + " TEXT,"
                + CONTACTS_FAX + " TEXT," + CONTACTS_NAME + " TEXT,"
                + CONTACTS_DES + " TEXT," + CONTACTS_MOBIL + " TEXT,"
                + CONTACTS_OWNER_RECORDID + " TEXT," + CONTACTS_RNAME + " TEXT," + CONTACTS_CONFIRM + " TEXT,"+ CONTACTS_PHONE + " TEXT,"
                + CONTACTS_ICON + " TEXT,"+ CONTACTS_COVER + " TEXT,"+ CONTACTS_SEX + " TEXT,"+ CONTACTS_STATUE + " TEXT)";
        db.execSQL(sql);

        if(newVersion == 8)
        {
            SharedPreferences sharedPre = context.getSharedPreferences("LAST_USER", 0);
            if (sharedPre != null) {
                SharedPreferences.Editor e2 = sharedPre.edit();
                e2.putBoolean("USER_ISLOGIN", false);
                e2.commit();
            }
        }

    }

    public ArrayList<Conversation> scanMessage(String userid,String keyword) {
        openDB();
        ArrayList<Conversation> mConversations = new ArrayList<Conversation>();
        HashMap<String,Conversation> hashMaps = new HashMap<String,Conversation>();
        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_OWNER_RECORDID +
                "=?"+"AND "+CONVERSATION_TYPE+"=?";

        Cursor c = db.rawQuery(sql, new String[]
                {userid,Conversation.CONVERSATION_TYPE_MESSAGE});

        while (c.moveToNext()) {

            Conversation info = new Conversation();
            info.mType = c.getString(c.getColumnIndex(CONVERSATION_TYPE));
            info.mRecordId = c.getString(c.getColumnIndex(CONVERSATION_RECORDID));
            info.mTime = c.getString(c.getColumnIndex(CONVERSATION_TIME));
            info.mTitle = c.getString(c.getColumnIndex(CONVERSATION_TITLE));
            info.mSubject = c.getString(c.getColumnIndex(CONVERSATION_SUBJECT));
            info.mHit = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_HIT)));
            info.detialId = c.getString(c.getColumnIndex(CONVERSATION_DETIALID));
            info.sourceId = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_ID));
            info.sourceType = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_TYPE)));
            info.sourceName = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_NAME));
            info.sourcePath = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_PATH));
            info.sourceSize = Long.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_SIZE)));
            info.isRead = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_READ)));
            info.isSendto = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSENDTO)));
            info.issend = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSEND)));
            if(info.issend == 0)
            {
                info.issend = -1;
            }
            if(info.mSubject.contains(keyword))
            {
               Conversation conversation = hashMaps.get(info.detialId);
                if(conversation == null)
                {
                    mConversations.add(info);
                    hashMaps.put(info.detialId,info);
                }
                else
                {
                    if(TimeUtils.measureBefore(conversation.mTime,info.mTime))
                    {
                        conversation.mTime = info.mTime;
                        conversation.mSubject = info.mSubject;
                    }
                }
            }

        }
        Collections.sort(mConversations,new ConversationOrderTimeDes());
        return mConversations;
    }


    public ArrayList<Conversation> scanConversation(String userid) {
        openDB();
        ArrayList<Conversation> mConversations = new ArrayList<Conversation>();

        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_OWNER_RECORDID +
                "=?";

        Cursor c = db.rawQuery(sql, new String[]
                {userid});


        while (c.moveToNext()) {

            Conversation info = new Conversation();
            info.mType = c.getString(c.getColumnIndex(CONVERSATION_TYPE));
            info.mRecordId = c.getString(c.getColumnIndex(CONVERSATION_RECORDID));
            info.mTime = c.getString(c.getColumnIndex(CONVERSATION_TIME));
            info.mTitle = c.getString(c.getColumnIndex(CONVERSATION_TITLE));
            info.mSubject = c.getString(c.getColumnIndex(CONVERSATION_SUBJECT));
            info.mHit = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_HIT)));
            info.detialId = c.getString(c.getColumnIndex(CONVERSATION_DETIALID));
            info.sourceId = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_ID));
            info.sourceType = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_TYPE)));
            info.sourceName = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_NAME));
            info.sourcePath = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_PATH));
            info.sourceSize = Long.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_SIZE)));
            info.isRead = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_READ)));
            info.isSendto = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSENDTO)));
            info.issend = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSEND)));
            if(info.issend == 0)
            {
                info.issend = -1;
            }
            if (info.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE))
            {
                if(info.isRead == false)
                {
                }

                boolean has = false;
                ArrayList<Conversation> conversations = BigWinerConversationManager.getInstance().messageConversation.get(info.detialId);
                if( conversations == null )
                {
                    conversations = new ArrayList<Conversation>();
                    BigWinerConversationManager.getInstance().messageConversation.put(info.detialId,conversations);
                }
                HashMap<String,Conversation> hashMap = BigWinerConversationManager.getInstance().messageConversation2.get(info.detialId);
                if(hashMap == null)
                {
                    hashMap = new HashMap<String,Conversation>();
                    BigWinerConversationManager.getInstance().messageConversation2.put(info.detialId,hashMap);
                }
                conversations.add(info);
                hashMap.put(info.mRecordId,info);
                Conversation infoh = BigWinerConversationManager.getInstance().messageHConversation.get(info.detialId);
                ArrayList<Conversation> conversationss = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE);

                if(infoh == null)
                {
                    infoh = new Conversation();
                    infoh.mTime = "1990-01-01 00:00:00";
                    BigWinerConversationManager.getInstance().messageHConversation.put(info.detialId,infoh);
                    conversationss.add(infoh);

                }
                if(TimeUtils.measureBefore(infoh.mTime+":00",info.mTime+":00") == true)
                {
                    infoh.mType = c.getString(c.getColumnIndex(CONVERSATION_TYPE));
                    infoh.mRecordId = c.getString(c.getColumnIndex(CONVERSATION_RECORDID));
                    infoh.mTime = c.getString(c.getColumnIndex(CONVERSATION_TIME));
                    infoh.mTitle = c.getString(c.getColumnIndex(CONVERSATION_TITLE));
                    infoh.mSubject = c.getString(c.getColumnIndex(CONVERSATION_SUBJECT));
                    if(info.isSendto == false && info.isRead == false)
                    infoh.mHit++;
                    infoh.detialId = c.getString(c.getColumnIndex(CONVERSATION_DETIALID));
                    infoh.sourceId = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_ID));
                    infoh.sourceType = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_TYPE)));
                    infoh.sourceName = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_NAME));
                    infoh.sourcePath = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_PATH));
                    infoh.sourceSize = Long.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_SIZE)));
                    infoh.isRead = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_READ)));
                    infoh.isSendto = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSENDTO)));
                    infoh.issend = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSEND)));
                }

            }
            else if (info.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)){
                ArrayList<Conversation> conversations;
                if( BigWinerConversationManager.getInstance().collectionConversation.containsKey(Conversation.CONVERSATION_TYPE_NOTICE))
                {
                    conversations = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE);

                }
                else
                {
                    conversations = new ArrayList<Conversation>();
                    BigWinerConversationManager.getInstance().collectionConversation.put(Conversation.CONVERSATION_TYPE_NOTICE,conversations);
                }
                conversations.add(info);
            }
            else if (info.mType.equals(Conversation.CONVERSATION_TYPE_NEWS)){
                ArrayList<Conversation> conversations;
                if( BigWinerConversationManager.getInstance().collectionConversation.containsKey(Conversation.CONVERSATION_TYPE_NEWS))
                {
                    conversations = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS);

                }
                else
                {
                    conversations = new ArrayList<Conversation>();
                    BigWinerConversationManager.getInstance().collectionConversation.put(Conversation.CONVERSATION_TYPE_NEWS,conversations);
                }
                conversations.add(info);
            }
            else if (info.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)){
                ArrayList<Conversation> conversations;
                if( BigWinerConversationManager.getInstance().collectionConversation.containsKey(Conversation.CONVERSATION_TYPE_MEETING))
                {
                    conversations = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING);

                }
                else
                {
                    conversations = new ArrayList<Conversation>();
                    BigWinerConversationManager.getInstance().collectionConversation.put(Conversation.CONVERSATION_TYPE_MEETING,conversations);
                }
                conversations.add(info);
            }
        }
        ArrayList<Conversation> conversationms =BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        Collections.sort(conversationms,new ConversationOrderTimeDes());
        ModuleDetial mModuleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MESSAGE);
        if(conversationms.size() > 1)
        {
            BigWinerConversationManager.getInstance().mConversations.add(conversationms.get(0));
            BigWinerConversationManager.getInstance().mConversations.add(conversationms.get(1));
            mModuleDetial.allpagesize = 2;
        }
        else if(conversationms.size() == 1)
        {
            BigWinerConversationManager.getInstance().mConversations.add(conversationms.get(0));
            mModuleDetial.allpagesize = 1;
        }
        else
        {
            mModuleDetial.allpagesize = 0;
        }
        for(int i = 0 ; i < conversationms.size() ; i++)
        {
            ArrayList<Conversation> conversations = BigWinerConversationManager.getInstance().messageConversation.get(conversationms.get(i).detialId);
            Collections.sort(conversations,new ConversationOrderTimeDes());
        }
        c.close();
        return mConversations;
    }

    public int addConversation(Conversation sInfo) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(CONVERSATION_TYPE, sInfo.mType);
        cv.put(CONVERSATION_RECORDID, sInfo.mRecordId);
        cv.put(CONVERSATION_TIME, sInfo.mTime);
        cv.put(CONVERSATION_SERVICE, "");
        cv.put(CONVERSATION_TITLE, sInfo.mTitle);
        cv.put(CONVERSATION_SUBJECT, sInfo.mSubject);
        cv.put(CONVERSATION_HIT, String.valueOf(sInfo.mHit));
        cv.put(CONVERSATION_DETIALID, sInfo.detialId);
        cv.put(CONVERSATION_OWNER_RECORDID, BigWinerConversationManager.getInstance().mUserid);
        cv.put(CONVERSATION_READ, String.valueOf(sInfo.isRead));
        cv.put(CONVERSATION_ISSENDTO, String.valueOf(sInfo.isSendto));
        cv.put(CONVERSATION_ISSEND, Integer.valueOf(sInfo.issend));
        cv.put(CONVERSATION_SOURCE_ID,sInfo.sourceId);
        cv.put(CONVERSATION_SOURCE_TYPE, String.valueOf(sInfo.sourceType));
        cv.put(CONVERSATION_SOURCE_NAME,sInfo.sourceName);
        cv.put(CONVERSATION_SOURCE_PATH,sInfo.sourcePath);
        cv.put(CONVERSATION_SOURCE_SIZE, String.valueOf(sInfo.sourceSize));
        if(sInfo.mType.length() == 0)
        {

        }
        else{
            int iRet = (int) db.insert(TABLE_CONVERSATION, null, cv);
            if (-1 == iRet) {
                iRet = db.update(TABLE_CONVERSATION, cv, CONVERSATION_RECORDID + "=?", new String[]
                        {sInfo.mRecordId});
            }
            return iRet;
        }
        return 0;
    }

    public Conversation getConversation(Conversation sInfo) {
        openDB();
        ArrayList<Conversation> mConversations = new ArrayList<Conversation>();

        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_OWNER_RECORDID +
                "=?"+"AND "+CONVERSATION_RECORDID+"=?"+"AND "+CONVERSATION_TYPE+"=?";

        Cursor c = db.rawQuery(sql, new String[]
                {BigWinerConversationManager.getInstance().mUserid,sInfo.mRecordId,Conversation.CONVERSATION_TYPE_MESSAGE});
        while (c.moveToNext()) {

            Conversation info = new Conversation();
            info.mType = c.getString(c.getColumnIndex(CONVERSATION_TYPE));
            info.mRecordId = c.getString(c.getColumnIndex(CONVERSATION_RECORDID));
            info.mTime = c.getString(c.getColumnIndex(CONVERSATION_TIME));
            info.mTitle = c.getString(c.getColumnIndex(CONVERSATION_TITLE));
            info.mSubject = c.getString(c.getColumnIndex(CONVERSATION_SUBJECT));
            info.mHit = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_HIT)));
            info.detialId = c.getString(c.getColumnIndex(CONVERSATION_DETIALID));
            info.sourceId = c.getString(c.getColumnIndex(CONVERSATION_OWNER_RECORDID));
            info.sourceType = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_TYPE)));
            info.sourceName = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_NAME));
            info.sourcePath = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_PATH));
            info.sourceSize = Long.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_SIZE)));
            info.isRead = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_READ)));
            info.isSendto = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSENDTO)));
            info.issend = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSEND)));
            if(info.issend == 0)
            {
                info.issend = -1;
            }
            mConversations.add(info);
        }
        if(mConversations.size() > 0)
        {
            return  mConversations.get(0);
        }
        else
        {
            return null;
        }
    }

    public void updateConversation(ArrayList<Conversation> conversations) {
        openDB();
        for(int i = 0 ; i < conversations.size() ;i++)
        {
            Conversation sInfo = conversations.get(i);
            ContentValues cv = new ContentValues();
            cv.put(CONVERSATION_TYPE, sInfo.mType);
            cv.put(CONVERSATION_RECORDID, sInfo.mRecordId);
            cv.put(CONVERSATION_TIME, sInfo.mTime);
            cv.put(CONVERSATION_SERVICE, "");
            cv.put(CONVERSATION_TITLE, sInfo.mTitle);
            cv.put(CONVERSATION_SUBJECT, sInfo.mSubject);
            cv.put(CONVERSATION_HIT, String.valueOf(sInfo.mHit));
            cv.put(CONVERSATION_DETIALID, sInfo.detialId);
            cv.put(CONVERSATION_OWNER_RECORDID, BigWinerConversationManager.getInstance().mUserid);
            cv.put(CONVERSATION_READ, String.valueOf(sInfo.isRead));
            cv.put(CONVERSATION_ISSENDTO, String.valueOf(sInfo.isSendto));
            cv.put(CONVERSATION_ISSEND, Integer.valueOf(sInfo.issend));
            cv.put(CONVERSATION_SOURCE_ID,sInfo.sourceId);
            cv.put(CONVERSATION_SOURCE_TYPE, String.valueOf(sInfo.sourceType));
            cv.put(CONVERSATION_SOURCE_NAME,sInfo.sourceName);
            cv.put(CONVERSATION_SOURCE_PATH,sInfo.sourcePath);
            cv.put(CONVERSATION_SOURCE_SIZE, String.valueOf(sInfo.sourceSize));

            if(sInfo.mType.length() == 0)
            {
//                AppUtils.showMessage(BigWinerConversationManager.getInstance().context,"更新会话类型为空");
            }
            else
            {
                db.update(TABLE_CONVERSATION, cv, CONVERSATION_RECORDID + "=?", new String[]
                        {sInfo.mRecordId});
            }

        }
    }

    public ArrayList<Conversation> scanConversationMessage(String detialid)
    {
        openDB();
        ArrayList<Conversation> mConversations = new ArrayList<Conversation>();

        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_OWNER_RECORDID +
                "=?"+"AND "+CONVERSATION_DETIALID+"=?"+"AND "+CONVERSATION_TYPE+"=?";

        Cursor c = db.rawQuery(sql, new String[]
                {BigWinerConversationManager.getInstance().mUserid,detialid,Conversation.CONVERSATION_TYPE_MESSAGE});
        while (c.moveToNext()) {

            Conversation info = new Conversation();
            info.mType = c.getString(c.getColumnIndex(CONVERSATION_TYPE));
            info.mRecordId = c.getString(c.getColumnIndex(CONVERSATION_RECORDID));
            info.mTime = c.getString(c.getColumnIndex(CONVERSATION_TIME));
            info.mTitle = c.getString(c.getColumnIndex(CONVERSATION_TITLE));
            info.mSubject = c.getString(c.getColumnIndex(CONVERSATION_SUBJECT));
            info.mHit = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_HIT)));
            info.detialId = c.getString(c.getColumnIndex(CONVERSATION_DETIALID));
            info.sourceId = c.getString(c.getColumnIndex(CONVERSATION_OWNER_RECORDID));
            info.sourceType = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_TYPE)));
            info.sourceName = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_NAME));
            info.sourcePath = c.getString(c.getColumnIndex(CONVERSATION_SOURCE_PATH));
            info.sourceSize = Long.valueOf(c.getString(c.getColumnIndex(CONVERSATION_SOURCE_SIZE)));
            info.isRead = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_READ)));
            info.isSendto = Boolean.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSENDTO)));
            info.issend = Integer.valueOf(c.getString(c.getColumnIndex(CONVERSATION_ISSEND)));
            if(info.issend == 0)
            {
                info.issend = -1;
            }
            mConversations.add(info);
        }
        return mConversations;
    }


    public int deleteConversation(Conversation sInfo) {
        return db.delete(TABLE_CONVERSATION, CONVERSATION_RECORDID + "=? and " + CONVERSATION_OWNER_RECORDID +  "=? ", new String[]
                {sInfo.mRecordId, BigWinerConversationManager.getInstance().mUserid});
    }

    public int deleteConversation(String detial) {
        return db.delete(TABLE_CONVERSATION, CONVERSATION_DETIALID + "=? and " + CONVERSATION_OWNER_RECORDID +  "=? ", new String[]
                {detial, BigWinerConversationManager.getInstance().mUserid});
    }

    public int deleteConversationByType(String type) {
        return db.delete(TABLE_CONVERSATION, CONVERSATION_TYPE + "=? and " + CONVERSATION_OWNER_RECORDID +  "=?" , new String[]
                {type,BigWinerConversationManager.getInstance().mUserid});
    }

    public void setConversationMessaegRed(String id) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CONVERSATION_READ, "true");
        db.update(TABLE_CONVERSATION, cv, CONVERSATION_DETIALID + "=?"+"AND "+CONVERSATION_TYPE+"=?", new String[]
                {id,Conversation.CONVERSATION_TYPE_MESSAGE});
    }

    public void setConversationMessaegTitle(String id,String title) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CONVERSATION_READ, "true");
        cv.put(CONVERSATION_TITLE, title);
        db.update(TABLE_CONVERSATION, cv, CONVERSATION_DETIALID + "=?"+"AND "+CONVERSATION_TYPE+"=?", new String[]
                {id,Conversation.CONVERSATION_TYPE_MESSAGE});
    }

    public void setConversationMessaegDelete(String id) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CONVERSATION_READ, "true");
        db.delete(TABLE_CONVERSATION, CONVERSATION_DETIALID + "=?"+"AND "+CONVERSATION_TYPE+"=?", new String[]
                {id,Conversation.CONVERSATION_TYPE_MESSAGE});
    }

    public void conversationMessaegClean() {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CONVERSATION_READ, "true");
        db.delete(TABLE_CONVERSATION, CONVERSATION_TYPE+"=?", new String[]
                {Conversation.CONVERSATION_TYPE_MESSAGE});
    }

    public int getAllUnReadCount() {
        openDB();
        ContentValues cv = new ContentValues();
        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_OWNER_RECORDID +
                "=?"+"AND "+CONVERSATION_READ+"=?";
        Cursor c = db.rawQuery(sql, new String[]
                {BigWinerConversationManager.getInstance().mUserid,"false"});
        int count = c.getCount();
        return count;
    }

    public int getAllUnReadCount(String type) {
        openDB();
        ContentValues cv = new ContentValues();
        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_OWNER_RECORDID +
                " = ? "+" AND "+CONVERSATION_READ+" = ? " +" AND "+CONVERSATION_TYPE+" = ? ";
        Cursor c = db.rawQuery(sql, new String[]
                {BigWinerConversationManager.getInstance().mUserid,"false",type});
        int count = c.getCount();
        return count;
    }

    public void cleanAll() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);

        String sql = "CREATE TABLE " + TABLE_CONVERSATION + " (" + CONVERSATION_RECORDID
                + " TEXT PRIMARY KEY," + CONVERSATION_SERVICE + " TEXT,"
                + CONVERSATION_TIME + " TEXT," + CONVERSATION_TITLE + " TEXT,"
                + CONVERSATION_TYPE + " TEXT," + CONVERSATION_SUBJECT + " TEXT,"
                + CONVERSATION_OWNER_RECORDID + " TEXT," + CONVERSATION_DETIALID + " TEXT,"
                + CONVERSATION_SOURCE_ID + " TEXT," + CONVERSATION_SOURCE_TYPE + " TEXT,"
                + CONVERSATION_READ + " TEXT," + CONVERSATION_SOURCE_NAME + " TEXT," + CONVERSATION_SOURCE_SIZE + " TEXT,"
                + CONVERSATION_ISSENDTO + " TEXT,"+ CONVERSATION_SOURCE_PATH + " TEXT,"+ CONVERSATION_ISSEND + " TEXT,"+ CONVERSATION_HIT + " TEXT)";
        db.execSQL(sql);
    }

    public void scanContacts(HashMap<String,Contacts> friendHashMap,ArrayList<Contacts> contacts,HashMap<String,Contacts> mhashheads,ArrayList<Contacts> mheads
            ,ArrayList<Contacts> all,boolean[] typeboolfriend,HashMap<String,ArrayList<Contacts>> friendHeadHashMap,String userid) {
        openDB();
        ArrayList<Conversation> mConversations = new ArrayList<Conversation>();

        String sql = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + CONTACTS_OWNER_RECORDID +
                "=?";

        Cursor c = db.rawQuery(sql, new String[]
                {userid});

        while (c.moveToNext()) {

            Contacts info = new Contacts();
            info.mRecordid = c.getString(c.getColumnIndex(CONTACTS_RECORDID));
            info.mPosition = c.getString(c.getColumnIndex(CONTACTS_ACCREDITATION));
            info.location = c.getString(c.getColumnIndex(CONTACTS_AREA));
            info.province = c.getString(c.getColumnIndex(CONTACTS_AREA));
            info.typearea = c.getString(c.getColumnIndex(CONTACTS_BUSINESSAREA));
            info.typevalue = c.getString(c.getColumnIndex(CONTACTS_BUSINESSTYPE));
            info.eMail = c.getString(c.getColumnIndex(CONTACTS_EMAIL));
            info.mFax = c.getString(c.getColumnIndex(CONTACTS_FAX));
            info.mName = c.getString(c.getColumnIndex(CONTACTS_NAME));
            info.des = c.getString(c.getColumnIndex(CONTACTS_DES));
            info.mMobile = c.getString(c.getColumnIndex(CONTACTS_MOBIL));
            info.mRName = c.getString(c.getColumnIndex(CONTACTS_RNAME));
            info.mPhone = c.getString(c.getColumnIndex(CONTACTS_PHONE));
            info.confrim = c.getString(c.getColumnIndex(CONTACTS_CONFIRM));
            info.icon = c.getString(c.getColumnIndex(CONTACTS_ICON));
            info.cover = c.getString(c.getColumnIndex(CONTACTS_COVER));
            info.mSex = c.getString(c.getColumnIndex(CONTACTS_SEX));
            info.staue = c.getString(c.getColumnIndex(CONTACTS_STATUE));
            if(info.mSex.equals("男"))
                info.sex = 0;
            else if(info.mSex.equals("女"))
                info.sex = 1;
            else
                info.sex = 2;
            friendHashMap.put(info.mRecordid,info);
            contacts.add(info);
            all.add(info);
            String s = info.getmPingyin().substring(0, 1).toUpperCase();
            ArrayList<Contacts> hContacts = friendHeadHashMap.get(s);
            if(hContacts == null)
            {
                hContacts = new ArrayList<Contacts>();
                friendHeadHashMap.put(s,hContacts);
            }
            hContacts.add(info);
            int pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (typeboolfriend[pos] == false) {
                    Contacts contactsh = new Contacts(s);
                    mheads.add(contactsh);
                    mhashheads.put(s,contactsh);
                    all.add(contactsh);
                    typeboolfriend[pos] = true;
                }
            }
            else
            {
                s = "#";
                pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (typeboolfriend[pos] == false) {
                        Contacts contactsh = new Contacts(s);
                        mheads.add(contactsh);
                        mhashheads.put(s,contactsh);
                        all.add(contactsh);
                        typeboolfriend[pos] = true;
                    }
                }
            }


        }
        Collections.sort(contacts, new SortContactComparator());
        Collections.sort(all, new SortContactComparator());
        Collections.sort(mheads, new SortContactComparator());
    }

    public void saveContacts(Contacts sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CONTACTS_KEY, sInfo.mRecordid+BigWinerConversationManager.getInstance().mUserid);
        cv.put(CONTACTS_RECORDID, sInfo.mRecordid);
        cv.put(CONTACTS_ACCREDITATION, sInfo.mPosition);
        cv.put(CONTACTS_AREA, sInfo.location);
        cv.put(CONTACTS_BUSINESSAREA, sInfo.typearea);
        cv.put(CONTACTS_BUSINESSTYPE, sInfo.typevalue);
        cv.put(CONTACTS_EMAIL, sInfo.eMail);
        cv.put(CONTACTS_FAX, sInfo.mFax);
        cv.put(CONTACTS_NAME, sInfo.mName);
        cv.put(CONTACTS_OWNER_RECORDID, BigWinerConversationManager.getInstance().mUserid);
        cv.put(CONTACTS_DES, sInfo.des);
        cv.put(CONTACTS_MOBIL, sInfo.mMobile);
        cv.put(CONTACTS_RNAME, sInfo.mRName);
        cv.put(CONTACTS_PHONE,sInfo.mPhone);
        cv.put(CONTACTS_CONFIRM,sInfo.confrim);
        cv.put(CONTACTS_ICON,sInfo.icon);
        cv.put(CONTACTS_COVER,sInfo.cover);
        cv.put(CONTACTS_SEX, sInfo.mSex);
        cv.put(CONTACTS_STATUE, sInfo.staue);
        int iRet = (int) db.insert(TABLE_CONTACTS, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_CONTACTS, cv, CONTACTS_KEY + "=?", new String[]
                    {sInfo.mRecordid+BigWinerConversationManager.getInstance().mUserid});
        }
        return;
    }

    public void removeContacts(Contacts contacts) {
        db.delete(TABLE_CONTACTS, CONTACTS_RECORDID + "=? and " + CONTACTS_OWNER_RECORDID +  "=? ", new String[]
                {contacts.mRecordid, BigWinerConversationManager.getInstance().mUserid});
    }

    public void cleanContacts() {
        db.delete(TABLE_CONTACTS, CONTACTS_OWNER_RECORDID +  "=? ", new String[]
                {BigWinerConversationManager.getInstance().mUserid});
    }

    public class SortContactComparator implements Comparator {

        @Override
        public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
            // TODO Auto-generated method stub
            String[] array = new String[2];

            array[0] = ((Contacts) mMailPersonItem1).getmPingyin();
            array[1] = ((Contacts) mMailPersonItem2).getmPingyin();
            if (array[0].equals(array[1])) {
                return 0;
            }
            Arrays.sort(array);
            if (array[0].equals(((Contacts) mMailPersonItem1).getmPingyin())) {
                return -1;
            } else if (array[0].equals(((Contacts) mMailPersonItem2).getmPingyin())) {
                return 1;
            }
            return 0;
        }
    }
}
