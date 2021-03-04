package intersky.function.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.function.FunctionUtils;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class FunctionBusObject extends BusObject {
    public FunctionBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if (TextUtils.equals(bizName, "function/updateOahit")) {
            FunctionUtils.getInstance().getOaHit();
            return null;
        }
        return null;
    }
}
