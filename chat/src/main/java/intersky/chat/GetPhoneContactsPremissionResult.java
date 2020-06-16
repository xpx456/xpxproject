package intersky.chat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;

public class GetPhoneContactsPremissionResult implements PermissionResult {


    public Activity context;
    public Handler handler;

    public GetPhoneContactsPremissionResult(Activity context,Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_READ_CONTACTS:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        handler.sendEmptyMessage(PermissionCode.PERMISSION_REQUEST_READ_CONTACTS);
                    }
                    else
                    {
                        AppUtils.showMessage(context,context.getString(R.string.contacts_premission_fail));
                    }
                }
                else
                {
                    AppUtils.showMessage(context,context.getString(R.string.contacts_premission_fail));
                }
                break;
        }
    }
}
