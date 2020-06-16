package intersky.notice.bus;

import android.content.Context;
import android.text.TextUtils;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.notice.NoticeManager;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class NoticeBusObject extends BusObject {
    public NoticeBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
         if(TextUtils.equals(bizName, "notice/startNotice")) {
            NoticeManager.getInstance().startNotice(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "notice/startDetialConversation")) {
            NoticeManager.getInstance().startDetial(context,(String)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "notice/startNewNotice")) {
            NoticeManager.getInstance().newNotice(context);
            return null;
        }
        else if(TextUtils.equals(bizName, "notice/sendUpdate")) {
            NoticeManager.getInstance().sendNoticeUpdate((String)var3[0]);
            return null;
        }
        return null;
    }
}
