package intersky.filetools.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import intersky.filetools.entity.DownloadInfo;
import intersky.xpxnet.net.Service;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "downloadurl.db";
    public static final int DB_VERSION = 1;

    private static final String TABLE_DOWNLOADURL = "TABLE_DOWNLOADURL";
    private static final String DOWNLOADURL_URL = "DOWNLOADURL_URL";
    private static final String DOWNLOADURL_SIZE = "DOWNLOADURL_SIZE";
    private static final String DOWNLOADURL_FILENAME = "DOWNLOADURL_FILENAME";

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADURL);


        String sql = "CREATE TABLE " + TABLE_DOWNLOADURL + " (" + DOWNLOADURL_URL
                + " TEXT PRIMARY KEY," + DOWNLOADURL_SIZE + " TEXT," + DOWNLOADURL_FILENAME + " TEXT)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 46)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADURL);

            String sql = "CREATE TABLE " + TABLE_DOWNLOADURL + " (" + DOWNLOADURL_URL
                    + " TEXT PRIMARY KEY," + DOWNLOADURL_SIZE + " TEXT," + DOWNLOADURL_FILENAME + " TEXT)";
            db.execSQL(sql);
        }
    }


    public DownloadInfo getInfo(String url) {
        openDB();

        String sql = "SELECT * FROM " + TABLE_DOWNLOADURL + " WHERE " + DOWNLOADURL_URL
                + " = ?";
        Cursor c = db.rawQuery(sql, new String[]
                {url});

        DownloadInfo info = null;
        while (c.moveToNext()) {
            info = new DownloadInfo();
            info.url = c.getString(c.getColumnIndex(DOWNLOADURL_URL));
            info.size = Long.valueOf(c.getString(c.getColumnIndex(DOWNLOADURL_SIZE)));
            info.url = c.getString(c.getColumnIndex(DOWNLOADURL_FILENAME));
            break;
        }

        c.close();

        return info;
    }

    public int addInfo(DownloadInfo sInfo) {
        openDB();

        ContentValues cv = new ContentValues();

        cv.put(DOWNLOADURL_URL, sInfo.url);
        cv.put(DOWNLOADURL_SIZE, String.valueOf(sInfo.size));
        cv.put(DOWNLOADURL_FILENAME, sInfo.name);

        int iRet = (int) db.insert(TABLE_DOWNLOADURL, null, cv);
        if (-1 == iRet) {
            iRet = db.update(TABLE_DOWNLOADURL, cv, DOWNLOADURL_URL + "=?", new String[]
                    {sInfo.url});
        }

        return iRet;
    }

    public int deleteInfo(DownloadInfo sInfo) {
        return db.delete(TABLE_DOWNLOADURL, DOWNLOADURL_URL + "=?", new String[]
                {sInfo.url});
    }


}
