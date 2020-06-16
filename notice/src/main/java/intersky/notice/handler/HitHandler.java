package intersky.notice.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.notice.NoticeManager;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.entity.Notice;
import intersky.notice.prase.NoticePrase;
import intersky.xpxnet.net.NetObject;
//00
public class HitHandler extends Handler {




    public Context context;

    public HitHandler(Context context){
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {

            switch (msg.what) {
                case NoticeAsks.EVENT_GET_DETIAL_SUCCESS:
                    BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
                    baseActivity.waitDialog.hide();
                    boolean exist = NoticePrase.praseDetial((NetObject) msg.obj,context);
                    Notice notice = (Notice) ((NetObject) msg.obj).item;
                    if(exist = true)
                        NoticeManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),notice);
                    break;
            }
            super.handleMessage(msg);

    }
}
