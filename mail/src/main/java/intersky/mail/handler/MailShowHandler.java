package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailShowActivity;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//05
public class MailShowHandler extends Handler {
   public MailShowActivity theActivity;
    public final static int SAVE_SUCCESS = 3200600;
    public MailShowHandler(MailShowActivity mMailShowActivity) {
        theActivity = mMailShowActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_VIEW_SUCCESS:
                theActivity.waitDialog.hide();
                MailPrase.praseMail((NetObject) msg.obj,theActivity.mMail);
                theActivity.mMailShowPresenter.initWebView();
                MailManager.getInstance().getReadCount();
                theActivity.mMailShowPresenter.initAttachmentView();
                break;
            case MailAsks.MAIL_DELETE_SUCCESS:
                if(MailPrase.praseDate((NetObject) msg.obj)) {
                    AppUtils.sendSampleBroadCast(theActivity,MailManager.ACTION_MAIL_LIST_UPDATE);
                    theActivity.finish();
                }
                else
                {
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.delete_mail_fail));
                }
                break;
            case MailAsks.MAIL_APPROVE_SUCCESS:
                if(MailPrase.praseDate((NetObject) msg.obj)) {
                    AppUtils.sendSampleBroadCast(theActivity,MailManager.ACTION_MAIL_LIST_UPDATE);
                    theActivity.finish();
                }
                else
                {
                    AppUtils.showMessage(theActivity,theActivity.getString(R.string.submit_mail_fail));
                }
                break;
            case SAVE_SUCCESS:
                Intent intent1 = (Intent) msg.obj;
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.mail_save_success));
                break;
        }

    }
}
