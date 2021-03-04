package intersky.chat;

import android.app.Activity;
import android.content.pm.PackageManager;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;
import intersky.talk.ImFaceRelativeLayout;

public class RecordAudioPremissionResult implements PermissionResult{


    public Activity context;
    public ImFaceRelativeLayout mImFaceRelativeLayout;
    public RecordAudioPremissionResult(ImFaceRelativeLayout mImFaceRelativeLayout, Activity context) {
        this.context = context;
        this.mImFaceRelativeLayout = mImFaceRelativeLayout;
    }

    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
                if(grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        if(mImFaceRelativeLayout != null)
                        mImFaceRelativeLayout.canadudio = true;
                    }
                    else
                    {
                        AppUtils.showMessage(context,context.getString(intersky.talk.R.string.promiss_error_audio));
                    }
                }
                else
                {
                    AppUtils.showMessage(context,context.getString(intersky.talk.R.string.promiss_error_audio));
                }
                break;
        }
    }
}
