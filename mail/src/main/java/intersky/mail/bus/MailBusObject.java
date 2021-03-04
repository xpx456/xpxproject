package intersky.mail.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.appbase.utils.XpxShare;
import intersky.mail.MailManager;
import intersky.appbase.bus.BusObject;
import intersky.xpxnet.net.Service;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class MailBusObject extends BusObject {
    public MailBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {

        if (TextUtils.equals(bizName, "mail/getMailHit")) {
            MailManager.getInstance().getReadCount();
            return null;
        }
        else if (TextUtils.equals(bizName, "mail/senMailAddress")) {
            MailManager.getInstance().sendMail(context,(String) var3[0]);
            return null;
        }
        else if (TextUtils.equals(bizName, "mail/getMailHitCount")) {
            if(MailManager.getInstance() != null)
            {
                if(MailManager.getInstance().account.iscloud == false)
                    return MailManager.getInstance().shoujiancounts;
                else
                    return MailManager.getInstance().allcount1;
            }
            else
            {
                return 0;
            }

        }
        else if (TextUtils.equals(bizName, "mail/newMail")) {
            MailManager.getInstance().newMail(context);

        }
        return null;
    }
}
