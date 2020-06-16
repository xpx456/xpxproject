package intersky.scan;

import android.app.Activity;
import android.content.pm.PackageManager;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;

public class ScanPremissionResult implements PermissionResult {


    public Activity context;
    public String className = "";
    public ScanPremissionResult(Activity context, String className) {
        this.context = context;
        this.className = className;
    }


    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        if(className.length() > 0)
                            ScanUtils.getInstance().startScan(context,className);
                        else
                            ScanUtils.getInstance().startScan(context);
                    }
                    else
                    {
                        AppUtils.showMessage(context,context.getString(R.string.promiss_error_camera));
                    }
                }
                else
                {
                    AppUtils.showMessage(context,context.getString(R.string.promiss_error_camera));
                }
                break;
        }
    }
}
