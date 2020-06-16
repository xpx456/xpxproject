package intersky.document.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.document.DBHelper;
import intersky.document.DocumentManager;
import intersky.xpxnet.net.Service;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class DocumentBusObject extends BusObject {
    public DocumentBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {


        return null;
    }
}
