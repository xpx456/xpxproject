package intersky.scan;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.PermissionCode;

public class PermissionHandler extends Handler {



    public Activity activity;
    public String className;

    public PermissionHandler(Activity activity, String className) {
        this.activity = activity;
        this.className = className;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA:
                ScanUtils.getInstance().startScan(activity,  className);
                break;
        }

    }
}
