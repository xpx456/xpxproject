package intersky.apputils;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class PermissionHandler extends Handler {



    public Activity activity;
    public String number;

    public PermissionHandler(Activity activity, String number) {
        this.activity = activity;
        this.number = number;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case AppUtils.PERMISSION_REQUEST_TEL:
                AppUtils.del(activity,  number);
                break;
        }

    }
}
