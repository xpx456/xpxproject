package intersky.notice.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.notice.NoticeManager;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.entity.Notice;
import intersky.notice.prase.NoticePrase;
import intersky.notice.view.activity.NewNoticeActivity;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.NetObject;
//01
public class NewNoticeHandler extends Handler {

    public static final int EVENT_SET_SEND = 3210100;
    public static final int EVENT_ADD_PIC = 3210101;
    public static final int EVENT_NOTICE_SET_CONTENT = 3210102;
    public NewNoticeActivity theActivity;

    public NewNoticeHandler(NewNoticeActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case NoticeAsks.EVENT_GET_SAVE_SUCCESS:
                    theActivity.waitDialog.hide();
                    theActivity.issub = false;
                    if(NoticePrase.praseData((NetObject) msg.obj,theActivity)) { ;
                        NetObject netObject = (NetObject) msg.obj;
                        Notice notice = (Notice) ((NetObject) msg.obj).item;
                        if(notice.nid.length() == 0)
                        {
                            NoticeManager.getInstance().sendNoticeAdd();
                        }
                        else
                        {
                            NoticeManager.getInstance().sendNoticeUpdate(notice.nid);
                        }
                        theActivity.finish();
                    }
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT:
                    theActivity.mNewNoticePresenter.doSave((String) msg.obj);
                    theActivity.waitDialog.hide();
                    break;
                case OaUtils.EVENT_UPLOAD_FILE_RESULT_FAIL:
                    theActivity.waitDialog.hide();
                    AppUtils.showMessage(theActivity, (String) msg.obj);
                    break;
                case EVENT_SET_SEND:
                    theActivity.mNewNoticePresenter.setSender((Intent) msg.obj);
                    break;
                case EVENT_ADD_PIC:
                    theActivity.waitDialog.hide();
                    theActivity.mNewNoticePresenter.setpic((Intent) msg.obj);
                    break;
                case EVENT_NOTICE_SET_CONTENT:
                    theActivity.waitDialog.hide();
                    theActivity.mCountText.setText((String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
