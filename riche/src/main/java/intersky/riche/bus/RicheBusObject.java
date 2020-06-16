package intersky.riche.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.riche.RicheManager;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class RicheBusObject extends BusObject {
    public RicheBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if (TextUtils.equals(bizName, "riche/init")) {
            RicheManager.init(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "riche/startEdit")) {
            RicheManager.getInstance().startEdit(context,(String)var3[0],(String)var3[1]);
        }
        return null;
    }
}
