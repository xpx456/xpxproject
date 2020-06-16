package intersky.mywidget.conturypick;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DbHelper {

    public static final String DB_NAME = "country.db";//数据库
    public static final String PACKAGE_NAME = "com.bigwiner";
    // 在手机存放数据库的位置
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME + "/" + DB_NAME; // 在手机里存放数据库的位置
    private static SQLiteDatabase database;
    public static final String HX_Country = "HX_Country";
    public static final String name = "name";
    public static final String cnname = "cnname";
    public static final String code = "code";
    public static final String region = "region";
    public static final String areacode = "areacode";
    public static SQLiteDatabase getDb(Context context) {
        if(database == null)
        {
            database = openDatabase(context);
        }
        return database;
    }


    private static SQLiteDatabase openDatabase(Context context) {
        try {
            if (!(new File(DB_PATH).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库

                InputStream is = context.getResources().getAssets()
                        .open(DB_NAME); // 欲导入的数据库
                FileOutputStream fos = new FileOutputStream(DB_PATH);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                    Log.i("info", buffer.length + "");
                    fos.flush();
                }
                fos.close();
                is.close();
            }


            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Country> getAll(Context context) {
        ArrayList<Country> countries = new ArrayList<Country>();
        getDb(context);
        String sql = "SELECT * FROM " + HX_Country;
        Cursor c = getDb(context).rawQuery(sql, new String[]
                {});
        while (c.moveToNext())
        {
            Country country = new Country();
            country.code = Integer.valueOf(c.getString(c.getColumnIndex(areacode)));
            country.ename = c.getString(c.getColumnIndex(name));
            country.name = c.getString(c.getColumnIndex(cnname));
            country.locale = c.getString(c.getColumnIndex(region));
            countries.add(country);
        }
        return countries;
    }

}
