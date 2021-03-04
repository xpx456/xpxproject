package intersky.filetools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import intersky.appbase.ScreenDefine;
import intersky.apputils.AppUtils;
import intersky.filetools.entity.LocalDocument;

public class PathUtils {

    public String APP_PATH = "";
    public volatile static PathUtils pathUtils = null;
    public Context context;
    private String base = "";

    public static PathUtils init(Context context) {
        if (pathUtils == null) {
            synchronized (PathUtils.class) {
                if (pathUtils == null) {
                    pathUtils = new PathUtils(context);
                }

            }
        }
        return pathUtils;
    }

    public static PathUtils init(Context context,String base) {
        if (pathUtils == null) {
            synchronized (PathUtils.class) {
                if (pathUtils == null) {
                    pathUtils = new PathUtils(context,base);
                }

            }
        }
        return pathUtils;
    }


    public String getBasePath() {
        return context.getExternalFilesDir(null).getPath();
    }


    public PathUtils(Context context) {
        APP_PATH = context.getExternalFilesDir(null).getPath();
        this.context = context;
    }

    public PathUtils(Context context,String base) {
        APP_PATH = context.getExternalFilesDir(null).getPath()+base;
        File mfile = new File(APP_PATH );
        if (!mfile.exists())
            mfile.mkdirs();
        this.context = context;
    }

    public void setAppBase(String base) {
        APP_PATH =  context.getExternalFilesDir(null).getPath()+base;
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
