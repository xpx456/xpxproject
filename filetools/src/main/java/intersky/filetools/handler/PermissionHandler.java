package intersky.filetools.handler;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.PermissionCode;
import intersky.filetools.FileUtils;

public class PermissionHandler extends Handler {


    public Activity activity;
    public String path;
    public int id = -1;
    public PermissionHandler(Activity activity,String path) {
        this.activity = activity;
        this.path = path;
    }

    public PermissionHandler(Activity activity,String path,int id) {
        this.activity = activity;
        this.path = path;
        this.id = id;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_PHOTO:
                if(id == -1)
                FileUtils.mFileUtils.takePhoto(activity,  path);
                else
                    FileUtils.mFileUtils.takePhoto(activity,  path,id);
                break;
            case PermissionCode.PERMISSION_REQUEST_CAMERA_VIDEO:
                if(id == -1)
                FileUtils.mFileUtils.takeVideo(activity, path);
                else
                    FileUtils.mFileUtils.takePhoto(activity,  path,id);
                break;
        }

    }
}
