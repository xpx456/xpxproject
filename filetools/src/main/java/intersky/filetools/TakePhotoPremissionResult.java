package intersky.filetools;

import android.app.Activity;
import android.content.pm.PackageManager;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;

public class TakePhotoPremissionResult implements PermissionResult{


    public Activity context;
    public String path;
    public TakePhotoPremissionResult(Activity context,String path) {
        this.context = context;
        this.path = path;
    }


    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_PHOTO:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        FileUtils.mFileUtils.takePhoto(context,this.path);
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
