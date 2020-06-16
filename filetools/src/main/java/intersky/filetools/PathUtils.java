package intersky.filetools;

import android.os.Environment;

import java.io.File;

import intersky.appbase.ScreenDefine;
import intersky.apputils.AppUtils;
import intersky.filetools.entity.LocalDocument;

public class PathUtils {

    public String APP_PATH = Environment.getExternalStorageDirectory().getPath();
    public volatile static PathUtils pathUtils = null;
    private String base = "";

    public static PathUtils init() {
        if (pathUtils == null) {
            synchronized (PathUtils.class) {
                if (pathUtils == null) {
                    pathUtils = new PathUtils();
                }

            }
        }
        return pathUtils;
    }



    public String getBasePath() {
        return Environment.getExternalStorageDirectory().getPath();
    }


    public PathUtils() {
    }

    public void setBase(String base) {
        APP_PATH =  Environment.getExternalStorageDirectory().getPath()+base;
        File file1 = new File(APP_PATH);
        if(!file1.exists())
        {
            file1.mkdirs();
        }
    }

    public String getfilePath(String path) {
        File mfile = new File(APP_PATH +"/"+path);
        if (!mfile.exists())
            mfile.mkdirs();
        return mfile.getPath();
    }

    public String getAppPath() {
        File mfile = new File(APP_PATH );
        if (!mfile.exists())
            mfile.mkdirs();
        return mfile.getPath();
    }

    public static void recursionDeleteFile(File file) {
        if (file.isFile()) {
            String a = file.getName();
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursionDeleteFile(f);
            }
            // file.delete();
        }
    }
}
