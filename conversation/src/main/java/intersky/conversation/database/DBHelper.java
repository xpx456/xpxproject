package intersky.conversation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.ConversationManager;
import intersky.appbase.entity.Conversation;
import intersky.conversation.entity.ConversationCollectMap;

public class DBHelper extends SQLiteOpenHelper {


    private static final String TABLE_CONVERSATION = "TABLE_CONVERSATION";
    private static final String CONVERSATION_RECORDID = "CONVERSATION_RECORDID";
    private static final String CONVERSATION_TIME = "CONVERSATION_TIME";
    private static final String CONVERSATION_TITLE = "CONVERSATION_TITLE";
    private static final String CONVERSATION_TYPE = "CONVERSATION_TYPE";
    private static final String CONVERSATION_SUBJECT = "CONVERSATION_SUBJECT";
    private static final String CONVERSATION_DETIALID = "CONVERSATION_DETIALID";
    private static final String CONVERSATION_ICON = "CONVERSATION_ICON";
    private static final String CONVERSATION_ICON_TYPE = "CONVERSATION_ICON_TYPE";
    private static final String CONVERSATION_HIT = "CONVERSATION_HIT";
    private static final String CONVERSATION_SOURCE_ID = "CONVERSATION_SOURCE_ID";
    private static final String CONVERSATION_SOURCE_PATH = "CONVERSATION_SOURCE_PATH";
    private static final String CONVERSATION_SOURCE_TYPE = "CONVERSATION_SOURCE_TYPE";
    private static final String CONVERSATION_SOURCE_NAME= "CONVERSATION_SOURCE_NAME";
    private static final String CONVERSATION_SOURCE_SIZE= "CONVERSATION_SOURCE_SIZE";
    private static final String CONVERSATION_BASEID= "CONVERSATION_BASEID";
    private static final String CONVERSATION_READ = "CONVERSATION_READ";
    private static final String CONVERSATION_ISSENDTO = "CONVERSATION_ISSENDTO";
    private static final String CONVERSATION_ISSEND = "CONVERSATION_ISSEND";
    private static final String CONVERSATION_MESSAGEID = "CONVERSATION_MESSAGEID";

    private static DBHelper mDBHelper;
    private SQLiteDatabase db = null;

    public static void init(Context context,String name,int version) {
        mDBHelper = new DBHelper(context,name,version);
    }

    public static DBHelper getInstance() {
        return mDBHelper;
    }

    private DBHelper(Context context,String name,int version) {
        super(context, name, null, version);
        openDB();
    }


    private void openDB() {
        if (null == db || !db.isOpen()) {
            db = this.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);

        String sql = "CREATE TABLE " + TABLE_CONVERSATION + " (" + CONVERSATION_RECORDID
                + " TEXT PRIMARY KEY," + CONVERSATION_ICON + " TEXT,"+ CONVERSATION_ICON_TYPE + " TEXT,"
                + CONVERSATION_TIME + " TEXT," + CONVERSATION_TITLE + " TEXT,"+ CONVERSATION_MESSAGEID + " TEXT,"
                + CONVERSATION_TYPE + " TEXT," + CONVERSATION_SUBJECT + " TEXT,"
                + CONVERSATION_BASEID + " TEXT," + CONVERSATION_DETIALID + " TEXT,"
                + CONVERSATION_SOURCE_ID + " TEXT," + CONVERSATION_SOURCE_TYPE + " TEXT,"
                + CONVERSATION_READ + " TEXT," + CONVERSATION_SOURCE_NAME + " TEXT," + CONVERSATION_SOURCE_SIZE + " TEXT,"
                + CONVERSATION_ISSENDTO + " TEXT,"+ CONVERSATION_SOURCE_PATH + " TEXT,"+ CONVERSATION_ISSEND + " TEXT,"+ CONVERSATION_HIT + " TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);


       String sql = "CREATE TABLE " + TABLE_CONVERSATION + " (" + CONVERSATION_RECORDID
                + " TEXT PRIMARY KEY," + CONVERSATION_ICON + " TEXT,"+ CONVERSATION_ICON_TYPE + " TEXT,"
                + CONVERSATION_TIME + " TEXT," + CONVERSATION_TITLE + " TEXT,"+ CONVERSATION_MESSAGEID + " TEXT,"
                + CONVERSATION_TYPE + " TEXT," + CONVERSATION_SUBJECT + " TEXT,"
                + CONVERSATION_BASEID + " TEXT," + CONVERSATION_DETIALID + " TEXT,"
                + CONVERSATION_SOURCE_ID + " TEXT," + CONVERSATION_SOURCE_TYPE + " TEXT,"
                + CONVERSATION_READ + " TEXT," + CONVERSATION_SOURCE_NAME + " TEXT," + CONVERSATION_SOURCE_SIZE + " TEXT,"
                + CONVERSATION_ISSENDTO + " TEXT,"+ CONVERSATION_SOURCE_PATH + CONVERSATION_ISSEND + " TEXT,"+ " TEXT,"+ CONVERSATION_HIT + " TEXT)";
        db.execSQL(sql);

    }


    public void initConversationValue(Conversation info,Cursor c)
    {
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
        info.mIcon = c.getString(c.getColumnIndex(CONVERSATION_ICON));
        info.iconType = c.getString(c.getColumnIndex(CONVERSATION_ICON_TYPE));
        info.messageId = c.getString(c.getColumnIndex(CONVERSATION_MESSAGEID));
    }

    public void initConversationCursor(Conversation sInfo,ContentValues cv)
    {
        cv.put(CONVERSATION_TYPE, sInfo.mType);
        cv.put(CONVERSATION_RECORDID, sInfo.mRecordId);
        cv.put(CONVERSATION_TIME, sInfo.mTime);
        cv.put(CONVERSATION_ICON, sInfo.mIcon);
        cv.put(CONVERSATION_TITLE, sInfo.mTitle);
        cv.put(CONVERSATION_SUBJECT, sInfo.mSubject);
        cv.put(CONVERSATION_HIT, String.valueOf(sInfo.mHit));
        cv.put(CONVERSATION_DETIALID, sInfo.detialId);
        cv.put(CONVERSATION_ICON_TYPE, sInfo.iconType);
        cv.put(CONVERSATION_READ, String.valueOf(sInfo.isRead));
        cv.put(CONVERSATION_ISSENDTO, String.valueOf(sInfo.isSendto));
        cv.put(CONVERSATION_ISSEND, String.valueOf(sInfo.issend));
        cv.put(CONVERSATION_BASEID, ConversationManager.getInstance().dataBaseId);
        cv.put(CONVERSATION_SOURCE_ID,sInfo.sourceId);
        cv.put(CONVERSATION_SOURCE_TYPE, String.valueOf(sInfo.sourceType));
        cv.put(CONVERSATION_SOURCE_NAME,sInfo.sourceName);
        cv.put(CONVERSATION_SOURCE_PATH,sInfo.sourcePath);
        cv.put(CONVERSATION_SOURCE_SIZE, String.valueOf(sInfo.sourceSize));
        cv.put(CONVERSATION_MESSAGEID,sInfo.messageId);
    }

    public void scanConversation(ConversationCollectMap conversationAll) {
        openDB();
        ArrayList<Conversation> mConversations = new ArrayList<Conversation>();

        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " +CONVERSATION_BASEID+"=?";

        Cursor c = db.rawQuery(sql, new String[]
                {ConversationManager.getInstance().dataBaseId});


        while (c.moveToNext()) {
            Conversation info = new Conversation();
            initConversationValue(info, c);
            if(info.issend == Conversation.MESSAGE_STATAUE_SENDING)
            {
                ContentValues cv = new ContentValues();
                cv.put(CONVERSATION_ISSEND, String.valueOf(Conversation.MESSAGE_STATAUE_FAIL));
                info.issend = Conversation.MESSAGE_STATAUE_FAIL;
                db.update(TABLE_CONVERSATION, cv, CONVERSATION_RECORDID + "=?", new String[]
                        {info.mRecordId});
            }
            conversationAll.addConversationDb(info);
        }
    }

    public int addConversation(Conversation sInfo) {
        openDB();

        ContentValues cv = new ContentValues();
        initConversationCursor(sInfo,cv);
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

    public int upDataConversation(Conversation sInfo) {
        openDB();

        ContentValues cv = new ContentValues();
        initConversationCursor(sInfo,cv);
        if(sInfo.mType.length() == 0)
        {

        }
        else{
            int iRet = db.update(TABLE_CONVERSATION, cv, CONVERSATION_RECORDID + "=?", new String[]
                    {sInfo.mRecordId});
            return iRet;
        }
        return 0;
    }


    public int deleteConversation(Conversation sInfo) {
        openDB();
        return db.delete(TABLE_CONVERSATION, CONVERSATION_RECORDID + "=? and "  + CONVERSATION_BASEID + "=?", new String[]
                {sInfo.mRecordId, ConversationManager.getInstance().dataBaseId});
    }

    public int deleteConversationByType(String type) {
        openDB();
        return db.delete(TABLE_CONVERSATION, CONVERSATION_TYPE + "=? and "  + CONVERSATION_BASEID + "=?", new String[]
                {type,ConversationManager.getInstance().dataBaseId});
    }

    public int deleteConversationByTypeDetial(String type,String detialid) {
        openDB();
        return db.delete(TABLE_CONVERSATION,CONVERSATION_TYPE + "=? and "  + CONVERSATION_BASEID + "=? and " + CONVERSATION_DETIALID + "=?", new String[]
                {type,ConversationManager.getInstance().dataBaseId,detialid});
    }

    public int readConversationByTypeDetial(String type,String detialid) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(CONVERSATION_READ, "true");
        return db.update(TABLE_CONVERSATION, cv,CONVERSATION_TYPE + "=? and "  + CONVERSATION_BASEID + "=? and " + CONVERSATION_DETIALID + "=?", new String[]
                {type,ConversationManager.getInstance().dataBaseId,detialid});
    }

    public int getAllUnReadCount(String baseid) {
        openDB();
        ContentValues cv = new ContentValues();
        String sql = "SELECT * FROM " + TABLE_CONVERSATION + " WHERE " + CONVERSATION_BASEID +
                " = ? "+" AND "+CONVERSATION_READ+" = ? ";
        Cursor c = db.rawQuery(sql, new String[]
                {baseid,"false"});
        int count = c.getCount();
        return count;
    }


    public void cleanAll() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);


        String sql = "CREATE TABLE " + TABLE_CONVERSATION + " (" + CONVERSATION_RECORDID
                + " TEXT PRIMARY KEY," + CONVERSATION_ICON + " TEXT,"+ CONVERSATION_ICON_TYPE + " TEXT,"
                + CONVERSATION_TIME + " TEXT," + CONVERSATION_TITLE + " TEXT,"+ CONVERSATION_MESSAGEID + " TEXT,"
                + CONVERSATION_TYPE + " TEXT," + CONVERSATION_SUBJECT + " TEXT,"
                + CONVERSATION_BASEID + " TEXT," + CONVERSATION_DETIALID + " TEXT,"
                + CONVERSATION_SOURCE_ID + " TEXT," + CONVERSATION_SOURCE_TYPE + " TEXT,"
                + CONVERSATION_READ + " TEXT," + CONVERSATION_SOURCE_NAME + " TEXT," + CONVERSATION_SOURCE_SIZE + " TEXT,"
                + CONVERSATION_ISSENDTO + " TEXT,"+ CONVERSATION_SOURCE_PATH + CONVERSATION_ISSEND + " TEXT,"+ " TEXT,"+ CONVERSATION_HIT + " TEXT)";
        db.execSQL(sql);
    }
}
