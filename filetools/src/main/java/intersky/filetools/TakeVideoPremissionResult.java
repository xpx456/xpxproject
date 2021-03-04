package intersky.filetools;

import android.app.Activity;
import android.content.pm.PackageManager;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;

public class TakeVideoPremissionResult implements PermissionResult{


    public Activity context;
    public String path;

    public TakeVideoPremissionResult(Activity context,String path) {
        this.context = context;
        this.path = path;
    }


    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_CAMERA_VIDEO:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        FileUtils.mFileUtils.takeVideo(context,this.path);
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
