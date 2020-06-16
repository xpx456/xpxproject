package intersky.apputils;

import android.app.Activity;
import android.content.pm.PackageManager;


public class TelPremissionResult implements PermissionResult {


    public Activity context;
    public String number = "";
    public TelPremissionResult(Activity context, String number) {
        this.context = context;
        this.number = number;
    }


    @Override
    public void doResult(int requestCode ,int[] grantResults) {
        switch (requestCode) {
            case AppUtils.PERMISSION_REQUEST_TEL:
                if(grantResults.length > 0 )
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // MyPermission Granted
                        AppUtils.del(context,number);
                    }
                    else
                    {
                        AppUtils.showMessage(context,context.getString(R.string.keyword_promiess));
                    }
                }
                else
                {
                    AppUtils.showMessage(context,context.getString(R.string.keyword_promiess));
                }
                break;
        }
    }
}
