package intersky.sign.bus;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.sign.SignManager;
import intersky.sign.view.activity.SignActivity;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class SignBusObject extends BusObject {
    public SignBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if(TextUtils.equals(bizName, "sign/startSign")) {
            Intent intent = new Intent(context, SignActivity.class);
            if(SignManager.getInstance().mapManager != null)
            {
//                SignManager.getInstance().mapManager.startLocation();
                context.startActivity(intent);
            }

            return null;
        }



        return null;
    }
}
