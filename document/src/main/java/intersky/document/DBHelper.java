package intersky.document;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import intersky.document.entity.DocumentNet;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "intersky_document.db";
    public static final int DB_VERSION = 6;

    private static final String TABLE_DOCUMENT = "TABLE_DOCUMENT";
    private static final String DOCUMENT_URL = "DOCUMENT_URL";
    private static final String DOCUMENT_FINISHSIZE = "DOCUMENT_FINISHSIZE";
    private static final String DOCUMENT_TOTALSIZE = "DOCUMENT_TOTALSIZE";
    private static final String DOCUMENT_DETE = "DOCUMENT_DETE";
    private static final String DOCUMENT_NAME = "DOCUMENT_NAME";
    private static final String DOCUMENT_RECORDID = "DOCUMENT_RECORDID";
    private static final String DOCUMENT_USERID = "DOCUMENT_USERID";
    private static final String DOCUMENT_PATH = "DOCUMENT_PATH";
    private static final String DOCUMENT_IMG = "DOCUMENT_IMG";
    private static final String DOCUMENT_STATE = "DOCUMENT_STATE";
    private static final String DOCUMENT_OWNERTYPE = "DOCUMENT_OWNERTYPE";
    private static final String DOCUMENT_PARENTID = "DOCUMENT_PARENTID";
    private static final String DOCUMENT_OWNERID = "DOCUMENT_OWNERID";
    private static final String DOCUMENT_CATALOGUEID = "DOCUMENT_CATALOGUEID";
    private static final String DOCUMENT_SERVICE = "DOCUMENT_SERVICE";
    private static final String DOCUMENT_ISLOCAL = "DOCUMENT_ISLOCAL";
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT);


        String sql = "CREATE TABLE " + TABLE_DOCUMENT + " (" + DOCUMENT_RECORDID
                + " TEXT PRIMARY KEY," + DOCUMENT_NAME + " TEXT," + DOCUMENT_URL + " TEXT," + DOCUMENT_FINISHSIZE
                + " TEXT," + DOCUMENT_TOTALSIZE + " TEXT," + DOCUMENT_PATH + " TEXT," + DOCUMENT_IMG + " TEXT,"+ DOCUMENT_DETE + " TEXT,"
                + DOCUMENT_OWNERTYPE + " TEXT," + DOCUMENT_PARENTID + " TEXT," + DOCUMENT_OWNERID + " TEXT,"+ DOCUMENT_CATALOGUEID + " TEXT,"
                + DOCUMENT_SERVICE + " TEXT,"+ DOCUMENT_ISLOCAL + " TEXT,"+ DOCUMENT_STATE+ " TEXT," + DOCUMENT_USERID + " TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT);


        String sql = "CREATE TABLE " + TABLE_DOCUMENT + " (" + DOCUMENT_RECORDID
                + " TEXT PRIMARY KEY," + DOCUMENT_NAME + " TEXT," + DOCUMENT_URL + " TEXT," + DOCUMENT_FINISHSIZE
                + " TEXT," + DOCUMENT_TOTALSIZE + " TEXT," + DOCUMENT_PATH + " TEXT," + DOCUMENT_IMG + " TEXT,"+ DOCUMENT_DETE + " TEXT,"
                + DOCUMENT_OWNERTYPE + " TEXT," + DOCUMENT_PARENTID + " TEXT," + DOCUMENT_OWNERID + " TEXT,"+DOCUMENT_CATALOGUEID + " TEXT,"
                + DOCUMENT_SERVICE + " TEXT," + DOCUMENT_ISLOCAL + " TEXT,"+ DOCUMENT_STATE + " TEXT,"+ DOCUMENT_USERID + " TEXT)";
        db.execSQL(sql);



    }



    public void addDoucment(DocumentNet sInfo)
    {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(DOCUMENT_OWNERTYPE, sInfo.mOwnerType);
        cv.put(DOCUMENT_PARENTID, sInfo.parentid);
        cv.put(DOCUMENT_OWNERID, sInfo.mOwnerID);
        cv.put(DOCUMENT_RECORDID, sInfo.mRecordID);
        cv.put(DOCUMENT_PATH, sInfo.mPath);
        cv.put(DOCUMENT_DETE, sInfo.mDate);
        cv.put(DOCUMENT_NAME, sInfo.mName);
        cv.put(DOCUMENT_CATALOGUEID, sInfo.mCatalogueID);
        cv.put(DOCUMENT_STATE, String.valueOf(sInfo.mState));
        cv.put(DOCUMENT_USERID, DocumentManager.getInstance().mAccount.mRecordId);
        cv.put(DOCUMENT_SERVICE, DocumentManager.getInstance().service.sAddress);
        cv.put(DOCUMENT_URL, sInfo.mUrl);
        cv.put(DOCUMENT_FINISHSIZE, String.valueOf(sInfo.mFinishSize));
        cv.put(DOCUMENT_TOTALSIZE, String.valueOf(sInfo.mSize));
        cv.put(DOCUMENT_ISLOCAL, Integer.valueOf(sInfo.mType));
        cv.put(DOCUMENT_IMG, sInfo.mUrl);
        int iRet = (int) db.insert(TABLE_DOCUMENT, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_DOCUMENT, cv, DOCUMENT_RECORDID + "=?", new String[]
                    {sInfo.mRecordID});
        }
    }

    public void updataDocument(DocumentNet sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(DOCUMENT_OWNERTYPE, sInfo.mOwnerType);
        cv.put(DOCUMENT_PARENTID, sInfo.parentid);
        cv.put(DOCUMENT_OWNERID, sInfo.mOwnerID);
        cv.put(DOCUMENT_RECORDID, sInfo.mRecordID);
        cv.put(DOCUMENT_PATH, sInfo.mPath);
        cv.put(DOCUMENT_DETE, sInfo.mDate);
        cv.put(DOCUMENT_NAME, sInfo.mName);
        cv.put(DOCUMENT_CATALOGUEID, sInfo.mCatalogueID);
        cv.put(DOCUMENT_STATE, String.valueOf(sInfo.mState));
        cv.put(DOCUMENT_USERID, DocumentManager.getInstance().mAccount.mRecordId);
        cv.put(DOCUMENT_SERVICE, DocumentManager.getInstance().service.sAddress);
        cv.put(DOCUMENT_URL, sInfo.mUrl);
        cv.put(DOCUMENT_FINISHSIZE, String.valueOf(sInfo.mFinishSize));
        cv.put(DOCUMENT_TOTALSIZE, String.valueOf(sInfo.mSize));
        cv.put(DOCUMENT_ISLOCAL, Integer.valueOf(sInfo.mType));
        cv.put(DOCUMENT_IMG, sInfo.mUrl);
        db.update(TABLE_DOCUMENT, cv, DOCUMENT_RECORDID + "=?", new String[]
                {sInfo.mRecordID});
    }

    public DocumentNet getDocument(String recordid, int type) {
        openDB();
        String sql = "SELECT * FROM " + TABLE_DOCUMENT + " WHERE " +DOCUMENT_ISLOCAL + "=? and "+DOCUMENT_RECORDID + "=? and "+DOCUMENT_SERVICE + "=? and "+ DOCUMENT_USERID +
                "=?";
        Cursor c = db.rawQuery(sql, new String[]
                {String.valueOf(type),recordid,DocumentManager.getInstance().service.sAddress,DocumentManager.getInstance().mAccount.mRecordId});
        DocumentNet mDocumentNet = null;
        while (c.moveToNext()) {
            mDocumentNet = new DocumentNet();
            mDocumentNet.mType  = Integer.valueOf(c.getString(c.getColumnIndex(DOCUMENT_ISLOCAL)));
            mDocumentNet.mOwnerType  = c.getString(c.getColumnIndex(DOCUMENT_OWNERTYPE));
            mDocumentNet.parentid  = c.getString(c.getColumnIndex(DOCUMENT_PARENTID));
            mDocumentNet.mOwnerID  = c.getString(c.getColumnIndex(DOCUMENT_OWNERID));
            mDocumentNet.mRecordID  = c.getString(c.getColumnIndex(DOCUMENT_RECORDID));
            mDocumentNet.mPath  = c.getString(c.getColumnIndex(DOCUMENT_PATH));
            mDocumentNet.mName  = c.getString(c.getColumnIndex(DOCUMENT_NAME));
            mDocumentNet.mCatalogueID  = c.getString(c.getColumnIndex(DOCUMENT_CATALOGUEID));
            mDocumentNet.mState  = Integer.valueOf(c.getString(c.getColumnIndex(DOCUMENT_STATE)));
            mDocumentNet.mUrl  = c.getString(c.getColumnIndex(DOCUMENT_URL));
            mDocumentNet.mFinishSize  = Long.valueOf(c.getString(c.getColumnIndex(DOCUMENT_FINISHSIZE)));
            mDocumentNet.mSize  = Long.valueOf(c.getString(c.getColumnIndex(DOCUMENT_TOTALSIZE)));
        }
        return mDocumentNet;
    }

    public void scanDocument() {
        openDB();
        String sql = "SELECT * FROM " + TABLE_DOCUMENT + " WHERE " +DOCUMENT_SERVICE + "=? and "+ DOCUMENT_USERID +
                "=?";
        Cursor c = db.rawQuery(sql, new String[]
                {String.valueOf(DocumentManager.getInstance().service.sAddress),DocumentManager.getInstance().mAccount.mRecordId});
        while (c.moveToNext()) {
            DocumentNet mDocumentNet = new DocumentNet();
            mDocumentNet.mType  = Integer.valueOf(c.getString(c.getColumnIndex(DOCUMENT_ISLOCAL)));
            mDocumentNet.mOwnerType  = c.getString(c.getColumnIndex(DOCUMENT_OWNERTYPE));
            mDocumentNet.parentid  = c.getString(c.getColumnIndex(DOCUMENT_PARENTID));
            mDocumentNet.mOwnerID  = c.getString(c.getColumnIndex(DOCUMENT_OWNERID));
            mDocumentNet.mRecordID  = c.getString(c.getColumnIndex(DOCUMENT_RECORDID));
            mDocumentNet.mPath  = c.getString(c.getColumnIndex(DOCUMENT_PATH));
            mDocumentNet.mName  = c.getString(c.getColumnIndex(DOCUMENT_NAME));
            mDocumentNet.mCatalogueID  = c.getString(c.getColumnIndex(DOCUMENT_CATALOGUEID));
            mDocumentNet.mState  = Integer.valueOf(c.getString(c.getColumnIndex(DOCUMENT_STATE)));
            mDocumentNet.mUrl  = c.getString(c.getColumnIndex(DOCUMENT_URL));
            mDocumentNet.mFinishSize  = Long.valueOf(c.getString(c.getColumnIndex(DOCUMENT_FINISHSIZE)));
            mDocumentNet.mSize  = Long.valueOf(c.getString(c.getColumnIndex(DOCUMENT_TOTALSIZE)));
            if(DocumentManager.TYPE_DOWNLOAD_NOMAL == mDocumentNet.mType)
            {
                if(mDocumentNet.mState == DocumentManager.STATE_FINISH) {
                    if(DocumentManager.getInstance().mdownloadTitleFinish == null)
                    {
                        DocumentManager.getInstance().mdownloadTitleFinish = new DocumentNet();
                        DocumentManager.getInstance().mdownloadTitleFinish.mType = DocumentManager.TYPE_DOWNLOAD_FINISH;
                        DocumentManager.getInstance().mdownloadTitleFinish.mState = 1;
                        if(DocumentManager.getInstance().mdownloadTitleUnFinish == null)
                        {
                            DocumentManager.getInstance().downloads.add(0,DocumentManager.getInstance().mdownloadTitleFinish);
                            DocumentManager.getInstance().downloads.add(1,mDocumentNet);
                            DocumentManager.getInstance().downloadsMap.put(mDocumentNet.mRecordID,mDocumentNet);
                        }
                        else
                        {
                            DocumentManager.getInstance().downloads.add(DocumentManager.getInstance().mdownloadTitleUnFinish.mState+1,DocumentManager.getInstance().mdownloadTitleFinish);
                            DocumentManager.getInstance().downloads.add(DocumentManager.getInstance().mdownloadTitleUnFinish.mState+2,mDocumentNet);
                            DocumentManager.getInstance().downloadsMap.put(mDocumentNet.mRecordID,mDocumentNet);
                        }

                    }
                    else
                    {
                        DocumentManager.getInstance().mdownloadTitleFinish.mState++;
                        if(DocumentManager.getInstance().mdownloadTitleUnFinish == null)
                        {
                            DocumentManager.getInstance().downloads.add(1,mDocumentNet);
                            DocumentManager.getInstance().downloadsMap.put(mDocumentNet.mRecordID,mDocumentNet);
                        }
                        else
                        {
                            DocumentManager.getInstance().downloads.add(DocumentManager.getInstance().mdownloadTitleUnFinish.mState+2,mDocumentNet);
                            DocumentManager.getInstance().downloadsMap.put(mDocumentNet.mRecordID,mDocumentNet);
                        }
                    }
                }
                else {
                    if(DocumentManager.getInstance().mdownloadTitleUnFinish == null)
                    {
                        DocumentManager.getInstance().mdownloadTitleUnFinish = new DocumentNet();
                        DocumentManager.getInstance().mdownloadTitleUnFinish.mType = DocumentManager.TYPE_DOWNLOAD_UNFINISH;
                        DocumentManager.getInstance().mdownloadTitleUnFinish.mState = 1;
                        DocumentManager.getInstance().downloads.add(0,DocumentManager.getInstance().mdownloadTitleUnFinish);
                        DocumentManager.getInstance().downloads.add(1,mDocumentNet);
                        DocumentManager.getInstance().downloadsMap.put(mDocumentNet.mRecordID,mDocumentNet);
                    }
                    else
                    {
                        DocumentManager.getInstance().mdownloadTitleUnFinish.mState++;
                        DocumentManager.getInstance().downloads.add(1,mDocumentNet);
                        DocumentManager.getInstance().downloadsMap.put(mDocumentNet.mRecordID,mDocumentNet);

                    }
                }
            }
            else if(DocumentManager.TYPE_UPLOAD_NOMAL == mDocumentNet.mType) {
                if(mDocumentNet.mState == DocumentManager.STATE_FINISH) {
                    if(DocumentManager.getInstance().muploadTitleFinish == null) {
                        DocumentManager.getInstance().muploadTitleFinish = new DocumentNet();
                        DocumentManager.getInstance().muploadTitleFinish.mState = 1;
                        DocumentManager.getInstance().muploadTitleFinish.mType = DocumentManager.TYPE_UPLOAD_FINISH;
                        if(DocumentManager.getInstance().muploadTitleUnFinish == null)
                        {
                            DocumentManager.getInstance().uploads.add(0,DocumentManager.getInstance().muploadTitleFinish);
                            DocumentManager.getInstance().uploads.add(1,mDocumentNet);
                            DocumentManager.getInstance().uploadssMap.put(mDocumentNet.mName+mDocumentNet.mCatalogueID+mDocumentNet.mOwnerType,mDocumentNet);
                        }
                        else
                        {
                            DocumentManager.getInstance().uploads.add(DocumentManager.getInstance().muploadTitleUnFinish.mState+1,DocumentManager.getInstance().muploadTitleFinish);
                            DocumentManager.getInstance().uploads.add(DocumentManager.getInstance().muploadTitleUnFinish.mState+2,mDocumentNet);
                            DocumentManager.getInstance().uploadssMap.put(mDocumentNet.mName+mDocumentNet.mCatalogueID+mDocumentNet.mOwnerType,mDocumentNet);
                        }

                    }
                    else {
                        DocumentManager.getInstance().muploadTitleFinish.mState++;
                        if(DocumentManager.getInstance().muploadTitleUnFinish == null)
                        {
                            DocumentManager.getInstance().uploads.add(1,mDocumentNet);
                            DocumentManager.getInstance().uploadssMap.put(mDocumentNet.mName+mDocumentNet.mCatalogueID+mDocumentNet.mOwnerType,mDocumentNet);
                        }
                        else
                        {
                            DocumentManager.getInstance().uploads.add(DocumentManager.getInstance().muploadTitleUnFinish.mState+2,mDocumentNet);
                            DocumentManager.getInstance().uploadssMap.put(mDocumentNet.mName+mDocumentNet.mCatalogueID+mDocumentNet.mOwnerType,mDocumentNet);
                        }
                    }
                }
                else {
                    if(DocumentManager.getInstance().muploadTitleUnFinish == null)
                    {
                        DocumentManager.getInstance().muploadTitleUnFinish = new DocumentNet();
                        DocumentManager.getInstance().muploadTitleUnFinish.mState = 1;
                        DocumentManager.getInstance().muploadTitleUnFinish.mType = DocumentManager.TYPE_UPLOAD_UNFINISH;
                        DocumentManager.getInstance().uploads.add(0,DocumentManager.getInstance().muploadTitleUnFinish);
                        DocumentManager.getInstance().uploads.add(1,mDocumentNet);
                        DocumentManager.getInstance().uploadssMap.put(mDocumentNet.mName+mDocumentNet.mCatalogueID+mDocumentNet.mOwnerType,mDocumentNet);
                    }
                    else
                    {
                        DocumentManager.getInstance().muploadTitleUnFinish.mState++;
                        DocumentManager.getInstance().uploads.add(1,mDocumentNet);
                        DocumentManager.getInstance().uploadssMap.put(mDocumentNet.mName+mDocumentNet.mCatalogueID+mDocumentNet.mOwnerType,mDocumentNet);

                    }
                }
            }
        }

    }

    public int deleteDoucment(DocumentNet sInfo) {
        return db.delete(TABLE_DOCUMENT, DOCUMENT_RECORDID + "=? and " + DOCUMENT_USERID +  "=? and " + DOCUMENT_SERVICE + "=?", new String[]
                {sInfo.mRecordID, DocumentManager.getInstance().mAccount.mRecordId,DocumentManager.getInstance().service.sAddress});
    }
}
